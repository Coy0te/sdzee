package com.sdzee.forums.beans;

import java.io.IOException;
import java.io.Serializable;
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
import com.sdzee.forums.dao.PostDao;
import com.sdzee.forums.dao.TopicDao;
import com.sdzee.forums.entities.Forum;
import com.sdzee.forums.entities.Post;
import com.sdzee.forums.entities.Topic;
import com.sdzee.membres.entities.Member;

/**
 * TopicsBackingBean est le bean sur lequel s'appuie notamment la page de chaque forum. Il s'agit d'un ManagedBean JSF, ayant pour portée
 * une vue.
 * 
 * @author Médéric Munier
 * @version %I%, %G%
 */
@ManagedBean( name = "topicsBean" )
@ViewScoped
public class TopicsBackingBean implements Serializable {
    private static final long   serialVersionUID   = 1L;
    private static final String URL_TOPIC_PAGE     = "/topic.jsf?topicId=";
    private static final int    NB_TOPICS_PER_PAGE = 5;

    private Topic               topic;
    private Post                post;
    private Forum               forum;
    private List<Topic>         stickyTopics;
    private List<Topic>         paginatedTopics;
    private int                 pagesNumber;
    private int                 forumId;
    private int                 page               = 1;

    @EJB
    private TopicDao            topicDao;
    @EJB
    private ForumDao            forumDao;
    @EJB
    private PostDao             postDao;

    /**
     * Cette méthode initialise la variable d'instance <code>forum</code> en récupérant en base le forum correspondant à l'id transmis par
     * la Facelet <code>forum.xhtml</code>, contenu dans la variable <code>forumId</code>.
     * <p>
     * Elle est exécutée automatiquement par JSF, après le constructeur de la classe s'il existe. À l'appel du constructeur classique, le
     * bean n'est pas encore initialisé, et donc aucune dépendance n'est injectée. Cependant lorsque cette méthode est appelée, le bean est
     * déjà initialisé et il est donc possible de faire appel à des dépendances. Ici, c'est le DAO {@link ForumDao} injecté via l'annotation
     * <code>@EJB</code> qui entre en jeu.
     * <p>
     * À la différence de la plupart des autres backing-beans, cette méthode n'est pas annotée avec <code>@PostConstruct</code>. Ceci est
     * simplement dû au fait qu'elle fait appel à une variable qui est initialisée depuis la vue, en l'occurrence l'id du sujet courant.
     * Puisqu'elle dépend de l'action du visiteur, son cycle de vie ne peut pas être entièrement géré par JSF.
     */
    public void init() {
        topic = new Topic();
        post = new Post();
        forum = forumDao.find( forumId );
        pagesNumber = (int) Math.ceil( forum.getNbTopics() / NB_TOPICS_PER_PAGE );
        stickyTopics = topicDao.listStickies( forum );
        paginatedTopics = topicDao.list( forum, page, NB_TOPICS_PER_PAGE );
    }

    public List<Topic> getPaginatedTopics() {
        return paginatedTopics;
    }

    public List<Topic> getStickyTopics() {
        return stickyTopics;
    }

    /**
     * Cette méthode permet la création d'un {@link Topic} dans le {@link Forum} courant.
     * 
     * @param member le membre à l'origine de l'action.
     * @throws IOException si la page vers laquelle effectuer une redirection n'existe pas.
     */
    public void create( Member member ) throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();
        post.setIpAddress( Faces.getRemoteAddr() );
        post.setCreationDate( new Date( System.currentTimeMillis() ) );
        post.setAuthor( member );
        topic.setForum( forum );
        try {
            topicDao.create( topic );
            context.addMessage( null, new FacesMessage( FacesMessage.SEVERITY_INFO, "Nouveau sujet créé avec succès",
                    topic.getTitle() ) );
            // TODO : à surveiller, manque peut-être un refresh ici pour que le topic récupère bien son ID après sa création !
            post.setTopic( topic );
            postDao.create( post );
            // TODO : à surveiller, manque peut-être un refresh ici pour que le post récupère bien son ID après sa création !
            topic.setFirstPost( post );
            // la valeur nbPosts est par défaut mise à 1 en BDD, donc rien à faire lors de la création d'un topic à ce niveau.
            // pareil pour lastPost qui est mis à null par défaut en BDD
            topicDao.update( topic );
            // on ajoute 1 au compteur dénormalisé (TODO : ça devrait être le boulot d'un Trigger BDD ou d'un JPA event)
            forum.addTopic();
            forum.setLastPost( post );
            forumDao.update( forum );
            ExternalContext externalContext = context.getExternalContext();
            externalContext.redirect( externalContext.getRequestContextPath() + URL_TOPIC_PAGE
                    + String.valueOf( topic.getId() ) );
        } catch ( DAOException e ) {
            context.addMessage( null, new FacesMessage( FacesMessage.SEVERITY_ERROR, "Echec de la création du sujet",
                    "Une erreur est survenue..." ) );
            // TODO: logger
        }
    }

    public Forum getForum() {
        return forum;
    }

    public int getPagesNumber() {
        return pagesNumber;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic( Topic topic ) {
        this.topic = topic;
    }

    public Post getPost() {
        return post;
    }

    public void setPost( Post post ) {
        this.post = post;
    }

    public int getForumId() {
        return forumId;
    }

    public void setForumId( int forumId ) {
        this.forumId = forumId;
    }

    public int getPage() {
        return page;
    }

    public void setPage( int page ) {
        this.page = page;
    }

    /**
     * Cette méthode génère le fil d'Ariane pour la page d'un forum donné.
     * 
     * @return la liste des items à afficher dans le fil d'Ariane.
     */
    public List<BreadCrumbItem> getBreadCrumb() {
        String path = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
        List<BreadCrumbItem> breadCrumb = BreadCrumbHelper.initBreadCrumb( path );
        BreadCrumbHelper.addForumsItem( breadCrumb, path, true );
        BreadCrumbHelper.addItem( breadCrumb, forum.getTitle(), null );
        return breadCrumb;
    }
}