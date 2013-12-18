package com.sdzee.privatemessages.entities;

import java.io.Serializable;
import java.util.Date;

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

import org.eclipse.persistence.annotations.JoinFetch;

import com.sdzee.membres.entities.Member;

/**
 * PrivatePost est l'entité JPA décrivant la table des réponses aux MPs. La validation des champs s'appuie sur la JSR-303.
 * 
 * @author Médéric Munier
 * @version %I%, %G%
 */
@Entity
@Table( name = "private_post" )
public class PrivatePost implements Serializable {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long         id;

    @NotNull( message = "{private.post.author.notnull}" )
    @ManyToOne( fetch = FetchType.EAGER )
    @JoinColumn( name = "author" )
    @JoinFetch
    private Member       author;

    @NotNull( message = "{private.post.topic.notnull}" )
    @ManyToOne( fetch = FetchType.EAGER )
    @JoinColumn( name = "privateTopic" )
    @JoinFetch
    private PrivateTopic privateTopic;

    @NotNull( message = "{private.post.text.notnull}" )
    private String       text;

    @NotNull( message = "{private.post.creationDate.notnull}" )
    @Temporal( TemporalType.TIMESTAMP )
    private Date         creationDate;

    @Temporal( TemporalType.TIMESTAMP )
    private Date         lastEditDate;

    @ManyToOne( fetch = FetchType.EAGER )
    @JoinColumn( name = "lastEditBy" )
    @JoinFetch
    private Member       lastEditBy;

    @NotNull( message = "{private.post.ipAddress.notnull}" )
    private String       ipAddress;

    public Long getId() {
        return id;
    }

    public void setId( Long id ) {
        this.id = id;
    }

    public Member getAuthor() {
        return author;
    }

    public void setAuthor( Member author ) {
        this.author = author;
    }

    public PrivateTopic getPrivateTopic() {
        return privateTopic;
    }

    public void setPrivateTopic( PrivateTopic privateTopic ) {
        this.privateTopic = privateTopic;
    }

    public String getText() {
        return text;
    }

    public void setText( String text ) {
        this.text = text;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate( Date creationDate ) {
        this.creationDate = creationDate;
    }

    public Date getLastEditDate() {
        return lastEditDate;
    }

    public void setLastEditDate( Date lastEditDate ) {
        this.lastEditDate = lastEditDate;
    }

    public Member getLastEditBy() {
        return lastEditBy;
    }

    public void setLastEditBy( Member lastEditBy ) {
        this.lastEditBy = lastEditBy;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress( String ipAddress ) {
        this.ipAddress = ipAddress;
    }

    @Override
    public String toString() {
        return String.format( "PrivatePost[%d]", id );
    }

    @Override
    public boolean equals( Object obj ) {
        if ( this == obj )
            return true;
        if ( obj == null )
            return false;
        if ( getClass() != obj.getClass() )
            return false;
        PrivatePost other = (PrivatePost) obj;
        if ( id == null ) {
            if ( other.id != null )
                return false;
        } else if ( !id.equals( other.id ) )
            return false;
        return true;
    }
}
