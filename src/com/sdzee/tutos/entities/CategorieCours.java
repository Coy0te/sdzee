package com.sdzee.tutos.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table( name = "cours_categorie" )
public class CategorieCours {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long   id;
    @NotNull( message = "{tutos.categorie.titre.notnull}" )
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
