package com.sdzee.forums.entities;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.sdzee.membres.entities.Membre;

@Entity
@Table( name = "forum_reponse" )
public class Reponse {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long      id;
    @NotNull( message = "{reponse.auteur.notnull}" )
    @ManyToMany
    @JoinColumn( name = "auteur" )
    private Membre    auteur;
    @NotNull( message = "{reponse.sujet.notnull}" )
    @OneToOne
    @JoinColumn( name = "sujet" )
    private Sujet     sujet;
    @NotNull( message = "{reponse.texte.notnull}" )
    private String    texte;
    @NotNull( message = "{reponse.dateCreation.notnull}" )
    private Timestamp dateCreation;
    @NotNull( message = "{reponse.adresseIP.notnull}" )
    private String    adresseIP;
}
