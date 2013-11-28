package com.sdzee.membres.validators;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator( value = "confirmationMotDePasseValidator" )
public class ConfirmationMotDePasseValidator implements Validator {

    private static final String FIELD_PASSWORD      = "composantMotDePasse";
    private static final String DIFFERENT_PASSWORDS = "Le mot de passe et la confirmation doivent être identiques.";

    @Override
    public void validate( FacesContext context, UIComponent component, Object value ) throws ValidatorException {
        /*
         * Récupération de l'attribut mot de passe parmi la liste des attributs du composant confirmation
         */
        UIInput passwordComponent = (UIInput) component.getAttributes().get( FIELD_PASSWORD );
        /*
         * Récupération de la valeur du champ, c'est-à-dire le mot de passe saisi
         */
        String password = (String) passwordComponent.getValue();
        /* Récupération de la valeur du champ confirmation */
        String confirmation = (String) value;

        if ( confirmation != null && !confirmation.equals( password ) ) {
            /*
             * Envoi d'une exception contenant une erreur de validation JSF initialisée avec le message destiné à l'utilisateur, si les mots
             * de passe sont différents
             */
            throw new ValidatorException(
                    new FacesMessage( FacesMessage.SEVERITY_ERROR, DIFFERENT_PASSWORDS, null ) );
        }
    }
}