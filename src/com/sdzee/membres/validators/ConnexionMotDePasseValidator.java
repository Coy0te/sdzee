package com.sdzee.membres.validators;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import org.jasypt.util.password.ConfigurablePasswordEncryptor;
import org.omnifaces.util.Messages;

import com.sdzee.membres.dao.MembreDao;
import com.sdzee.membres.entities.Membre;

@ManagedBean
@RequestScoped
public class ConnexionMotDePasseValidator implements Validator {

    private static final String ALGO_CHIFFREMENT     = "SHA-256";
    private static final String CHAMP_PSEUDO         = "composantPseudo";
    private static final String MAUVAIS_MOT_DE_PASSE = "Le mot de passe est incorrect !";
    private static final String MAUVAIS_PSEUDO       = "Le pseudo saisi n'existe pas !";
    @EJB
    private MembreDao           membreDao;

    @Override
    public void validate( FacesContext context, UIComponent component, Object value ) throws ValidatorException {
        /*
         * Récupération de l'attribut pseudo parmi la liste des attributs du composant motDePasse, puis récupération de sa valeur (pseudo
         * saisi)
         */
        UIInput composantPseudo = (UIInput) component.getAttributes().get( CHAMP_PSEUDO );
        String pseudo = (String) composantPseudo.getValue();
        /* Récupération de la valeur du champ mot de passe */
        String motDePasseEnClair = (String) value;
        /* Préparation de la vérification du mot de passe */
        ConfigurablePasswordEncryptor passwordEncryptor = new ConfigurablePasswordEncryptor();
        passwordEncryptor.setAlgorithm( ALGO_CHIFFREMENT );
        passwordEncryptor.setPlainDigest( false );
        /* Récupération du membre correspondant au pseudo saisi */
        Membre membreInscrit = membreDao.trouver( "pseudo", pseudo );
        if ( membreInscrit == null ) {
            Messages.addError( "formulaire:pseudo", MAUVAIS_PSEUDO );
            throw new ValidatorException( new FacesMessage() ); // message vide car erreur sur le champ pseudo, et pas sur le mdp
        } else if ( !passwordEncryptor.checkPassword( motDePasseEnClair, membreInscrit.getMotDePasse() ) ) {
            throw new ValidatorException(
                    new FacesMessage( FacesMessage.SEVERITY_ERROR, MAUVAIS_MOT_DE_PASSE, null ) );
        }
    }
}