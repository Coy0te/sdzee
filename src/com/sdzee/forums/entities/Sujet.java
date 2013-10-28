package com.sdzee.forums.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.sdzee.membres.entities.Membre;

@Entity
@Table( name = "forum_sujet" )
public class Sujet {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long    id;

    @NotNull( message = "{forums.sujet.titre.notnull}" )
    @Size( max = 60, message = "{forums.sujet.titre.taille}" )
    private String  titre;

    @Size( max = 80, message = "{forums.sujet.sousTitre.taille}" )
    private String  sousTitre;

    @NotNull( message = "{forums.sujet.texte.notnull}" )
    private String  texte;

    @NotNull( message = "{forums.sujet.auteur.notnull}" )
    @ManyToOne( fetch = FetchType.LAZY )
    @JoinColumn( name = "auteur" )
    private Membre  auteur;

    @NotNull( message = "{forums.sujet.forum.notnull}" )
    @ManyToOne( fetch = FetchType.LAZY )
    @JoinColumn( name = "forum" )
    private Forum   forum;

    @NotNull( message = "{forums.sujet.dateCreation.notnull}" )
    @Temporal( TemporalType.TIMESTAMP )
    private Date    dateCreation;

    @Column( nullable = false, columnDefinition = "TINYINT(1)" )
    private boolean ferme         = false;

    @Column( nullable = false, columnDefinition = "TINYINT(1)" )
    private boolean sticky        = false;

    @Column( nullable = false, columnDefinition = "TINYINT(1)" )
    private boolean resolu        = false;

    private Integer vues          = 0;

    @Temporal( TemporalType.TIMESTAMP )
    private Date    lastEditDate;

    @ManyToOne( fetch = FetchType.LAZY )
    @JoinColumn( name = "lastEditBy" )
    private Membre  lastEditBy;

    private Integer votesPositifs = 0;

    private Integer votesNegatifs = 0;

    @NotNull( message = "{forums.sujet.adresseIP.notnull}" )
    private String  adresseIP;

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

    public Membre getAuteur() {
        return auteur;
    }

    public void setAuteur( Membre auteur ) {
        this.auteur = auteur;
    }

    public Forum getForum() {
        return forum;
    }

    public void setForum( Forum forum ) {
        this.forum = forum;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation( Date dateCreation ) {
        this.dateCreation = dateCreation;
    }

    public boolean isFerme() {
        return ferme;
    }

    public void setFerme( boolean ferme ) {
        this.ferme = ferme;
    }

    public boolean isSticky() {
        return sticky;
    }

    public void setSticky( boolean sticky ) {
        this.sticky = sticky;
    }

    public Integer getVues() {
        return vues;
    }

    public void setVues( Integer vues ) {
        this.vues = vues;
    }

    public Integer getVotesPositifs() {
        return votesPositifs;
    }

    public void setVotesPositifs( Integer votesPositifs ) {
        this.votesPositifs = votesPositifs;
    }

    public void addVotePositif() {
        this.votesPositifs++;
    }

    public void removeVotePositif() {
        this.votesPositifs--;
    }

    public Integer getVotesNegatifs() {
        return votesNegatifs;
    }

    public void setVotesNegatifs( Integer votesNegatifs ) {
        this.votesNegatifs = votesNegatifs;
    }

    public void addVoteNegatif() {
        this.votesNegatifs++;
    }

    public void removeVoteNegatif() {
        this.votesNegatifs--;
    }

    public String getTexte() {
        return texte;
    }

    public void setTexte( String texte ) {
        this.texte = texte;
    }

    public String getAdresseIP() {
        return adresseIP;
    }

    public void setAdresseIP( String adresseIP ) {
        this.adresseIP = adresseIP;
    }

    public String getSousTitre() {
        return sousTitre;
    }

    public void setSousTitre( String sousTitre ) {
        this.sousTitre = sousTitre;
    }

    public boolean isResolu() {
        return resolu;
    }

    public void setResolu( boolean resolu ) {
        this.resolu = resolu;
    }

    public Date getLastEditDate() {
        return lastEditDate;
    }

    public void setLastEditDate( Date lastEditDate ) {
        this.lastEditDate = lastEditDate;
    }

    public Membre getLastEditBy() {
        return lastEditBy;
    }

    public void setLastEditBy( Membre lastEditBy ) {
        this.lastEditBy = lastEditBy;
    }
}
