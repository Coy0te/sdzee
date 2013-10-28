package com.sdzee.tutos.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import com.sdzee.dao.DAOException;
import com.sdzee.tutos.entities.Licence;

@Stateless
public class LicenceDao {
    private static final String JPQL_LISTE_LICENCES = "SELECT l FROM Licence l ORDER BY l.id";

    @PersistenceContext( unitName = "bdd_sdzee_PU" )
    private EntityManager       em;

    /* Enregistrement d'une nouvelle licence */
    public void creer( Licence licence ) throws DAOException {
        try {
            em.persist( licence );
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /* Récupération d'une licence via son id */
    public Licence trouver( long id ) throws DAOException {
        try {
            return em.find( Licence.class, id );
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /* Récupération de la liste des licences */
    public List<Licence> lister() throws DAOException {
        try {
            TypedQuery<Licence> query = em.createQuery( JPQL_LISTE_LICENCES, Licence.class );
            return query.getResultList();
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /* Mise à jour d'une licence */
    public void update( Licence licence ) throws DAOException {
        try {
            em.merge( licence );
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /* Suppression d'une licence */
    public void supprimer( Licence licence ) throws DAOException {
        try {
            em.remove( em.merge( licence ) );
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }
}
