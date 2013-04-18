package com.sdzee.forums.beans;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import com.sdzee.breadcrumb.beans.BreadCrumbHelper;
import com.sdzee.breadcrumb.beans.BreadCrumbItem;
import com.sdzee.dao.DAOException;
import com.sdzee.forums.dao.ReponseDao;
import com.sdzee.forums.dao.SujetDao;
import com.sdzee.forums.dao.VoteDao;
import com.sdzee.forums.entities.Reponse;
import com.sdzee.forums.entities.Sujet;
import com.sdzee.forums.entities.Vote;
import com.sdzee.membres.entities.Membre;

@ManagedBean( name = "reponsesBean" )
@ViewScoped
public class ReponsesBackingBean implements Serializable {
    private static final long   serialVersionUID     = 1L;
    private static final String HEADER_REQUETE_PROXY = "X-FORWARDED-FOR";
    private static final String URL_PAGE_FORUM       = "/forum.jsf?forumId=";
    private static final int    VOTE_POSITIF         = 1;
    private static final int    VOTE_NEGATIF         = -1;

    private Reponse             reponse;

    private String              queryString;

    @EJB
    private ReponseDao          reponseDao;
    @EJB
    private SujetDao            sujetDao;
    @EJB
    private VoteDao             voteDao;

    @PostConstruct
    public void init() {
        reponse = new Reponse();
    }

    public Sujet getSujet( int sujetId ) {
        return sujetDao.trouver( sujetId );
    }

    public List<Reponse> getReponses( int sujetId ) {
        return reponseDao.lister( sujetDao.trouver( sujetId ) );
    }

    public void repondre( Membre membre, Sujet sujet ) {
        // TODO: remplacer par la méthode propre issue de OmniFaces
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
                .getRequest();
        String adresseIP = request.getHeader( HEADER_REQUETE_PROXY );
        if ( adresseIP == null ) {
            adresseIP = request.getRemoteAddr();
        }
        reponse.setAdresseIP( adresseIP );
        reponse.setDateCreation( new Timestamp( System.currentTimeMillis() ) );
        reponse.setAuteur( membre );
        reponse.setSujet( sujet );
        try {
            reponseDao.creer( reponse );
            reponse = null;
        } catch ( DAOException e ) {
            // TODO: logger
        }
    }

    public void voteUp( Membre membre, Reponse reponse ) {
        // TODO
        Vote vote = voteDao.trouver( membre, reponse );
        if ( vote == null ) {
            // le membre n'a pas encore voté sur ce message
            vote = new Vote();
            vote.setMembre( membre );
            vote.setReponse( reponse );
            vote.setValeur( VOTE_POSITIF );
            voteDao.creer( vote );
            reponse.addVotePositif();
            // TODO: reponseDao.update( reponse );
        } else if ( vote.getValeur() == VOTE_POSITIF ) {
            // le membre a déjà voté pouce en l'air, donc on supprime son vote
            voteDao.supprimer( vote );
            reponse.removeVotePositif();
            // TODO: reponseDao.update( reponse )
        } else {
            // le membre a déjà voté, mais pouce en bas, donc on remplace son vote
            vote.setValeur( VOTE_POSITIF );
            // TODO: voteDao.update( vote );
            reponse.addVotePositif();
            reponse.addVotePositif();
        }
    }

    public void voteDown( Membre membre, Reponse reponse ) {
        // TODO
        reponse.addVoteNegatif();
    }

    public void voteUp( Membre membre, Sujet sujet ) {
        // TODO
        sujet.addVotePositif();
    }

    public void voteDown( Membre membre, Sujet sujet ) {
        // TODO
        sujet.addVoteNegatif();
    }

    public List<BreadCrumbItem> getBreadCrumb( Sujet sujet ) {
        String chemin = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
        List<BreadCrumbItem> breadCrumb = BreadCrumbHelper.initBreadCrumb( chemin );
        BreadCrumbHelper.addForumsItem( breadCrumb, chemin, true );
        BreadCrumbHelper.addItem( breadCrumb, sujet.getForum().getTitre(), chemin + URL_PAGE_FORUM
                + sujet.getForum().getId() );
        BreadCrumbHelper.addItem( breadCrumb, sujet.getTitre(), null );
        return breadCrumb;
    }

    public String getQueryString() {
        return queryString;
    }

    public void setQueryString( String queryString ) {
        this.queryString = queryString;
    }

    public Reponse getReponse() {
        return reponse;
    }

    public void setReponse( Reponse reponse ) {
        this.reponse = reponse;
    }
}