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
@Table( name = "cours_section" )
public class Section {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long     id;

    @NotNull( message = "{tutos.section.titre.notnull}" )
    private String   titre;

    @NotNull( message = "{tutos.section.texte.notnull}" )
    private String   texte;

    @NotNull( message = "{tutos.section.chapitre.notnull}" )
    @ManyToOne( fetch = FetchType.LAZY )
    @JoinColumn( name = "chapitre" )
    private Chapitre chapitre;

    private Integer  position;

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

    public Chapitre getChapitre() {
        return chapitre;
    }

    public void setChapitre( Chapitre chapitre ) {
        this.chapitre = chapitre;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition( Integer position ) {
        this.position = position;
    }

    public String getTexte() {
        return texte;
    }

    public void setTexte( String texte ) {
        this.texte = texte;
    }

}
