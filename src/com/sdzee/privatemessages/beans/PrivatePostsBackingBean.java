package com.sdzee.privatemessages.beans;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;

import com.sdzee.breadcrumb.beans.BreadCrumbHelper;
import com.sdzee.breadcrumb.beans.BreadCrumbItem;
import com.sdzee.dao.DAOException;
import com.sdzee.forums.dao.NotificationDao;
import com.sdzee.forums.dao.TopicDao;
import com.sdzee.membres.entities.Member;
import com.sdzee.privatemessages.dao.PrivateNotificationDao;
import com.sdzee.privatemessages.dao.PrivatePostDao;
import com.sdzee.privatemessages.dao.PrivateTopicDao;
import com.sdzee.privatemessages.entities.PrivateNotification;
import com.sdzee.privatemessages.entities.PrivatePost;
import com.sdzee.privatemessages.entities.PrivateTopic;

/**
 * PrivatePostsBackingBean est le bean sur lequel s'appuie notamment la page d'un sujet de forum. Il s'agit d'un ManagedBean JSF, ayant pour
 * portée une vue. Il contient une variable <code>sujetId</code> initialisée en amont par la Facelet <code>sujet.xhtml</code>.
 * 
 * @author Médéric Munier
 * @version %I%, %G%
 */
@ManagedBean( name = "privatePostsBean" )
@ViewScoped
public class PrivatePostsBackingBean implements Serializable {
    private static final long      serialVersionUID       = 1L;
    private static final String    URL_PRIVATE_TOPIC_PAGE = "/privateTopic.jsf?topicId=";
    private static final String    URL_404                = "/404.jsf";
    private static final String    SESSION_MEMBER         = "member";
    private static final int       NB_POSTS_PER_PAGE      = 5;

    private PrivatePost            privatePost;
    private PrivateTopic           privateTopic;
    private List<PrivatePost>      paginatedPrivatePosts;
    private int                    pagesNumber;
    private int                    topicId;
    private int                    page                   = 1;

    @EJB
    private PrivatePostDao         privatePostDao;
    @EJB
    private PrivateTopicDao        privateTopicDao;
    @EJB
    private PrivateNotificationDao privateNotificationDao;

