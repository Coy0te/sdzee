package com.sdzee.membres.beans;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import com.sdzee.membres.dao.MembreDao;
import com.sdzee.membres.entities.Membre;

@ManagedBean
@SessionScoped
public class ConnecterBean implements Serializable {
    private static final long   serialVersionUID = 1L;
    private static final String PSEUDO           = "pseudo";
    private static final String SUCCES_CONNEXION = "Succès de la connexion !";

    private String              pseudo;
    private String              motDePasse;
    private Membre              membre;

    // Injection de notre EJB (Session Bean Stateless)
    @EJB
    private MembreDao           membreDao;

    // Méthode d'action appelée lors du clic sur le bouton du formulaire
    // de connexion
    public void connecter() {
        /* Récupération du membre correspondant au pseudo saisi */
        Membre membre = membreDao.trouver( PSEUDO, pseudo );

        FacesMessage message = new FacesMessage( SUCCES_CONNEXION );
        FacesContext.getCurrentInstance().addMessage( null, message );
    }

    public void deconnecter() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
    }

    public boolean isConnecte() {
        return membre != null;
    }

    public Membre getMembre() {
        return membre;
    }
}