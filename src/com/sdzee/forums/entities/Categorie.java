package com.sdzee.forums.entities;

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
    private Long   id;
    @NotNull( message = "{forums.categorie.titre.notnull}" )
    private String titre;

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

}
