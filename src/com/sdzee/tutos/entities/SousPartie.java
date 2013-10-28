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
@Table( name = "tuto_souspartie" )
public class SousPartie {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long     id;

    @NotNull( message = "{tuto.souspartie.titre.notnull}" )
    private String   titre;

    private String   texte;

    @NotNull( message = "{tuto.souspartie.mtchap.notnull}" )
    @ManyToOne( fetch = FetchType.EAGER )
    @JoinColumn( name = "mtchap" )
    private BTMTChap mtchap;                  // côté "customer" du mapping d'une sous-partie qui appartient à un chapitre ou un MT

    @NotNull( message = "{tuto.souspartie.position.notnull}" )
    private Integer  position;

    @NotNull( message = "{tuto.souspartie.dateCreation.notnull}" )
    @Temporal( TemporalType.TIMESTAMP )
    private Date     dateCreation;

    @NotNull( message = "{tuto.souspartie.dateDerniereModification.notnull}" )
    @Temporal( TemporalType.TIMESTAMP )
    private Date     dateDerniereModification;

    @NotNull( message = "{tuto.souspartie.adresseIP.notnull}" )
    private String   adresseIP;

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

    public String getTexte() {
        return texte;
    }

    public void setTexte( String texte ) {
        this.texte = texte;
    }

    public BTMTChap getMtchap() {
        return mtchap;
    }

    public void setMtchap( BTMTChap mtchap ) {
        this.mtchap = mtchap;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition( Integer position ) {
        this.position = position;
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
