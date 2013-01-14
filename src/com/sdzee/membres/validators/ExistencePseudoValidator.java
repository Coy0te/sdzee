package com.sdzee.membres.validators;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import com.sdzee.dao.DAOException;
import com.sdzee.membres.dao.MembreDao;

@ManagedBean
@RequestScoped
public class ExistencePseudoValidator implements Validator {

    private static final String PSEUDO_EXISTE_DEJA = "Ce pseudo est déjà utilisé.";

    @EJB
    private MembreDao           membreDao;

    @Override
    public void validate( FacesContext context, UIComponent component, Object value ) throws ValidatorException {
        /* Récupération de la valeur à traiter depuis le paramètre value */
        String pseudo = (String) value;
        try {
            if ( membreDao.trouver( "pseudo", pseudo ) != null ) {
                /*
                 * Si une adresse est retournée, alors on envoie une exception
                 * propre à JSF, qu'on initialise avec un FacesMessage de
                 * gravité "Erreur" et contenant le message d'explication. Le
                 * framework va alors gérer lui-même cette exception et s'en
                 * servir pour afficher le message d'erreur à l'utilisateur.
                 */
                throw new ValidatorException(
                        new FacesMessage( FacesMessage.SEVERITY_ERROR, PSEUDO_EXISTE_DEJA, null ) );
            }
        } catch ( DAOException e ) {
            /*
             * En cas d'erreur imprévue émanant de la BDD, on prépare un message
             * d'erreur contenant l'exception retournée, pour l'afficher à
             * l'utilisateur ensuite.
             */
            FacesMessage message = new FacesMessage( FacesMessage.SEVERITY_ERROR, e.getMessage(), null );
            FacesContext facesContext = FacesContext.getCurrentInstance();
            facesContext.addMessage( component.getClientId( facesContext ), message );
        }
    }
}