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
@Table( name = "tuto_question" )
public class QCMQuestion {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long     id;

    @NotNull( message = "{tuto.question.enonce.notnull}" )
    private String   enonce;

    @NotNull( message = "{tuto.question.explication.notnull}" )
    private String   explication;

    @NotNull( message = "{tuto.question.chapitre.notnull}" )
    @ManyToOne( fetch = FetchType.LAZY )
    @JoinColumn( name = "mtchap" )
    private Chapitre chapitre;                // peut aussi être un objet de type mini-tuto

    @NotNull( message = "{tuto.question.position.notnull}" )
    private Integer  position;

    @NotNull( message = "{tuto.question.dateCreation.notnull}" )
    @Temporal( TemporalType.TIMESTAMP )
    private Date     dateCreation;

    @NotNull( message = "{tuto.question.dateDerniereModification.notnull}" )
    @Temporal( TemporalType.TIMESTAMP )
    private Date     dateDerniereModification;

    @NotNull( message = "{tuto.question.adresseIP.notnull}" )
    private String   adresseIP;

    public Long getId() {
        return id;
    }

    public void setId( Long id ) {
        this.id = id;
    }

    public String getEnonce() {
        return enonce;
    }

    public void setEnonce( String enonce ) {
        this.enonce = enonce;
    }

    public String getExplication() {
        return explication;
    }

    public void setExplication( String explication ) {
        this.explication = explication;
    }

    public Chapitre getChapitre() {
        return chapitre;
    }

    public void setChapitre( Chapitre chapitre ) {
        this.chapitre = chapitre;
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
