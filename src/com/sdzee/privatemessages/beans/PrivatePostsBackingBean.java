package com.sdzee.privatemessages.beans;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;

import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.sdzee.breadcrumb.beans.BreadCrumbHelper;
import com.sdzee.breadcrumb.beans.BreadCrumbItem;
import com.sdzee.dao.DAOException;
import com.sdzee.forums.dao.NotificationDao;
import com.sdzee.forums.dao.TopicDao;
import com.sdzee.membres.dao.MemberDao;
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
@URLMapping( id = "mpPost", pattern = "/mp/#{topicId : privatePostsBean.topicId}/", viewId = "/privateTopic.jsf" )
public class PrivatePostsBackingBean implements Serializable {
    private static final long      serialVersionUID       = 1L;
    private static final String    URL_PRIVATE_TOPIC_PAGE = "/privateTopic.jsf?page=%d&topicId=%d&faces-redirect=true";
    private static final String    URL_404                = "/404";
    private static final String    SESSION_MEMBER         = "member";
    private static final String    PARTICIPANTS_SEPARATOR = ",";
    private static final double    NB_POSTS_PER_PAGE      = 5;

    private PrivatePost            privatePost;
    private PrivateTopic           privateTopic;
    private List<PrivatePost>      paginatedPrivatePosts;
    private int                    pagesNumber;
    private int                    topicId;
    private int                    page                   = 1;
    private String                 newParticipantsNames;

