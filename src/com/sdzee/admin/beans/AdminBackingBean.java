package com.sdzee.admin.beans;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import com.sdzee.breadcrumb.beans.BreadCrumbHelper;
import com.sdzee.breadcrumb.beans.BreadCrumbItem;
import com.sdzee.forums.dao.CategorieForumsDao;
import com.sdzee.forums.dao.ForumDao;
import com.sdzee.forums.entities.CategorieForum;
import com.sdzee.forums.entities.Forum;

@ManagedBean( name = "adminBean" )
@ViewScoped
public class AdminBackingBean implements Serializable {
    private static final long    serialVersionUID = 1L;
    private static final String  TITRE_PAGE_ADMIN = "Administration";

    @EJB
    private ForumDao             forumDao;
    @EJB
    private CategorieForumsDao   categorieDao;

    private List<CategorieForum> categories;
    private List<Forum>          forums;

    @PostConstruct
    public void init() {
        categories = categorieDao.lister();
        forums = forumDao.lister();
    }

    public List<CategorieForum> getCategories() {
        return categories;
    }

    public List<Forum> getForums() {
        return forums;
    }

    public List<Forum> getForums( CategorieForum categorie ) {
        return forumDao.lister( categorie );
    }

    public void creer() {
        addMessage( "Données créées." );
    }

    public void modifier() {
        addMessage( "Données mises à jour." );
    }

    public void supprimer() {
        addMessage( "Données supprimées." );
    }

    public void addMessage( String summary ) {
        FacesMessage message = new FacesMessage( FacesMessage.SEVERITY_INFO, summary, null );
        FacesContext.getCurrentInstance().addMessage( null, message );
    }

    public List<BreadCrumbItem> getBreadCrumb() {
        String chemin = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
        List<BreadCrumbItem> breadCrumb = BreadCrumbHelper.initBreadCrumb( chemin );
        BreadCrumbHelper.addItem( breadCrumb, TITRE_PAGE_ADMIN, null );
        return breadCrumb;
    }
}