package com.sdzee.forums.beans;

import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import com.sdzee.forums.dao.ReponseDao;
import com.sdzee.forums.dao.SujetDao;
import com.sdzee.forums.entities.Reponse;
import com.sdzee.forums.entities.Sujet;

@ManagedBean
@RequestScoped
public class CreerReponseBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private Reponse           reponse;

    @EJB
    private ReponseDao        reponseDao;
    @EJB
    private SujetDao          sujetDao;

    public List<Reponse> getReponsesParSujet( int sujetId ) {
        return reponseDao.lister( sujetDao.trouver( sujetId ) );
    }

    public void repondre() {
        reponseDao.creer( reponse );
    }

    public Integer getDecompteReponsesParSujet( Sujet sujet ) {
        return reponseDao.decompte( sujet );
    }

    public Reponse getReponse() {
        return reponse;
    }

    public void setReponse( Reponse reponse ) {
        this.reponse = reponse;
    }
}