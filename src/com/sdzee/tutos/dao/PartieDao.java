package com.sdzee.tutos.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import com.sdzee.dao.DAOException;
import com.sdzee.tutos.entities.Cours;
import com.sdzee.tutos.entities.Partie;

@Stateless
public class PartieDao {
    private static final String JPQL_LISTE_PARTIES_PAR_COURS = "SELECT p FROM Partie p WHERE p.forum=:forum ORDER BY p.id";
    private static final String PARAM_COURS                  = "cours";

    @PersistenceContext( unitName = "bdd_sdzee_PU" )
    private EntityManager       em;

    /* Enregistrement d'une nouvelle partie */
    public void creer( Partie partie ) throws DAOException {
        try {
            em.persist( partie );
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /* Récupération d'une partie via son id */
    public Partie trouver( long id ) throws DAOException {
        try {
            return em.find( Partie.class, id );
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /* Récupération de la liste des parties pour un cours donné */
    public List<Partie> lister( Cours cours ) throws DAOException {
        try {
            TypedQuery<Partie> query = em.createQuery( JPQL_LISTE_PARTIES_PAR_COURS, Partie.class );
            query.setParameter( PARAM_COURS, cours );
            return query.getResultList();
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /* Suppression d'une partie */
    public void supprimer( Partie partie ) throws DAOException {
        try {
            em.remove( em.merge( partie ) );
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }
}
