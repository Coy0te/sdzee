package com.sdzee.forums.beans;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;

import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.sdzee.breadcrumb.beans.BreadCrumbHelper;
import com.sdzee.breadcrumb.beans.BreadCrumbItem;
import com.sdzee.dao.DAOException;
import com.sdzee.forums.dao.AlertDao;
import com.sdzee.forums.dao.BookmarkDao;
import com.sdzee.forums.dao.ForumDao;
import com.sdzee.forums.dao.NotificationDao;
import com.sdzee.forums.dao.PostDao;
import com.sdzee.forums.dao.TopicDao;
import com.sdzee.forums.dao.VoteDao;
import com.sdzee.forums.entities.Alert;
import com.sdzee.forums.entities.Bookmark;
import com.sdzee.forums.entities.Forum;
import com.sdzee.forums.entities.Notification;
import com.sdzee.forums.entities.Post;
import com.sdzee.forums.entities.Topic;
import com.sdzee.forums.entities.Vote;
import com.sdzee.membres.entities.Member;

/**
 * PostsBackingBean est le bean sur lequel s'appuie notamment la page d'un sujet de forum. Il s'agit d'un ManagedBean JSF, ayant pour portée
 * une vue. Il contient une variable <code>sujetId</code> initialisée en amont par la Facelet <code>sujet.xhtml</code>.
 * 
 * @author Médéric Munier
 * @version %I%, %G%
 */
@ManagedBean( name = "postsBean" )
@ViewScoped
@URLMapping( id = "topic", pattern = "/forums/topic/#{topicId : postsBean.topicId}/", viewId = "/topic.jsf" )
public class PostsBackingBean implements Serializable {
    private static final long   serialVersionUID      = 1L;
    private static final String URL_FORUM_PAGE        = "/forum.jsf?forumId=";
    private static final String URL_TOPIC_PAGE        = "/topic.jsf?topicId=%d&page=%d&faces-redirect=true";
    private static final String URL_404               = "/404";
    private static final int    VOTE_UP               = 1;
    private static final int    VOTE_DOWN             = -1;
    private static final String SESSION_MEMBER        = "member";
    private static final double NB_POSTS_PER_PAGE     = 5;
    private static final String VOTE_OBJECT_TYPE_POST = "post";

    private Post                post;
    private Topic               topic;
    private Alert               alert;
    private Forum               forumDeplacement;
    private List<Post>          paginatedPosts;
    private int                 pagesNumber;
    private int                 topicId;
    private int                 page                  = 1;
    private boolean             bookmarked            = false;

    @EJB
    private PostDao             postDao;
    @EJB
    private TopicDao            topicDao;
    @EJB
    private ForumDao            forumDao;
    @EJB
    private AlertDao            alertDao;
    @EJB
    private VoteDao             voteDao;
    @EJB
    private BookmarkDao         bookmarkDao;
    @EJB
    private NotificationDao     notificationDao;

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
        post = ( post == null ? new Post() : post );
        alert = new Alert();
        forumDeplacement = new Forum();
        topic = topicDao.find( topicId );
        pagesNumber = (int) Math.ceil( postDao.count( topic ) / NB_POSTS_PER_PAGE );
        paginatedPosts = postDao.list( topic, page, (int) NB_POSTS_PER_PAGE );

