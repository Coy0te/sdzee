package com.sdzee.membres.entities;

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

/**
 * Sanction est l'entité JPA décrivant la table des sanctions sur les membres du site. La validation des champs s'appuie sur la JSR-303.
 * 
 * @author Médéric Munier
 * @version %I%, %G%
 */
@Entity
@Table( name = "sanctions" )
public class Sanction implements Serializable {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long   id;

    @NotNull( message = "{ban.member.notnull}" )
    @ManyToOne( fetch = FetchType.EAGER )
    @JoinColumn( name = "member" )
    @JoinFetch
    private Member member;

    @NotNull( message = "{ban.moderator.notnull}" )
    @ManyToOne( fetch = FetchType.EAGER )
    @JoinColumn( name = "moderator" )
    @JoinFetch
    private Member moderator;

    @NotNull( message = "{ban.type.notnull}" )
    private String type;

    @NotNull( message = "{ban.publicReason.notnull}" )
    private String publicReason;

    @NotNull( message = "{ban.privateReason.notnull}" )
    private String privateReason;

    @NotNull( message = "{ban.startDate.notnull}" )
    @Temporal( TemporalType.TIMESTAMP )
    private Date   startDate;

    @Temporal( TemporalType.TIMESTAMP )
    private Date   endDate;

    public Long getId() {
        return id;
    }

    public void setId( Long id ) {
        this.id = id;
    }

    public Member getMember() {
        return member;
    }

    public void setMember( Member member ) {
        this.member = member;
    }

    public Member getModerator() {
        return moderator;
    }

    public void setModerator( Member moderator ) {
        this.moderator = moderator;
    }

    public String getType() {
        return type;
    }

    public void setType( String type ) {
        this.type = type;
    }

    public String getPublicReason() {
        return publicReason;
    }

    public void setPublicReason( String publicReason ) {
        this.publicReason = publicReason;
    }

    public String getPrivateReason() {
        return privateReason;
    }

    public void setPrivateReason( String privateReason ) {
        this.privateReason = privateReason;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate( Date startDate ) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate( Date endDate ) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return String.format( "Sanction[%d]", id );
    }

    @Override
    public boolean equals( Object obj ) {
        if ( this == obj )
            return true;
        if ( obj == null )
            return false;
        if ( getClass() != obj.getClass() )
            return false;
        Sanction other = (Sanction) obj;
        if ( id == null ) {
            if ( other.id != null )
                return false;
        } else if ( !id.equals( other.id ) )
            return false;
        return true;
    }
}
