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
import com.sdzee.forums.entities.Sujet;
import com.sdzee.membres.entities.Membre;

@Stateless
public class BookmarkDao {

    private static final String JPQL_SELECT_UNIQUE_BOOKMARK     = "SELECT b FROM Bookmark b WHERE b.idMembre=:idMembre AND b.idSujet=:idSujet";
    private static final String JPQL_LISTE_BOOKMARKS_PAR_MEMBRE = "SELECT b FROM Bookmark b WHERE b.idMembre=:idMembre";
    private static final String JPQL_LISTE_BOOKMARKS_PAR_SUJET  = "SELECT b FROM Bookmark b WHERE b.idSujet=:idSujet";
    private static final String PARAM_ID_MEMBRE                 = "idMembre";
    private static final String PARAM_ID_SUJET                  = "idSujet";

    @PersistenceContext( unitName = "bdd_sdzee_PU" )
    private EntityManager       em;

    /* Enregistrement d'un nouveau bookmark */
    public void creer( Bookmark bookmark ) throws DAOException {
        try {
            // Insertion uniquement si un bookmark n'existe pas déjà.
            if ( trouver( bookmark.getIdMembre(), bookmark.getIdSujet() ) == null ) {
                em.persist( bookmark );
            }
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /* Récupération de la liste des bookmarks pour un membre donné */
    public List<Bookmark> lister( Membre membre ) throws DAOException {
        try {
            TypedQuery<Bookmark> query = em.createQuery( JPQL_LISTE_BOOKMARKS_PAR_MEMBRE, Bookmark.class );
            query.setParameter( PARAM_ID_MEMBRE, membre.getId() );
            return query.getResultList();
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /* Récupération de la liste des bookmarks pour un sujet donné */
    public List<Bookmark> lister( Sujet sujet ) throws DAOException {
        try {
            TypedQuery<Bookmark> query = em.createQuery( JPQL_LISTE_BOOKMARKS_PAR_SUJET, Bookmark.class );
            query.setParameter( PARAM_ID_SUJET, sujet.getId() );
            return query.getResultList();
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /* Récupération du bookmark pour un membre et un sujet donnés */
    public Bookmark trouver( Long idMembre, Long idSujet ) throws DAOException {

        try {
            Query query = em.createQuery( JPQL_SELECT_UNIQUE_BOOKMARK );
            query.setParameter( PARAM_ID_MEMBRE, idMembre );
            query.setParameter( PARAM_ID_SUJET, idSujet );
            return (Bookmark) query.getSingleResult();
        } catch ( NoResultException e ) {
            // Si aucune bookmark trouvé, on retourne null.
            return null;
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /* Mise à jour d'un bookmark */
    public void update( Bookmark bookmark ) throws DAOException {

        try {
            em.merge( bookmark );
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /* Suppression d'un bookmark */
    public void supprimer( Bookmark bookmark ) throws DAOException {

        try {
            em.remove( em.merge( bookmark ) );
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }
}
