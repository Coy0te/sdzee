package com.sdzee.forums.entities;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.sdzee.membres.entities.Membre;

@Entity
public class Sujet {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long          id;
    private String        titre;
    // FK
    private Membre        auteur;
    // FK
    private Forum         forum;
    private Timestamp     dateCreation;
    private Boolean       ferme;
    private Boolean       sticky;
    private Integer       vues;
    private List<Reponse> messages;
}
