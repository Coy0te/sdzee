package com.sdzee.forums.entities;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class Forum {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long        id;
    @NotNull( message = "{forum.titre.notnull}" )
    private String      titre;
    @NotNull( message = "{forum.description.notnull}" )
    private String      description;
    // FK
    @NotNull( message = "{forum.categorie.notnull}" )
    private Categorie   categorie;
    private List<Sujet> sujets;

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

    public String getDescription() {
        return description;
    }

    public void setDescription( String description ) {
        this.description = description;
    }

    public Categorie getCategorie() {
        return categorie;
    }

    public void setCategorie( Categorie categorie ) {
        this.categorie = categorie;
    }

    public List<Sujet> getSujets() {
        return sujets;
    }

    public void setSujets( List<Sujet> sujets ) {
        this.sujets = sujets;
    }
}
