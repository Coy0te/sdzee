package com.sdzee.forums.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.eclipse.persistence.annotations.JoinFetch;

/**
 * Topic est l'entité JPA décrivant la table des sujets de forums. La validation des champs s'appuie sur la JSR-303.
 * 
 * @author Médéric Munier
 * @version %I%, %G%
 */
@Entity
@Table( name = "forum_topic" )
public class Topic implements Serializable {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long    id;

    @NotNull( message = "{forums.topic.title.notnull}" )
    @Size( max = 60, message = "{forums.topic.title.maxsize}" )
    private String  title;

    @Size( max = 80, message = "{forums.topic.subTitle.maxsize}" )
    private String  subTitle;

    @NotNull( message = "{forums.topic.forum.notnull}" )
    @ManyToOne( fetch = FetchType.EAGER )
    @JoinColumn( name = "forum" )
    @JoinFetch
    private Forum   forum;

    @ManyToOne( fetch = FetchType.EAGER )
    @JoinColumn( name = "firstPost" )
    @JoinFetch
    private Post    firstPost;

    @ManyToOne( fetch = FetchType.EAGER )
    @JoinColumn( name = "lastPost" )
    @JoinFetch
    private Post    lastPost;

    @Column( nullable = false, columnDefinition = "TINYINT(1)" )
    private boolean locked = false;

    @Column( nullable = false, columnDefinition = "TINYINT(1)" )
    private boolean sticky = false;

    @Column( nullable = false, columnDefinition = "TINYINT(1)" )
    private boolean solved = false;

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

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle( String subTitle ) {
        this.subTitle = subTitle;
    }

    public Forum getForum() {
        return forum;
    }

    public void setForum( Forum forum ) {
        this.forum = forum;
    }

    public Post getFirstPost() {
        return firstPost;
    }

    public void setFirstPost( Post firstPost ) {
        this.firstPost = firstPost;
    }

    public Post getLastPost() {
        return lastPost;
    }

    public void setLastPost( Post lastPost ) {
        this.lastPost = lastPost;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked( boolean locked ) {
        this.locked = locked;
    }

    public boolean isSticky() {
        return sticky;
    }

    public void setSticky( boolean sticky ) {
        this.sticky = sticky;
    }

    public boolean isSolved() {
        return solved;
    }

    public void setSolved( boolean solved ) {
        this.solved = solved;
    }

    @Override
    public String toString() {
        return String.format( "Topic[%d]", id );
    }

    @Override
    public boolean equals( Object obj ) {
        if ( this == obj )
            return true;
        if ( obj == null )
            return false;
        if ( getClass() != obj.getClass() )
            return false;
        Topic other = (Topic) obj;
        if ( id == null ) {
            if ( other.id != null )
                return false;
        } else if ( !id.equals( other.id ) )
            return false;
        return true;
    }
}
