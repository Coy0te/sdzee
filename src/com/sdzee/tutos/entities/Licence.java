package com.sdzee.tutos.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table( name = "licence" )
public class Licence {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long   id;

    @NotNull( message = "{tutos.licence.titre.notnull}" )
    private String titre;

    private String description;

    private String lien;

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

    public String getLien() {
        return lien;
    }

    public void setLien( String lien ) {
        this.lien = lien;
    }

}
