package com.sdzee.forums.entities;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.sdzee.membres.entities.Membre;

@Entity
public class Reponse {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long      id;
    // FK
    private Membre    auteur;
    // FK
    private Sujet     sujet;
    private String    texte;
    private Timestamp dateCreation;
    private String    adresseIP;
}
