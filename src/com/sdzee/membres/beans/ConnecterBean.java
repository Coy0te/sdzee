package com.sdzee.membres.beans;

import java.io.IOException;
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.RequestDispatcher;

import com.sdzee.membres.dao.MembreDao;
import com.sdzee.membres.entities.Membre;

@ManagedBean
@ViewScoped
public class ConnecterBean implements Serializable {
    private static final long   serialVersionUID     = 1L;
    private static final String PSEUDO               = "pseudo";
    private static final String SESSION_MEMBRE       = "membre";
    private static final String URL_PARAM_SEPARATEUR = "?";
    private static final String PAGE_ACCUEIL         = "/accueil.xhtml";
    private static final String PAGE_CONNEXION       = "/connexion.xhtml";

    private String              pseudo;
    private String              motDePasse;
    private String              urlOrigine;

    // Injection de notre EJB (Session Bean Stateless)
    @EJB
    private MembreDao           membreDao;

    @PostConstruct
    public void init() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        urlOrigine = (String) externalContext.getRequestMap().get( RequestDispatcher.FORWARD_REQUEST_URI );

        if ( urlOrigine == null ) {
            urlOrigine = externalContext.getRequestContextPath() + PAGE_ACCUEIL;
        } else {
            String queryStringOrigine = (String) externalContext.getRequestMap().get(
                    RequestDispatcher.FORWARD_QUERY_STRING );

            if ( queryStringOrigine != null ) {
                urlOrigine += URL_PARAM_SEPARATEUR + queryStringOrigine;
            }
        }
    }

    // Méthode d'action appelée lors du clic sur le bouton du formulaire
    // de connexion
    public void connecter() throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();

        // On récupère le membre depuis la BDD
        Membre membre = membreDao.trouver( PSEUDO, pseudo );

        // On le met en session, puisque si on arrive ici c'est qu'on est
        // déjà passé par le validator associé au formulaire de connexion,
        // qui a vérifié que le pseudo et le mot de passe sont corrects !
        externalContext.getSessionMap().put( SESSION_MEMBRE, membre );

        // On redirige le membre vers la page qu'il voulait visiter
        externalContext.redirect( urlOrigine );
    }

    public void deconnecter() throws IOException {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        externalContext.invalidateSession();
        externalContext.redirect( externalContext.getRequestContextPath() + PAGE_ACCUEIL );
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo( String pseudo ) {
        this.pseudo = pseudo;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse( String motDePasse ) {
        this.motDePasse = motDePasse;
    }
}