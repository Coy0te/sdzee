package com.sdzee.forums.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.sdzee.dao.DAOException;
import com.sdzee.forums.entities.Forum;
import com.sdzee.forums.entities.Sujet;

@Stateless
public class SujetDao {
    private static final String JPQL_LISTE_SUJETS                    = "SELECT s FROM Sujet s ORDER BY s.id";
    private static final String JPQL_LISTE_SUJETS_PAR_FORUM          = "SELECT s FROM Sujet s WHERE s.forum=:forum AND s.sticky=false ORDER BY s.id DESC";
    private static final String JPQL_LISTE_SUJETS_STICKIES_PAR_FORUM = "SELECT s FROM Sujet s WHERE s.forum=:forum AND s.sticky=true ORDER BY s.id DESC";
    private static final String PARAM_FORUM                          = "forum";

    @PersistenceContext( unitName = "bdd_sdzee_PU" )
    private EntityManager       em;

    /* Enregistrement d'un nouveau sujet */
    public void creer( Sujet sujet ) throws DAOException {
        try {
            em.persist( sujet );
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /* Récupération d'un sujet via son id */
    public Sujet trouver( long id ) throws DAOException {
        try {
            return em.find( Sujet.class, id );
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /* Récupération du dernier sujet pour un forum donné */
    public Sujet trouverDernier( Forum forum ) throws DAOException {
        try {
            Query query = em.createQuery( JPQL_LISTE_SUJETS_PAR_FORUM );
            query.setParameter( PARAM_FORUM, forum );
            return (Sujet) query.setMaxResults( 1 ).getSingleResult();
        } catch ( NoResultException e ) {
            // Si aucun sujet dans le forum, on retourne null.
            return null;
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /* Récupération de la liste des sujets */
    public List<Sujet> lister() throws DAOException {
        try {
            TypedQuery<Sujet> query = em.createQuery( JPQL_LISTE_SUJETS, Sujet.class );
            return query.getResultList();
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /* Récupération de la liste des sujets pour un forum donné */
    public List<Sujet> lister( Forum forum ) throws DAOException {
        try {
            TypedQuery<Sujet> query = em.createQuery( JPQL_LISTE_SUJETS_PAR_FORUM, Sujet.class );
            query.setParameter( PARAM_FORUM, forum );
            return query.getResultList();
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /* Récupération de la liste des sujets marqués "sticky" pour un forum donné */
    public List<Sujet> listerStickies( Forum forum ) throws DAOException {
        try {
            TypedQuery<Sujet> query = em.createQuery( JPQL_LISTE_SUJETS_STICKIES_PAR_FORUM, Sujet.class );
            query.setParameter( PARAM_FORUM, forum );
            return query.getResultList();
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /* Mise à jour d'un sujet */
    public void update( Sujet sujet ) throws DAOException {
        try {
            em.merge( sujet );
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /* Suppression d'un sujet */
    public void supprimer( Sujet sujet ) throws DAOException {
        try {
            em.remove( em.merge( sujet ) );
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }
}
