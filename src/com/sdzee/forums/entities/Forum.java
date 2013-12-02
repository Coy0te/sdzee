package com.sdzee.forums.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.eclipse.persistence.annotations.JoinFetch;

/**
 * Forum est l'entité JPA décrivant la table des forums. La validation des champs s'appuie sur la JSR-303.
 * 
 * @author Médéric Munier
 * @version %I%, %G%
 */
@Entity
@Table( name = "forum_forum" )
public class Forum implements Serializable {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long          id;

    @NotNull( message = "{forums.forum.title.notnull}" )
    private String        title;

    @NotNull( message = "{forums.forum.description.notnull}" )
    private String        description;

    @ManyToOne
    @JoinColumn( name = "category" )
    @NotNull( message = "{forums.forum.category.notnull}" )
    @JoinFetch
    private ForumCategory category;    // côté "customer" du mapping d'un forum qui appartient à une catégorie

    @OneToOne( fetch = FetchType.EAGER )
    @JoinColumn( name = "lastPost" )
    @JoinFetch
    private Post          lastPost;

    @NotNull( message = "{forums.forum.position.notnull}" )
    private Integer       position;

    private Integer       nbTopics = 0;

    public Long getId() {
        return id;
    }

    public void setId( Long id ) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle( String title ) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription( String description ) {
        this.description = description;
    }

    public ForumCategory getCategory() {
        return category;
    }

    public void setCategory( ForumCategory category ) {
        this.category = category;
    }

    public Post getLastPost() {
        return lastPost;
    }

    public void setLastPost( Post lastPost ) {
        this.lastPost = lastPost;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition( Integer position ) {
        this.position = position;
    }

    public Integer getNbTopics() {
        return nbTopics;
    }

    public void setNbTopics( Integer nbTopics ) {
        this.nbTopics = nbTopics;
    }

    public void addTopic() {
        this.nbTopics++;
    }

    public void removeTopic() {
        this.nbTopics--;
    }

    @Override
    public String toString() {
        return String.format( "Forum[%d]", id );
    }

    @Override
    public boolean equals( Object obj ) {
        if ( this == obj )
            return true;
        if ( obj == null )
            return false;
        if ( getClass() != obj.getClass() )
            return false;
        Forum other = (Forum) obj;
        if ( id == null ) {
            if ( other.id != null )
                return false;
        } else if ( !id.equals( other.id ) )
            return false;
        return true;
    }
}
