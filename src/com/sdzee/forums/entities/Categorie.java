package com.sdzee.forums.entities;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table( name = "forum_categorie" )
public class Categorie {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long        id;
    @NotNull( message = "{categorie.titre.notnull}" )
    private String      titre;
    private List<Forum> forums;

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

    public List<Forum> getForums() {
        return forums;
    }

    public void setForums( List<Forum> forums ) {
        this.forums = forums;
    }

}
