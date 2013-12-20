package com.sdzee.admin.beans;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.sdzee.breadcrumb.beans.BreadCrumbHelper;
import com.sdzee.breadcrumb.beans.BreadCrumbItem;
import com.sdzee.forums.dao.ForumCategoryDao;
import com.sdzee.forums.dao.ForumDao;
import com.sdzee.forums.entities.Forum;
import com.sdzee.forums.entities.ForumCategory;

@ManagedBean( name = "adminBean" )
@ViewScoped
@URLMapping( id = "admin", pattern = "/admin/", viewId = "/admin/admin.jsf" )
public class AdminBackingBean implements Serializable {
    private static final long   serialVersionUID = 1L;
    private static final String TITRE_PAGE_ADMIN = "Administration";

    @EJB
    private ForumDao            forumDao;
    @EJB
    private ForumCategoryDao    categoryDao;

    private List<ForumCategory> categories;

    @PostConstruct
    public void init() {
        categories = categoryDao.list();
    }

    public List<ForumCategory> getCategories() {
        return categories;
    }

    public List<Forum> getForums( ForumCategory categorie ) {
        return forumDao.list( categorie );
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
        String path = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
        List<BreadCrumbItem> breadCrumb = BreadCrumbHelper.initBreadCrumb( path );
        BreadCrumbHelper.addItem( breadCrumb, TITRE_PAGE_ADMIN, null );
        return breadCrumb;
    }
}