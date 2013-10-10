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
import com.sdzee.membres.entities.Membre;

@Stateless
public class NotificationDao {
    private static final String JPQL_SELECT_UNIQUE_NOTIFICATION     = "SELECT n FROM Notification n WHERE n.idMembre=:idMembre AND n.idSujet=:idSujet";
    private static final String JPQL_DELETE_NOTIFICATION            = "DELETE FROM Notification n WHERE n.idMembre=:idMembre AND n.idSujet=:idSujet";
    private static final String JPQL_LISTE_NOTIFICATIONS_PAR_MEMBRE = "SELECT n FROM Notification n WHERE n.idMembre=:idMembre";
    private static final String PARAM_ID_MEMBRE                     = "idMembre";
    private static final String PARAM_ID_SUJET                      = "idSujet";

    @PersistenceContext( unitName = "bdd_sdzee_PU" )
    private EntityManager       em;

    /* Enregistrement d'une nouvelle notification */
    public void creer( Notification notification ) throws DAOException {
        try {
            // Insertion uniquement si une notification n'existe pas déjà.
            if ( trouver( notification.getIdMembre(), notification.getIdSujet() ) == null ) {
                em.persist( notification );
            }
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /* Récupération de la liste des notifications pour un membre donné */
    public List<Notification> lister( Membre membre ) throws DAOException {
        try {
            TypedQuery<Notification> query = em.createQuery( JPQL_LISTE_NOTIFICATIONS_PAR_MEMBRE, Notification.class );
            query.setParameter( PARAM_ID_MEMBRE, membre.getId() );
            return query.getResultList();
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /* Récupération de la notification pour un membre et un sujet donnés */
    public Notification trouver( Long idMembre, Long idSujet ) throws DAOException {
        try {
            Query query = em.createQuery( JPQL_SELECT_UNIQUE_NOTIFICATION );
            query.setParameter( PARAM_ID_MEMBRE, idMembre );
            query.setParameter( PARAM_ID_SUJET, idSujet );
            return (Notification) query.getSingleResult();
        } catch ( NoResultException e ) {
            // Si aucune notification trouvé, on retourne null.
            return null;
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /* Mise à jour d'une notification */
    public void update( Notification notification ) throws DAOException {
        try {
            em.merge( notification );
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /* Suppression d'une notification pour un membre et un sujet donnés */
    public void supprimer( Long idMembre, Long idSujet ) throws DAOException {
        try {
            Query query = em.createQuery( JPQL_DELETE_NOTIFICATION );
            query.setParameter( PARAM_ID_MEMBRE, idMembre );
            query.setParameter( PARAM_ID_SUJET, idSujet );
            query.executeUpdate();
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /* Suppression d'une notification */
    public void supprimer( Notification notification ) throws DAOException {
        try {
            em.remove( em.merge( notification ) );
        } catch ( Exception e ) {
            throw new DAOException( e ); // TODO : vérifier ce qui se passe ici quand on essaie de supprimer une notification qui n'existe
                                         // pas en base
        }
    }
}
