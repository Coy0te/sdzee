package com.sdzee.privatemessages.beans;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.omnifaces.util.Faces;

import com.sdzee.breadcrumb.beans.BreadCrumbHelper;
import com.sdzee.breadcrumb.beans.BreadCrumbItem;
import com.sdzee.dao.DAOException;
import com.sdzee.forums.dao.ForumDao;
import com.sdzee.membres.dao.MemberDao;
import com.sdzee.membres.entities.Member;
import com.sdzee.privatemessages.dao.PrivateNotificationDao;
import com.sdzee.privatemessages.dao.PrivatePostDao;
import com.sdzee.privatemessages.dao.PrivateTopicDao;
import com.sdzee.privatemessages.entities.PrivateNotification;
import com.sdzee.privatemessages.entities.PrivatePost;
import com.sdzee.privatemessages.entities.PrivateTopic;

/**
 * PrivateTopicsBackingBean est le bean sur lequel s'appuie la page des MP. Il s'agit d'un ManagedBean JSF, ayant pour portée une vue.
 * 
 * @author Médéric Munier
 * @version %I%, %G%
 */
@ManagedBean( name = "privateTopicsBean" )
@ViewScoped
public class PrivateTopicsBackingBean implements Serializable {
    private static final long      serialVersionUID           = 1L;
    private static final String    URL_PRIVATE_TOPIC_PAGE     = "/privateTopic.jsf?topicId=";
    private static final String    PARTICIPANTS_SEPARATOR     = ",";
    private static final double    NB_PRIVATE_TOPICS_PER_PAGE = 5;
    private static final String    SESSION_MEMBER             = "member";

    private PrivateTopic           privateTopic;
    private PrivatePost            privatePost;
    private String                 participantsNames;
    private List<PrivateTopic>     paginatedPrivateTopics;
    private int                    pagesNumber;
    private int                    page                       = 1;

    @EJB
    private PrivateTopicDao        privateTopicDao;
    @EJB
    private PrivatePostDao         privatePostDao;
    @EJB
    private PrivateNotificationDao privateNotificationDao;
    @EJB
    private MemberDao              memberDao;

