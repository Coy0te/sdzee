package com.sdzee.forums.entities;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table( name = "forum_forum" )
public class Forum {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long      id;

    @NotNull( message = "{forums.forum.titre.notnull}" )
    private String    titre;

    @NotNull( message = "{forums.forum.description.notnull}" )
    private String    description;

    @ManyToOne( fetch = FetchType.LAZY )
    @JoinColumn( name = "categorie" )
    @NotNull( message = "{forums.forum.categorie.notnull}" )
    private Categorie categorie;

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
}
