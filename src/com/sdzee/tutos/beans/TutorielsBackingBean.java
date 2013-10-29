package com.sdzee.tutos.beans;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import com.sdzee.breadcrumb.beans.BreadCrumbHelper;
import com.sdzee.breadcrumb.beans.BreadCrumbItem;
import com.sdzee.tutos.dao.BTMTChapDao;
import com.sdzee.tutos.entities.BTMTChap;

@ManagedBean( name = "tutosBean" )
@ViewScoped
public class TutorielsBackingBean implements Serializable {
    private static final long   serialVersionUID = 1L;
    private static final String URL_PAGE_TUTOS   = "/tutoriels.jsf";

    private List<BTMTChap>      tutos            = null;

    @EJB
    private BTMTChapDao         tutosDao;

    // Récupération de la liste des tutos à l'initialisation du bean
    @PostConstruct
    public void init() {
        tutos = tutosDao.lister();
    }

    public List<BreadCrumbItem> getBreadCrumb() {
        String chemin = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
        List<BreadCrumbItem> breadCrumb = BreadCrumbHelper.initBreadCrumb( chemin );
        BreadCrumbHelper.addTutosItem( breadCrumb, chemin, false );
        return breadCrumb;
    }

    public List<BTMTChap> getTutos() {
        return tutos;
    }

    public void setTutos( List<BTMTChap> tutos ) {
        this.tutos = tutos;
    }

}