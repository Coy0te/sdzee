package com.sdzee.forums.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import com.sdzee.dao.DAOException;
import com.sdzee.forums.entities.Categorie;
import com.sdzee.forums.entities.Forum;

@Stateless
public class ForumDao {
    private static final String JPQL_LISTE_FORUMS               = "SELECT f FROM Forum f ORDER BY f.id";
    private static final String JPQL_LISTE_FORUMS_PAR_CATEGORIE = "SELECT f FROM Forum f WHERE f.categorie=:categorie ORDER BY f.id";
    private static final String PARAM_CATEGORIE                 = "categorie";

    @PersistenceContext( unitName = "bdd_sdzee_PU" )
    private EntityManager       em;

    /* Enregistrement d'un nouveau forum */
    public void creer( Forum forum ) throws DAOException {
        try {
            em.persist( forum );
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /* Récupération d'un forum via son id */
    public Forum trouver( long id ) throws DAOException {
        try {
            return em.find( Forum.class, id );
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /* Récupération de la liste des forums */
    public List<Forum> lister() throws DAOException {
        try {
            TypedQuery<Forum> query = em.createQuery( JPQL_LISTE_FORUMS, Forum.class );
            return query.getResultList();
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /* Récupération de la liste des forums pour une catégorie donnée */
    public List<Forum> lister( Categorie categorie ) throws DAOException {
        try {
            TypedQuery<Forum> query = em.createQuery( JPQL_LISTE_FORUMS_PAR_CATEGORIE, Forum.class );
            query.setParameter( PARAM_CATEGORIE, categorie );
            return query.getResultList();
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /* Suppression d'un forum */
    public void supprimer( Forum forum ) throws DAOException {
        try {
            em.remove( em.merge( forum ) );
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }
}
