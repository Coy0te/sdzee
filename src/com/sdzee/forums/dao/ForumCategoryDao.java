package com.sdzee.forums.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.sdzee.dao.DAOException;
import com.sdzee.forums.entities.ForumCategory;

/**
 * ForumCategoryDao est la classe DAO contenant les opérations CRUD réalisables sur la table des catégories de forums. Il s'agit d'un EJB
 * Stateless dont la structure s'appuie sur JPA et JPQL.
 * 
 * @author Médéric Munier
 * @version %I%, %G%
 */
@Stateless
public class ForumCategoryDao {
    private static final String JPQL_CATEGORIES_LIST  = "SELECT c FROM ForumCategory c ORDER BY c.position";
    private static final String JPQL_FIND_BY_POSITION = "SELECT c FROM ForumCategory c WHERE c.position=:position";
    private static final String PARAM_POSITION        = "position";

    @PersistenceContext( unitName = "bdd_sdzee_PU" )
    private EntityManager       em;

    /**
     * Cette méthode permet d'enregistrer une nouvelle {@link ForumCategory} en base.
     * 
     * @param category la nouvelle categorie à insérer en base.
     * @throws {@link DAOException} lorsqu'une erreur survient lors de l'opération en base.
     */
    public void create( ForumCategory category ) throws DAOException {
        try {
            em.persist( category );
        } catch ( Exception e ) {
            e.printStackTrace();
            throw new DAOException( e );
        }
    }

    /**
     * Cette méthode permet de chercher une {@link ForumCategory} via son id.
     * 
     * @param id l'id de la catégorie de forum à récupérer.
     * @return la catégorie correspondant, ou <code>null</code> si aucune catégorie n'est trouvée.
     * @throws {@link DAOException} lorsqu'une erreur survient lors de l'opération en base.
     */
    public ForumCategory find( long id ) throws DAOException {
        try {
            return em.find( ForumCategory.class, id );
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /**
     * Cette méthode permet de chercher une {@link ForumCategory} via sa position.
     * 
     * @param position la position souhaitée.
     * @return la catégorie correspondant, ou <code>null</code> si aucune catégorie n'est trouvée.
     * @throws {@link DAOException} lorsqu'une erreur survient lors de l'opération en base.
     */
    public ForumCategory find( int position ) throws DAOException {
        try {
            Query query = em.createQuery( JPQL_FIND_BY_POSITION );
            query.setParameter( PARAM_POSITION, position );
            return (ForumCategory) query.setMaxResults( 1 ).getSingleResult();
        } catch ( NoResultException e ) {
            return null;
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /**
     * Cette méthode permet de lister toutes les {@link ForumCategory}.
     * 
     * @return la liste des catégories triées par position, ou <code>null</code> si aucune catégorie n'est trouvée.
     * @throws {@link DAOException} lorsqu'une erreur survient lors de l'opération en base.
     */
    public List<ForumCategory> list() throws DAOException {
        try {
            TypedQuery<ForumCategory> query = em.createQuery( JPQL_CATEGORIES_LIST, ForumCategory.class );
            return query.getResultList();
        } catch ( Exception e ) {
            e.printStackTrace();
            throw new DAOException( e );
        }
    }

    /**
     * Cette méthode permet de mettre à jour une {@link ForumCategory} en base.
     * 
     * @param category la catégorie à mettre à jour en base.
     * @throws {@link DAOException} lorsqu'une erreur survient lors de l'opération en base.
     */
    public void update( ForumCategory category ) throws DAOException {
        try {
            em.merge( category );
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /**
     * Cette méthode permet de supprimer une {@link ForumCategory} de la base.
     * 
     * @param category la catégorie à supprimer de la base.
     * @throws {@link DAOException} lorsqu'une erreur survient lors de l'opération en base.
     */
    public void delete( ForumCategory category ) throws DAOException {
        try {
            em.remove( em.merge( category ) );
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }
}
