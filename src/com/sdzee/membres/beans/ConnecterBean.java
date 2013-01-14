package com.sdzee.membres.beans;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import com.sdzee.membres.dao.MembreDao;
import com.sdzee.membres.entities.Membre;

@ManagedBean
@RequestScoped
public class ConnecterBean implements Serializable {
    private static final long   serialVersionUID = 1L;
    private static final String SUCCES_CONNEXION = "Succès de la connexion !";

    private Membre              membre;

    // Injection de notre EJB (Session Bean Stateless)
    @EJB
    private MembreDao           membreDao;

    // Initialisation de l'entité utilisateur
    public ConnecterBean() {
        membre = new Membre();
    }

    // Méthode d'action appelée lors du clic sur le bouton du formulaire
    // de connexion
    public void connecter() {
        // mettre le membre en session... bwwaaah, JSF et les sessions >_<
        FacesMessage message = new FacesMessage( SUCCES_CONNEXION );
        FacesContext.getCurrentInstance().addMessage( null, message );
    }

    public Membre getMembre() {
        return membre;
    }
}