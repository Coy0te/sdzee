package com.sdzee.membres.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.jasypt.util.password.ConfigurablePasswordEncryptor;

/**
 * Member est l'entité JPA décrivant la table des membres du site. La validation des champs s'appuie sur la JSR-303.
 * 
 * @author Médéric Munier
 * @version %I%, %G%
 */
@Entity
@Table( name = "member" )
public class Member implements Serializable {
    private static final String ENCRYPTION_ALGORYTHM = "SHA-256";
    private static final String REGEX_EMAIL          = "([^.@]+)(\\.[^.@]+)*@([^.@]+\\.)+([^.@]+)";

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long                id;

    @Column( unique = true )
    @NotNull( message = "{member.email.notnull}" )
    @Pattern( regexp = REGEX_EMAIL, message = "{member.email.pattern}" )
    private String              email;

    @NotNull( message = "{member.password.notnull}" )
    @Size( min = 3, message = "{member.password.minsize}" )
    private String              password;

    @Column( unique = true )
    @NotNull( message = "{member.nickName.notnull}" )
    @Size( min = 3, message = "{member.nickName.minsize}" )
    private String              nickName;

    @Size( min = 2, message = "{member.firstName.minsize}" )
    private String              firstName;

    @Size( min = 2, message = "{member.lastName.minsize}" )
    private String              lastName;

    @Temporal( TemporalType.TIMESTAMP )
    private Date                registrationDate;

    @Temporal( TemporalType.TIMESTAMP )
    private Date                lastConnectionDate;

    @Column( columnDefinition = "INT(2) default 1" )
    private Integer             rights               = 1;

    private String              avatar;

    private String              signature;

    private String              biography;

    private String              website;

    @Column( nullable = false, columnDefinition = "TINYINT(1)" )
    private boolean             showEmail            = false;

    @Column( nullable = false, columnDefinition = "TINYINT(1)" )
    private boolean             showSignatures       = false;

    public Long getId() {
        return id;
    }

    public void setId( Long id ) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail( String email ) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    /**
     * Cette méthode s'appuie sur la bibliothèque Jasypt pour chiffrer le mot de passe efficacement.
     * 
     * L'algorithme SHA-256 est ici utilisé, avec par défaut un salage aléatoire et un grand nombre d'itérations de la fonction de hashage.
     * 
     * La chaîne générée est de longueur 56 et contient le hash en Base64.
     */
    public void setPassword( String password ) {
        ConfigurablePasswordEncryptor passwordEncryptor = new ConfigurablePasswordEncryptor();
        passwordEncryptor.setAlgorithm( ENCRYPTION_ALGORYTHM );
        passwordEncryptor.setPlainDigest( false );
        String encryptedPassword = passwordEncryptor.encryptPassword( password );

        this.password = encryptedPassword;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName( String nickName ) {
        this.nickName = nickName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName( String firstName ) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName( String lastName ) {
        this.lastName = lastName;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate( Date registrationDate ) {
        this.registrationDate = registrationDate;
    }

    public Date getLastConnectionDate() {
        return lastConnectionDate;
    }

    public void setLastConnectionDate( Date lastConnectionDate ) {
        this.lastConnectionDate = lastConnectionDate;
    }

    public Integer getRights() {
        return rights;
    }

    public void setRights( Integer rights ) {
        this.rights = rights;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar( String avatar ) {
        this.avatar = avatar;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature( String signature ) {
        this.signature = signature;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography( String biography ) {
        this.biography = biography;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite( String website ) {
        this.website = website;
    }

    public boolean isShowEmail() {
        return showEmail;
    }

    public void setShowEmail( boolean showEmail ) {
        this.showEmail = showEmail;
    }

    public boolean isShowSignatures() {
        return showSignatures;
    }

    public void setShowSignatures( boolean showSignatures ) {
        this.showSignatures = showSignatures;
    }

    @Override
    public String toString() {
        return String.format( "Member[%d]", id );
    }

    @Override
    public boolean equals( Object obj ) {
        if ( this == obj )
            return true;
        if ( obj == null )
            return false;
        if ( getClass() != obj.getClass() )
            return false;
        Member other = (Member) obj;
        if ( id == null ) {
            if ( other.id != null )
                return false;
        } else if ( !id.equals( other.id ) )
            return false;
        return true;
    }
}
