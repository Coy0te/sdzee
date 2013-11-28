package com.sdzee.forums.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

/**
 * Bookmark est l'entité JPA décrivant la table des bookmarks. La validation des champs s'appuie sur la JSR-303.
 * 
 * @author Médéric Munier
 * @version %I%, %G%
 */
@Entity
@Table( name = "forum_bookmark", uniqueConstraints = { @UniqueConstraint( columnNames = {
        "member",
        "topic" } ) } )
@IdClass( BookmarkId.class )
public class Bookmark implements Serializable {
    @NotNull( message = "{forums.bookmark.memberId.notnull}" )
    @Column( name = "member" )
    @Id
    private Long memberId;

    @NotNull( message = "{forums.bookmark.topicId.notnull}" )
    @Column( name = "topic" )
    @Id
    private Long topicId;

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId( Long memberId ) {
        this.memberId = memberId;
    }

    public Long getTopicId() {
        return topicId;
    }

    public void setTopicId( Long topicId ) {
        this.topicId = topicId;
    }
}

/**
 * BookmarkId est la classe de définition de la clé primaire composite de la table des bookmarks.
 * 
 * @author Médéric Munier
 * @version %I%, %G%
 */
class BookmarkId implements Serializable {
    Long memberId;
    Long topicId;
}
