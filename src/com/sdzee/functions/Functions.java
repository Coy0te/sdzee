package com.sdzee.functions;

import java.sql.Timestamp;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public final class Functions {

    private Functions() {
        // Hide constructor.
    }

    public static String convertDateTime( Timestamp date ) {
        DateTime dateTime = new DateTime( date );
        DateTime today = new DateTime();
        DateTime yesterday = today.minusDays( 1 );
        Period period = new Period( dateTime, today );

        DateTimeFormatter fullDateFmt = DateTimeFormat.forPattern( "dd MMMM yyyy à HH:mma" );
        DateTimeFormatter timeFmt = DateTimeFormat.forPattern( "hh:mma" );

        String resultat;
        if ( period.getHours() > 24 ) {
            if ( dateTime.toLocalDate().equals( yesterday.toLocalDate() ) ) {
                resultat = "Hier à " + dateTime.toString( timeFmt );
            } else {
                resultat = "Le " + dateTime.toString( fullDateFmt );
            }
        } else if ( period.getMinutes() > 60 ) {
            resultat = "Il y a environ " + period.getHours() + " heures";
        } else if ( period.getSeconds() > 60 ) {
            resultat = "Il y a " + period.getMinutes() + " minutes";
        } else {
            resultat = "Il y a " + period.getSeconds() + " secondes";
        }
        return resultat;
    }

}