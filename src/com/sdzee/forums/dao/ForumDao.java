package com.sdzee.forums.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.sdzee.dao.DAOException;
import com.sdzee.forums.entities.Forum;
import com.sdzee.forums.entities.ForumCategory;

/**
 * ForumDao est la classe DAO contenant les opérations CRUD réalisables sur la table des forums. Il s'agit d'un EJB Stateless dont la structure
 * s'appuie sur JPA et JPQL.
 * 
 * @author Médéric Munier
 * @version %I%, %G%
 */
@Stateless
public class ForumDao {
    private static final String JPQL_FORUMS_LIST                   = "SELECT f FROM Forum f ORDER BY f.id";
    private static final String JPQL_FORUMS_LIST_PER_CATEGORY      = "SELECT f FROM Forum f WHERE f.category=:category ORDER BY f.position";
    private static final String JPQL_FIND_BY_CATEGORY_AND_POSITION = "SELECT f FROM Forum f WHERE f.category=:category AND f.position=:position";
    private static final String PARAM_POSITION                     = "position";
    private static final String PARAM_CATEGORY                     = "category";

    @PersistenceContext( unitName = "bdd_sdzee_PU" )
    private EntityManager       em;

    /**
     * Cette méthode permet d'enregistrer un nouveau {@link Forum} en base.
     * 
     * @param forum le nouveau forum à insérer en base.
     * @throws {@link DAOException} lorsqu'une erreur survient lors de l'opération en base.
     */
    public void create( Forum forum ) throws DAOException {
        try {
            em.persist( forum );
        } catch ( Exception e ) {
            e.printStackTrace();
            throw new DAOException( e );
        }
    }

    /**
     * Cette méthode permet de chercher un {@link Forum} via son id.
     * 
     * @param id l'id du forum à récupérer.
     * @return le forum correspondant, ou <code>null</code> si aucun forum n'est trouvé.
     * @throws {@link DAOException} lorsqu'une erreur survient lors de l'opération en base.
     */
    public Forum find( long id ) throws DAOException {
        try {
            return em.find( Forum.class, id );
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /**
     * Cette méthode permet de chercher un {@link Forum} via sa position dans une {@link ForumCategory}.
     * 
     * @param category la catégorie dans laquelle effectuer la recherche.
     * @param position la position souhaitée dans la catégorie donnée.
     * @return le forum correspondant, ou <code>null</code> si aucun forum n'est trouvé.
     * @throws {@link DAOException} lorsqu'une erreur survient lors de l'opération en base.
     */
    public Forum find( ForumCategory category, int position ) throws DAOException {
        try {
            Query query = em.createQuery( JPQL_FIND_BY_CATEGORY_AND_POSITION );
            query.setParameter( PARAM_POSITION, position );
            query.setParameter( PARAM_CATEGORY, category );
            return (Forum) query.setMaxResults( 1 ).getSingleResult();
        } catch ( NoResultException e ) {
            return null;
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /**
     * Cette méthode permet de lister tous les {@link Forum}.
     * 
     * @return la liste des forums triés par id, ou <code>null</code> si aucun forum n'est trouvé.
     * @throws {@link DAOException} lorsqu'une erreur survient lors de l'opération en base.
     */
    public List<Forum> list() throws DAOException {
        try {
            TypedQuery<Forum> query = em.createQuery( JPQL_FORUMS_LIST, Forum.class );
            return query.getResultList();
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /**
     * Cette méthode permet de lister tous les {@link Forum} d'une {@link ForumCategory} donnée.
     * 
     * @param category la catégorie dans laquelle effectuer la recherche.
     * @return la liste des forums triés par position, ou <code>null</code> si aucun forum n'est trouvé.
     * @throws {@link DAOException} lorsqu'une erreur survient lors de l'opération en base.
     */
    public List<Forum> list( ForumCategory category ) throws DAOException {
        try {
            TypedQuery<Forum> query = em.createQuery( JPQL_FORUMS_LIST_PER_CATEGORY, Forum.class );
            query.setParameter( PARAM_CATEGORY, category );
            return query.getResultList();
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /**
     * Cette méthode permet de mettre à jour un {@link Forum} en base.
     * 
     * @param forum le forum à mettre à jour en base.
     * @throws {@link DAOException} lorsqu'une erreur survient lors de l'opération en base.
     */
    public void update( Forum forum ) throws DAOException {
        try {
            em.merge( forum );
        } catch ( Exception e ) {
            e.printStackTrace();
            throw new DAOException( e );
        }
    }

    /**
     * Cette méthode permet de rafraîchir un {@link Forum} dans le modèle objet.
     * 
     * @param forum le forum à rafraîchir dans le modèle objet.
     * @throws {@link DAOException} lorsqu'une erreur survient lors de l'opération en base.
     */
    public void refresh( Forum forum ) throws DAOException {
        try {
            em.refresh( find( forum.getId() ) );
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /**
     * Cette méthode permet de supprimer un {@link Forum} de la base.
     * 
     * @param forum le forum à supprimer de la base.
     * @throws {@link DAOException} lorsqu'une erreur survient lors de l'opération en base.
     */
    public void delete( Forum forum ) throws DAOException {
        try {
            em.remove( em.merge( forum ) );
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }
}
