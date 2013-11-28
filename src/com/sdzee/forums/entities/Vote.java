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
 * Vote est l'entité JPA décrivant la table des votes. La validation des champs s'appuie sur la JSR-303.
 * 
 * @author Médéric Munier
 * @version %I%, %G%
 */
@Entity
@Table( name = "forum_vote",
        uniqueConstraints = { @UniqueConstraint( columnNames = { "objectType", "member", "object" } ) } )
@IdClass( VoteId.class )
public class Vote implements Serializable {
    @NotNull( message = "{forums.vote.memberId.notnull}" )
    @Column( name = "member" )
    @Id
    private Long   memberId;

    @NotNull( message = "{forums.vote.objectId.notnull}" )
    @Column( name = "object" )
    @Id
    private Long   objectId;

    @NotNull( message = "{forums.vote.objectType.notnull}" )
    @Column( name = "objectType" )
    @Id
    private String objectType;

    private int    value;

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId( Long memberId ) {
        this.memberId = memberId;
    }

    public Long getObjectId() {
        return objectId;
    }

    public void setObjectId( Long objectId ) {
        this.objectId = objectId;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType( String objectType ) {
        this.objectType = objectType;
    }

    public int getValue() {
        return value;
    }

    public void setValue( int value ) {
        this.value = value;
    }
}

/**
 * VoteId est la classe de définition de la clé primaire composite de la table des votes.
 * 
 * @author Médéric Munier
 * @version %I%, %G%
 */
class VoteId implements Serializable {
    Long   memberId;
    Long   objectId;
    String objectType;
}