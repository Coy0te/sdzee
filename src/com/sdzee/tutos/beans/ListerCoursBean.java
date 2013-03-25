package com.sdzee.tutos.beans;

import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import com.sdzee.tutos.dao.CoursDao;
import com.sdzee.tutos.entities.CategorieCours;
import com.sdzee.tutos.entities.Cours;

@ManagedBean
@RequestScoped
public class ListerCoursBean implements Serializable {
    private static final long serialVersionUID = 1L;

    @EJB
    private CoursDao          coursDao;

    public List<Cours> getCoursParCategorie( CategorieCours categorie ) {
        return coursDao.lister( categorie );
    }
}