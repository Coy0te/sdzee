package com.sdzee.membres.entities;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.jasypt.util.password.ConfigurablePasswordEncryptor;

@Entity
@Table( name = "membre" )
public class Membre {
    private static final String ALGO_CHIFFREMENT = "SHA-256";
    private static final String REGEX_EMAIL      = "([^.@]+)(\\.[^.@]+)*@([^.@]+\\.)+([^.@]+)";

    // ID
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long                id;

    // PARAMETRES
    @Column( unique = true )
    @NotNull( message = "{membre.email.notnull}" )
    @Pattern( regexp = REGEX_EMAIL, message = "{membre.email.pattern}" )
    private String              email;

    @NotNull( message = "{membre.motdepasse.notnull}" )
    @Size( min = 3, message = "{membre.motdepasse.taille}" )
    private String              motDePasse;

    // INFOS
    @Column( unique = true )
    @NotNull( message = "{membre.pseudo.notnull}" )
    @Size( min = 3, message = "{membre.pseudo.taille}" )
    private String              pseudo;

    @Size( min = 2, message = "{membre.prenom.taille}" )
    private String              prenom;

    @Size( min = 2, message = "{membre.nom.taille}" )
    private String              nom;

    private Timestamp           dateInscription;

    private Timestamp           dateDerniereConnexion;

    public Long getId() {
        return id;
    }

    public void setId( Long id ) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail( String email ) {
        this.email = email;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse( String motDePasse ) {
        /*
         * Utilisation de la bibliothèque Jasypt pour chiffrer le mot de passe
         * efficacement.
         * 
         * L'algorithme SHA-256 est ici utilisé, avec par défaut un salage
         * aléatoire et un grand nombre d'itérations de la fonction de hashage.
         * 
         * La String retournée est de longueur 56 et contient le hash en Base64.
         */
        ConfigurablePasswordEncryptor passwordEncryptor = new ConfigurablePasswordEncryptor();
        passwordEncryptor.setAlgorithm( ALGO_CHIFFREMENT );
        passwordEncryptor.setPlainDigest( false );
        String motDePasseChiffre = passwordEncryptor.encryptPassword( motDePasse );

        this.motDePasse = motDePasseChiffre;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo( String pseudo ) {
        this.pseudo = pseudo;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom( String prenom ) {
        this.prenom = prenom;
    }

    public String getNom() {
        return nom;
    }

    public void setNom( String nom ) {
        this.nom = nom;
    }

    public Timestamp getDateInscription() {
        return dateInscription;
    }

    public void setDateInscription( Timestamp dateInscription ) {
        this.dateInscription = dateInscription;
    }

    public Timestamp getDateDerniereConnexion() {
        return dateDerniereConnexion;
    }

    public void setDateDerniereConnexion( Timestamp dateDerniereConnexion ) {
        this.dateDerniereConnexion = dateDerniereConnexion;
    }

}