    /**
     * Cette méthode initialise la variable d'instance <code>forum</code> en récupérant en base le forum correspondant à l'id transmis par la Facelet
     * <code>forum.xhtml</code>, contenu dans la variable <code>forumId</code>.
     * <p>
     * Elle est exécutée automatiquement par JSF, après le constructeur de la classe s'il existe. À l'appel du constructeur classique, le bean n'est
     * pas encore initialisé, et donc aucune dépendance n'est injectée. Cependant lorsque cette méthode est appelée, le bean est déjà initialisé et il
     * est donc possible de faire appel à des dépendances. Ici, c'est le DAO {@link ForumDao} injecté via l'annotation <code>@EJB</code> qui entre en
     * jeu.
     * <p>
     * À la différence de la plupart des autres backing-beans, cette méthode n'est pas annotée avec <code>@PostConstruct</code>. Ceci est simplement
     * dû au fait qu'elle fait appel à une variable qui est initialisée depuis la vue, en l'occurrence l'id du sujet courant. Puisqu'elle dépend de
     * l'action du visiteur, son cycle de vie ne peut pas être entièrement géré par JSF.
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
            privateTopic = new PrivateTopic();
            privatePost = new PrivatePost();
            pagesNumber = (int) Math.ceil( privateTopicDao.count( member ) / NB_PRIVATE_TOPICS_PER_PAGE );
            paginatedPrivateTopics = privateTopicDao.list( member, page, (int) NB_PRIVATE_TOPICS_PER_PAGE );
        }
    }

    public List<PrivateTopic> getPaginatedPrivateTopics() {
        return paginatedPrivateTopics;
    }

    /**
     * Cette méthode permet la création d'un {@link PrivateTopic}.
     * 
     * @param member le membre à l'origine de l'action.
     * @throws IOException si la page vers laquelle effectuer une redirection n'existe pas.
     */
    public void create( Member member ) throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();
        privatePost.setIpAddress( Faces.getRemoteAddr() );
        privatePost.setCreationDate( new Date( System.currentTimeMillis() ) );
        privatePost.setAuthor( member );
        privateTopic.setAuthor( member );
        try {
            privateTopicDao.create( privateTopic );
            context.addMessage( null, new FacesMessage( FacesMessage.SEVERITY_INFO, "Nouveau MP créé avec succès",
                    privateTopic.getTitle() ) );
            // TODO : à surveiller, manque peut-être un refresh ici pour que le topic récupère bien son ID après sa création !
            privatePost.setPrivateTopic( privateTopic );
            privatePostDao.create( privatePost );
            // TODO : à surveiller, manque peut-être un refresh ici pour que le post récupère bien son ID après sa création !
            privateTopic.setFirstPrivatePost( privatePost );
            privateTopicDao.update( privateTopic );
            // la valeur nbPosts est par défaut mise à 1 en BDD, donc rien à faire lors de la création d'un topic à ce niveau.
            // pareil pour lastPost qui est mis à null par défaut en BDD
            // Quand on arrive ici, on est déjà passés par le validator, donc tous les pseudos saisis sont OK.
            participantsNames = participantsNames.replaceAll( "\\s+", "" );
            List<String> participantsNickNames = new ArrayList<String>( Arrays.asList( participantsNames
                    .split( PARTICIPANTS_SEPARATOR ) ) );
            // On ajoute l'auteur dans la liste des participants, pour que ça soit complet.
            participantsNickNames.add( member.getNickName() );
            List<Member> participants = memberDao.list( participantsNickNames );
            privateTopic.setParticipants( participants );
            privateTopicDao.update( privateTopic );

            // Enregistrer des notifications pour les participants au MP...
            PrivateNotification notification;
            for ( Member item : participants ) {
                if ( item.getId() != member.getId() ) { // On ne se notifie pas de son propre MP...
                    notification = new PrivateNotification();
                    notification.setMemberId( item.getId() );
                    notification.setPrivateTopicId( privateTopic.getId() );
                    notification.setPrivatePost( privatePost );
                    privateNotificationDao.create( notification ); // Rien ne sera créé s'il existe déjà une notification, voir implémentation DAO.
                }
            }

            ExternalContext externalContext = context.getExternalContext();
            externalContext.redirect( externalContext.getRequestContextPath() + URL_PRIVATE_TOPIC_PAGE
                    + String.valueOf( privateTopic.getId() ) );
        } catch ( DAOException e ) {
            context.addMessage( null, new FacesMessage( FacesMessage.SEVERITY_ERROR, "Echec de la création du sujet",
                    "Une erreur est survenue..." ) );
            // TODO: logger
        }
    }

    public int getPagesNumber() {
        return pagesNumber;
    }

    public PrivateTopic getPrivateTopic() {
        return privateTopic;
    }

    public void setPrivateTopic( PrivateTopic privateTopic ) {
        this.privateTopic = privateTopic;
    }

    public PrivatePost getPrivatePost() {
        return privatePost;
    }

    public void setPrivatePost( PrivatePost privatePost ) {
        this.privatePost = privatePost;
    }

    public int getPage() {
        return page;
    }

    public void setPage( int page ) {
        this.page = page;
    }

    public String getParticipantsNames() {
        return participantsNames;
    }

    public void setParticipantsNames( String participantsNames ) {
        this.participantsNames = participantsNames;
    }

    /**
     * Cette méthode génère le fil d'Ariane pour la page d'un forum donné.
     * 
     * @return la liste des items à afficher dans le fil d'Ariane.
     */
    public List<BreadCrumbItem> getBreadCrumb() {
        String path = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
        List<BreadCrumbItem> breadCrumb = BreadCrumbHelper.initBreadCrumb( path );
        // TODO : ci-dessous, changer l'entrée forums du fil par "MP"
        BreadCrumbHelper.addPrivatesItem( breadCrumb, path, false );
        return breadCrumb;
    }
}