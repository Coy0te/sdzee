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
@Table( name = "cours_chapitre" )
public class Chapitre {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long    id;

    @NotNull( message = "{tutos.chapitre.titre.notnull}" )
    private String  titre;

    private String  introduction;

    private String  conclusion;

    @NotNull( message = "{tutos.chapitre.partie.notnull}" )
    @ManyToOne( fetch = FetchType.LAZY )
    @JoinColumn( name = "partie" )
    private Partie  partie;

    private Integer position;

    public String   logo;

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

    public Partie getPartie() {
        return partie;
    }

    public void setPartie( Partie partie ) {
        this.partie = partie;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition( Integer position ) {
        this.position = position;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo( String logo ) {
        this.logo = logo;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction( String introduction ) {
        this.introduction = introduction;
    }

    public String getConclusion() {
        return conclusion;
    }

    public void setConclusion( String conclusion ) {
        this.conclusion = conclusion;
    }

}
