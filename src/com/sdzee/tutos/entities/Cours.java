package com.sdzee.tutos.entities;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.joda.time.Duration;

import com.sdzee.membres.entities.Membre;

@Entity
@Table( name = "cours_cours" )
public class Cours {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long           id;

    @NotNull( message = "{tutos.cours.titre.notnull}" )
    private String         titre;

    @NotNull( message = "{tutos.cours.auteur.notnull}" )
    @ManyToOne( fetch = FetchType.LAZY )
    @JoinColumn( name = "auteur" )
    private Membre         auteur;

    @NotNull( message = "{tutos.cours.categorie.notnull}" )
    @ManyToOne( fetch = FetchType.LAZY )
    @JoinColumn( name = "categorie" )
    private CategorieCours categorie;

    @NotNull( message = "{tutos.cours.dateCreation.notnull}" )
    private Timestamp      dateCreation;

    @Column( nullable = false, columnDefinition = "TINYINT(1)" )
    private Boolean        sticky;

    private Integer        vues;

    private Integer        note;

    private Integer        difficulte;

    private Duration       duree;

    @NotNull( message = "{tutos.cours.licence.notnull}" )
    @ManyToOne( fetch = FetchType.LAZY )
    @JoinColumn( name = "licence" )
    private Licence        licence;

}