    @EJB
    private PrivatePostDao         privatePostDao;
    @EJB
    private PrivateTopicDao        privateTopicDao;
    @EJB
    private MemberDao              memberDao;
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
                externalContext.redirect( externalContext.getRequestContextPath() + "/logon?next=/mp/" );
                return;
            } catch ( IOException e ) {
            }
        } else {
            privatePost = ( privatePost == null ? new PrivatePost() : privatePost );
            privateTopic = privateTopicDao.find( topicId );

            if ( !privateTopic.getParticipants().contains( member ) ) {
                // si le membre n'est pas un participant du MP, on le dégage et on loggue
                try {
                    // TODO : logger la tentative d'espionnage du MP
                    externalContext.redirect( externalContext.getRequestContextPath() + URL_404 );
                    return;
                } catch ( IOException e ) {
                }
            }

            pagesNumber = (int) Math.ceil( privatePostDao.count( privateTopic ) / NB_POSTS_PER_PAGE );
            paginatedPrivatePosts = privatePostDao.list( privateTopic, page, (int) NB_POSTS_PER_PAGE );
            privateNotificationDao.delete( member.getId(), Long.valueOf( topicId ) );
        }
    }

    public List<PrivatePost> getPaginatedPrivatePosts() {
        return paginatedPrivatePosts;
    }

    public String create( Member member ) {
        if ( member != null ) {
            try {
                // on sauve le dernier post affiché à l'utilisateur
                PrivatePost lastDisplayedPrivatePost = ( privateTopic.getLastPrivatePost() == null ? privateTopic
                        .getFirstPrivatePost() : privateTopic.getLastPrivatePost() );
                // on rafraichit l'entité PrivateTopic, pour s'assurer qu'aucune modif n'a été apportée entre temps dessus
                privateTopic = privateTopicDao.refresh( privateTopic );
                // on récupère le "vrai" dernier post, depuis le topic rafraichi
                PrivatePost lastActualPrivatePost = ( privateTopic.getLastPrivatePost() == null ? privateTopic
                        .getFirstPrivatePost() : privateTopic.getLastPrivatePost() );

                // on vérifie qu'un nouveau post n'a pas été ajouté entre temps par quelqu'un d'autre
                if ( !lastDisplayedPrivatePost.equals( lastActualPrivatePost ) ) {
                    Messages.addFlashGlobalWarn( "Attention, au moins un autre membre est intervenu dans la discussion pendant que vous rédigiez votre message !" );
                    return null;
                }

                privatePost.setIpAddress( Faces.getRemoteAddr() );
                privatePost.setCreationDate( new Date( System.currentTimeMillis() ) );
                privatePost.setAuthor( member );
                privatePost.setPrivateTopic( privateTopic );

                privatePostDao.create( privatePost );
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
                privatePost = new PrivatePost(); // TODO : encore nécessaire après la redirection mise en place ci-après?
                Messages.addFlashGlobalInfo( "Votre réponse a bien été ajoutée." );
                return String.format( URL_PRIVATE_TOPIC_PAGE, page, privateTopic.getId() );
            } catch ( DAOException e ) {
                // TODO : logger l'échec de la création d'un message
                e.printStackTrace();
                return URL_404;
            }
        } else {
            // TODO: logger l'intrus qui essaie de créer un message sans y
            // être autorisé...
            return URL_404;
        }
    }

    public void editPrivatePost( Member member, PrivatePost privatePost ) {
        if ( member != null
                && ( member.getRights() >= 3 || member.getNickName().equals( privatePost.getAuthor().getNickName() ) ) ) {
            try {
                // si le membre est Staff, on enregistre l'édition sans conditions
                if ( member.getRights() >= 3 ) {
                    privatePost.setLastEditDate( new Date( System.currentTimeMillis() ) );
                    privatePost.setLastEditBy( member );
                    privatePostDao.update( privatePost );
                    return;
                }
                // sinon, on rafraichit l'entité PrivateTopic, pour s'assurer qu'aucune modif n'a été apportée entre temps dessus
                privateTopic = privateTopicDao.refresh( privateTopic );
                // et on vérifie que le message en train d'être édité ici est bien le dernier du MP
                if ( privatePost.equals( privateTopic.getLastPrivatePost() == null ? privateTopic
                        .getFirstPrivatePost() : privateTopic.getLastPrivatePost() ) ) {
                    privatePost.setLastEditDate( new Date( System.currentTimeMillis() ) );
                    privatePost.setLastEditBy( member );
                    privatePostDao.update( privatePost );
                } else {
                    // TODO : gérer la tentative de modif impossible car nouveau message posté entre-temps
                }
            } catch ( DAOException e ) {
                // TODO : logger l'échec de la mise à jour en base de la réponse
            }
        } else {
            // TODO : logger l'intrus qui essaie d'éditer un message sans y être
            // autorisé...
        }
    }

    public String addParticipants( Member member ) {
        if ( member != null
                && ( member.getRights() >= 3 || member.getNickName().equals( privateTopic.getAuthor().getNickName() ) ) ) {
            try {
                // on rafraichit l'entité PrivateTopic, pour s'assurer qu'aucune modif n'a été apportée entre temps dessus
                privateTopic = privateTopicDao.refresh( privateTopic );

                // Quand on arrive ici, on est déjà passés par le validator, donc tous les pseudos saisis sont OK.
                newParticipantsNames = newParticipantsNames.replaceAll( "\\s+", "" );
                List<String> participantsNickNames = new ArrayList<String>( Arrays.asList( newParticipantsNames
                        .split( PARTICIPANTS_SEPARATOR ) ) );
                List<Member> newParticipants = memberDao.list( participantsNickNames );
                // on n'ajoute un participant que s'il n'est pas déjà dans la liste
                for ( Member participant : newParticipants ) {
                    if ( !privateTopic.getParticipants().contains( participant ) ) {
                        privateTopic.addParticipant( participant );
                    }
                }
                privateTopicDao.update( privateTopic );
                return String.format( URL_PRIVATE_TOPIC_PAGE, 1, privateTopic.getId() );
            } catch ( DAOException e ) {
                // TODO : logger l'échec de la mise à jour en base de la réponse
                return URL_404;
            }
        } else {
            // TODO : logger l'intrus qui essaie d'ajouter un participant sans y être
            // autorisé...
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

    public String getNewParticipantsNames() {
        return newParticipantsNames;
    }

    public void setNewParticipantsNames( String newParticipantsNames ) {
        this.newParticipantsNames = newParticipantsNames;
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