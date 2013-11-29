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

import com.sdzee.breadcrumb.beans.BreadCrumbHelper;
import com.sdzee.breadcrumb.beans.BreadCrumbItem;
import com.sdzee.dao.DAOException;
import com.sdzee.forums.dao.BookmarkDao;
import com.sdzee.forums.dao.ForumDao;
import com.sdzee.forums.dao.NotificationDao;
import com.sdzee.forums.dao.PostDao;
import com.sdzee.forums.dao.TopicDao;
import com.sdzee.forums.dao.VoteDao;
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
public class PostsBackingBean implements Serializable {
    private static final long   serialVersionUID       = 1L;
    private static final String URL_FORUM_PAGE         = "/forum.jsf?forumId=";
    private static final String URL_TOPIC_PAGE         = "/topic.jsf?topicId=";
    private static final String URL_404                = "/404.jsf";
    private static final int    VOTE_UP                = 1;
    private static final int    VOTE_DOWN              = -1;
    private static final int    DROITS_REQUIS_MASQUAGE = 3;
    private static final String SESSION_MEMBER         = "member";
    private static final int    NB_POSTS_PER_PAGE      = 5;
    private static final String VOTE_OBJECT_TYPE_POST  = "post";

    private Post                post;
    private Topic               topic;
    private Forum               forumDeplacement;
    private List<Post>          paginatedPosts;
    private int                 pagesNumber;
    private int                 topicId;
    private int                 page                   = 1;

    @EJB
    private PostDao             postDao;
    @EJB
    private TopicDao            topicDao;
    @EJB
    private ForumDao            forumDao;
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
        post = new Post();
        forumDeplacement = new Forum();
        topic = topicDao.find( topicId );
        setPagesNumber( (int) Math.ceil( topic.getNbPosts() / NB_POSTS_PER_PAGE ) );
        paginatedPosts = postDao.list( topic, page, NB_POSTS_PER_PAGE );

