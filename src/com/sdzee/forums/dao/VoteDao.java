package com.sdzee.forums.dao;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.sdzee.dao.DAOException;
import com.sdzee.forums.entities.Vote;
import com.sdzee.membres.entities.Member;

/**
 * VoteDao est la classe DAO contenant les opérations CRUD réalisables sur la table des votes. Il s'agit d'un EJB Stateless dont la
 * structure s'appuie sur JPA et JPQL.
 * 
 * @author Médéric Munier
 * @version %I%, %G%
 */
@Stateless
public class VoteDao {
    private static final String JPQL_SELECT_UNIQUE_VOTE = "SELECT v FROM Vote v WHERE v.memberId=:memberId AND v.objectId=:objectId AND v.objectType=:objectType";
    private static final String PARAM_MEMBER_ID         = "memberId";
    private static final String PARAM_OBJECT_ID         = "objectId";
    private static final String PARAM_OBJECT_TYPE       = "objectType";

    @PersistenceContext( unitName = "bdd_sdzee_PU" )
    private EntityManager       em;

    /**
     * Cette méthode permet d'enregistrer un nouveau {@link Vote} en base.
     * 
     * @param vote le nouveau vote à insérer en base.
     * @throws {@link DAOException} lorsqu'une erreur survient lors de l'opération en base.
     */
    public void create( Vote vote ) throws DAOException {
        try {
            em.persist( vote );
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /**
     * Cette méthode permet de chercher un {@link Vote} via l'id du {@link Member} qui en est à l'origine, l'id de l'objet concerné et son
     * type.
     * 
     * @param memberId l'id du membre à l'origine du vote.
     * @param objectId l'id de l'objet concerné par le vote.
     * @param objectType le type de l'objet concerné par le vote.
     * @return le vote correspondant, ou <code>null</code> si aucun vote n'est trouvé.
     * @throws {@link DAOException} lorsqu'une erreur survient lors de l'opération en base.
     */
    public Vote find( Long memberId, Long objectId, String objectType ) throws DAOException {
        try {
            Query query = em.createQuery( JPQL_SELECT_UNIQUE_VOTE );
            query.setParameter( PARAM_MEMBER_ID, memberId );
            query.setParameter( PARAM_OBJECT_ID, objectId );
            query.setParameter( PARAM_OBJECT_TYPE, objectType );
            return (Vote) query.getSingleResult();
        } catch ( NoResultException e ) {
            return null;
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /**
     * Cette méthode permet de mettre à jour un {@link Vote} en base.
     * 
     * @param vote le vote à mettre à jour en base.
     * @throws {@link DAOException} lorsqu'une erreur survient lors de l'opération en base.
     */
    public void update( Vote vote ) throws DAOException {
        try {
            em.merge( vote );
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /**
     * Cette méthode permet de supprimer un {@link Vote} de la base.
     * 
     * @param vote le vote à supprimer de la base.
     * @throws {@link DAOException} lorsqu'une erreur survient lors de l'opération en base.
     */
    public void delete( Vote vote ) throws DAOException {
        try {
            em.remove( em.merge( vote ) );
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }
}
