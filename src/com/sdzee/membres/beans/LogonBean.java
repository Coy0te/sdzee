package com.sdzee.membres.beans;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
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
@URLMapping( id = "logon", pattern = "/logon", viewId = "/connection.jsf" )
public class LogonBean implements Serializable {
    private static final long   serialVersionUID     = 1L;
    private static final String NICKNAME             = "nickName";
    private static final String SESSION_MEMBER       = "member";
    private static final String PARAM_NEXT_URL       = "next";
    private static final String PAGE_ACCUEIL         = "/";
    private static final String TITRE_PAGE_CONNEXION = "Connexion";

    private String              nickName;
    private String              password;
    private String              autoConnect;
    private String              nextUrl;

    @EJB
    private MemberDao           memberDao;

    @PostConstruct
    public void init() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();

        Member loggedInMember = (Member) externalContext.getSessionMap()
                .get( SESSION_MEMBER );
        if ( loggedInMember != null ) {
            try {
                externalContext.redirect( externalContext.getRequestContextPath() + PAGE_ACCUEIL );
                return;
            } catch ( IOException e ) {
                // jamais, a priori.
            }
        }

        nextUrl = externalContext.getRequestParameterMap().get( PARAM_NEXT_URL );
        if ( nextUrl == null || nextUrl.isEmpty() ) {
            nextUrl = PAGE_ACCUEIL;
        }
        nextUrl = externalContext.getRequestContextPath() + nextUrl;
    }

    // Méthode d'action appelée lors du clic sur le bouton du formulaire
    // de connexion
    public void login() throws IOException {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();

        // On récupère le membre depuis la BDD
        Member member = memberDao.find( NICKNAME, nickName );

        // On le met en session, puisque si on arrive ici c'est qu'on est
        // déjà passé par le validator associé au formulaire de connexion,
        // qui a vérifié que le pseudo et le mot de passe sont corrects !
        externalContext.getSessionMap().put( SESSION_MEMBER, member );

        // On actualise la date de dernière connexion du membre
        member.setLastConnectionDate( new Date( System.currentTimeMillis() ) );
        memberDao.update( member );

        // On redirige le membre vers la page qu'il voulait visiter
        externalContext.redirect( nextUrl );
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName( String nickName ) {
        this.nickName = nickName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword( String password ) {
        this.password = password;
    }

    public String getConnexionAuto() {
        return autoConnect;
    }

    public void setConnexionAuto( String connexionAuto ) {
        this.autoConnect = connexionAuto;
    }

    public List<BreadCrumbItem> getBreadCrumb() {
        String path = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
        List<BreadCrumbItem> breadCrumb = BreadCrumbHelper.initBreadCrumb( path );
        BreadCrumbHelper.addItem( breadCrumb, TITRE_PAGE_CONNEXION, null );
        return breadCrumb;
    }
}