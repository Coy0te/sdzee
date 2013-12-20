package com.sdzee.membres.beans;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.sdzee.breadcrumb.beans.BreadCrumbHelper;
import com.sdzee.breadcrumb.beans.BreadCrumbItem;
import com.sdzee.membres.dao.MemberDao;
import com.sdzee.membres.entities.Member;

@ManagedBean
@ViewScoped
@URLMapping( id = "registration", pattern = "/register", viewId = "/registration.jsf" )
public class RegisterBean implements Serializable {
    private static final long   serialVersionUID       = 1L;
    private static final String SUCCES_INSCRIPTION     = "Succès de l'inscription !";
    private static final String TITRE_PAGE_INSCRIPTION = "Inscription";
    private static final String SESSION_MEMBER         = "member";
    private static final String URL_WELCOME_PAGE       = "/index?faces-redirect=true";

    private Member              member;

    @EJB
    private MemberDao           memberDao;

    @PostConstruct
    public void init() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();

        Member loggedInMember = (Member) externalContext.getSessionMap()
                .get( SESSION_MEMBER );
        if ( loggedInMember != null ) {
            try {
                externalContext.redirect( "index.jsf" );
                return;
            } catch ( IOException e ) {
                // jamais, a priori.
            }
        }

        member = new Member();
    }

    // Méthode d'action appelée lors du clic sur le bouton du formulaire d'inscription
    public String register() {
        member.setRegistrationDate( new Date( System.currentTimeMillis() ) );
        memberDao.create( member );
        FacesMessage message = new FacesMessage( SUCCES_INSCRIPTION );
        FacesContext.getCurrentInstance().addMessage( null, message );
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put( SESSION_MEMBER, member );
        return URL_WELCOME_PAGE;
    }

    public Member getMember() {
        return member;
    }

    public List<BreadCrumbItem> getBreadCrumb() {
        String path = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
        List<BreadCrumbItem> breadCrumb = BreadCrumbHelper.initBreadCrumb( path );
        BreadCrumbHelper.addItem( breadCrumb, TITRE_PAGE_INSCRIPTION, null );
        return breadCrumb;
    }
}