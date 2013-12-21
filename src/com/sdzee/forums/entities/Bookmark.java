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

    @Override
    public String toString() {
        return String.format( "Bookmark[%d,%d]", memberId, topicId );
    }

    @Override
    public boolean equals( Object obj ) {
        if ( this == obj )
            return true;
        if ( obj == null )
            return false;
        if ( getClass() != obj.getClass() )
            return false;
        Bookmark other = (Bookmark) obj;
        if ( memberId == null ) {
            if ( other.memberId != null )
                return false;
        } else if ( topicId == null ) {
            if ( other.topicId != null )
                return false;
        } else if ( memberId != other.memberId || topicId != other.topicId )
            return false;
        return true;
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

    public BookmarkId() {
    }
}
