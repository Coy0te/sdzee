package com.sdzee.forums.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.eclipse.persistence.annotations.JoinFetch;

/**
 * Notification est l'entité JPA décrivant la table des notifications. La validation des champs s'appuie sur la JSR-303.
 * 
 * @author Médéric Munier
 * @version %I%, %G%
 */
@Entity
@Table( name = "forum_topic_notification", uniqueConstraints = { @UniqueConstraint( columnNames = {
        "member",
        "topic" } ) } )
@IdClass( NotificationId.class )
public class Notification implements Serializable {

    @NotNull( message = "{forums.notification.memberId.notnull}" )
    @Column( name = "member" )
    @Id
    private Long memberId;

    @NotNull( message = "{forums.notification.topicId.notnull}" )
    @Column( name = "topic" )
    @Id
    private Long topicId;

    @ManyToOne( fetch = FetchType.EAGER )
    @JoinColumn( name = "post" )
    @JoinFetch
    private Post post;

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

    public Post getPost() {
        return post;
    }

    public void setPost( Post post ) {
        this.post = post;
    }

    @Override
    public String toString() {
        return String.format( "Notification[%d,%d]", memberId, topicId );
    }

    @Override
    public boolean equals( Object obj ) {
        if ( this == obj )
            return true;
        if ( obj == null )
            return false;
        if ( getClass() != obj.getClass() )
            return false;
        Notification other = (Notification) obj;
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
 * NotificationId est la classe de définition de la clé primaire composite de la table des notifications.
 * 
 * @author Médéric Munier
 * @version %I%, %G%
 */
class NotificationId implements Serializable {
    Long memberId;
    Long topicId;

    public NotificationId() {
    }

    @Override
    public boolean equals( Object obj ) {
        if ( this == obj )
            return true;
        if ( obj == null )
            return false;
        if ( getClass() != obj.getClass() )
            return false;
        NotificationId other = (NotificationId) obj;
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
