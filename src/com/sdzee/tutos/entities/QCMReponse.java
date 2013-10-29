package com.sdzee.tutos.entities;

import java.util.Date;

import javax.persistence.Column;
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
public class QCMReponse {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long        id;

    @NotNull( message = "{tuto.reponse.texte.notnull}" )
    private String      texte;

    @Column( nullable = false, columnDefinition = "TINYINT(1)" )
    private boolean     correct;

    @NotNull( message = "{tuto.reponse.question.notnull}" )
    @ManyToOne( fetch = FetchType.EAGER )
    @JoinColumn( name = "question" )
    private QCMQuestion question;

    @NotNull( message = "{tuto.reponse.dateCreation.notnull}" )
    @Temporal( TemporalType.TIMESTAMP )
    private Date        dateCreation;

    @NotNull( message = "{tuto.reponse.dateDerniereModification.notnull}" )
    @Temporal( TemporalType.TIMESTAMP )
    private Date        dateDerniereModification;

    @NotNull( message = "{tuto.reponse.adresseIP.notnull}" )
    private String      adresseIP;

    public Long getId() {
        return id;
    }

    public void setId( Long id ) {
        this.id = id;
    }

    public String getTexte() {
        return texte;
    }

    public void setTexte( String texte ) {
        this.texte = texte;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect( boolean correct ) {
        this.correct = correct;
    }

    public QCMQuestion getQuestion() {
        return question;
    }

    public void setQuestion( QCMQuestion question ) {
        this.question = question;
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
