package com.sdzee.forums.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

@Entity
@Table( name = "forum_vote",
        uniqueConstraints = { @UniqueConstraint( columnNames = { "id_membre", "id_objet", "type_objet" } ) } )
@IdClass( VoteId.class )
public class Vote {
    @NotNull( message = "{forums.vote.idMembre.notnull}" )
    @Column( name = "id_membre" )
    @Id
    private Long   idMembre;

    @NotNull( message = "{forums.vote.idObjet.notnull}" )
    @Column( name = "id_objet" )
    @Id
    private Long   idObjet;

    @NotNull( message = "{forums.vote.tpyeObjet.notnull}" )
    @Column( name = "type_objet" )
    @Id
    private String typeObjet;

    private int    valeur;

    public Long getIdMembre() {
        return idMembre;
    }

    public void setIdMembre( Long idMembre ) {
        this.idMembre = idMembre;
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

/* Classe de définition de la clé primaire composite */
class VoteId implements Serializable {
    Long   idMembre;
    Long   idObjet;
    String typeObjet;
}