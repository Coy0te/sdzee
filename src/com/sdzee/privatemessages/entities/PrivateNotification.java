package com.sdzee.privatemessages.entities;

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
 * PrivateNotification est l'entité JPA décrivant la table des notifications. La validation des champs s'appuie sur la JSR-303.
 * 
 * @author Médéric Munier
 * @version %I%, %G%
 */
@Entity
@Table( name = "private_topic_notification", uniqueConstraints = { @UniqueConstraint( columnNames = {
        "member",
        "privateTopic" } ) } )
@IdClass( PrivateNotificationId.class )
public class PrivateNotification implements Serializable {

    @NotNull( message = "{forums.notification.memberId.notnull}" )
    @Column( name = "member" )
    @Id
    private Long        memberId;

    @NotNull( message = "{forums.notification.topicId.notnull}" )
    @Column( name = "privateTopic" )
    @Id
    private Long        privateTopicId;

    @ManyToOne( fetch = FetchType.EAGER )
    @JoinColumn( name = "privatePost" )
    @JoinFetch
    private PrivatePost privatePost;

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId( Long memberId ) {
        this.memberId = memberId;
    }

    public Long getPrivateTopicId() {
        return privateTopicId;
    }

    public void setPrivateTopicId( Long privateTopicId ) {
        this.privateTopicId = privateTopicId;
    }

    public PrivatePost getPrivatePost() {
        return privatePost;
    }

    public void setPrivatePost( PrivatePost privatePost ) {
        this.privatePost = privatePost;
    }

}

/**
 * PrivateNotificationId est la classe de définition de la clé primaire composite de la table des notifications des MPs.
 * 
 * @author Médéric Munier
 * @version %I%, %G%
 */
class PrivateNotificationId implements Serializable {
    Long memberId;
    Long privateTopicId;
}
