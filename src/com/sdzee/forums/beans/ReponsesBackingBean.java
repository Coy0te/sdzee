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
    private static final String TYPE_REPONSE         = "reponse";
    private static final String TYPE_SUJET           = "sujet";

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

    public void vote( Membre membre, Long idObjet, String typeObjet, int valeur, Reponse reponse, Sujet sujet ) {
        Vote vote = voteDao.trouver( membre, idObjet, typeObjet );
        if ( vote == null ) {
            // le membre n'a pas encore voté sur ce message
            vote = new Vote();
            vote.setMembre( membre );
            vote.setIdObjet( idObjet );
            vote.setTypeObjet( typeObjet );
            vote.setValeur( valeur );
            voteDao.creer( vote );
            // il faut ensuite MAJ le compteur dénormalisé sur la table réponse ou sujet
            if ( valeur > 0 ) {
                if ( TYPE_REPONSE.equals( typeObjet ) ) {
                    reponse.addVotePositif();
                    reponseDao.update( reponse );
                } else {
                    sujet.addVotePositif();
                    sujetDao.update( sujet );
                }
            } else {
                if ( TYPE_REPONSE.equals( typeObjet ) ) {
                    reponse.addVoteNegatif();
                    reponseDao.update( reponse );
                } else {
                    sujet.addVoteNegatif();
                    sujetDao.update( sujet );
                }
            }
        } else if ( vote.getValeur() == valeur ) {
            // le membre avait déjà effectué le même vote, donc c'est un re-clic et il faut annuler son précédent vote
            voteDao.supprimer( vote );
            if ( TYPE_REPONSE.equals( typeObjet ) ) {
                if ( valeur > 0 ) {
                    reponse.removeVotePositif();
                } else {
                    reponse.removeVoteNegatif();
                }
                reponseDao.update( reponse );
            } else {
                if ( valeur > 0 ) {
                    sujet.removeVotePositif();
                } else {
                    sujet.removeVoteNegatif();
                }
                sujetDao.update( sujet );
            }
        } else {
            // le membre avait déjà voté, mais différemment, donc il faut remplacer son ancien vote
            vote.setValeur( valeur );
            voteDao.update( vote ); // TODO : nécessaire ou pas ? Si l'entité vote n'est pas détachée...
            if ( TYPE_REPONSE.equals( typeObjet ) ) {
                if ( valeur > 0 ) {
                    reponse.removeVoteNegatif();
                    reponse.addVotePositif();
                } else {
                    reponse.removeVotePositif();
                    reponse.addVoteNegatif();
                }
                reponseDao.update( reponse );
            } else {
                if ( valeur > 0 ) {
                    sujet.removeVoteNegatif();
                    sujet.addVotePositif();
                } else {
                    sujet.removeVotePositif();
                    sujet.addVoteNegatif();
                }
                sujetDao.update( sujet );
            }
        }
    }

    public void voteUp( Membre membre, Reponse reponse ) {
        vote( membre, reponse.getId(), TYPE_REPONSE, VOTE_POSITIF, reponse, null );
    }

    public void voteDown( Membre membre, Reponse reponse ) {
        vote( membre, reponse.getId(), TYPE_REPONSE, VOTE_NEGATIF, reponse, null );
    }

    public void voteUp( Membre membre, Sujet sujet ) {
        vote( membre, sujet.getId(), TYPE_SUJET, VOTE_POSITIF, null, sujet );
    }

    public void voteDown( Membre membre, Sujet sujet ) {
        vote( membre, sujet.getId(), TYPE_SUJET, VOTE_NEGATIF, null, sujet );
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