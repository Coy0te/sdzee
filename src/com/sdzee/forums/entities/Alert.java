package com.sdzee.forums.entities;

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
 * Alert est l'entité JPA décrivant la table des alertes sur le forum. La validation des champs s'appuie sur la JSR-303.
 * 
 * @author Médéric Munier
 * @version %I%, %G%
 */
@Entity
@Table( name = "forum_alert" )
public class Alert implements Serializable {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long   id;

    @NotNull( message = "{forums.alert.author.notnull}" )
    @ManyToOne( fetch = FetchType.EAGER )
    @JoinColumn( name = "author" )
    @JoinFetch
    private Member author;

    @NotNull( message = "{forums.alert.text.notnull}" )
    private String text;

    @NotNull( message = "{forums.alert.creationDate.notnull}" )
    @Temporal( TemporalType.TIMESTAMP )
    private Date   creationDate;

    @NotNull( message = "{forums.alert.post.notnull}" )
    @ManyToOne( fetch = FetchType.EAGER )
    @JoinColumn( name = "post" )
    @JoinFetch
    private Post   post;

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

    public Post getPost() {
        return post;
    }

    public void setPost( Post post ) {
        this.post = post;
    }

    @Override
    public String toString() {
        return String.format( "Alert[%d]", id );
    }

    @Override
    public boolean equals( Object obj ) {
        if ( this == obj )
            return true;
        if ( obj == null )
            return false;
        if ( getClass() != obj.getClass() )
            return false;
        Alert other = (Alert) obj;
        if ( id == null ) {
            if ( other.id != null )
                return false;
        } else if ( !id.equals( other.id ) )
            return false;
        return true;
    }
}