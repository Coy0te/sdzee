package com.sdzee.forums.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.validation.ConstraintViolationException;

import com.sdzee.dao.DAOException;
import com.sdzee.forums.entities.Forum;
import com.sdzee.forums.entities.Post;
import com.sdzee.forums.entities.Topic;

/**
 * PostDao est la classe DAO contenant les opérations CRUD réalisables sur la table des réponses à un sujet. Il s'agit d'un EJB Stateless
 * dont la structure s'appuie sur JPA et JPQL.
 * 
 * @author Médéric Munier
 * @version %I%, %G%
 */
@Stateless
public class PostDao {
    private static final String JPQL_POSTS_LIST_PER_TOPIC      = "SELECT p FROM Post p WHERE p.topic=:topic ORDER BY p.id ASC";
    private static final String JPQL_POSTS_LIST_PER_TOPIC_DESC = "SELECT p FROM Post p WHERE p.topic=:topic ORDER BY p.id DESC";
    private static final String JPQL_POSTS_LIST_PER_FORUM_DESC = "SELECT p FROM Post p JOIN p.topic t WHERE t.forum=:forum ORDER BY p.id DESC";
    private static final String PARAM_TOPIC                    = "topic";
    private static final String PARAM_FORUM                    = "forum";

    @PersistenceContext( unitName = "bdd_sdzee_PU" )
    private EntityManager       em;

    /**
     * Cette méthode permet d'enregistrer un nouveau {@link Post} en base.
     * 
     * @param post la nouvelle réponse à insérer en base.
     * @throws {@link DAOException} lorsqu'une erreur survient lors de l'opération en base.
     */
    public void create( Post post ) throws DAOException {
        try {
            em.persist( post );
        } catch ( ConstraintViolationException e ) {
            // TODO: logger
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /**
     * Cette méthode permet de chercher un {@link Post} via son id.
     * 
     * @param id l'id de la réponse à récupérer.
     * @return la réponse correspondant, ou <code>null</code> si aucune réponse n'est trouvée.
     * @throws {@link DAOException} lorsqu'une erreur survient lors de l'opération en base.
     */
    public Post find( long id ) throws DAOException {
        try {
            return em.find( Post.class, id );
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /**
     * Cette méthode permet de lister tous les {@link Post} d'un {@link Topic} donné.
     * 
     * @param topic le sujet dans lequel effectuer la recherche.
     * @return la liste des réponses triées par id par ordre croissant, ou <code>null</code> si aucune réponse n'est trouvée.
     * @throws {@link DAOException} lorsqu'une erreur survient lors de l'opération en base.
     */
    public List<Post> list( Topic topic ) throws DAOException {
        try {
            TypedQuery<Post> query = em.createQuery( JPQL_POSTS_LIST_PER_TOPIC, Post.class );
            query.setParameter( PARAM_TOPIC, topic );
            return query.getResultList();
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /**
     * Cette méthode permet de récupérer le dernier {@link Post} d'un {@link Topic} donné.
     * 
     * @param topic le sujet dans lequel effectuer la recherche.
     * @return la réponse correspondant, ou <code>null</code> si aucune réponse n'est trouvée.
     * @throws {@link DAOException} lorsqu'une erreur survient lors de l'opération en base.
     */
    public Post findLast( Topic topic ) throws DAOException {
        try {
            Query query = em.createQuery( JPQL_POSTS_LIST_PER_TOPIC_DESC );
            query.setParameter( PARAM_TOPIC, topic );
            return (Post) query.setMaxResults( 1 ).getSingleResult();
        } catch ( NoResultException e ) {
            return null;
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /**
     * Cette méthode permet de récupérer le dernier {@link Post} d'un {@link Forum} donné.
     * 
     * @param forum le forum dans lequel effectuer la recherche.
     * @return la réponse correspondant, ou <code>null</code> si aucune réponse n'est trouvée.
     * @throws {@link DAOException} lorsqu'une erreur survient lors de l'opération en base.
     */
    public Post findLast( Forum forum ) throws DAOException {
        try {
            Query query = em.createQuery( JPQL_POSTS_LIST_PER_FORUM_DESC );
            query.setParameter( PARAM_FORUM, forum );
            return (Post) query.setMaxResults( 1 ).getSingleResult();
        } catch ( NoResultException e ) {
            return null;
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /**
     * Cette méthode permet de lister les {@link Post} d'un {@link Topic} de manière paginée.
     * 
     * @param topic le sujet dans lequel effectuer la recherche.
     * @param pageNumber le numéro de la page de réponses à récupérer.
     * @param postsPerPage le nombre de réponses à retourner pour la page donnée.
     * @return la liste des réponses triées par id par ordre croissant, ou <code>null</code> si aucune réponse n'est trouvée.
     * @throws {@link DAOException} lorsqu'une erreur survient lors de l'opération en base.
     */
    public List<Post> list( Topic topic, int pageNumber, int postsPerPage ) throws DAOException {
        try {
            TypedQuery<Post> query = em.createQuery( JPQL_POSTS_LIST_PER_TOPIC, Post.class );
            query.setParameter( PARAM_TOPIC, topic ).setFirstResult( ( pageNumber - 1 ) * postsPerPage )
                    .setMaxResults( postsPerPage );
            return query.getResultList();
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /**
     * Cette méthode permet de mettre à jour un {@link Post} en base.
     * 
     * @param post la réponse à mettre à jour en base.
     * @throws {@link DAOException} lorsqu'une erreur survient lors de l'opération en base.
     */
    public void update( Post post ) throws DAOException {
        try {
            em.merge( post );
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /**
     * Cette méthode permet de supprimer un {@link Post} de la base.
     * 
     * @param post la réponse à supprimer de la base.
     * @throws {@link DAOException} lorsqu'une erreur survient lors de l'opération en base.
     */
    public void delete( Post post ) throws DAOException {
        try {
            em.remove( em.merge( post ) );
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }
}
