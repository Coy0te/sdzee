package com.sdzee.forums.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

@Entity
@Table( name = "forum_notification", uniqueConstraints = { @UniqueConstraint( columnNames = {
        "id_membre",
        "id_sujet" } ) } )
@IdClass( NotificationId.class )
public class Notification {

    @NotNull( message = "{forums.notification.idMembre.notnull}" )
    @Column( name = "id_membre" )
    @Id
    private Long    idMembre;

    @NotNull( message = "{forums.notification.idSujet.notnull}" )
    @Column( name = "id_sujet" )
    @Id
    private Long    idSujet;

    @ManyToOne( fetch = FetchType.EAGER )
    @JoinColumn( name = "reponse" )
    private Reponse reponse;

    public Long getIdMembre() {

        return idMembre;
    }

    public void setIdMembre( Long idMembre ) {

        this.idMembre = idMembre;
    }

    public Long getIdSujet() {

        return idSujet;
    }

    public void setIdSujet( Long idSujet ) {

        this.idSujet = idSujet;
    }

    public Reponse getReponse() {

        return reponse;
    }

    public void setReponse( Reponse reponse ) {

        this.reponse = reponse;
    }

}

/* Classe de définition de la clé primaire composite */
class NotificationId implements Serializable {

    Long idMembre;

    Long idSujet;

}
