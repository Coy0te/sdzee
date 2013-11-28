package com.sdzee.privatemessages.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.sdzee.dao.DAOException;
import com.sdzee.membres.entities.Member;
import com.sdzee.privatemessages.entities.PrivateNotification;
import com.sdzee.privatemessages.entities.PrivateTopic;

/**
 * PrivateNotificationDao est la classe DAO contenant les opérations CRUD réalisables sur la table des notifications des MPs. Il s'agit d'un EJB
 * Stateless dont la structure s'appuie sur JPA et JPQL.
 * 
 * @author Médéric Munier
 * @version %I%, %G%
 */
@Stateless
public class PrivateNotificationDao {
    private static final String JPQL_SELECT_UNIQUE_NOTIFICATION    = "SELECT n FROM PrivateNotification n WHERE n.memberId=:memberId AND n.privateTopicId=:privateTopicId";
    private static final String JPQL_DELETE_NOTIFICATION           = "DELETE FROM PrivateNotification n WHERE n.memberId=:memberId AND n.privateTopicId=:privateTopicId";
    private static final String JPQL_NOTIFICATIONS_LIST_PER_MEMBER = "SELECT n FROM PrivateNotification n WHERE n.memberId=:memberId";
    private static final String PARAM_MEMBER_ID                    = "memberId";
    private static final String PARAM_PRIVATE_TOPIC_ID             = "privateTopicId";

    @PersistenceContext( unitName = "bdd_sdzee_PU" )
    private EntityManager       em;

    /**
     * Cette méthode permet d'enregistrer une nouvelle {@link PrivateNotification} en base.
     * 
     * @param privateNotification la nouvelle notification à insérer en base.
     * @throws {@link DAOException} lorsqu'une erreur survient lors de l'opération en base.
     */
    public void create( PrivateNotification privateNotification ) throws DAOException {
        try {
            // Insertion uniquement si une notification n'existe pas déjà.
            if ( find( privateNotification.getMemberId(), privateNotification.getPrivateTopicId() ) == null ) {
                em.persist( privateNotification );
            }
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /**
     * Cette méthode permet de lister toutes les {@link PrivateNotification} d'un {@link Member} donné.
     * 
     * @param member le membre pour lequel effectuer la recherche.
     * @return la liste des notifications triées par id par ordre croissant, ou <code>null</code> si aucune notification n'est trouvée.
     * @throws {@link DAOException} lorsqu'une erreur survient lors de l'opération en base.
     */
    public List<PrivateNotification> list( Member member ) throws DAOException {
        try {
            TypedQuery<PrivateNotification> query = em.createQuery( JPQL_NOTIFICATIONS_LIST_PER_MEMBER,
                    PrivateNotification.class );
            query.setParameter( PARAM_MEMBER_ID, member.getId() );
            return query.getResultList();
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /**
     * Cette méthode permet de chercher une {@link PrivateNotification} via l'id du {@link Member} à notifier et l'id du {@link PrivateTopic} observé.
     * 
     * @param memberId l'id du membre à notifier.
     * @param privateTopicId l'id du sujet observé.
     * @return la notification correspondant, ou <code>null</code> si aucune notification n'est trouvée.
     * @throws {@link DAOException} lorsqu'une erreur survient lors de l'opération en base.
     */
    public PrivateNotification find( Long memberId, Long privateTopicId ) throws DAOException {
        try {
            Query query = em.createQuery( JPQL_SELECT_UNIQUE_NOTIFICATION );
            query.setParameter( PARAM_MEMBER_ID, memberId );
            query.setParameter( PARAM_PRIVATE_TOPIC_ID, privateTopicId );
            return (PrivateNotification) query.getSingleResult();
        } catch ( NoResultException e ) {
            return null;
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /**
     * Cette méthode permet de mettre à jour une {@link PrivateNotification} en base.
     * 
     * @param privateNotification la notification à mettre à jour en base.
     * @throws {@link DAOException} lorsqu'une erreur survient lors de l'opération en base.
     */
    public void update( PrivateNotification privateNotification ) throws DAOException {
        try {
            em.merge( privateNotification );
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /**
     * Cette méthode permet de supprimer une {@link PrivateNotification} en se basant sur l'id du {@link Member} et l'id du {@link PrivateTopic}
     * donnés.
     * 
     * @param memberId l'id du membre pour lequel effectuer la suppression.
     * @param privateTopicId l'id du sujet pour lequel effectuer la suppression.
     * @throws {@link DAOException} lorsqu'une erreur survient lors de l'opération en base.
     */
    public void delete( Long memberId, Long privateTopicId ) throws DAOException {
        try {
            Query query = em.createQuery( JPQL_DELETE_NOTIFICATION );
            query.setParameter( PARAM_MEMBER_ID, memberId );
            query.setParameter( PARAM_PRIVATE_TOPIC_ID, privateTopicId );
            query.executeUpdate();
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /**
     * Cette méthode permet de supprimer une {@link PrivateNotification} de la base.
     * 
     * @param privateNotification la notification à supprimer de la base.
     * @throws {@link DAOException} lorsqu'une erreur survient lors de l'opération en base.
     */
    public void delete( PrivateNotification privateNotification ) throws DAOException {
        try {
            em.remove( em.merge( privateNotification ) );
        } catch ( Exception e ) {
            throw new DAOException( e ); // TODO : vérifier ce qui se passe ici quand on essaie de supprimer une notification qui n'existe
                                         // pas en base
        }
    }
}
