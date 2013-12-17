package com.sdzee.privatemessages.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.sdzee.dao.DAOException;
import com.sdzee.membres.entities.Member;
import com.sdzee.privatemessages.entities.PrivateTopic;

/**
 * PrivateTopicDao est la classe DAO contenant les opérations CRUD réalisables sur la table des MP. Il s'agit d'un EJB Stateless dont la structure
 * s'appuie sur JPA et JPQL.
 * 
 * @author Médéric Munier
 * @version %I%, %G%
 */
@Stateless
public class PrivateTopicDao {
    private static final String JPQL_MPS_LIST                = "SELECT t FROM PrivateTopic t ORDER BY t.id DESC";
    private static final String JPQL_MPS_LIST_PER_MEMBER     = "SELECT DISTINCT (t) FROM PrivateTopic t LEFT JOIN t.lastPrivatePost lp LEFT JOIN t.firstPrivatePost fp WHERE :member MEMBER OF t.participants ORDER BY COALESCE(lp.creationDate, fp.creationDate) DESC";
    private static final String JPQL_COUNT_TOPICS_PER_MEMBER = "SELECT COUNT(t) FROM PrivateTopic t WHERE :member MEMBER OF t.participants";
    private static final String PARAM_MEMBER                 = "member";

    @PersistenceContext( unitName = "bdd_sdzee_PU" )
    private EntityManager       em;

    /**
     * Cette méthode permet d'enregistrer un nouveau {@link PrivateTopic} en base.
     * 
     * @param privateTopic le nouveau MP à insérer en base.
     * @throws {@link DAOException} lorsqu'une erreur survient lors de l'opération en base.
     */
    public void create( PrivateTopic privateTopic ) throws DAOException {
        try {
            em.persist( privateTopic );
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /**
     * Cette méthode permet de chercher un {@link PrivateTopic} via son id.
     * 
     * @param id l'id du MP à récupérer.
     * @return le MP correspondant, ou <code>null</code> si aucun MP n'est trouvé.
     * @throws {@link DAOException} lorsqu'une erreur survient lors de l'opération en base.
     */
    public PrivateTopic find( long id ) throws DAOException {
        try {
            return em.find( PrivateTopic.class, id );
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /**
     * Cette méthode permet de lister tous les {@link PrivateTopic}.
     * 
     * @return la liste des MPs triés par id, ou <code>null</code> si aucun MP n'est trouvé.
     * @throws {@link DAOException} lorsqu'une erreur survient lors de l'opération en base.
     */
    public List<PrivateTopic> list() throws DAOException {
        try {
            TypedQuery<PrivateTopic> query = em.createQuery( JPQL_MPS_LIST, PrivateTopic.class );
            return query.getResultList();
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /**
     * Cette méthode permet de récupérer le nombre de {@link PrivateTopic} pour un {@link Member} donné.
     * 
     * @param member le membre pour lequel effectuer le décompte.
     * @return le nombre de MPs correspondant.
     * @throws {@link DAOException} lorsqu'une erreur survient lors de l'opération en base.
     */
    public Integer count( Member member ) throws DAOException {
        try {
            Query query = em.createQuery( JPQL_COUNT_TOPICS_PER_MEMBER );
            query.setParameter( PARAM_MEMBER, member );
            // TODO : quid du cas NoResultException ?
            return ( (Long) query.getSingleResult() ).intValue();
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /**
     * Cette méthode permet de lister les {@link PrivateTopic} de manière paginée.
     * 
     * @param pageNumber le numéro de la page de MPs à récupérer.
     * @param mpsPerPage le nombre de MPs à retourner pour la page donnée.
     * @return la liste des MPs triés par id par ordre décroissant, ou <code>null</code> si aucun MP n'est trouvé.
     * @throws {@link DAOException} lorsqu'une erreur survient lors de l'opération en base.
     */
    public List<PrivateTopic> list( Member member, int pageNumber, int mpsPerPage ) throws DAOException {
        try {
            TypedQuery<PrivateTopic> query = em.createQuery( JPQL_MPS_LIST_PER_MEMBER, PrivateTopic.class );
            query.setParameter( PARAM_MEMBER, member );

            // TODO : WTF is wrong avec la pagination de cette requête.... Je deviens fou :
            // http://stackoverflow.com/questions/20264592/jpa-paginated-query-with-manytomany-collection-returns-wrong-number-of-entitie

            query.setFirstResult( ( pageNumber - 1 ) * mpsPerPage );
            query.setMaxResults( mpsPerPage );
            return query.getResultList();
        } catch ( Exception e ) {
            e.printStackTrace();
            throw new DAOException( e );
        }
    }

    /**
     * Cette méthode permet de mettre à jour un {@link PrivateTopic} en base.
     * 
     * @param privateTopic le MP à mettre à jour en base.
     * @throws {@link DAOException} lorsqu'une erreur survient lors de l'opération en base.
     */
    public void update( PrivateTopic privateTopic ) throws DAOException {
        try {
            em.merge( privateTopic );
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /**
     * Cette méthode permet de rafraîchir un {@link PrivateTopic} dans le modèle objet.
     * 
     * @param privateTopic le MP à rafraîchir dans le modèle objet.
     * @return le MP fraîchement récupéré depuis la base.
     * @throws {@link DAOException} lorsqu'une erreur survient lors de l'opération en base.
     */
    public PrivateTopic refresh( PrivateTopic privateTopic ) throws DAOException {
        try {
            PrivateTopic freshPrivateTopic = find( privateTopic.getId() );
            em.refresh( freshPrivateTopic );
            return freshPrivateTopic;
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /**
     * Cette méthode permet de supprimer un {@link PrivateTopic} de la base.
     * 
     * @param privateTopic le MP à supprimer de la base.
     * @throws {@link DAOException} lorsqu'une erreur survient lors de l'opération en base.
     */
    public void delete( PrivateTopic privateTopic ) throws DAOException {
        try {
            em.remove( em.merge( privateTopic ) );
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }
}
