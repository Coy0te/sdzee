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

import com.sdzee.membres.dao.MemberDao;
import com.sdzee.membres.entities.Member;

@ManagedBean
@RequestScoped
public class ConnexionMotDePasseValidator implements Validator {

    private static final String ENCRYPTION_ALGORYTHM = "SHA-256";
    private static final String FIELD_NICKNAME       = "composantPseudo";
    private static final String WRONG_PASSWORD       = "Le mot de passe est incorrect !";
    private static final String WRONG_NICKNAME       = "Le pseudo saisi n'existe pas !";
    @EJB
    private MemberDao           memberDao;

    @Override
    public void validate( FacesContext context, UIComponent component, Object value ) throws ValidatorException {
        /*
         * Récupération de l'attribut pseudo parmi la liste des attributs du composant motDePasse, puis récupération de sa valeur (pseudo
         * saisi)
         */
        UIInput nickNameComponent = (UIInput) component.getAttributes().get( FIELD_NICKNAME );
        String nickName = (String) nickNameComponent.getValue();
        /* Récupération de la valeur du champ mot de passe */
        String clearPassword = (String) value;
        /* Préparation de la vérification du mot de passe */
        ConfigurablePasswordEncryptor passwordEncryptor = new ConfigurablePasswordEncryptor();
        passwordEncryptor.setAlgorithm( ENCRYPTION_ALGORYTHM );
        passwordEncryptor.setPlainDigest( false );
        /* Récupération du membre correspondant au pseudo saisi */
        Member registeredMember = memberDao.find( "nickName", nickName );
        if ( registeredMember == null ) {
            Messages.addError( "formulaire:pseudo", WRONG_NICKNAME );
            throw new ValidatorException( new FacesMessage() ); // message vide car erreur sur le champ pseudo, et pas sur le mdp
        } else if ( !passwordEncryptor.checkPassword( clearPassword, registeredMember.getPassword() ) ) {
            throw new ValidatorException(
                    new FacesMessage( FacesMessage.SEVERITY_ERROR, WRONG_PASSWORD, null ) );
        }
    }
}