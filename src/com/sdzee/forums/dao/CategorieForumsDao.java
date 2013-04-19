package com.sdzee.forums.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import com.sdzee.dao.DAOException;
import com.sdzee.forums.entities.CategorieForum;

@Stateless
public class CategorieForumsDao {
    private static final String JPQL_LISTE_CATEGORIES = "SELECT c FROM CategorieForum c ORDER BY c.id";

    @PersistenceContext( unitName = "bdd_sdzee_PU" )
    private EntityManager       em;

    /* Enregistrement d'une nouvelle catégorie */
    public void creer( CategorieForum categorie ) throws DAOException {
        try {
            em.persist( categorie );
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /* Récupération d'une catégorie via son id */
    public CategorieForum trouver( long id ) throws DAOException {
        try {
            return em.find( CategorieForum.class, id );
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /* Récupération de la liste des catégories */
    public List<CategorieForum> lister() throws DAOException {
        try {
            TypedQuery<CategorieForum> query = em.createQuery( JPQL_LISTE_CATEGORIES, CategorieForum.class );
            return query.getResultList();
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /* Mise à jour d'une catégorie */
    public void update( CategorieForum categorie ) throws DAOException {
        try {
            em.merge( categorie );
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /* Suppression d'une catégorie */
    public void supprimer( CategorieForum categorie ) throws DAOException {
        try {
            em.remove( em.merge( categorie ) );
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }
}
