package com.sdzee.forums.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "forum_bookmark", uniqueConstraints = {@UniqueConstraint(columnNames = {
    "id_membre",
    "id_objet"})})
@IdClass(BookmarkId.class)
public class Bookmark {

    @NotNull(message = "{forums.bookmark.idMembre.notnull}")
    @Column(name = "id_membre")
    @Id
    private Long idMembre;

    @NotNull(message = "{forums.bookmark.idSujet.notnull}")
    @Column(name = "id_sujet")
    @Id
    private Long idSujet;

    public Long getIdMembre() {

        return idMembre;
    }

    public void setIdMembre(Long idMembre) {

        this.idMembre = idMembre;
    }

    public Long getIdSujet() {

        return idSujet;
    }

    public void setIdSujet(Long idSujet) {

        this.idSujet = idSujet;
    }

}

/* Classe de définition de la clé primaire composite */
class BookmarkId {

    Long idMembre;

    Long idSujet;

}
