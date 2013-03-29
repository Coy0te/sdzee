package com.sdzee.admin.beans;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.sdzee.membres.dao.MembreDao;
import com.sdzee.membres.entities.Membre;

@ManagedBean( name = "adminMembresBean" )
@ViewScoped
public class AdminMembresBackingBean implements Serializable {
    private static final long serialVersionUID = 1L;

    @EJB
    private MembreDao         membreDao;

    private List<Membre>      membres;
    private List<Membre>      filteredMembres;

    @PostConstruct
    public void init() {
        membres = membreDao.lister();
    }

    public List<Membre> getMembres() {
        return membres;
    }

    public List<Membre> getFilteredMembres() {
        return filteredMembres;
    }
}