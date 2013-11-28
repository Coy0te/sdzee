package com.sdzee.forums.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.sdzee.dao.DAOException;
import com.sdzee.forums.entities.Bookmark;
import com.sdzee.forums.entities.Topic;
import com.sdzee.membres.entities.Member;

/**
 * BookmarkDao est la classe DAO contenant les opérations CRUD réalisables sur la table des bookmarks. Il s'agit d'un EJB Stateless dont la
 * structure s'appuie sur JPA et JPQL.
 * 
 * @author Médéric Munier
 * @version %I%, %G%
 */
@Stateless
public class BookmarkDao {

    private static final String JPQL_SELECT_UNIQUE_BOOKMARK    = "SELECT b FROM Bookmark b WHERE b.memberId=:memberId AND b.topicId=:topicId";
    private static final String JPQL_BOOKMARKS_LIST_PER_MEMBER = "SELECT b FROM Bookmark b WHERE b.memberId=:memberId";
    private static final String JPQL_BOOKMARKS_LIST_PER_TOPIC  = "SELECT b FROM Bookmark b WHERE b.topicId=:topicId";
    private static final String PARAM_MEMBER_ID                = "memberId";
    private static final String PARAM_TOPIC_ID                 = "topicId";

    @PersistenceContext( unitName = "bdd_sdzee_PU" )
    private EntityManager       em;

    /**
     * Cette méthode permet d'enregistrer un nouveau {@link Bookmark} en base.
     * 
     * @param bookmark le nouveau bookmark à insérer en base.
     * @throws {@link DAOException} lorsqu'une erreur survient lors de l'opération en base.
     */
    public void create( Bookmark bookmark ) throws DAOException {
        try {
            // Insertion uniquement si un bookmark n'existe pas déjà.
            if ( find( bookmark.getMemberId(), bookmark.getTopicId() ) == null ) {
                em.persist( bookmark );
            }
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /**
     * Cette méthode permet de lister tous les {@link Bookmark} d'un {@link Member} donné.
     * 
     * @param member le membre pour lequel effectuer la recherche.
     * @return la liste des bookmarks, ou <code>null</code> si aucun bookmark n'est trouvé.
     * @throws {@link DAOException} lorsqu'une erreur survient lors de l'opération en base.
     */
    public List<Bookmark> list( Member member ) throws DAOException {
        try {
            TypedQuery<Bookmark> query = em.createQuery( JPQL_BOOKMARKS_LIST_PER_MEMBER, Bookmark.class );
            query.setParameter( PARAM_MEMBER_ID, member.getId() );
            return query.getResultList();
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /**
     * Cette méthode permet de lister tous les {@link Bookmark} pour un {@link Topic} donné.
     * 
     * @param topic le sujet pour lequel effectuer la recherche.
     * @return la liste des bookmarks, ou <code>null</code> si aucun bookmark n'est trouvé.
     * @throws {@link DAOException} lorsqu'une erreur survient lors de l'opération en base.
     */
    public List<Bookmark> list( Topic topic ) throws DAOException {
        try {
            TypedQuery<Bookmark> query = em.createQuery( JPQL_BOOKMARKS_LIST_PER_TOPIC, Bookmark.class );
            query.setParameter( PARAM_TOPIC_ID, topic.getId() );
            return query.getResultList();
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /**
     * Cette méthode permet de chercher un {@link Bookmark} via l'id du {@link Member} concerné et l'id du {@link Topic} observé.
     * 
     * @param memberId l'id du membre pour lequel effectuer la recherche.
     * @param topicId l'id du sujet pour lequel effectuer la recherche.
     * @return le bookrmark correspondant, ou <code>null</code> si aucun bookmark n'est trouvé.
     * @throws {@link DAOException} lorsqu'une erreur survient lors de l'opération en base.
     */
    public Bookmark find( Long memberId, Long topicId ) throws DAOException {
        try {
            Query query = em.createQuery( JPQL_SELECT_UNIQUE_BOOKMARK );
            query.setParameter( PARAM_MEMBER_ID, memberId );
            query.setParameter( PARAM_TOPIC_ID, topicId );
            return (Bookmark) query.getSingleResult();
        } catch ( NoResultException e ) {
            return null;
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /**
     * Cette méthode permet de mettre à jour un {@link Bookrmark} en base.
     * 
     * @param bookmark le bookmark à mettre à jour en base.
     * @throws {@link DAOException} lorsqu'une erreur survient lors de l'opération en base.
     */
    public void update( Bookmark bookmark ) throws DAOException {
        try {
            em.merge( bookmark );
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /**
     * Cette méthode permet de supprimer un {@link Bookmark} de la base.
     * 
     * @param bookmark le bookmark à supprimer de la base.
     * @throws {@link DAOException} lorsqu'une erreur survient lors de l'opération en base.
     */
    public void delete( Bookmark bookmark ) throws DAOException {
        try {
            em.remove( em.merge( bookmark ) );
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }
}
