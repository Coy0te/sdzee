package com.sdzee.functions;

import java.util.Date;
import java.util.Locale;

import javax.faces.context.FacesContext;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.pegdown.Extensions;
import org.pegdown.PegDownProcessor;

/**
 * Classe finale contenant des fonctions EL personnalisées, utilisables depuis les Facelets en y précisant
 * xmlns:uf="http://sdzee.com/functions/functions" dans les namespaces définis.
 * 
 * La configuration est effectuée dans le /META-INF et déclarée dans le web.xml du projet :
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
        // Hide constructor.
    }

    /**
     * Convertit la date : en durée relative à la date courante (temps écoulé) si la date est proche, en format complet sinon.
     * 
     * @param date La date courante passée depuis l'EL dans la Facelet
     * @return Une chaîne contenant une durée relative si la date est proche du jour courant, ou la date complète sinon.
     * 
     */
    public static String convertDateTime( Date date ) {
        DateTime dateTime = new DateTime( date );
        DateTime today = new DateTime();
        DateTime yesterday = today.minusDays( 1 );
        Period period = new Period( dateTime, today );
        Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();

        DateTimeFormatter fullDateFmt = DateTimeFormat.forPattern( "dd MMMM yyyy à HH:mm" ).withLocale( locale );
        DateTimeFormatter timeFmt = DateTimeFormat.forPattern( "HH:mm" ).withLocale( locale );

        String rendu;
        // TODO: réécrire cette méthode un peu plus proprement que ça
        if ( period.getDays() > 0 || period.getWeeks() > 0 || period.getMonths() > 0 || period.getYears() > 0 ) {
            if ( dateTime.toLocalDate().equals( yesterday.toLocalDate() ) ) {
                rendu = "Hier à " + dateTime.toString( timeFmt );
            } else {
                rendu = "Le " + dateTime.toString( fullDateFmt );
            }
        } else if ( period.getHours() > 0 ) {
            rendu = "Il y a environ " + period.getHours();
            rendu += period.getHours() > 1 ? " heures" : " heure";
        } else if ( period.getMinutes() > 0 ) {
            rendu = "Il y a " + period.getMinutes();
            rendu += period.getMinutes() > 1 ? " minutes" : " minute";
        } else {
            rendu = "Il y a " + period.getSeconds();
            rendu += period.getSeconds() > 1 ? " secondes" : " seconde";
        }
        return rendu;
    }

    /**
     * Convertit le texte source contenant des éléments markdown en texte affichable à l'utilisateur.
     * 
     * @param texte Le texte source contenant les balises markdown passé depuis l'EL dans la Facelet
     * @return Une chaîne contenant le texte formaté pour l'affichage à l'utilisateur.
     * 
     */
    public static String parseMarkdown( String texte ) {
        // TODO: ne pas initialiser un processor à chaque appel... Un seul pour toute l'appli ? Ou autre ?
        PegDownProcessor processor = new PegDownProcessor( Extensions.HARDWRAPS + Extensions.AUTOLINKS + Extensions.FENCED_CODE_BLOCKS
                + Extensions.SUPPRESS_ALL_HTML );
        texte = processor.markdownToHtml( texte );

        return texte;
    }
}