package com.sdzee.membres.beans;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import com.sdzee.breadcrumb.beans.BreadCrumbHelper;
import com.sdzee.breadcrumb.beans.BreadCrumbItem;
import com.sdzee.membres.dao.MembreDao;
import com.sdzee.membres.entities.Membre;

@ManagedBean
@RequestScoped
public class InscrireBean implements Serializable {
    private static final long   serialVersionUID       = 1L;
    private static final String SUCCES_INSCRIPTION     = "Succès de l'inscription !";
    private static final String TITRE_PAGE_INSCRIPTION = "Inscription";

    private Membre              membre;

    // Injection de notre EJB (Session Bean Stateless)
    @EJB
    private MembreDao           membreDao;

    // Initialisation de l'entité utilisateur
    @PostConstruct
    public void init() {
        membre = new Membre();
    }

    // Méthode d'action appelée lors du clic sur le bouton du formulaire
    // d'inscription
    public void inscrire() {
        initialiserDateInscription();
        membreDao.creer( membre );
        FacesMessage message = new FacesMessage( SUCCES_INSCRIPTION );
        FacesContext.getCurrentInstance().addMessage( null, message );
    }

    public Membre getMembre() {
        return membre;
    }

    private void initialiserDateInscription() {
        Timestamp date = new Timestamp( System.currentTimeMillis() );
        membre.setDateInscription( date );
    }

    public List<BreadCrumbItem> getBreadCrumb() {
        String chemin = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
        List<BreadCrumbItem> breadCrumb = BreadCrumbHelper.initBreadCrumb( chemin );
        BreadCrumbHelper.addItem( breadCrumb, TITRE_PAGE_INSCRIPTION, null );
        return breadCrumb;
    }
}