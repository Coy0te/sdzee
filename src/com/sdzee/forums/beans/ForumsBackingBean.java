package com.sdzee.forums.beans;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import com.sdzee.breadcrumb.beans.BreadCrumbHelper;
import com.sdzee.breadcrumb.beans.BreadCrumbItem;
import com.sdzee.forums.dao.ForumCategoryDao;
import com.sdzee.forums.dao.ForumDao;
import com.sdzee.forums.entities.Forum;
import com.sdzee.forums.entities.ForumCategory;

/**
 * ForumsBackingBean est le bean sur lequel s'appuie notamment la page principale du forum. Il s'agit d'un ManagedBean JSF, ayant pour portée une vue.
 * 
 * @author Médéric Munier
 * @version %I%, %G%
 */
@ManagedBean( name = "forumsBean" )
@ViewScoped
public class ForumsBackingBean implements Serializable {
    private static final long   serialVersionUID = 1L;

    private List<ForumCategory> categories;

    @EJB
    private ForumCategoryDao    categoryDao;
    @EJB
    private ForumDao            forumDao;

    /**
     * Cette méthode permet le chargement des listes des catégories et forums lors de l'initialisation du bean.
     * <p>
     * Elle est exécutée automatiquement par JSF, après le constructeur de la classe s'il existe. À l'appel du constructeur classique, le bean n'est
     * pas encore initialisé, et donc aucune dépendance n'est injectée. Cependant lorsque cette méthode est appelée, le bean est déjà initialisé et il
     * est donc possible de faire appel à des dépendances. Ici, c'est le DAO {@link ForumCategoryDao} injecté via l'annotation <code>@EJB</code> qui
     * entre en jeu.
     * <p>
     * L'annotation <code>@PostConstruct</code> garantit que cette méthode ne sera appelée qu'une seule et unique fois durant le cycle de vie du bean,
     * c'est-à-dire une fois par requête, vue, session ou application selon le scope de définition du bean.
     */
    @PostConstruct
    public void init() {
        categories = categoryDao.list();
    }

    /**
     * Cette méthode donne accès à la liste des {@link ForumCategory} chargée à l'initialisation du bean.
     * 
     * @return la liste des catégories triées par position, ou <code>null</code> si aucune catégorie n'est trouvée.
     */
    public List<ForumCategory> getCategories() {
        return categories;
    }

    /**
     * Cette méthode tape exceptionnellement directement dans un DAO, mais elle n'est appelée que par un membre ayant les droits de modération depuis
     * la page d'un sujet, donc ça passe.
     * 
     * @return la liste des forums triés par id, ou <code>null</code> si aucun forum n'est trouvé.
     */
    public List<Forum> getForums() {
        return forumDao.list();
    }

    /**
     * Cette méthode génère le fil d'Ariane pour la page principale des forums.
     * 
     * @return la liste des items à afficher dans le fil d'Ariane.
     */
    public List<BreadCrumbItem> getBreadCrumb() {
        String path = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
        List<BreadCrumbItem> breadCrumb = BreadCrumbHelper.initBreadCrumb( path );
        BreadCrumbHelper.addForumsItem( breadCrumb, path, false );
        return breadCrumb;
    }
}