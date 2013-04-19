package com.sdzee.forums.entities;

import javax.persistence.Column;
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
        uniqueConstraints = { @UniqueConstraint( columnNames = { "id_membre", "id_objet", "type_objet" } ) } )
public class Vote {
    @NotNull( message = "{forums.vote.membre.notnull}" )
    @ManyToOne( fetch = FetchType.LAZY )
    @JoinColumn( name = "id_membre" )
    private Membre membre;

    @NotNull( message = "{forums.vote.idObjet.notnull}" )
    @Column( name = "id_objet" )
    private Long   idObjet;

    @NotNull( message = "{forums.vote.tpyeObjet.notnull}" )
    @Column( name = "type_objet" )
    private String typeObjet;

    private int    valeur;

    public Membre getMembre() {
        return membre;
    }

    public void setMembre( Membre membre ) {
        this.membre = membre;
    }

    public Long getIdObjet() {
        return idObjet;
    }

    public void setIdObjet( Long idObjet ) {
        this.idObjet = idObjet;
    }

    public String getTypeObjet() {
        return typeObjet;
    }

    public void setTypeObjet( String typeObjet ) {
        this.typeObjet = typeObjet;
    }

    public int getValeur() {
        return valeur;
    }

    public void setValeur( int valeur ) {
        this.valeur = valeur;
    }
}