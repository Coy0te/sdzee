package com.sdzee.tutos.beans;

import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import com.sdzee.breadcrumb.beans.BreadCrumbHelper;
import com.sdzee.breadcrumb.beans.BreadCrumbItem;
import com.sdzee.tutos.dao.BTMTChapDao;
import com.sdzee.tutos.entities.BTMTChap;

@ManagedBean( name = "tutoBean" )
@ViewScoped
public class TutoBackingBean implements Serializable {
    private static final long   serialVersionUID = 1L;
    private static final String URL_PAGE_TUTO    = "/tuto.jsf?tutoId=";

    private BTMTChap            tuto             = null;

    private int                 tutoId;

    @EJB
    private BTMTChapDao         tutoDao;

    public void init() {
        tuto = tutoDao.trouver( tutoId );
    }

    public List<BreadCrumbItem> getBreadCrumb( int tutoId ) {
        String chemin = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
        List<BreadCrumbItem> breadCrumb = BreadCrumbHelper.initBreadCrumb( chemin );
        BreadCrumbHelper.addTutosItem( breadCrumb, chemin, true );
        BreadCrumbHelper.addItem( breadCrumb, tuto.getTitre(), null );
        return breadCrumb;
    }

    public BTMTChap getTuto() {
        return tuto;
    }

    public void setTuto( BTMTChap tuto ) {
        this.tuto = tuto;
    }

    public int getTutoId() {
        return tutoId;
    }

    public void setTutoId( int tutoId ) {
        this.tutoId = tutoId;
    }
}