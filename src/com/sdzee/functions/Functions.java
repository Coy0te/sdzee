package com.sdzee.functions;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Locale;

import javax.faces.context.FacesContext;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.pegdown.Extensions;
import org.pegdown.PegDownProcessor;

import com.sdzee.membres.entities.Member;

/**
 * Functions est une classe finale contenant des fonctions EL personnalisées, utilisables depuis les Facelets en y précisant
 * <code>xmlns:uf="http://sdzee.com/functions/functions"</code> parmi les namespaces définis.
 * <p>
 * La configuration est effectuée dans le <code>/META-INF</code> et déclarée dans le web.xml du projet :
 * <p>
 * 
 * <pre>
 *   &lt;context-param&gt;
 *     &lt;param-name&gt;facelets.LIBRARIES&lt;/param-name&gt;
 *     &lt;param-value&gt;/META-INF/functions.taglib.xml&lt;/param-value&gt;
 *   &lt;/context-param&gt;
 * </pre>
 * 
 */
public final class Functions {

    private Functions() {
        // Masque le constructeur.
    }

    /**
     * Convertit la date en durée relative à la date courante (temps écoulé) si la date est proche, en format complet sinon.
     * 
     * @param date la date courante passée depuis l'EL dans la Facelet
     * @return une chaîne contenant une durée relative si la date est proche du jour courant, ou la date complète sinon.
     * 
     */
    public static String convertDateTime( Date date ) {
        if ( date == null ) {
            return "";
        }
        DateTime dateTime = new DateTime( date );
        DateTime today = new DateTime();
        DateTime yesterday = today.minusDays( 1 );
        Period period = new Period( dateTime, today );
        Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();

        DateTimeFormatter fullDateFmt = DateTimeFormat.forPattern( "dd MMMM yyyy à HH:mm" ).withLocale( locale );
        DateTimeFormatter timeFmt = DateTimeFormat.forPattern( "HH:mm" ).withLocale( locale );

        String formattedDate;
        // TODO: réécrire cette méthode un peu plus proprement que ça
        if ( period.getDays() > 0 || period.getWeeks() > 0 || period.getMonths() > 0 || period.getYears() > 0 ) {
            if ( dateTime.toLocalDate().equals( yesterday.toLocalDate() ) ) {
                formattedDate = "Hier à " + dateTime.toString( timeFmt );
            } else {
                formattedDate = "Le " + dateTime.toString( fullDateFmt );
            }
        } else if ( period.getHours() > 0 ) {
            formattedDate = "Il y a environ " + period.getHours();
            formattedDate += period.getHours() > 1 ? " heures" : " heure";
        } else if ( period.getMinutes() > 0 ) {
            formattedDate = "Il y a " + period.getMinutes();
            formattedDate += period.getMinutes() > 1 ? " minutes" : " minute";
        } else {
            formattedDate = "Il y a " + period.getSeconds();
            formattedDate += period.getSeconds() > 1 ? " secondes" : " seconde";
        }
        return formattedDate;
    }

    /**
     * Convertit le texte source contenant des éléments markdown en texte affichable à l'utilisateur.
     * 
     * @param text le texte source contenant les balises markdown passé depuis l'EL dans la Facelet
     * @return une chaîne contenant le texte formaté pour l'affichage à l'utilisateur.
     * 
     */
    public static String parseMarkdown( String text ) {
        // TODO: ne pas initialiser un processor à chaque appel... Un seul pour toute l'appli ? Une Factory quelque part ? Ou autre ?
        PegDownProcessor processor = new PegDownProcessor( Extensions.HARDWRAPS + Extensions.AUTOLINKS
                + Extensions.FENCED_CODE_BLOCKS
                + Extensions.SUPPRESS_ALL_HTML + Extensions.ABBREVIATIONS + Extensions.TABLES );
        text = processor.markdownToHtml( text );

        return text;
    }

    /**
     * Retourne l'url de l'avatar du membre, basée sur son profil s'il a défini un avatar, ou sur son Gravatar sinon.
     * 
     * @param member le membre pour lequel générer l'url
     * @param size la taille de l'image pour laquelle générer l'url (pour le moment, paramètre utile uniquement pour Gravatar)
     * @return l'url de l'avatar à afficher pour le membre donné.
     * 
     */
    public static String gravatar( Member member, int size ) {
        String url;
        if ( member.getAvatar() == null || member.getAvatar().isEmpty() ) {
            url = String.format( "http://www.gravatar.com/avatar/%s?d=identicon&s=%d&r=pg",
                    getHash( member.getEmail() ),
                    size );
        } else {
            url = member.getAvatar();
        }
        return url;
    }

    /**
     * Helper qui retourne l'héxadécimal du hash md5 de l'adresse email (= le token Gravatar).
     * 
     * @param email l'adresse email servant à générer le token
     * @return le token Gravatar.
     * 
     */
    private static String getHash( String email ) {
        try {
            return hex( MessageDigest.getInstance( "MD5" ).digest( email.getBytes( "UTF-8" ) ) );
        } catch ( NoSuchAlgorithmException e ) {
        } catch ( UnsupportedEncodingException e ) {
        }
        return null;
    }

    /**
     * Helper qui transforme une entrée en héxadécimal.
     * 
     * @param array la source de base à convertir
     * @return la chaîne transformée en hexa.
     * 
     */
    private static String hex( byte[] array ) {
        StringBuilder hexBuilder = new StringBuilder();
        for ( byte element : array ) {
            hexBuilder.append( Integer.toHexString( ( element & 0xFF ) | 0x100 ).substring( 1, 3 ) );
        }
        return hexBuilder.toString();
    }
}