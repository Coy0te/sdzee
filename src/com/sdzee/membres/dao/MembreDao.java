package com.sdzee.membres.dao;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.sdzee.dao.DAOException;
import com.sdzee.membres.entities.Membre;

@Stateless
public class MembreDao {
    private static final String JPQL_SELECT_PAR_EMAIL  = "SELECT m FROM Membre m WHERE m.email=:email";
    private static final String JPQL_SELECT_PAR_PSEUDO = "SELECT m FROM Membre m WHERE m.pseudo=:pseudo";
    private static final String PARAM_EMAIL            = "email";
    private static final String PARAM_PSEUDO           = "pseudo";

    // Injection du manager, qui s'occupe de la connexion avec la BDD
    @PersistenceContext( unitName = "bdd_sdzee_PU" )
    private EntityManager       em;

    // Enregistrement d'un nouvel utilisateur
    public void creer( Membre membre ) throws DAOException {
        try {
            em.persist( membre );
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    // Recherche d'un utilisateur à partir de son adresse email
    /**
     * Méthode de recherche dans la base. Le @param clause attend uniquement
     * "email" ou "pseudo", et recherche par défaut une adresse email si aucune
     * de ces deux valeurs n'est passée.
     */
    public Membre trouver( String clause, String valeur ) throws DAOException {
        Membre membre = null;
        Query requete = null;
        if ( PARAM_PSEUDO.equals( clause ) ) {
            requete = em.createQuery( JPQL_SELECT_PAR_PSEUDO );
            requete.setParameter( PARAM_PSEUDO, valeur );
        } else {
            requete = em.createQuery( JPQL_SELECT_PAR_EMAIL );
            requete.setParameter( PARAM_EMAIL, valeur );
        }
        try {
            membre = (Membre) requete.getSingleResult();
        } catch ( NoResultException e ) {
            return null;
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
        return membre;
    }
}
