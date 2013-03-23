package com.sdzee.tutos.entities;

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
@Table( name = "cours_partie" )
public class Partie {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long    id;

    @NotNull( message = "{tutos.partie.titre.notnull}" )
    private String  titre;

    @NotNull( message = "{tutos.partie.cours.notnull}" )
    @ManyToOne( fetch = FetchType.LAZY )
    @JoinColumn( name = "cours" )
    private Cours   cours;

    private Integer position;

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

    public Integer getPosition() {
        return position;
    }

    public void setPosition( Integer position ) {
        this.position = position;
    }

    public Cours getCours() {
        return cours;
    }

    public void setCours( Cours cours ) {
        this.cours = cours;
    }

}
