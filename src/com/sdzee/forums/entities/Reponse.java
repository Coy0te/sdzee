package com.sdzee.forums.entities;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.sdzee.membres.entities.Membre;

@Entity
@Table( name = "forum_reponse" )
public class Reponse {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long      id;

    @NotNull( message = "{forums.reponse.auteur.notnull}" )
    @ManyToOne( fetch = FetchType.LAZY )
    @JoinColumn( name = "auteur" )
    private Membre    auteur;

    @NotNull( message = "{forums.reponse.sujet.notnull}" )
    @ManyToOne( fetch = FetchType.LAZY )
    @JoinColumn( name = "sujet" )
    private Sujet     sujet;

    @NotNull( message = "{forums.reponse.texte.notnull}" )
    private String    texte;

    @NotNull( message = "{forums.reponse.dateCreation.notnull}" )
    private Timestamp dateCreation;

    private Integer   votesPositifs;

    private Integer   votesNegatifs;

    @NotNull( message = "{forums.reponse.adresseIP.notnull}" )
    private String    adresseIP;

    public Long getId() {
        return id;
    }

    public void setId( Long id ) {
        this.id = id;
    }

    public Membre getAuteur() {
        return auteur;
    }

    public void setAuteur( Membre auteur ) {
        this.auteur = auteur;
    }

    public Sujet getSujet() {
        return sujet;
    }

    public void setSujet( Sujet sujet ) {
        this.sujet = sujet;
    }

    public String getTexte() {
        return texte;
    }

    public void setTexte( String texte ) {
        this.texte = texte;
    }

    public Timestamp getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation( Timestamp dateCreation ) {
        this.dateCreation = dateCreation;
    }

    public Integer getVotesPositifs() {
        return votesPositifs;
    }

    public void setVotesPositifs( Integer votesPositifs ) {
        this.votesPositifs = votesPositifs;
    }

    public void addVotePositif() {
        this.votesPositifs++;
    }

    public void removeVotePositif() {
        this.votesPositifs--;
    }

    public Integer getVotesNegatifs() {
        return votesNegatifs;
    }

    public void setVotesNegatifs( Integer votesNegatifs ) {
        this.votesNegatifs = votesNegatifs;
    }

    public void addVoteNegatif() {
        this.votesNegatifs++;
    }

    public void removeVoteNegatif() {
        this.votesNegatifs--;
    }

    public String getAdresseIP() {
        return adresseIP;
    }

    public void setAdresseIP( String adresseIP ) {
        this.adresseIP = adresseIP;
    }
}
