package com.sdzee.membres.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.sdzee.dao.DAOException;
import com.sdzee.membres.entities.Member;

/**
 * MembreDao est la classe DAO contenant les opérations CRUD réalisables sur la table des membres du site. Il s'agit d'un EJB Stateless dont
 * la structure s'appuie sur JPA et JPQL.
 * 
 * @author Médéric Munier
 * @version %I%, %G%
 */
@Stateless
public class MemberDao {
    private static final String JPQL_MEMBERS_LIST      = "SELECT m FROM Member m ORDER BY m.id";
    private static final String JPQL_SELECT_BY_EMAIL   = "SELECT m FROM Member m WHERE m.email=:email";
    private static final String JPQL_SELECT_BY_PSEUDO  = "SELECT m FROM Member m WHERE m.nickName=:nickName";
    private static final String JPQL_SELECT_BY_PSEUDOS = "SELECT m FROM Member m WHERE m.nickName IN :nickNames";
    private static final String PARAM_EMAIL            = "email";
    private static final String PARAM_NICKNAME         = "nickName";
    private static final String PARAM_NICKNAMES        = "nickNames";

    @PersistenceContext( unitName = "bdd_sdzee_PU" )
    private EntityManager       em;

    /**
     * Cette méthode permet d'enregistrer un nouveau {@link Member} en base.
     * 
     * @param member le nouveau membre à insérer en base.
     * @throws {@link DAOException} lorsqu'une erreur survient lors de l'opération en base.
     */
    public void create( Member member ) throws DAOException {
        try {
            em.persist( member );
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /**
     * Cette méthode permet de chercher un {@link Member} via son id.
     * 
     * @param id l'id du membre à récupérer.
     * @return le membre correspondant, ou <code>null</code> si aucun membre n'est trouvé.
     * @throws {@link DAOException} lorsqu'une erreur survient lors de l'opération en base.
     */
    public Member find( long id ) throws DAOException {
        try {
            return em.find( Member.class, id );
        } catch ( Exception e ) {
            e.printStackTrace();
            throw new DAOException( e );
        }
    }

    /**
     * Cette méthode permet de lister tous les {@link Member}.
     * 
     * @return la liste des membres triés par id, ou <code>null</code> si aucun membre n'est trouvé.
     * @throws {@link DAOException} lorsqu'une erreur survient lors de l'opération en base.
     */
    public List<Member> list() throws DAOException {
        try {
            TypedQuery<Member> query = em.createQuery( JPQL_MEMBERS_LIST, Member.class );
            return query.getResultList();
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /**
     * Cette méthode permet de lister les {@link Member} du site de manière paginée.
     * 
     * @param pageNumber le numéro de la page de membres à récupérer.
     * @param membersPerPage le nombre de membres à retourner pour la page donnée.
     * @return la liste des membres triés par id par ordre décroissant, ou <code>null</code> si aucun membre n'est trouvé.
     * @throws {@link DAOException} lorsqu'une erreur survient lors de l'opération en base.
     */
    public List<Member> list( int pageNumber, int membersPerPage ) throws DAOException {
        try {
            TypedQuery<Member> query = em.createQuery( JPQL_MEMBERS_LIST, Member.class );
            query.setFirstResult( ( pageNumber - 1 ) * membersPerPage )
                    .setMaxResults( membersPerPage );
            return query.getResultList();
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /**
     * Cette méthode permet de chercher un {@link Member} via son email ou son pseudo.
     * 
     * @param parameter le paramètre sur lequel baser la recherche ("nickName" ou "email" uniquement).
     * @param value la valeur du paramètre sur lequel baser la recherche.
     * @return le membre correspondant, ou <code>null</code> si aucun membre n'est trouvé.
     * @throws {@link DAOException} lorsqu'une erreur survient lors de l'opération en base.
     */
    public Member find( String parameter, String value ) throws DAOException {
        Member membre = null;
        Query requete = null;
        if ( PARAM_NICKNAME.equals( parameter ) ) {
            requete = em.createQuery( JPQL_SELECT_BY_PSEUDO );
            requete.setParameter( PARAM_NICKNAME, value );
        } else {
            requete = em.createQuery( JPQL_SELECT_BY_EMAIL );
            requete.setParameter( PARAM_EMAIL, value );
        }
        try {
            membre = (Member) requete.getSingleResult();
        } catch ( NoResultException e ) {
            return null;
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
        return membre;
    }

    /**
     * Cette méthode permet de chercher une liste {@link Member} via son email ou son pseudo.
     * 
     * @param nickNames les pseudos des membres à récupérer.
     * @return la liste des membres correspondant, ou <code>null</code> si aucun membre n'est trouvé.
     * @throws {@link DAOException} lorsqu'une erreur survient lors de l'opération en base.
     */
    public List<Member> list( List<String> nickNames ) {
        try {
            TypedQuery<Member> query = em.createQuery( JPQL_SELECT_BY_PSEUDOS, Member.class );
            query.setParameter( PARAM_NICKNAMES, nickNames );
            return query.getResultList();
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /**
     * Cette méthode permet de mettre à jour un {@link Member} en base.
     * 
     * @param member le membre à mettre à jour en base.
     * @throws {@link DAOException} lorsqu'une erreur survient lors de l'opération en base.
     */
    public void update( Member member ) throws DAOException {
        try {
            em.merge( member );
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /**
     * Cette méthode permet de supprimer un {@link Member} de la base.
     * 
     * @param member le membre à supprimer de la base.
     * @throws {@link DAOException} lorsqu'une erreur survient lors de l'opération en base.
     */
    public void delete( Member member ) throws DAOException {
        try {
            em.remove( em.merge( member ) );
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }
}
