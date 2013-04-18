package com.sdzee.forums.entities;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import com.sdzee.membres.entities.Membre;

@Entity
@Table( name = "forum_vote",
        uniqueConstraints = { @UniqueConstraint( columnNames = { "id_membre", "id_message" } ) } )
public class Vote {
    @NotNull( message = "{forums.vote.membre.notnull}" )
    @ManyToOne( fetch = FetchType.LAZY )
    @JoinColumn( name = "id_membre" )
    private Membre  membre;

    @NotNull( message = "{forums.vote.reponse.notnull}" )
    @ManyToOne( fetch = FetchType.LAZY )
    @JoinColumn( name = "id_message" )
    private Reponse reponse;

    private int     valeur;

    public Membre getMembre() {
        return membre;
    }

    public void setMembre( Membre membre ) {
        this.membre = membre;
    }

    public Reponse getReponse() {
        return reponse;
    }

    public void setReponse( Reponse reponse ) {
        this.reponse = reponse;
    }

    public int getValeur() {
        return valeur;
    }

    public void setValeur( int valeur ) {
        this.valeur = valeur;
    }
}