package com.sdzee.forums.beans;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import com.sdzee.forums.dao.CategorieForumsDao;
import com.sdzee.forums.entities.CategorieForum;

@ManagedBean
@RequestScoped
public class ListerCategoriesForumsBean implements Serializable {
    private static final long    serialVersionUID = 1L;

    private List<CategorieForum> categories;

    @EJB
    private CategorieForumsDao   categorieDao;

    // Récupération de la liste des catégories
    @PostConstruct
    public void init() {
        categories = categorieDao.lister();
    }

    public List<CategorieForum> getCategories() {
        return categories;
    }
}