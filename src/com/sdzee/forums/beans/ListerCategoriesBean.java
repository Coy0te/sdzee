package com.sdzee.forums.beans;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import com.sdzee.forums.dao.CategorieDao;
import com.sdzee.forums.entities.Categorie;

@ManagedBean
@RequestScoped
public class ListerCategoriesBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<Categorie>   categories;

    @EJB
    private CategorieDao      categorieDao;

    // Récupération de la liste des catégories
    @PostConstruct
    public void init() {
        categories = categorieDao.lister();
    }

    public List<Categorie> getCategories() {
        return categories;
    }
}