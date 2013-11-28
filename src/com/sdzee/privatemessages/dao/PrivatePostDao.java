package com.sdzee.privatemessages.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.validation.ConstraintViolationException;

import com.sdzee.dao.DAOException;
import com.sdzee.privatemessages.entities.PrivatePost;
import com.sdzee.privatemessages.entities.PrivateTopic;

/**
 * PrivatePostDao est la classe DAO contenant les opérations CRUD réalisables sur la table des réponses à un MP. Il s'agit d'un EJB Stateless dont la
 * structure s'appuie sur JPA et JPQL.
 * 
 * @author Médéric Munier
 * @version %I%, %G%
 */
@Stateless
public class PrivatePostDao {
    private static final String JPQL_COUNT_POSTS_PER_PRIVATE_TOPIC     = "SELECT count(p) FROM PrivatePost p WHERE p.privateTopic=:privateTopic";
    private static final String JPQL_POSTS_LIST_PER_PRIVATE_TOPIC      = "SELECT p FROM PrivatePost p WHERE p.privateTopic=:privateTopic ORDER BY p.id ASC";
    private static final String JPQL_POSTS_LIST_PER_PRIVATE_TOPIC_DESC = "SELECT p FROM PrivatePost p WHERE p.privateTopic=:privateTopic ORDER BY p.id DESC";
    private static final String PARAM_PRIVATE_TOPIC                    = "privateTopic";

    @PersistenceContext( unitName = "bdd_sdzee_PU" )
    private EntityManager       em;

    /**
     * Cette méthode permet d'enregistrer un nouveau {@link PrivatePost} en base.
     * 
     * @param privatePost la nouvelle réponse privée à insérer en base.
     * @throws {@link DAOException} lorsqu'une erreur survient lors de l'opération en base.
     */
    public void create( PrivatePost privatePost ) throws DAOException {
        try {
            em.persist( privatePost );
        } catch ( ConstraintViolationException e ) {
            // TODO: logger
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /**
     * Cette méthode permet de chercher un {@link PrivatePost} via son id.
     * 
     * @param id l'id de la réponse privée à récupérer.
     * @return la réponse privée correspondant, ou <code>null</code> si aucune réponse privée n'est trouvée.
     * @throws {@link DAOException} lorsqu'une erreur survient lors de l'opération en base.
     */
    public PrivatePost find( long id ) throws DAOException {
        try {
            return em.find( PrivatePost.class, id );
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /**
     * Cette méthode permet de récupérer le nombre de {@link PrivatePost} pour un {@link PrivateTopic} donné.
     * 
     * @param privateTopic le MP dans lequel effectuer le décompte.
     * @return le nombre de réponses privées correspondant.
     * @throws {@link DAOException} lorsqu'une erreur survient lors de l'opération en base.
     */
    public Integer count( PrivateTopic privateTopic ) throws DAOException {
        try {
            Query query = em.createQuery( JPQL_COUNT_POSTS_PER_PRIVATE_TOPIC );
            query.setParameter( PARAM_PRIVATE_TOPIC, privateTopic );
            // TODO : quid du cas NoResultException ?
            return ( (Long) query.getSingleResult() ).intValue();
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /**
     * Cette méthode permet de chercher le dernier {@link PrivatePost} pour un {@link PrivateTopic} donné.
     * 
     * @param privateTopic le sujet dans lequel effectuer la recherche.
     * @return la réponse privée correspondant, ou <code>null</code> si aucune réponse privée n'est trouvée.
     * @throws {@link DAOException} lorsqu'une erreur survient lors de l'opération en base.
     */
    public PrivatePost findLast( PrivateTopic privateTopic ) throws DAOException {
        try {
            Query query = em.createQuery( JPQL_POSTS_LIST_PER_PRIVATE_TOPIC_DESC );
            query.setParameter( PARAM_PRIVATE_TOPIC, privateTopic );
            return (PrivatePost) query.setMaxResults( 1 ).getSingleResult();
        } catch ( NoResultException e ) {
            return null;
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /**
     * Cette méthode permet de lister tous les {@link PrivatePost} d'un {@link PrivateTopic} donné.
     * 
     * @param privateTopic le sujet dans lequel effectuer la recherche.
     * @return la liste des réponses privées triées par id par ordre croissant, ou <code>null</code> si aucune réponse privée n'est trouvée.
     * @throws {@link DAOException} lorsqu'une erreur survient lors de l'opération en base.
     */
    public List<PrivatePost> list( PrivateTopic privateTopic ) throws DAOException {
        try {
            TypedQuery<PrivatePost> query = em.createQuery( JPQL_POSTS_LIST_PER_PRIVATE_TOPIC, PrivatePost.class );
            query.setParameter( PARAM_PRIVATE_TOPIC, privateTopic );
            return query.getResultList();
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /**
     * Cette méthode permet de lister les {@link PrivatePost} d'un {@link PrivateTopic} de manière paginée.
     * 
     * @param privateTopic le MP dans lequel effectuer la recherche.
     * @param pageNumber le numéro de la page de réponses privées à récupérer.
     * @param postsPerPage le nombre de réponses privées à retourner pour la page donnée.
     * @return la liste des réponses privées triées par id par ordre décroissant, ou <code>null</code> si aucune réponse privée n'est trouvée.
     * @throws {@link DAOException} lorsqu'une erreur survient lors de l'opération en base.
     */
    public List<PrivatePost> list( PrivateTopic privateTopic, int pageNumber, int postsPerPage ) throws DAOException {
        try {
            TypedQuery<PrivatePost> query = em.createQuery( JPQL_POSTS_LIST_PER_PRIVATE_TOPIC, PrivatePost.class );
            query.setParameter( PARAM_PRIVATE_TOPIC, privateTopic ).setFirstResult( ( pageNumber - 1 ) * postsPerPage )
                    .setMaxResults( postsPerPage );
            return query.getResultList();
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /**
     * Cette méthode permet de mettre à jour un {@link PrivatePost} en base.
     * 
     * @param privatePost la réponse privée à mettre à jour en base.
     * @throws {@link DAOException} lorsqu'une erreur survient lors de l'opération en base.
     */
    public void update( PrivatePost privatePost ) throws DAOException {
        try {
            em.merge( privatePost );
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /**
     * Cette méthode permet de supprimer un {@link PrivatePost} de la base.
     * 
     * @param privatePost la réponse privée à supprimer de la base.
     * @throws {@link DAOException} lorsqu'une erreur survient lors de l'opération en base.
     */
    public void delete( PrivatePost privatePost ) throws DAOException {
        try {
            em.remove( em.merge( privatePost ) );
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }
}
