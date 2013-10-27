package com.sdzee.tutos.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

@Entity
@Table( name = "tuto_btmtchap" )
public class Chapitre {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long    id;

    @NotNull( message = "{tuto.chapitre.titre.notnull}" )
    private String  titre;

    @ManyToOne( fetch = FetchType.LAZY )
    @JoinColumn( name = "partie" )
    private Partie  partie;

    @ManyToOne( fetch = FetchType.LAZY )
    @JoinColumn( name = "licence" )
    private Licence licence;

    private boolean fini;

    @NotNull( message = "{tuto.chapitre.position.notnull}" )
    private Integer position;

    private Integer difficulte;

    private String  introduction;

    private String  conclusion;

    @NotNull( message = "{tuto.chapitre.dateCreation.notnull}" )
    @Temporal( TemporalType.TIMESTAMP )
    private Date    dateCreation;

    @NotNull( message = "{tuto.chapitre.dateDerniereModification.notnull}" )
    @Temporal( TemporalType.TIMESTAMP )
    private Date    dateDerniereModification;

    @NotNull( message = "{tuto.chapitre.adresseIP.notnull}" )
    private String  adresseIP;

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

    public Partie getPartie() {
        return partie;
    }

    public void setPartie( Partie partie ) {
        this.partie = partie;
    }

    public Licence getLicence() {
        return licence;
    }

    public void setLicence( Licence licence ) {
        this.licence = licence;
    }

    public boolean isFini() {
        return fini;
    }

    public void setFini( boolean fini ) {
        this.fini = fini;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition( Integer position ) {
        this.position = position;
    }

    public Integer getDifficulte() {
        return difficulte;
    }

    public void setDifficulte( Integer difficulte ) {
        this.difficulte = difficulte;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction( String introduction ) {
        this.introduction = introduction;
    }

    public String getConclusion() {
        return conclusion;
    }

    public void setConclusion( String conclusion ) {
        this.conclusion = conclusion;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation( Date dateCreation ) {
        this.dateCreation = dateCreation;
    }

    public Date getDateDerniereModification() {
        return dateDerniereModification;
    }

    public void setDateDerniereModification( Date dateDerniereModification ) {
        this.dateDerniereModification = dateDerniereModification;
    }

    public String getAdresseIP() {
        return adresseIP;
    }

    public void setAdresseIP( String adresseIP ) {
        this.adresseIP = adresseIP;
    }

}
