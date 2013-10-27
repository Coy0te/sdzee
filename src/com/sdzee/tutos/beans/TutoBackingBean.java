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
import com.sdzee.forums.entities.Forum;
import com.sdzee.forums.entities.Sujet;

@ManagedBean( name = "tutoBean" )
@ViewScoped
public class TutoBackingBean implements Serializable {
    private static final long   serialVersionUID     = 1L;
    private static final String HEADER_REQUETE_PROXY = "X-FORWARDED-FOR";
    private static final String URL_PAGE_SUJET       = "/tuto.jsf?tutoId=";

    private Tuto                tuto                 = null;

    @EJB
    private TutoDao             tutoDao;

    @PostConstruct
    public void init() {
        sujet = new Sujet();
    }

    public Forum getTuto( int tutoId ) {
        if ( tuto == null ) {
            tuto = tutoDao.trouver( tutoId );
        }
        return tuto;
    }

    public List<BreadCrumbItem> getBreadCrumb( int tutoId ) {
        String chemin = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
        Tuto tuto = getTuto( tutoId );
        List<BreadCrumbItem> breadCrumb = BreadCrumbHelper.initBreadCrumb( chemin );
        BreadCrumbHelper.addTutosItem( breadCrumb, chemin, true );
        BreadCrumbHelper.addItem( breadCrumb, tuto.getTitre(), null );
        return breadCrumb;
    }
}