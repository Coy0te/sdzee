package com.sdzee.tutos.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import com.sdzee.dao.DAOException;
import com.sdzee.tutos.entities.Chapitre;
import com.sdzee.tutos.entities.Partie;

@Stateless
public class ChapitreDao {
    private static final String JPQL_LISTE_CHAPITRES_PAR_PARTIES = "SELECT c FROM Chapitre c WHERE c.partie=:partie ORDER BY c.id";
    private static final String PARAM_PARTIE                     = "partie";

    @PersistenceContext( unitName = "bdd_sdzee_PU" )
    private EntityManager       em;

    /* Enregistrement d'un nouveau chapitre */
    public void creer( Chapitre chapitre ) throws DAOException {
        try {
            em.persist( chapitre );
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /* Récupération d'un chapitre via son id */
    public Chapitre trouver( long id ) throws DAOException {
        try {
            return em.find( Chapitre.class, id );
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /* Récupération de la liste des chapitres pour une partie donnée */
    public List<Chapitre> lister( Partie partie ) throws DAOException {
        try {
            TypedQuery<Chapitre> query = em.createQuery( JPQL_LISTE_CHAPITRES_PAR_PARTIES, Chapitre.class );
            query.setParameter( PARAM_PARTIE, partie );
            return query.getResultList();
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /* Suppression d'un chapitre */
    public void supprimer( Chapitre chapitre ) throws DAOException {
        try {
            em.remove( em.merge( chapitre ) );
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }
}
