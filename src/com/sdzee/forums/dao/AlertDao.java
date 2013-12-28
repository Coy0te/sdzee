package com.sdzee.forums.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.validation.ConstraintViolationException;

import com.sdzee.dao.DAOException;
import com.sdzee.forums.entities.Alert;

/**
 * AlertDao est la classe DAO contenant les opérations CRUD réalisables sur la table des alertes du forum. Il s'agit d'un EJB Stateless dont
 * la structure s'appuie sur JPA et JPQL.
 * 
 * @author Médéric Munier
 * @version %I%, %G%
 */
@Stateless
public class AlertDao {
    private static final String JPQL_ALERTS_LIST  = "SELECT a FROM Alert a";
    private static final String JPQL_COUNT_ALERTS = "SELECT count(a) FROM Alert a";

    @PersistenceContext( unitName = "bdd_sdzee_PU" )
    private EntityManager       em;

    /**
     * Cette méthode permet d'enregistrer une nouvelle {@link Alert} en base.
     * 
     * @param alert la nouvelle alerte à insérer en base.
     * @throws {@link DAOException} lorsqu'une erreur survient lors de l'opération en base.
     */
    public void create( Alert alert ) throws DAOException {
        try {
            em.persist( alert );
        } catch ( ConstraintViolationException e ) {
            // TODO: logger
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /**
     * Cette méthode permet de chercher une {@link Alert} via son id.
     * 
     * @param id l'id de l'alerte à récupérer.
     * @return l'alerte correspondant, ou <code>null</code> si aucune alerte n'est trouvée.
     * @throws {@link DAOException} lorsqu'une erreur survient lors de l'opération en base.
     */
    public Alert find( long id ) throws DAOException {
        try {
            return em.find( Alert.class, id );
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /**
     * Cette méthode permet de récupérer le nombre d'{@link Alert}.
     * 
     * @return le nombre d'alertes correspondant.
     * @throws {@link DAOException} lorsqu'une erreur survient lors de l'opération en base.
     */
    public Integer count() throws DAOException {
        try {
            Query query = em.createQuery( JPQL_COUNT_ALERTS );
            // TODO : quid du cas NoResultException ?
            return ( (Long) query.getSingleResult() ).intValue();
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /**
     * Cette méthode permet de lister toutes les {@link Alert} en cours.
     * 
     * @return la liste de toutes les alertes, ou <code>null</code> si aucune alerte n'est trouvée.
     * @throws {@link DAOException} lorsqu'une erreur survient lors de l'opération en base.
     */
    public List<Alert> list() throws DAOException {
        try {
            TypedQuery<Alert> query = em.createQuery( JPQL_ALERTS_LIST, Alert.class );
            return query.getResultList();
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /**
     * Cette méthode permet de lister les {@link Alert} de manière paginée.
     * 
     * @param pageNumber le numéro de la page d'alertes à récupérer.
     * @param alertsPerPage le nombre d'alertes à retourner pour la page donnée.
     * @return la liste des alertes, ou <code>null</code> si aucune alerte n'est trouvée.
     * @throws {@link DAOException} lorsqu'une erreur survient lors de l'opération en base.
     */
    public List<Alert> list( int pageNumber, int alertsPerPage ) throws DAOException {
        try {
            TypedQuery<Alert> query = em.createQuery( JPQL_ALERTS_LIST, Alert.class );
            query.setFirstResult( ( pageNumber - 1 ) * alertsPerPage );
            query.setMaxResults( alertsPerPage );
            return query.getResultList();
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /**
     * Cette méthode permet de mettre à jour une {@link Alert} en base.
     * 
     * @param alert l'alerte à mettre à jour en base.
     * @throws {@link DAOException} lorsqu'une erreur survient lors de l'opération en base.
     */
    public void update( Alert alert ) throws DAOException {
        try {
            em.merge( alert );
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /**
     * Cette méthode permet de supprimer une {@link Alert} de la base.
     * 
     * @param alert l'alerte à supprimer de la base.
     * @throws {@link DAOException} lorsqu'une erreur survient lors de l'opération en base.
     */
    public void delete( Alert alert ) throws DAOException {
        try {
            em.remove( em.merge( alert ) );
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }
}
