package com.sdzee.forums.beans;

import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import com.sdzee.breadcrumb.beans.BreadCrumbHelper;
import com.sdzee.breadcrumb.beans.BreadCrumbItem;
import com.sdzee.forums.dao.ForumDao;
import com.sdzee.forums.dao.ReponseDao;
import com.sdzee.forums.dao.SujetDao;
import com.sdzee.forums.entities.Forum;
import com.sdzee.forums.entities.Reponse;
import com.sdzee.forums.entities.Sujet;

@ManagedBean( name = "sujetsBean" )
@RequestScoped
public class SujetsBackingBean implements Serializable {
    private static final long   serialVersionUID     = 1L;
    private static final String HEADER_REQUETE_PROXY = "X-FORWARDED-FOR";

    private String              texteSujet;
    private String              titreSujet;
    private String              sousTitreSujet;

    @EJB
    private SujetDao            sujetDao;
    @EJB
    private ForumDao            forumDao;
    @EJB
    private ReponseDao          reponseDao;

    public Forum getForum( int forumId ) {
        return forumDao.trouver( forumId );
    }

    public List<Sujet> getSujets( int forumId ) {
        return sujetDao.lister( forumDao.trouver( forumId ) );
    }

    public Reponse getDerniereReponse( Sujet sujet ) {
        return reponseDao.trouverDerniere( sujet );
    }

    public Integer getDecompteReponses( Sujet sujet ) {
        return reponseDao.decompte( sujet );
    }

    public void creer() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
                .getRequest();
        String adresseIP = request.getHeader( HEADER_REQUETE_PROXY );
        if ( adresseIP == null ) {
            adresseIP = request.getRemoteAddr();
        }

        // TODO: sujetDao.creer( titreSujet, sousTitreSujet, texteSujet, adresseIP );
    }

    public List<BreadCrumbItem> getBreadCrumb( int forumId ) {
        String chemin = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
        Forum forum = getForum( forumId );
        List<BreadCrumbItem> breadCrumb = BreadCrumbHelper.initBreadCrumb( chemin );
        BreadCrumbHelper.addForumsItem( breadCrumb, chemin, true );
        BreadCrumbHelper.addItem( breadCrumb, forum.getTitre(), null );
        return breadCrumb;
    }

    public String getTexteSujet() {
        return texteSujet;
    }

    public void setTexteSujet( String texteSujet ) {
        this.texteSujet = texteSujet;
    }

    public String getTitreSujet() {
        return titreSujet;
    }

    public void setTitreSujet( String titreSujet ) {
        this.titreSujet = titreSujet;
    }

    public String getSousTitreSujet() {
        return sousTitreSujet;
    }

    public void setSousTitreSujet( String sousTitreSujet ) {
        this.sousTitreSujet = sousTitreSujet;
    }
}