package com.sdzee.admin.beans;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import com.sdzee.breadcrumb.beans.BreadCrumbHelper;
import com.sdzee.breadcrumb.beans.BreadCrumbItem;
import com.sdzee.membres.dao.MembreDao;
import com.sdzee.membres.entities.Membre;

@ManagedBean( name = "adminMembresBean" )
@ViewScoped
public class AdminMembresBackingBean implements Serializable {
    private static final long   serialVersionUID         = 1L;
    private static final String TITRE_PAGE_ADMIN         = "Administration";
    private static final String URL_PAGE_ADMIN           = "/admin/admin.jsf";
    private static final String TITRE_PAGE_ADMIN_MEMBRES = "Membres";

    @EJB
    private MembreDao           membreDao;

    private List<Membre>        membres;
    private List<Membre>        filteredMembres;

    @PostConstruct
    public void init() {
        membres = membreDao.lister();
    }

    public List<Membre> getMembres() {
        return membres;
    }

    public List<Membre> getFilteredMembres() {
        return filteredMembres;
    }

    public List<BreadCrumbItem> getBreadCrumb() {
        String chemin = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
        List<BreadCrumbItem> breadCrumb = BreadCrumbHelper.initBreadCrumb( chemin );
        BreadCrumbHelper.addItem( breadCrumb, TITRE_PAGE_ADMIN, chemin + URL_PAGE_ADMIN );
        BreadCrumbHelper.addItem( breadCrumb, TITRE_PAGE_ADMIN_MEMBRES, null );
        return breadCrumb;
    }
}