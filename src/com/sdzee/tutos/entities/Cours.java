package com.sdzee.tutos.entities;

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

import org.joda.time.Duration;

import com.sdzee.membres.entities.Membre;

@Entity
@Table( name = "cours_cours" )
public class Cours {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long      id;

    @NotNull( message = "{tutos.cours.titre.notnull}" )
    private String    titre;

    @NotNull( message = "{tutos.cours.auteur.notnull}" )
    @ManyToOne( fetch = FetchType.LAZY )
    @JoinColumn( name = "auteur" )
    private Membre    auteur;

    @NotNull( message = "{tutos.cours.categorie.notnull}" )
    @ManyToOne( fetch = FetchType.LAZY )
    @JoinColumn( name = "categorie" )
    private Categorie categorie;

    @NotNull( message = "{tutos.cours.dateCreation.notnull}" )
    private Timestamp dateCreation;

    @Column( nullable = false, columnDefinition = "TINYINT(1)" )
    private Boolean   sticky;

    private Integer   vues;

    private Integer   note;

    private Integer   difficulte;

    private Duration  duree;

    @NotNull( message = "{tutos.cours.licence.notnull}" )
    @ManyToOne( fetch = FetchType.LAZY )
    @JoinColumn( name = "licence" )
    private Licence   licence;

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

    public Categorie getCategorie() {
        return categorie;
    }

    public void setCategorie( Categorie categorie ) {
        this.categorie = categorie;
    }

    public Timestamp getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation( Timestamp dateCreation ) {
        this.dateCreation = dateCreation;
    }

    public Boolean isSticky() {
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

    public Integer getNote() {
        return note;
    }

    public void setNote( Integer note ) {
        this.note = note;
    }

    public Integer getDifficulte() {
        return difficulte;
    }

    public void setDifficulte( Integer difficulte ) {
        this.difficulte = difficulte;
    }

    public Duration getDuree() {
        return duree;
    }

    public void setDuree( Duration duree ) {
        this.duree = duree;
    }

    public Licence getLicence() {
        return licence;
    }

    public void setLicence( Licence licence ) {
        this.licence = licence;
    }
}
