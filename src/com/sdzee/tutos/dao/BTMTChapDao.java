package com.sdzee.tutos.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import com.sdzee.dao.DAOException;
import com.sdzee.tutos.entities.BTMTChap;
import com.sdzee.tutos.entities.Partie;

@Stateless
public class BTMTChapDao {
    private static final String JPQL_LISTE_CHAPITRES_PAR_PARTIES = "SELECT c FROM BTMTChap c WHERE c.partie=:partie ORDER BY c.position";
    private static final String JPQL_LISTE_BTMT                  = "SELECT btmt FROM BTMTChap btmt WHERE btmt.partie IS NULL ORDER BY btmt.id";
    private static final String PARAM_PARTIE                     = "partie";

    @PersistenceContext( unitName = "bdd_sdzee_PU" )
    private EntityManager       em;

    /* Enregistrement d'un nouveau bigtuto, minituto ou chapitre */
    public void creer( BTMTChap btmtchap ) throws DAOException {
        try {
            em.persist( btmtchap );
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /* Récupération d'un bigtuto, minituto ou chapitre via son id */
    public BTMTChap trouver( long id ) throws DAOException {
        try {
            return em.find( BTMTChap.class, id );
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /* Récupération de la liste des bigtutos et minitutos */
    public List<BTMTChap> lister() throws DAOException {
        try {
            TypedQuery<BTMTChap> query = em.createQuery( JPQL_LISTE_BTMT, BTMTChap.class );
            return query.getResultList();
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /* Récupération de la liste des chapitres pour une partie donnée triés selon leur position */
    public List<BTMTChap> lister( Partie partie ) throws DAOException {
        try {
            TypedQuery<BTMTChap> query = em.createQuery( JPQL_LISTE_CHAPITRES_PAR_PARTIES, BTMTChap.class );
            query.setParameter( PARAM_PARTIE, partie );
            return query.getResultList();
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /* Mise à jour d'un bigtuto, minituto ou chapitre */
    public void update( BTMTChap btmtchap ) throws DAOException {
        try {
            em.merge( btmtchap );
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /* Suppression d'un bigtuto, minituto ou chapitre */
    public void supprimer( BTMTChap btmtchap ) throws DAOException {
        try {
            em.remove( em.merge( btmtchap ) );
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }
}
