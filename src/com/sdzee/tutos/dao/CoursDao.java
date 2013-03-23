package com.sdzee.tutos.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import com.sdzee.dao.DAOException;
import com.sdzee.tutos.entities.Categorie;
import com.sdzee.tutos.entities.Cours;

@Stateless
public class CoursDao {
    private static final String JPQL_LISTE_COURS               = "SELECT c FROM Cours c ORDER BY c.id";
    private static final String JPQL_LISTE_COURS_PAR_CATEGORIE = "SELECT c FROM Cours c WHERE c.categorie=:categorie ORDER BY c.id";
    private static final String PARAM_CATEGORIE                 = "categorie";

    @PersistenceContext( unitName = "bdd_sdzee_PU" )
    private EntityManager       em;

    /* Enregistrement d'un nouveau cours */
    public void creer( Cours cours ) throws DAOException {
        try {
            em.persist( cours );
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /* Récupération d'un cours via son id */
    public Cours trouver( long id ) throws DAOException {
        try {
            return em.find( Cours.class, id );
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /* Récupération de la liste des cours */
    public List<Cours> lister() throws DAOException {
        try {
            TypedQuery<Cours> query = em.createQuery( JPQL_LISTE_COURS, Cours.class );
            return query.getResultList();
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /* Récupération de la liste des cours pour une catégorie donnée */
    public List<Cours> lister( Categorie categorie ) throws DAOException {
        try {
            TypedQuery<Cours> query = em.createQuery( JPQL_LISTE_COURS_PAR_CATEGORIE, Cours.class );
            query.setParameter( PARAM_CATEGORIE, categorie );
            return query.getResultList();
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /* Suppression d'un cours */
    public void supprimer( Cours cours ) throws DAOException {
        try {
            em.remove( em.merge( cours ) );
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }
}