    /**
     * Cette méthode initialise la variable d'instance <code>topic</code> en récupérant en base le sujet correspondant à l'id transmis par
     * la Facelet <code>topic.xhtml</code>, contenu dans la variable <code>topicId</code>. Elle vérifie ensuite si le visiteur accédant au
     * sujet est connecté, et si oui, elle va supprimer de la base l'éventuelle notification associée à ce sujet pour le membre en question.
     * <p>
     * Elle est exécutée automatiquement par JSF, après le constructeur de la classe s'il existe. À l'appel du constructeur classique, le
     * bean n'est pas encore initialisé, et donc aucune dépendance n'est injectée. Cependant lorsque cette méthode est appelée, le bean est
     * déjà initialisé et il est donc possible de faire appel à des dépendances. Ici, ce sont les DAO {@link TopicDao} et
     * {@link NotificationDao} injectés via l'annotation <code>@EJB</code> qui entrent en jeu.
     * <p>
     * À la différence de la plupart des autres backing-beans, cette méthode n'est pas annotée avec <code>@PostConstruct</code>. Ceci est
     * simplement dû au fait qu'elle fait appel à une variable qui est initialisée depuis la vue, en l'occurrence l'id du sujet courant.
     * Puisqu'elle dépend de l'action du visiteur, son cycle de vie ne peut pas être entièrement géré par JSF.
     */
    public void init() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        Member member = (Member) externalContext.getSessionMap().get( SESSION_MEMBER );
        if ( member == null ) {
            // si le visiteur n'est pas connecté, on le redirige
            try {
                externalContext.redirect( "connection.jsf?urlOrigine=/sdzee/privateTopics.jsf" );
                return;
            } catch ( IOException e ) {
            }
        } else {
            privatePost = new PrivatePost();
            privateTopic = privateTopicDao.find( topicId );
            setPagesNumber( (int) Math.ceil( privateTopic.getNbPrivatePosts() / NB_POSTS_PER_PAGE ) );
            paginatedPrivatePosts = privatePostDao.list( privateTopic, page, NB_POSTS_PER_PAGE );
            privateNotificationDao.delete( member.getId(), Long.valueOf( topicId ) );
        }
    }

    public List<PrivatePost> getPaginatedPrivatePosts() {
        return paginatedPrivatePosts;
    }

    public String create( Member member ) {
        privatePost.setIpAddress( Faces.getRemoteAddr() );
        privatePost.setCreationDate( new Date( System.currentTimeMillis() ) );
        privatePost.setAuthor( member );
        privatePost.setPrivateTopic( privateTopic );

        try {
            // TODO : si l'id de la dernière réponse sur le sujet n'est pas le même que quand le type a répondu, alors ça veut dire qu'un
            // autre gars a répondu ou que l'auteur a supprimé sa réponse, du coup il faut prévenir le posteur avant d'enregistrer sa
            // réponse...

            /*
             * if ( topic.getLastPost().equals( derniereReponseAfficheeSurLaPage ) ) { context.addMessage( null, new FacesMessage(
             * FacesMessage.SEVERITY_ERROR, "Le contenu du sujet a changé pendant que vous rédigiez votre message !", "Attention" ) );
             * return null; }
             */

            privatePostDao.create( privatePost );
            // on ajoute 1 au compteur dénormalisé (TODO : ça devrait être le boulot d'un Trigger BDD ou d'un JPA event)
            privateTopic.addPrivatePost();
            privateTopic.setLastPrivatePost( privatePost );
            privateTopicDao.update( privateTopic );
            PrivateNotification notification;
            for ( Member item : privateTopic.getParticipants() ) {
                if ( item.getId() != member.getId() ) { // On ne se notifie pas de sa propre réponse...
                    notification = new PrivateNotification();
                    notification.setMemberId( item.getId() );
                    notification.setPrivateTopicId( privateTopic.getId() );
                    notification.setPrivatePost( privatePost );
                    privateNotificationDao.create( notification ); // Rien ne sera créé s'il existe déjà une notification, voir
                                                                   // implémentation DAO.
                }
            }
            // post = null; // TODO : encore nécessaire après la redirection mise en place ci-après?
            Messages.addFlashGlobalInfo( "Votre réponse a bien été ajoutée." );
            return URL_PRIVATE_TOPIC_PAGE + privateTopic.getId() + "&faces-redirect=true";
        } catch ( DAOException e ) {
            // TODO : logger l'échec de la création d'une réponse
            e.printStackTrace();
            return URL_404;
        }
    }

    public PrivatePost getPrivatePost() {
        return privatePost;
    }

    public void setPrivatePost( PrivatePost privatePost ) {
        this.privatePost = privatePost;
    }

    public int getTopicId() {
        return topicId;
    }

    public void setTopicId( int topicId ) {
        this.topicId = topicId;
    }

    public PrivateTopic getPrivateTopic() {
        return privateTopic;
    }

    public void setPrivateTopic( PrivateTopic privateTopic ) {
        this.privateTopic = privateTopic;
    }

    public int getPagesNumber() {
        return pagesNumber;
    }

    public void setPagesNumber( int pagesNumber ) {
        this.pagesNumber = pagesNumber;
    }

    public int getPage() {
        return page;
    }

    public void setPage( int page ) {
        this.page = page;
    }

    /**
     * Cette méthode génère le fil d'Ariane pour la page d'un sujet donné.
     * 
     * @return la liste des items à afficher dans le fil d'Ariane.
     */
    public List<BreadCrumbItem> getBreadCrumb() {
        String path = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
        List<BreadCrumbItem> breadCrumb = BreadCrumbHelper.initBreadCrumb( path );
        BreadCrumbHelper.addPrivatesItem( breadCrumb, path, true );
        BreadCrumbHelper.addItem( breadCrumb, privateTopic.getTitle(), null );
        return breadCrumb;
    }
}