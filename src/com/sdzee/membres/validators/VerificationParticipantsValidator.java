package com.sdzee.membres.validators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import com.sdzee.dao.DAOException;
import com.sdzee.membres.dao.MemberDao;
import com.sdzee.membres.entities.Member;

@ManagedBean
@RequestScoped
public class VerificationParticipantsValidator implements Validator {

    private static final String NICKNAME_REQUIRED       = "Au moins un participant est requis.";
    private static final String SELF_MESSAGE            = "Vous ne pouvez pas être destinataire de votre message.";
    private static final String NICKNAME_DOES_NOT_EXIST = "Le pseudo suivant est incorrect : ";
    private static final String NICKNAMES_DO_NOT_EXIST  = "Les pseudos suivants sont incorrects : ";
    private static final String PARTICIPANTS_SEPARATOR  = ",";
    private static final String SESSION_MEMBER          = "member";

    @EJB
    private MemberDao           memberDao;

    @Override
    public void validate( FacesContext context, UIComponent component, Object value ) throws ValidatorException {
        Member member = (Member) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
                .get( SESSION_MEMBER );

        /* Récupération de la valeur à traiter depuis le paramètre value */
        String participantsNickNames = (String) value;

        if ( participantsNickNames == null || "".equals( participantsNickNames ) ) {
            throw new ValidatorException(
                    new FacesMessage( FacesMessage.SEVERITY_ERROR, NICKNAME_REQUIRED, null ) );
        }

        // Suppression des espaces qui trainent éventuellement ça et là dans la liste
        participantsNickNames = participantsNickNames.replaceAll( "\\s+", "" );
        List<String> participantsNickNamesList = Arrays.asList( participantsNickNames.split( PARTICIPANTS_SEPARATOR ) );

        if ( participantsNickNamesList.contains( member.getNickName() ) ) {
            throw new ValidatorException(
                    new FacesMessage( FacesMessage.SEVERITY_ERROR, SELF_MESSAGE, null ) );
        }

        List<String> badParticipantsNamesList = new ArrayList<String>();

        try {
            // TODO : doit y'avoir moyen de se débrouiller avec une seule requête SELECT WHERE nickName IN ... et ensuite de comparer entre
            // la liste des pseudos des membres retournés, et la liste des noms entrés dans l'input.
            for ( String participantNickName : participantsNickNamesList ) {
                if ( memberDao.find( "nickName", participantNickName.trim() ) == null ) {
                    badParticipantsNamesList.add( participantNickName.trim() );
                }
            }

            /*
             * Si au moins un pseudo est foireux, alors on envoie une exception propre à JSF, qu'on initialise avec un FacesMessage de gravité
             * "Erreur" et contenant le message d'explication. Le framework va alors gérer lui-même cette exception et s'en servir pour afficher le
             * message d'erreur à l'utilisateur.
             */
            if ( badParticipantsNamesList.size() == 1 ) {
                throw new ValidatorException(
                        new FacesMessage( FacesMessage.SEVERITY_ERROR, NICKNAME_DOES_NOT_EXIST
                                + badParticipantsNamesList.get( 0 ), null ) );
            } else if ( badParticipantsNamesList.size() > 1 ) {
                StringBuilder sb = new StringBuilder();
                String participantsSeparator = "";
                for ( String s : badParticipantsNamesList )
                {
                    sb.append( participantsSeparator );
                    participantsSeparator = PARTICIPANTS_SEPARATOR;
                    sb.append( s );
                }
                throw new ValidatorException(
                        new FacesMessage( FacesMessage.SEVERITY_ERROR, NICKNAMES_DO_NOT_EXIST + sb.toString(), null ) );
            }
        } catch ( DAOException e ) {
            /*
             * En cas d'erreur imprévue émanant de la BDD, on prépare un message d'erreur contenant l'exception retournée, pour l'afficher à
             * l'utilisateur ensuite.
             */
            FacesMessage message = new FacesMessage( FacesMessage.SEVERITY_ERROR, e.getMessage(), null );
            FacesContext facesContext = FacesContext.getCurrentInstance();
            facesContext.addMessage( component.getClientId( facesContext ), message );
        }
    }
}