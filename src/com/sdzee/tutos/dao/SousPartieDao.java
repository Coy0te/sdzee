package com.sdzee.tutos.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import com.sdzee.dao.DAOException;
import com.sdzee.tutos.entities.BTMTChap;
import com.sdzee.tutos.entities.SousPartie;

@Stateless
public class SousPartieDao {
    private static final String JPQL_LISTE_SOUSPARTIES_PAR_CHAPITRE = "SELECT s FROM SousPartie sp WHERE sp.chapitre=:chapitre ORDER BY sp.position";
    private static final String PARAM_CHAPITRE                      = "chapitre";

    @PersistenceContext( unitName = "bdd_sdzee_PU" )
    private EntityManager       em;

    /* Enregistrement d'une nouvelle sous-partie */
    public void creer( SousPartie sousPartie ) throws DAOException {
        try {
            em.persist( sousPartie );
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /* Récupération d'une sous-partie via son id */
    public SousPartie trouver( long id ) throws DAOException {
        try {
            return em.find( SousPartie.class, id );
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /* Récupération de la liste des sous-parties pour un chapitre donné triées selon leur position */
    public List<SousPartie> lister( BTMTChap chapitre ) throws DAOException {
        try {
            TypedQuery<SousPartie> query = em.createQuery( JPQL_LISTE_SOUSPARTIES_PAR_CHAPITRE, SousPartie.class );
            query.setParameter( PARAM_CHAPITRE, chapitre );
            return query.getResultList();
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /* Mise à jour d'une sous-partie */
    public void update( SousPartie sousPartie ) throws DAOException {
        try {
            em.merge( sousPartie );
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /* Suppression d'une sous-partie */
    public void supprimer( SousPartie sousPartie ) throws DAOException {
        try {
            em.remove( em.merge( sousPartie ) );
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }
}
