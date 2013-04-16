package com.sdzee.forums.entities;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.sdzee.membres.entities.Membre;

@Entity
@Table( name = "forum_sujet" )
public class Sujet {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long      id;

    @NotNull( message = "{forums.sujet.titre.notnull}" )
    @Size( max = 60, message = "{forums.sujet.titre.taille}" )
    private String    titre;

    @Size( max = 80, message = "{forums.sujet.sousTitre.taille}" )
    private String    sousTitre;

    @NotNull( message = "{forums.sujet.texte.notnull}" )
    private String    texte;

    @NotNull( message = "{forums.sujet.auteur.notnull}" )
    @ManyToOne( fetch = FetchType.LAZY )
    @JoinColumn( name = "auteur" )
    private Membre    auteur;

    @NotNull( message = "{forums.sujet.forum.notnull}" )
    @ManyToOne( fetch = FetchType.LAZY )
    @JoinColumn( name = "forum" )
    private Forum     forum;

    @NotNull( message = "{forums.sujet.dateCreation.notnull}" )
    private Timestamp dateCreation;

    @Column( nullable = false, columnDefinition = "TINYINT(1)" )
    private Boolean   ferme;

    @Column( nullable = false, columnDefinition = "TINYINT(1)" )
    private Boolean   sticky;

    private Integer   vues;

    private Integer   votesPositifs;

    private Integer   votesNegatifs;

    @NotNull( message = "{forums.sujet.adresseIP.notnull}" )
    private String    adresseIP;

    public Long getId() {
        return id;
    }

    public void setId( Long id ) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre( String titre ) {
        this.titre = titre;
    }

    public Membre getAuteur() {
        return auteur;
    }

    public void setAuteur( Membre auteur ) {
        this.auteur = auteur;
    }

    public Forum getForum() {
        return forum;
    }

    public void setForum( Forum forum ) {
        this.forum = forum;
    }

    public Timestamp getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation( Timestamp dateCreation ) {
        this.dateCreation = dateCreation;
    }

    public Boolean getFerme() {
        return ferme;
    }

    public void setFerme( Boolean ferme ) {
        this.ferme = ferme;
    }

    public Boolean getSticky() {
        return sticky;
    }

    public void setSticky( Boolean sticky ) {
        this.sticky = sticky;
    }

    public Integer getVues() {
        return vues;
    }

    public void setVues( Integer vues ) {
        this.vues = vues;
    }

    public Integer getVotesPositifs() {
        return votesPositifs;
    }

    public void setVotesPositifs( Integer votesPositifs ) {
        this.votesPositifs = votesPositifs;
    }

    public Integer getVotesNegatifs() {
        return votesNegatifs;
    }

    public void setVotesNegatifs( Integer votesNegatifs ) {
        this.votesNegatifs = votesNegatifs;
    }

    public String getTexte() {
        return texte;
    }

    public void setTexte( String texte ) {
        this.texte = texte;
    }

    public String getAdresseIP() {
        return adresseIP;
    }

    public void setAdresseIP( String adresseIP ) {
        this.adresseIP = adresseIP;
    }

    public String getSousTitre() {
        return sousTitre;
    }

    public void setSousTitre( String sousTitre ) {
        this.sousTitre = sousTitre;
    }
}
