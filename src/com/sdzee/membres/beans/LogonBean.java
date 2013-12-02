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

import com.sdzee.breadcrumb.beans.BreadCrumbHelper;
import com.sdzee.breadcrumb.beans.BreadCrumbItem;
import com.sdzee.membres.dao.MemberDao;
import com.sdzee.membres.entities.Member;

@ManagedBean
@ViewScoped
public class LogonBean implements Serializable {
    private static final long   serialVersionUID           = 1L;
    private static final String NICKNAME                   = "nickName";
    private static final String SESSION_MEMBER             = "member";
    private static final String PARAM_URL_ORIGINE          = "urlOrigine";
    private static final String PARAM_QUERY_STRING_ORIGINE = "queryStringOrigine";
    private static final String URL_PARAM_SEPARATEUR       = "?";
    private static final String PAGE_ACCUEIL               = "/index.jsf";
    private static final String TITRE_PAGE_CONNEXION       = "Connexion";

    private String              nickName;
    private String              password;
    private String              autoConnect;
    private String              urlOrigine;

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

        urlOrigine = externalContext.getRequestParameterMap().get( PARAM_URL_ORIGINE );
        String queryStringOrigine = externalContext.getRequestParameterMap().get( PARAM_QUERY_STRING_ORIGINE );

        if ( urlOrigine == null || urlOrigine.isEmpty() ) {
            urlOrigine = externalContext.getRequestContextPath() + PAGE_ACCUEIL;
        } else {
            if ( queryStringOrigine != null && !queryStringOrigine.isEmpty() ) {
                urlOrigine += URL_PARAM_SEPARATEUR + queryStringOrigine;
            }
        }
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
        externalContext.redirect( urlOrigine );
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