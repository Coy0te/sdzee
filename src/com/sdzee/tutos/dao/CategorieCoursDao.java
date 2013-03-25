package com.sdzee.tutos.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import com.sdzee.dao.DAOException;
import com.sdzee.tutos.entities.CategorieCours;

@Stateless
public class CategorieCoursDao {
    private static final String JPQL_LISTE_CATEGORIES = "SELECT c FROM CategorieCours c ORDER BY c.id";

    @PersistenceContext( unitName = "bdd_sdzee_PU" )
    private EntityManager       em;

    /* Enregistrement d'une nouvelle catégorie */
    public void creer( CategorieCours categorie ) throws DAOException {
        try {
            em.persist( categorie );
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /* Récupération d'une catégorie via son id */
    public CategorieCours trouver( long id ) throws DAOException {
        try {
            return em.find( CategorieCours.class, id );
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /* Récupération de la liste des catégories */
    public List<CategorieCours> lister() throws DAOException {
        try {
            TypedQuery<CategorieCours> query = em.createQuery( JPQL_LISTE_CATEGORIES, CategorieCours.class );
            return query.getResultList();
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /* Suppression d'une catégorie */
    public void supprimer( CategorieCours categorie ) throws DAOException {
        try {
            em.remove( em.merge( categorie ) );
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }
}
