package com.sdzee.tutos.beans;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import com.sdzee.tutos.dao.CategorieCoursDao;
import com.sdzee.tutos.entities.CategorieCours;

@ManagedBean
@RequestScoped
public class ListerCategoriesCoursBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<CategorieCours>   categories;

    @EJB
    private CategorieCoursDao      categorieDao;

    // Récupération de la liste des catégories
    @PostConstruct
    public void init() {
        categories = categorieDao.lister();
    }

    public List<CategorieCours> getCategories() {
        return categories;
    }
}