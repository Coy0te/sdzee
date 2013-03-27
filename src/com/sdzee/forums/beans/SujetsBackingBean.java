package com.sdzee.forums.beans;

import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import com.sdzee.forums.dao.ForumDao;
import com.sdzee.forums.dao.ReponseDao;
import com.sdzee.forums.dao.SujetDao;
import com.sdzee.forums.entities.Reponse;
import com.sdzee.forums.entities.Sujet;

@ManagedBean( name = "sujetsBean" )
@RequestScoped
public class SujetsBackingBean implements Serializable {
    private static final long serialVersionUID = 1L;

    @EJB
    private SujetDao          sujetDao;
    @EJB
    private ForumDao          forumDao;
    @EJB
    private ReponseDao        reponseDao;

    public List<Sujet> getSujets( int forumId ) {
        return sujetDao.lister( forumDao.trouver( forumId ) );
    }

    public Reponse getDerniereReponse( Sujet sujet ) {
        return reponseDao.trouverDerniere( sujet );
    }

    public Integer getDecompteReponses( Sujet sujet ) {
        return reponseDao.decompte( sujet );
    }
}