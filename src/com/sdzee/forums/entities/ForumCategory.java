package com.sdzee.forums.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.eclipse.persistence.annotations.JoinFetch;

/**
 * ForumCategory est l'entité JPA décrivant la table des catégories de forums. La validation des champs s'appuie sur la JSR-303.
 * 
 * @author Médéric Munier
 * @version %I%, %G%
 */
@Entity
@Table( name = "forum_category" )
public class ForumCategory implements Serializable {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long        id;

    @NotNull( message = "{forums.category.title.notnull}" )
    private String      title;

    @NotNull( message = "{forums.category.position.notnull}" )
    private Integer     position;

    @OneToMany( fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "category" )
    @OrderBy( "position ASC" )
    @JoinFetch
    private List<Forum> forums;  // côté "owner" du mapping d'une catégorie qui contient des forums

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

    public Integer getPosition() {
        return position;
    }

    public void setPosition( Integer position ) {
        this.position = position;
    }

    public List<Forum> getForums() {
        return forums;
    }

    public void setForums( List<Forum> forums ) {
        this.forums = forums;
    }

    public void addForum( Forum forum ) {
        this.forums.add( forum );
    }

    public void removeForum( Forum forum ) {
        this.forums.remove( forum );
    }

    @Override
    public String toString() {
        return String.format( "ForumCategory[%d]", id );
    }

    @Override
    public boolean equals( Object obj ) {
        if ( this == obj )
            return true;
        if ( obj == null )
            return false;
        if ( getClass() != obj.getClass() )
            return false;
        ForumCategory other = (ForumCategory) obj;
        if ( id == null ) {
            if ( other.id != null )
                return false;
        } else if ( !id.equals( other.id ) )
            return false;
        return true;
    }

}
