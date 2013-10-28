package com.sdzee.tutos.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import com.sdzee.dao.DAOException;
import com.sdzee.tutos.entities.QCMQuestion;
import com.sdzee.tutos.entities.QCMReponse;

@Stateless
public class QCMReponseDao {
    private static final String JPQL_LISTE_REPONSES_PAR_QUESTION = "SELECT r FROM QCMReponse r WHERE r.question=:question ORDER BY r.id";
    private static final String PARAM_QUESTION                   = "question";

    @PersistenceContext( unitName = "bdd_sdzee_PU" )
    private EntityManager       em;

    /* Enregistrement d'une nouvelle réponse */
    public void creer( QCMReponse reponse ) throws DAOException {
        try {
            em.persist( reponse );
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /* Récupération d'une réponse via son id */
    public QCMReponse trouver( long id ) throws DAOException {
        try {
            return em.find( QCMReponse.class, id );
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /* Récupération de la liste des réponses pour une question donnée */
    public List<QCMReponse> lister( QCMQuestion question ) throws DAOException {
        try {
            TypedQuery<QCMReponse> query = em.createQuery( JPQL_LISTE_REPONSES_PAR_QUESTION, QCMReponse.class );
            query.setParameter( PARAM_QUESTION, question );
            return query.getResultList();
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /* Mise à jour d'une réponse */
    public void update( QCMReponse reponse ) throws DAOException {
        try {
            em.merge( reponse );
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    /* Suppression d'une question */
    public void supprimer( QCMReponse reponse ) throws DAOException {
        try {
            em.remove( em.merge( reponse ) );
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }
}
