package com.sdzee.forums.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.sdzee.dao.DAOException;
import com.sdzee.forums.entities.Reponse;
import com.sdzee.forums.entities.Sujet;

@Stateless
public class ReponseDao {
    private static final String JPQL_COMPTE_REPONSES_PAR_SUJET     = "SELECT count(r) FROM Reponse r WHERE r.sujet=:sujet";
    private static final String JPQL_LISTE_REPONSES_PAR_SUJET      = "SELECT r FROM Reponse r WHERE r.sujet=:sujet ORDER BY r.id ASC";
    private static final String JPQL_LISTE_REPONSES_PAR_SUJET_DESC = "SELECT r FROM Reponse r WHERE r.sujet=:sujet ORDER BY r.id DESC";
    private static final String PARAM_SUJET                        = "sujet";

    @PersistenceContext( unitName = "bdd_sdzee_PU" )
    private EntityManager       em;

    /* Enregistrement d'une nouvelle réponse */
    public void creer( Reponse reponse ) throws DAOException {
        try {
            em.persist( reponse );
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /* Récupération d'une réponse via son id */
    public Reponse trouver( long id ) throws DAOException {
        try {
            return em.find( Reponse.class, id );
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /* Récupération du nombre de réponses pour un sujet donné */
    public Integer decompte( Sujet sujet ) throws DAOException {
        try {
            Query query = em.createQuery( JPQL_COMPTE_REPONSES_PAR_SUJET );
            query.setParameter( PARAM_SUJET, sujet );
            return ( (Long) query.getSingleResult() ).intValue();
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /* Récupération de la dernière réponse pour un sujet donné */
    public Reponse trouverDerniere( Sujet sujet ) throws DAOException {
        try {
            Query query = em.createQuery( JPQL_LISTE_REPONSES_PAR_SUJET_DESC );
            query.setParameter( PARAM_SUJET, sujet );
            return (Reponse) query.setMaxResults( 1 ).getSingleResult();
        } catch ( NoResultException e ) {
            // Si aucune réponse dans le sujet, on retourne null.
            return null;
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /* Récupération de la liste des réponses pour un sujet donné */
    public List<Reponse> lister( Sujet sujet ) throws DAOException {
        try {
            TypedQuery<Reponse> query = em.createQuery( JPQL_LISTE_REPONSES_PAR_SUJET, Reponse.class );
            query.setParameter( PARAM_SUJET, sujet );
            return query.getResultList();
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /* Mise à jour d'une réponse */
    public void update( Reponse reponse ) throws DAOException {
        try {
            em.merge( reponse );
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /* Suppression d'une réponse */
    public void supprimer( Reponse reponse ) throws DAOException {
        try {
            em.remove( em.merge( reponse ) );
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }
}
