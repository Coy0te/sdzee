package com.sdzee.privatemessages.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.eclipse.persistence.annotations.BatchFetch;
import org.eclipse.persistence.annotations.BatchFetchType;
import org.eclipse.persistence.annotations.JoinFetch;

import com.sdzee.membres.entities.Member;

/**
 * PrivateTopic est l'entité JPA décrivant la table des MP. La validation des champs s'appuie sur la JSR-303.
 * 
 * @author Médéric Munier
 * @version %I%, %G%
 */
@Entity
@Table( name = "private_topic" )
public class PrivateTopic implements Serializable {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long         id;

    @NotNull( message = "{private.topic.title.notnull}" )
    @Size( max = 60, message = "{private.topic.title.maxsize}" )
    private String       title;

    @Size( max = 80, message = "{private.topic.subTitle.maxsize}" )
    private String       subTitle;

    @NotNull( message = "{private.topic.author.notnull}" )
    @ManyToOne( fetch = FetchType.EAGER )
    @JoinColumn( name = "author" )
    @JoinFetch
    private Member       author;

    @OneToOne( fetch = FetchType.EAGER )
    @JoinColumn( name = "firstPrivatePost" )
    @JoinFetch
    private PrivatePost  firstPrivatePost;

    @OneToOne( fetch = FetchType.EAGER )
    @JoinColumn( name = "lastPrivatePost" )
    @JoinFetch
    private PrivatePost  lastPrivatePost;

    @ManyToMany
    @JoinTable(
            name = "private_join_topic_participant",
            joinColumns = @JoinColumn( name = "privateTopic" ),
            inverseJoinColumns = @JoinColumn( name = "participant" ) )
    @BatchFetch( BatchFetchType.IN )
    private List<Member> participants = new ArrayList<Member>();

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

    public Member getAuthor() {
        return author;
    }

    public void setAuthor( Member author ) {
        this.author = author;
    }

    public PrivatePost getFirstPrivatePost() {
        return firstPrivatePost;
    }

    public void setFirstPrivatePost( PrivatePost firstPrivatePost ) {
        this.firstPrivatePost = firstPrivatePost;
    }

    public PrivatePost getLastPrivatePost() {
        return lastPrivatePost;
    }

    public void setLastPrivatePost( PrivatePost lastPrivatePost ) {
        this.lastPrivatePost = lastPrivatePost;
    }

    public List<Member> getParticipants() {
        return participants;
    }

    public void setParticipants( List<Member> participants ) {
        this.participants = participants;
    }

    public void addParticipant( Member participant ) {
        this.participants.add( participant );
    }

    public void removeParticipant( Member participant ) {
        this.participants.remove( participant );
    }

    @Override
    public String toString() {
        return String.format( "PrivateTopic[%d]", id );
    }

    @Override
    public boolean equals( Object obj ) {
        if ( this == obj )
            return true;
        if ( obj == null )
            return false;
        if ( getClass() != obj.getClass() )
            return false;
        PrivateTopic other = (PrivateTopic) obj;
        if ( id == null ) {
            if ( other.id != null )
                return false;
        } else if ( !id.equals( other.id ) )
            return false;
        return true;
    }
}
