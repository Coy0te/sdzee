package com.sdzee.forums.beans;

import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

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
    private static final long serialVersionUID = 1L;

    @EJB
    private SujetDao          sujetDao;
    @EJB
    private ForumDao          forumDao;
    @EJB
    private ReponseDao        reponseDao;

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

    public List<BreadCrumbItem> getBreadCrumb( int forumId ) {
        String chemin = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
        Forum forum = getForum( forumId );
        List<BreadCrumbItem> breadCrumb = BreadCrumbHelper.initBreadCrumb( chemin );
        BreadCrumbHelper.addForumsItem( breadCrumb, chemin, true );
        BreadCrumbHelper.addItem( breadCrumb, forum.getTitre(), null );
        return breadCrumb;
    }
}