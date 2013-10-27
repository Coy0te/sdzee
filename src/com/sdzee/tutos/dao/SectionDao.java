package com.sdzee.tutos.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import com.sdzee.dao.DAOException;
import com.sdzee.tutos.entities.Chapitre;
import com.sdzee.tutos.entities.SousPartie;

@Stateless
public class SectionDao {
    private static final String JPQL_LISTE_SECTIONS_PAR_CHAPITRES = "SELECT s FROM Section s WHERE s.chapitre=:chapitre ORDER BY s.id";
    private static final String PARAM_CHAPITRE                    = "chapitre";

    @PersistenceContext( unitName = "bdd_sdzee_PU" )
    private EntityManager       em;

    /* Enregistrement d'une nouvelle section */
    public void creer( SousPartie section ) throws DAOException {
        try {
            em.persist( section );
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /* Récupération d'une section via son id */
    public SousPartie trouver( long id ) throws DAOException {
        try {
            return em.find( SousPartie.class, id );
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /* Récupération de la liste des sections pour un chapitre donné */
    public List<SousPartie> lister( Chapitre chapitre ) throws DAOException {
        try {
            TypedQuery<SousPartie> query = em.createQuery( JPQL_LISTE_SECTIONS_PAR_CHAPITRES, SousPartie.class );
            query.setParameter( PARAM_CHAPITRE, chapitre );
            return query.getResultList();
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /* Suppression d'une section */
    public void supprimer( SousPartie section ) throws DAOException {
        try {
            em.remove( em.merge( section ) );
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }
}