        Member member = (Member) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
                .get( SESSION_MEMBER );
        if ( member != null ) {
            notificationDao.delete( member.getId(), Long.valueOf( topicId ) );
            bookmarked = bookmarkDao.find( member.getId(), Long.valueOf( topicId ) ) == null ? false : true;
        }
    }

    public List<Post> getPaginatedPosts() {
        return paginatedPosts;
    }

    public String create( Member member ) {
        if ( member != null ) {
            try {
                // on sauve le dernier post affiché à l'utilisateur
                Post lastDisplayedPost = ( topic.getLastPost() == null ? topic.getFirstPost() : topic.getLastPost() );
                // on rafraichit l'entité Topic, pour s'assurer qu'aucune modif n'a été apportée entre temps dessus
                topic = topicDao.refresh( topic );
                // on récupère le "vrai" dernier post, depuis le topic rafraichi
                Post lastActualPost = ( topic.getLastPost() == null ? topic.getFirstPost() : topic.getLastPost() );

                // on vérifie que le sujet n'est pas fermé
                if ( topic.isLocked() && member.getRights() < 3 ) {
                    return URL_404;
                }
                // on vérifie qu'un nouveau post n'a pas été ajouté entre temps par quelqu'un d'autre
                if ( !lastDisplayedPost.equals( lastActualPost ) ) {
                    Messages.addFlashGlobalWarn( "Attention, au moins un autre membre est intervenu dans la discussion pendant que vous rédigiez votre message !" );
                    return null;
                }

                post.setIpAddress( Faces.getRemoteAddr() );
                post.setCreationDate( new Date( System.currentTimeMillis() ) );
                post.setAuthor( member );
                post.setTopic( topic );

                postDao.create( post );
                topic.setLastPost( post );
                topicDao.update( topic );
                topic.getForum().setLastPost( post );
                forumDao.update( topic.getForum() );

                // Ajout auto d'un bookmark sur le sujet pour l'auteur de la réponse
                Bookmark bookmark = new Bookmark();
                bookmark.setMemberId( member.getId() );
                bookmark.setTopicId( topic.getId() );
                bookmarkDao.create( bookmark );
                // Ajout en série de notifications aux membres qui suivent le sujet
                List<Bookmark> bookmarks = bookmarkDao.list( topic );
                if ( bookmarks == null ) {
                    bookmarks = new ArrayList<Bookmark>();
                }
                Notification notification;
                for ( Bookmark item : bookmarks ) {
                    if ( item.getMemberId() != member.getId() ) { // On ne se notifie pas de sa propre réponse...
                        notification = new Notification();
                        notification.setMemberId( item.getMemberId() );
                        notification.setTopicId( item.getTopicId() );
                        notification.setPost( post );
                        notificationDao.create( notification ); // Rien ne sera créé s'il existe déjà une notification, voir implémentation
                                                                // DAO.
                    }
                }

                post = new Post(); // TODO : encore nécessaire après la redirection mise en place ci-après?
                Messages.addFlashGlobalInfo( "Votre réponse a bien été ajoutée." );
                return String.format( URL_TOPIC_PAGE, topic.getId(), page );
            } catch ( DAOException e ) {
                // TODO : logger l'échec de la création d'une réponse
                e.printStackTrace();
                return URL_404;
            }
        } else {
            // TODO: logger l'intrus qui essaie de créer un message sans y
            // être autorisé...
            return URL_404;
        }
    }

    public void editPost( Member member, Post post ) {
        if ( member != null
                && ( member.getRights() >= 3 || member.getNickName().equals( post.getAuthor().getNickName() ) ) ) {
            try {
                if ( post.equals( topic.getFirstPost() ) ) {
                    // Si on édite le premier post, on met aussi à jour le topic (au cas où le titre/sous-titre ait été modifié)
                    topicDao.update( topic );
                }
                post.setLastEditDate( new Date( System.currentTimeMillis() ) );
                post.setLastEditBy( member );
                postDao.update( post );

                // TODO : résolution auto des alertes associées si l'édit est fait par un staff ?
            } catch ( DAOException e ) {
                // TODO : logger l'échec de la mise à jour en base de la réponse
            }
        } else {
            // TODO : logger l'intrus qui essaie d'éditer un message sans y être
            // autorisé...
        }
    }

    public String solveTopic( Member member ) {
        if ( member != null
                && ( member.getRights() >= 3 || member.getNickName().equals(
                        topic.getFirstPost().getAuthor().getNickName() ) ) ) {
            try {
                // on commence par rafraichir l'entité Topic, pour s'assurer qu'aucune modif n'a été apportée entre temps dessus
                topic = topicDao.refresh( topic );

                topic.setSolved( !topic.isSolved() );
                topicDao.update( topic );
                return String.format( URL_TOPIC_PAGE, topic.getId(), 1 );
            } catch ( DAOException e ) {
                // TODO: logger l'échec de la mise à jour en base du sujet
                return URL_404;
            }
        } else {
            // TODO: logger l'intrus qui essaie de résoudre un message sans y
            // être autorisé...
            return URL_404;
        }
    }

    public String pinTopic( Member member ) {
        if ( member != null && ( member.getRights() >= 3 ) ) {
            try {
                // on commence par rafraichir l'entité Topic, pour s'assurer qu'aucune modif n'a été apportée entre temps dessus
                topic = topicDao.refresh( topic );

                topic.setSticky( !topic.isSticky() );
                topicDao.update( topic );
                Messages.addFlashGlobalInfo( "Le sujet " + ( topic.isSticky() ? "est maintenant" : "n''est plus" )
                        + " épinglé en Post-It." );
                return String.format( URL_TOPIC_PAGE, topic.getId(), 1 );
            } catch ( DAOException e ) {
                // TODO: logger l'échec de la mise à jour en base du sujet
                return URL_404;
            }
        } else {
            // TODO: logger l'intrus qui essaie de mettre en post-it un message sans y
            // être autorisé...
            return URL_404;
        }
    }

    public String lockTopic( Member member ) {
        if ( member != null && ( member.getRights() >= 3 ) ) {
            try {
                // on commence par rafraichir l'entité Topic, pour s'assurer qu'aucune modif n'a été apportée entre temps dessus
                topic = topicDao.refresh( topic );

                topic.setLocked( !topic.isLocked() );
                topicDao.update( topic );
                return String.format( URL_TOPIC_PAGE, topic.getId(), 1 );
            } catch ( DAOException e ) {
                // TODO: logger l'échec de la mise à jour en base du sujet
                return URL_404;
            }
        } else {
            // TODO: logger l'intrus qui essaie de mettre en post-it un message sans y
            // être autorisé...
            return URL_404;
        }
    }

    public String moveTopic( Member member ) {
        if ( member != null && member.getRights() >= 3 && forumDeplacement != null
                && topic.getForum() != forumDeplacement ) {
            try {
                // on commence par rafraichir l'entité Topic, pour s'assurer qu'aucune modif n'a été apportée entre temps dessus
                topic = topicDao.refresh( topic );

                Forum forumBefore = topic.getForum();
                topic.setForum( forumDeplacement );
                if ( forumDeplacement.getLastPost() == null
                        || forumDeplacement.getLastPost().getId() < ( topic.getLastPost() == null ? topic
                                .getFirstPost().getId() : topic
                                .getLastPost().getId() ) ) {
                    forumDeplacement.setLastPost( topic.getLastPost() == null ? topic.getFirstPost() : topic
                            .getLastPost() );
                }
                forumDao.update( forumDeplacement );
                topicDao.update( topic );
                forumDeplacement = new Forum();
                if ( ( topic.getLastPost() != null && topic.getLastPost().equals( forumBefore.getLastPost() ) )
                        || ( topic.getLastPost() == null && topic.getFirstPost().equals( forumBefore.getLastPost() ) ) ) {
                    forumBefore.setLastPost( postDao.findLast( forumBefore ) );
                }
                forumDao.update( forumBefore );
                // TODO : logger la modification faite par le modo (date, membre, changement)
                Messages.addFlashGlobalInfo( "Le sujet a bien été déplacé." );
                return String.format( URL_TOPIC_PAGE, topic.getId(), 1 );
            } catch ( DAOException e ) {
                // TODO: logger l'échec de la mise à jour en base de la réponse
                return URL_404;
            }
        } else {
            // TODO: logger l'intrus qui essaie de jouer aux modos...
            return URL_404;
        }
    }

    public String followTopic( Member member ) {
        if ( member != null ) {
            try {
                // on commence par rafraichir l'entité Topic, pour s'assurer qu'aucune modif n'a été apportée entre temps dessus
                topic = topicDao.refresh( topic );

                // tester si bookmark existe pour membre et topic
                Bookmark bookmark = bookmarkDao.find( member.getId(), topic.getId() );

                if ( bookmark == null ) {
                    // Ajout auto d'un bookmark sur le sujet pour l'auteur de la réponse
                    bookmark = new Bookmark();
                    bookmark.setMemberId( member.getId() );
                    bookmark.setTopicId( topic.getId() );
                    bookmarkDao.create( bookmark );
                    Messages.addFlashGlobalInfo( "Le sujet a bien été mis en favori." );
                } else {
                    bookmarkDao.delete( bookmark );
                    Messages.addFlashGlobalInfo( "Le sujet ne fait plus partie de vos favoris." );
                }
                return String.format( URL_TOPIC_PAGE, topic.getId(), 1 );
                // si oui, le supprimer
            } catch ( DAOException e ) {
                // TODO: logger l'échec de la mise à jour en base du sujet
                return URL_404;
            }
        } else {
            // TODO: logger l'intrus qui essaie de suivre un topic sans
            // être connecté...
            return URL_404;
        }
    }

    public void vote( Long memberId, Long objectId, int value, Post post ) {
        Vote vote = voteDao.find( memberId, objectId, VOTE_OBJECT_TYPE_POST );
        post = postDao.refresh( post );
        if ( vote == null ) {
            // le membre n'a pas encore voté sur ce message
            vote = new Vote();
            vote.setMemberId( memberId );
            vote.setObjectId( objectId );
            vote.setObjectType( VOTE_OBJECT_TYPE_POST );
            vote.setValue( value );
            voteDao.create( vote );
            // il faut ensuite MAJ le compteur dénormalisé sur la table réponse
            if ( value > 0 ) {
                post.addUpVote();
            } else {
                post.addDownVote();
            }
            postDao.update( post );
        } else if ( vote.getValue() == value ) {
            // le membre avait déjà effectué le même vote, donc c'est un re-clic
            // et il faut annuler son précédent vote
            voteDao.delete( vote );
            if ( value > 0 ) {
                post.removeUpVote();
            } else {
                post.removeDownVote();
            }
            postDao.update( post );
        } else {
            // le membre avait déjà voté, mais différemment, donc il faut
            // remplacer son ancien vote
            vote.setValue( value );
            voteDao.update( vote ); // TODO : nécessaire ou pas ? Si l'entité vote n'est pas détachée...
            if ( value > 0 ) {
                post.removeDownVote();
                post.addUpVote();
            } else {
                post.removeUpVote();
                post.addDownVote();
            }
            postDao.update( post );
        }
    }

    public void upVotePost( Member member, Post post ) {
        // on ne vote pas sur ses propres messages
        if ( !member.getNickName().equals( post.getAuthor().getNickName() ) ) {
            vote( member.getId(), post.getId(), VOTE_UP, post );
        }
    }

    public void downVotePost( Member member, Post post ) {
        if ( !member.getNickName().equals( post.getAuthor().getNickName() ) ) {
            vote( member.getId(), post.getId(), VOTE_DOWN, post );
        }
    }

    public String deletePost( Member member, Post post ) {
        if ( member != null && ( member.getRights() >= 3 ) ) {
            try {
                // on commence par rafraichir l'entité Post et Topic, pour s'assurer qu'aucune modif n'a été apportée entre temps dessus
                post = postDao.refresh( post );
                topic = topicDao.refresh( topic );

                if ( post.equals( topic.getFirstPost() ) ) {
                    // si on est sur le premier post, on supprime le topic et on redirige vers le forum
                    postDao.delete( post );
                    // TODO : mettre à jour le lastPost du forum
                    if ( topic.getForum().getLastPost().equals( post )
                            || topic.getForum().getLastPost().equals( topic.getLastPost() ) ) {
                        topic.getForum().setLastPost( postDao.findLast( topic.getForum() ) );
                    }
                    forumDao.update( topic.getForum() );
                    topicDao.delete( topic ); // TODO : est-ce que ça supprimerait automatiquement le post précédent en même temps ?
                    Messages.addFlashGlobalInfo( "Le sujet a bien été supprimé." );
                    return String.format( URL_TOPIC_PAGE, topic.getId(), 1 );
                } else if ( post.equals( topic.getLastPost() ) ) {
                    // si on est sur le dernier post, on modifie le lastPost sur le topic
                    postDao.delete( post );
                    topic.setLastPost( postDao.findLast( topic ) );
                    topicDao.update( topic );
                    // si on vient de supprimer le lastPost du forum
                    if ( topic.getForum().getLastPost().equals( post ) ) {
                        topic.getForum().setLastPost( postDao.findLast( topic.getForum() ) );
                        forumDao.update( topic.getForum() );
                    }
                    Messages.addFlashGlobalInfo( "Le message a bien été supprimé." );
                    return String.format( URL_TOPIC_PAGE, topic.getId(), 1 );
                } else {
                    postDao.delete( post );
                    Messages.addFlashGlobalInfo( "Le message a bien été supprimé." );
                    return String.format( URL_TOPIC_PAGE, topic.getId(), 1 );
                }

            } catch ( DAOException e ) {
                // TODO: logger l'échec de la mise à jour en base du sujet
                return URL_404;
            }
        } else {
            // TODO: logger l'intrus qui essaie de modérer un message sans y être autorisé...
            return URL_404;
        }
    }

    public String markPostAsUseful( Member member, Post post ) {
        if ( member != null
                && ( member.getRights() >= 3 || member.getNickName().equals(
                        topic.getFirstPost().getAuthor().getNickName() ) )
                && !post.equals( topic.getFirstPost() ) ) {
            try {
                // on commence par rafraichir l'entité Post, pour s'assurer qu'aucune modif n'a été apportée entre temps dessus
                post = postDao.refresh( post );

                post.setUseful( !post.isUseful() );
                postDao.update( post );
                Messages.addFlashGlobalInfo( "Le message " + ( post.isUseful() ? "a bien été" : "n''est plus" )
                        + " marqué comme utile, merci !" );
                return String.format( URL_TOPIC_PAGE, topic.getId(), page );
            } catch ( DAOException e ) {
                // TODO: logger l'échec de la mise à jour en base du post
                return URL_404;
            }
        } else {
            // TODO: logger l'intrus qui essaie de mettre en avant une réponse sans y être autorisé...
            return URL_404;
        }
    }

    public String hidePost( Member member, Post post ) {
        if ( member != null && ( member.getRights() >= 3 ) ) {
            try {
                // on commence par rafraichir l'entité Post, pour s'assurer qu'aucune modif n'a été apportée entre temps dessus
                post = postDao.refresh( post );

                post.setHidden( !post.isHidden() );
                post.setHiddenBy( member );
                post.setHiddenCause( "TODO : Message de masquage." );
                postDao.update( post );
                Messages.addFlashGlobalInfo( "Le message a bien été " + ( post.isHidden() ? "masqué." : "restauré." ) );
                return String.format( URL_TOPIC_PAGE, topic.getId(), page );
            } catch ( DAOException e ) {
                // TODO: logger l'échec de la mise à jour en base du post
                return URL_404;
            }
        } else {
            // TODO: logger l'intrus qui essaie de masquer une réponse sans y être autorisé...
            return URL_404;
        }
    }

    public String alertPost( Member member, Post post ) throws IOException {
        if ( member != null ) {
            try {
                // on commence par rafraichir l'entité Post, pour s'assurer qu'aucune modif n'a été apportée entre temps dessus
                post = postDao.refresh( post );

                alert.setAuthor( member );
                alert.setCreationDate( new Date( System.currentTimeMillis() ) );
                alert.setPost( post );
                alert.setText( "TODO: implémenter la saisie de message d'alerte." );
                alertDao.create( alert );
                // TODO : ci-dessous bien nécessaire ?
                post.addAlert( alert );
                postDao.update( post );
                alert = new Alert();
                Messages.addFlashGlobalInfo( "Votre alerte a bien été envoyée au Staff, merci !" );
                return String.format( URL_TOPIC_PAGE, topic.getId(), page );
            } catch ( DAOException e ) {
                // TODO: logger l'échec de la mise à jour en base
                return URL_404;
            }
        } else {
            // TODO: logger l'intrus qui essaie d'alerter un post sans y être autorisé...
            return URL_404;
        }
    }

    public String solvePostAlert( Member member, Alert alert, Post post ) throws IOException {
        if ( member != null && member.getRights() >= 3 ) {
            try {
                alertDao.delete( alert );
                // on rafraichit l'entité Post, pour s'assurer qu'aucune modif n'a été apportée entre temps dessus
                post = postDao.refresh( post );
                post.removeAlert( alert );
                postDao.update( post );
                alert = new Alert();
                Messages.addFlashGlobalInfo( "L'alerte a bien été résolue !" );

                // TODO : envoyer un MP auto à l'auteur de l'alerte pour le prévenir de la résolution.

                return String.format( URL_TOPIC_PAGE, topic.getId(), page );
            } catch ( DAOException e ) {
                // TODO: logger l'échec de la mise à jour en base
                return URL_404;
            }
        } else {
            // TODO: logger l'intrus qui essaie de résoudre une alerte sur un post sans y être autorisé...
            return URL_404;
        }
    }

    public Post getPost() {
        return post;
    }

    public void setPost( Post post ) {
        this.post = post;
    }

    public int getTopicId() {
        return topicId;
    }

    public void setTopicId( int topicId ) {
        this.topicId = topicId;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic( Topic topic ) {
        this.topic = topic;
    }

    public Forum getForumDeplacement() {
        return forumDeplacement;
    }

    public void setForumDeplacement( Forum forumDeplacement ) {
        this.forumDeplacement = forumDeplacement;
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

    public boolean isBookmarked() {
        return bookmarked;
    }

    public void setBookmarked( boolean bookmarked ) {
        this.bookmarked = bookmarked;
    }

    /**
     * Cette méthode génère le fil d'Ariane pour la page d'un sujet donné.
     * 
     * @return la liste des items à afficher dans le fil d'Ariane.
     */
    public List<BreadCrumbItem> getBreadCrumb() {
        String path = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
        List<BreadCrumbItem> breadCrumb = BreadCrumbHelper.initBreadCrumb( path );
        BreadCrumbHelper.addForumsItem( breadCrumb, path, true );
        BreadCrumbHelper.addItem( breadCrumb, topic.getForum().getTitle(), path + URL_FORUM_PAGE
                + topic.getForum().getId() );
        BreadCrumbHelper.addItem( breadCrumb, topic.getTitle(), null );
        return breadCrumb;
    }
}