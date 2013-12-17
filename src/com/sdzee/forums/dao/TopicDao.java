package com.sdzee.forums.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.sdzee.dao.DAOException;
import com.sdzee.forums.entities.Forum;
import com.sdzee.forums.entities.Topic;

/**
 * TopicDao est la classe DAO contenant les opérations CRUD réalisables sur la table des sujets d'un forum. Il s'agit d'un EJB Stateless dont la
 * structure s'appuie sur JPA et JPQL.
 * 
 * @author Médéric Munier
 * @version %I%, %G%
 */
@Stateless
public class TopicDao {
    private static final String JPQL_TOPICS_LIST             = "SELECT t FROM Topic t ORDER BY t.id";
    private static final String JPQL_COUNT_TOPICS_PER_FORUM  = "SELECT count(t) FROM Topic t WHERE t.forum=:forum ORDER BY t.id";
    private static final String JPQL_TOPICS_LIST_PER_FORUM   = "SELECT t FROM Topic t LEFT JOIN t.lastPost lp LEFT JOIN t.firstPost fp WHERE t.forum=:forum AND t.sticky=false ORDER BY COALESCE(lp.creationDate, fp.creationDate) DESC";
    private static final String JPQL_STICKIES_LIST_PER_FORUM = "SELECT t FROM Topic t WHERE t.forum=:forum AND t.sticky=true ORDER BY t.id DESC";
    private static final String PARAM_FORUM                  = "forum";

    @PersistenceContext( unitName = "bdd_sdzee_PU" )
    private EntityManager       em;

    /**
     * Cette méthode permet d'enregistrer un nouveau {@link Topic} en base.
     * 
     * @param topic le nouveau sujet à insérer en base.
     * @throws {@link DAOException} lorsqu'une erreur survient lors de l'opération en base.
     */
    public void create( Topic topic ) throws DAOException {
        try {
            em.persist( topic );
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /**
     * Cette méthode permet de chercher un {@link Topic} via son id.
     * 
     * @param id l'id du sujet à récupérer.
     * @return le sujet correspondant, ou <code>null</code> si aucun sujet n'est trouvé.
     * @throws {@link DAOException} lorsqu'une erreur survient lors de l'opération en base.
     */
    public Topic find( long id ) throws DAOException {
        try {
            return em.find( Topic.class, id );
        } catch ( Exception e ) {
            e.printStackTrace();
            throw new DAOException( e );
        }
    }

    /**
     * Cette méthode permet de récupérer le nombre de {@link Topic} pour un {@link Forum} donné.
     * 
     * @param forum le forum dans lequel effectuer le décompte.
     * @return le nombre de sujets correspondant.
     * @throws {@link DAOException} lorsqu'une erreur survient lors de l'opération en base.
     */
    public Integer count( Forum forum ) throws DAOException {
        try {
            Query query = em.createQuery( JPQL_COUNT_TOPICS_PER_FORUM );
            query.setParameter( PARAM_FORUM, forum );
            // TODO : quid du cas NoResultException ?
            return ( (Long) query.getSingleResult() ).intValue();
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /**
     * Cette méthode permet de lister tous les {@link Topic}.
     * 
     * @return la liste des sujets triés par id, ou <code>null</code> si aucun sujet n'est trouvé.
     * @throws {@link DAOException} lorsqu'une erreur survient lors de l'opération en base.
     */
    public List<Topic> list() throws DAOException {
        try {
            TypedQuery<Topic> query = em.createQuery( JPQL_TOPICS_LIST, Topic.class );
            return query.getResultList();
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /**
     * Cette méthode permet de lister les {@link Topic} d'un {@link Forum} de manière paginée, en excluant les sujets épinglés en Post-It.
     * 
     * @param forum le forum dans lequel effectuer la recherche.
     * @param pageNumber le numéro de la page de sujets à récupérer.
     * @param topicsPerPage le nombre de sujets à retourner pour la page donnée.
     * @return la liste des sujets triés par id par ordre décroissant, ou <code>null</code> si aucun sujet n'est trouvé.
     * @throws {@link DAOException} lorsqu'une erreur survient lors de l'opération en base.
     */
    public List<Topic> list( Forum forum, int pageNumber, int topicsPerPage ) throws DAOException {
        try {
            TypedQuery<Topic> query = em.createQuery( JPQL_TOPICS_LIST_PER_FORUM, Topic.class );
            query.setParameter( PARAM_FORUM, forum );
            query.setFirstResult( ( pageNumber - 1 ) * topicsPerPage );
            query.setMaxResults( topicsPerPage );
            return query.getResultList();
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /**
     * Cette méthode permet de lister les {@link Topic} épinglés en Post-It pour un {@link Forum} donné.
     * 
     * @param forum le forum dans lequel effectuer la recherche.
     * @return la liste des sujets épinglés dans un forum triés par id par ordre décroissant, ou <code>null</code> si aucun sujet n'est trouvé.
     * @throws {@link DAOException} lorsqu'une erreur survient lors de l'opération en base.
     */
    public List<Topic> listStickies( Forum forum ) throws DAOException {
        try {
            TypedQuery<Topic> query = em.createQuery( JPQL_STICKIES_LIST_PER_FORUM, Topic.class );
            query.setParameter( PARAM_FORUM, forum );
            return query.getResultList();
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /**
     * Cette méthode permet de mettre à jour un {@link Topic} en base.
     * 
     * @param topic le sujet à mettre à jour en base.
     * @throws {@link DAOException} lorsqu'une erreur survient lors de l'opération en base.
     */
    public void update( Topic topic ) throws DAOException {
        try {
            em.merge( topic );
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /**
     * Cette méthode permet de rafraîchir un {@link Topic} dans le modèle objet.
     * 
     * @param topic le sujet à rafraîchir dans le modèle objet.
     * @return le sujet fraîchement récupéré depuis la base.
     * @throws {@link DAOException} lorsqu'une erreur survient lors de l'opération en base.
     */
    public Topic refresh( Topic topic ) throws DAOException {
        try {
            Topic freshTopic = find( topic.getId() );
            em.refresh( freshTopic );
            return freshTopic;
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /**
     * Cette méthode permet de supprimer un {@link Topic} de la base.
     * 
     * @param topic le sujet à supprimer de la base.
     * @throws {@link DAOException} lorsqu'une erreur survient lors de l'opération en base.
     */
    public void delete( Topic topic ) throws DAOException {
        try {
            em.remove( em.merge( topic ) );
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }
}