        Member member = (Member) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
                .get( SESSION_MEMBER );
        if ( member != null ) {
            notificationDao.delete( member.getId(), Long.valueOf( topicId ) );
        }
    }

    public List<Post> getPaginatedPosts() {
        return paginatedPosts;
    }

    public String create( Member member ) {
        // on commence par vérifier que le sujet n'est pas fermé
        if ( topic.isLocked() && member.getRights() < 3 ) {
            return URL_404;
        }
        post.setIpAddress( Faces.getRemoteAddr() );
        post.setCreationDate( new Date( System.currentTimeMillis() ) );
        post.setAuthor( member );
        post.setTopic( topic );

        try {
            // TODO : si l'id de la dernière réponse sur le sujet n'est pas le même que quand le type a répondu, alors ça veut dire qu'un
            // autre gars a répondu ou que l'auteur a supprimé sa réponse, du coup il faut prévenir le posteur avant d'enregistrer sa
            // réponse...

            /*
             * if ( topic.getLastPost().equals( derniereReponseAfficheeSurLaPage ) ) { context.addMessage( null, new FacesMessage(
             * FacesMessage.SEVERITY_ERROR, "Le contenu du sujet a changé pendant que vous rédigiez votre message !", "Attention" ) );
             * return null; }
             */

            postDao.create( post );
            // on ajoute 1 au compteur dénormalisé (TODO : ça devrait être le boulot d'un Trigger BDD ou d'un JPA event)
            topic.addPost();
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
                    notificationDao.create( notification ); // Rien ne sera créé s'il existe déjà une notification, voir implémentation DAO.
                }
            }
            // post = null; // TODO : encore nécessaire après la redirection mise en place ci-après?
            return URL_TOPIC_PAGE + topic.getId() + "&faces-redirect=true";
        } catch ( DAOException e ) {
            // TODO : logger l'échec de la création d'une réponse
            e.printStackTrace();
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
                topic.setSolved( topic.isSolved() ? false : true );
                topicDao.update( topic );
                return URL_TOPIC_PAGE + topic.getId() + "&faces-redirect=true";
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
                topic.setSticky( topic.isSticky() ? false : true );
                topicDao.update( topic );
                return URL_TOPIC_PAGE + topic.getId() + "&faces-redirect=true";
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
                topic.setLocked( topic.isLocked() ? false : true );
                topicDao.update( topic );
                return URL_TOPIC_PAGE + topic.getId() + "&faces-redirect=true";
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
                Forum forumBefore = topic.getForum();
                topic.setForum( forumDeplacement );
                // on ajoute 1 au compteur des topics (devrait être un bdd trigger ou jpa event)
                forumDeplacement.addTopic();
                if ( forumDeplacement.getLastPost() == null
                        || forumDeplacement.getLastPost().getId() < ( topic.getLastPost() == null ? topic
                                .getFirstPost().getId() : topic
                                .getLastPost().getId() ) ) {
                    forumDeplacement.setLastPost( topic.getLastPost() == null ? topic.getFirstPost() : topic
                            .getLastPost() );
                }
                forumDao.update( forumDeplacement );
                topicDao.update( topic );
                forumDeplacement = null;
                // on retire 1 du compteur des topics (devrait être un bdd trigger ou jpa event)
                forumBefore.removeTopic();
                if ( ( topic.getLastPost() != null && topic.getLastPost().equals( forumBefore.getLastPost() ) )
                        || ( topic.getLastPost() == null && topic.getFirstPost().equals( forumBefore.getLastPost() ) ) ) {
                    forumBefore.setLastPost( postDao.findLast( forumBefore ) );
                }
                forumDao.update( forumBefore );
                // TODO : logger la modification faite par le modo (date, membre, changement)
                return URL_TOPIC_PAGE + topic.getId() + "&faces-redirect=true";
            } catch ( DAOException e ) {
                // TODO: logger l'échec de la mise à jour en base de la réponse
                return URL_404;
            }
        } else {
            // TODO: logger l'intrus qui essaie de jouer aux modos...
            return URL_TOPIC_PAGE + topic.getId() + "&faces-redirect=true";
        }
    }

    public void vote( Long memberId, Long objectId, int value, Post post ) {
        Vote vote = voteDao.find( memberId, objectId, VOTE_OBJECT_TYPE_POST );
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
                postDao.update( post );
            } else {
                post.addDownVote();
                postDao.update( post );
            }
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

    // TODO : refaire ça avec du if(objet instanceOf Reponse)... et ainsi se
    // débarrasser des arguments superflus

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

    public void signaler( Post post ) throws IOException {
        Member member = (Member) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
                .get( SESSION_MEMBER );
        if ( member != null ) {
            // TODO : créer une table d'alertes ? si oui, le DAO et l'entité qui vont avec.
            // alerteDao.signaler( reponse );
        }
    }

    public String deletePost( Member member, Post post ) {
        if ( member != null && ( member.getRights() >= 3 ) ) {
            try {
                if ( post.equals( topic.getFirstPost() ) ) {
                    // si on est sur le premier post, on supprime le topic et on redirige vers le forum
                    postDao.delete( post );
                    // on retire 1 au compteur dénormalisé (TODO : ça devrait être le boulot d'un Trigger BDD ou d'un JPA event)
                    topic.getForum().removeTopic();
                    // TODO : mettre à jour le lastPost du forum
                    if ( topic.getForum().getLastPost().equals( post )
                            || topic.getForum().getLastPost().equals( topic.getLastPost() ) ) {
                        topic.getForum().setLastPost( postDao.findLast( topic.getForum() ) );
                    }
                    forumDao.update( topic.getForum() );
                    long forumId = topic.getForum().getId();
                    topicDao.delete( topic ); // TODO : est-ce que ça supprimerait automatiquement le post précédent en même temps ?
                    return URL_FORUM_PAGE + forumId + "&faces-redirect=true";
                } else if ( post.equals( topic.getLastPost() ) ) {
                    // si on est sur le dernier post, on modifie le lastPost sur le topic
                    postDao.delete( post );
                    topic.setLastPost( postDao.findLast( topic ) );
                    topic.removePost();
                    topicDao.update( topic );
                    // si on vient de supprimer le lastPost du forum
                    if ( topic.getForum().getLastPost().equals( post ) ) {
                        topic.getForum().setLastPost( postDao.findLast( topic.getForum() ) );
                        forumDao.update( topic.getForum() );
                    }
                    return URL_TOPIC_PAGE + topic.getId() + "&faces-redirect=true";
                } else {
                    postDao.delete( post );
                    // on retire 1 au compteur dénormalisé (TODO : ça devrait être le boulot d'un Trigger BDD ou d'un JPA event)
                    topic.removePost();
                    topicDao.update( topic );
                    return URL_TOPIC_PAGE + topic.getId() + "&faces-redirect=true";
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
                post.setUseful( post.isUseful() ? false : true );
                postDao.update( post );
                return URL_TOPIC_PAGE + topic.getId() + "&faces-redirect=true";
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
                post.setHidden( post.isHidden() ? false : true );
                post.setHiddenBy( member );
                post.setHiddenCause( "TODO : Message de masquage." );
                postDao.update( post );
                return URL_TOPIC_PAGE + topic.getId() + "&faces-redirect=true";
            } catch ( DAOException e ) {
                // TODO: logger l'échec de la mise à jour en base du post
                return URL_404;
            }
        } else {
            // TODO: logger l'intrus qui essaie de masquer une réponse sans y être autorisé...
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