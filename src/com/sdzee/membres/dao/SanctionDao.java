package com.sdzee.membres.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import com.sdzee.dao.DAOException;
import com.sdzee.membres.entities.Member;
import com.sdzee.membres.entities.Sanction;

/**
 * SanctionDao est la classe DAO contenant les opérations CRUD réalisables sur la table des sanctions sur les membres du site. Il s'agit
 * d'un EJB Stateless dont la structure s'appuie sur JPA et JPQL.
 * 
 * @author Médéric Munier
 * @version %I%, %G%
 */
@Stateless
public class SanctionDao {
    private static final String JPQL_SANCTIONS_LIST            = "SELECT s FROM Sanction s ORDER BY s.id";
    private static final String JPQL_SANCTIONS_LIST_PER_MEMBER = "SELECT s FROM Sanction s WHERE s.member=:member ORDER BY s.id";
    private static final String PARAM_MEMBER                   = "member";

    @PersistenceContext( unitName = "bdd_sdzee_PU" )
    private EntityManager       em;

    /**
     * Cette méthode permet d'enregistrer une nouvelle {@link Sanction} en base.
     * 
     * @param sanction la nouvelle sanction à insérer en base.
     * @throws {@link DAOException} lorsqu'une erreur survient lors de l'opération en base.
     */
    public void create( Sanction sanction ) throws DAOException {
        try {
            em.persist( sanction );
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /**
     * Cette méthode permet de chercher une {@link Sanction} via son id.
     * 
     * @param id l'id de la sanction à récupérer.
     * @return la sanction correspondant, ou <code>null</code> si aucune sanction n'est trouvée.
     * @throws {@link DAOException} lorsqu'une erreur survient lors de l'opération en base.
     */
    public Sanction find( long id ) throws DAOException {
        try {
            return em.find( Sanction.class, id );
        } catch ( Exception e ) {
            e.printStackTrace();
            throw new DAOException( e );
        }
    }

    /**
     * Cette méthode permet de lister toutes les {@link Sanction}.
     * 
     * @return la liste des sanctions triés par id, ou <code>null</code> si aucune sanction n'est trouvée.
     * @throws {@link DAOException} lorsqu'une erreur survient lors de l'opération en base.
     */
    public List<Sanction> list() throws DAOException {
        try {
            TypedQuery<Sanction> query = em.createQuery( JPQL_SANCTIONS_LIST, Sanction.class );
            return query.getResultList();
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /**
     * Cette méthode permet de lister les {@link Sanctions} d'un {@link Member} de manière paginée.
     * 
     * @param member le membre pour lequel effectuer la recherche.
     * @param pageNumber le numéro de la page de sanctions à récupérer.
     * @param sanctionsPerPage le nombre de sanctions à retourner pour la page donnée.
     * @return la liste des sanctions triées par id par ordre décroissant, ou <code>null</code> si aucune sanction n'est trouvée.
     * @throws {@link DAOException} lorsqu'une erreur survient lors de l'opération en base.
     */
    public List<Sanction> list( Member member, int pageNumber, int sanctionsPerPage ) throws DAOException {
        try {
            TypedQuery<Sanction> query = em.createQuery( JPQL_SANCTIONS_LIST_PER_MEMBER, Sanction.class );
            query.setParameter( PARAM_MEMBER, member );
            query.setFirstResult( ( pageNumber - 1 ) * sanctionsPerPage );
            query.setMaxResults( sanctionsPerPage );
            return query.getResultList();
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /**
     * Cette méthode permet de mettre à jour une {@link Sanction} en base.
     * 
     * @param sanction la sanction à mettre à jour en base.
     * @throws {@link DAOException} lorsqu'une erreur survient lors de l'opération en base.
     */
    public void update( Sanction sanction ) throws DAOException {
        try {
            em.merge( sanction );
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /**
     * Cette méthode permet de supprimer une {@link Sanction} de la base.
     * 
     * @param sanction la sanction à supprimer de la base.
     * @throws {@link DAOException} lorsqu'une erreur survient lors de l'opération en base.
     */
    public void delete( Sanction sanction ) throws DAOException {
        try {
            em.remove( em.merge( sanction ) );
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }
}
