package com.sdzee.tutos.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import com.sdzee.dao.DAOException;
import com.sdzee.tutos.entities.BTMTChap;
import com.sdzee.tutos.entities.QCMQuestion;
import com.sdzee.tutos.entities.SousPartie;

@Stateless
public class QCMQuestionDao {
    private static final String JPQL_LISTE_QUESTIONS_PAR_CHAPITRE = "SELECT q FROM QCMQuestion q WHERE q.chapitre=:chapitre ORDER BY q.position";
    private static final String PARAM_CHAPITRE                    = "chapitre";

    @PersistenceContext( unitName = "bdd_sdzee_PU" )
    private EntityManager       em;

    /* Enregistrement d'une nouvelle question */
    public void creer( QCMQuestion question ) throws DAOException {
        try {
            em.persist( question );
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /* Récupération d'une question via son id */
    public SousPartie trouver( long id ) throws DAOException {
        try {
            return em.find( SousPartie.class, id );
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /* Récupération de la liste des questions pour un chapitre donné triées selon leur position */
    public List<QCMQuestion> lister( BTMTChap chapitre ) throws DAOException {
        try {
            TypedQuery<QCMQuestion> query = em.createQuery( JPQL_LISTE_QUESTIONS_PAR_CHAPITRE, QCMQuestion.class );
            query.setParameter( PARAM_CHAPITRE, chapitre );
            return query.getResultList();
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /* Mise à jour d'une question */
    public void update( QCMQuestion question ) throws DAOException {
        try {
            em.merge( question );
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /* Suppression d'une question */
    public void supprimer( QCMQuestion question ) throws DAOException {
        try {
            em.remove( em.merge( question ) );
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }
}
