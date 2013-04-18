package com.sdzee.forums.dao;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.sdzee.dao.DAOException;
import com.sdzee.forums.entities.Reponse;
import com.sdzee.forums.entities.Vote;
import com.sdzee.membres.entities.Membre;

@Stateless
public class VoteDao {
    private static final String JPQL_VOTE_PAR_MEMBRE_PAR_MESSAGE = "SELECT v FROM Vote v WHERE v.membre=:membre AND v.message=:reponse";
    private static final String PARAM_MEMBRE                     = "membre";
    private static final String PARAM_REPONSE                    = "reponse";

    @PersistenceContext( unitName = "bdd_sdzee_PU" )
    private EntityManager       em;

    /* Enregistrement d'un nouveau vote */
    public void creer( Vote vote ) throws DAOException {
        try {
            em.persist( vote );
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /* Récupération du vote pour un membre et un message donné */
    public Vote trouver( Membre membre, Reponse reponse ) throws DAOException {
        try {
            Query query = em.createQuery( JPQL_VOTE_PAR_MEMBRE_PAR_MESSAGE );
            query.setParameter( PARAM_MEMBRE, membre );
            query.setParameter( PARAM_REPONSE, reponse );
            return (Vote) query.getSingleResult();
        } catch ( NoResultException e ) {
            // Si aucune vote trouvé, on retourne null.
            return null;
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /* Suppression d'un vote */
    public void supprimer( Vote vote ) throws DAOException {
        try {
            em.remove( em.merge( vote ) );
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }
}
