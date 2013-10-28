package com.sdzee.forums.dao;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.sdzee.dao.DAOException;
import com.sdzee.forums.entities.Vote;

@Stateless
public class VoteDao {
    private static final String JPQL_SELECT_UNIQUE_VOTE = "SELECT v FROM Vote v WHERE v.idMembre=:idMembre AND v.idObjet=:idObjet AND v.typeObjet=:typeObjet";
    private static final String PARAM_ID_MEMBRE         = "idMembre";
    private static final String PARAM_ID_OBJET          = "idObjet";
    private static final String PARAM_TYPE_OBJET        = "typeObjet";

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

    /* Récupération du vote pour un membre, un objet et un type d'objet donnés */
    public Vote trouver( Long idMembre, Long idObjet, String typeObjet ) throws DAOException {
        try {
            Query query = em.createQuery( JPQL_SELECT_UNIQUE_VOTE );
            query.setParameter( PARAM_ID_MEMBRE, idMembre );
            query.setParameter( PARAM_ID_OBJET, idObjet );
            query.setParameter( PARAM_TYPE_OBJET, typeObjet );
            return (Vote) query.getSingleResult();
        } catch ( NoResultException e ) {
            // Si aucun vote trouvé, on retourne null.
            return null;
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /* Mise à jour d'un vote */
    public void update( Vote vote ) throws DAOException {
        try {
            em.merge( vote );
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
