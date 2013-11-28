package com.sdzee.forums.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.sdzee.dao.DAOException;
import com.sdzee.forums.entities.Notification;
import com.sdzee.forums.entities.Topic;
import com.sdzee.membres.entities.Member;

/**
 * NotificationDao est la classe DAO contenant les opérations CRUD réalisables sur la table des notifications. Il s'agit d'un EJB Stateless
 * dont la structure s'appuie sur JPA et JPQL.
 * 
 * @author Médéric Munier
 * @version %I%, %G%
 */
@Stateless
public class NotificationDao {
    private static final String JPQL_SELECT_UNIQUE_NOTIFICATION    = "SELECT n FROM Notification n WHERE n.memberId=:memberId AND n.topicId=:topicId";
    private static final String JPQL_DELETE_NOTIFICATION           = "DELETE FROM Notification n WHERE n.memberId=:memberId AND n.topicId=:topicId";
    private static final String JPQL_NOTIFICATIONS_LIST_PER_MEMBER = "SELECT n FROM Notification n WHERE n.memberId=:memberId";
    private static final String PARAM_MEMBER_ID                    = "memberId";
    private static final String PARAM_TOPIC_ID                     = "topicId";

    @PersistenceContext( unitName = "bdd_sdzee_PU" )
    private EntityManager       em;

    /**
     * Cette méthode permet d'enregistrer une nouvelle {@link Notification} en base.
     * 
     * @param notification la nouvelle notification à insérer en base.
     * @throws {@link DAOException} lorsqu'une erreur survient lors de l'opération en base.
     */
    public void create( Notification notification ) throws DAOException {
        try {
            // Insertion uniquement si une notification n'existe pas déjà.
            if ( find( notification.getMemberId(), notification.getTopicId() ) == null ) {
                em.persist( notification );
            }
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /**
     * Cette méthode permet de lister toutes les {@link Notification} d'un {@link Member} donné.
     * 
     * @param member le membre pour lequel effectuer la recherche.
     * @return la liste des notifications triées par id par ordre croissant, ou <code>null</code> si aucune notification n'est trouvée.
     * @throws {@link DAOException} lorsqu'une erreur survient lors de l'opération en base.
     */
    public List<Notification> list( Member member ) throws DAOException {
        try {
            TypedQuery<Notification> query = em.createQuery( JPQL_NOTIFICATIONS_LIST_PER_MEMBER, Notification.class );
            query.setParameter( PARAM_MEMBER_ID, member.getId() );
            return query.getResultList();
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /**
     * Cette méthode permet de chercher une {@link Notification} via l'id du {@link Member} à notifier et l'id du {@link Topic} observé.
     * 
     * @param memberId l'id du membre à notifier.
     * @param topicId l'id du sujet observé.
     * @return la notification correspondant, ou <code>null</code> si aucune notification n'est trouvée.
     * @throws {@link DAOException} lorsqu'une erreur survient lors de l'opération en base.
     */
    public Notification find( Long memberId, Long topicId ) throws DAOException {
        try {
            Query query = em.createQuery( JPQL_SELECT_UNIQUE_NOTIFICATION );
            query.setParameter( PARAM_MEMBER_ID, memberId );
            query.setParameter( PARAM_TOPIC_ID, topicId );
            return (Notification) query.getSingleResult();
        } catch ( NoResultException e ) {
            return null;
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /**
     * Cette méthode permet de mettre à jour une {@link Notification} en base.
     * 
     * @param notification la notification à mettre à jour en base.
     * @throws {@link DAOException} lorsqu'une erreur survient lors de l'opération en base.
     */
    public void update( Notification notification ) throws DAOException {
        try {
            em.merge( notification );
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /**
     * Cette méthode permet de supprimer une {@link Notification} en se basant sur l'id du {@link Member} et l'id du {@link Topic} donnés.
     * 
     * @param memberId l'id du membre pour lequel effectuer la suppression.
     * @param topicId l'id du sujet pour lequel effectuer la suppression.
     * @throws {@link DAOException} lorsqu'une erreur survient lors de l'opération en base.
     */
    public void delete( Long memberId, Long topicId ) throws DAOException {
        try {
            Query query = em.createQuery( JPQL_DELETE_NOTIFICATION );
            query.setParameter( PARAM_MEMBER_ID, memberId );
            query.setParameter( PARAM_TOPIC_ID, topicId );
            query.executeUpdate();
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /**
     * Cette méthode permet de supprimer une {@link Notification} de la base.
     * 
     * @param notification la notification à supprimer de la base.
     * @throws {@link DAOException} lorsqu'une erreur survient lors de l'opération en base.
     */
    public void delete( Notification notification ) throws DAOException {
        try {
            em.remove( em.merge( notification ) );
        } catch ( Exception e ) {
            throw new DAOException( e ); // TODO : vérifier ce qui se passe ici quand on essaie de supprimer une notification qui n'existe
                                         // pas en base
        }
    }
}
