package com.sdzee.forums.beans;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import com.sdzee.forums.dao.CategorieForumsDao;
import com.sdzee.forums.dao.ForumDao;
import com.sdzee.forums.dao.SujetDao;
import com.sdzee.forums.entities.CategorieForum;
import com.sdzee.forums.entities.Forum;
import com.sdzee.forums.entities.Sujet;

@ManagedBean( name = "forumsBean" )
@RequestScoped
public class ForumsBackingBean implements Serializable {
    private static final long    serialVersionUID = 1L;

    private List<CategorieForum> categories;

    @EJB
    private ForumDao             forumDao;
    @EJB
    private SujetDao             sujetDao;
    @EJB
    private CategorieForumsDao   categorieDao;

    // Récupération de la liste des catégories à l'initialisation du bean
    @PostConstruct
    public void init() {
        categories = categorieDao.lister();
    }

    public List<CategorieForum> getCategories() {
        return categories;
    }

    public List<Forum> getForums( CategorieForum categorie ) {
        return forumDao.lister( categorie );
    }

    public Sujet getDernierSujet( Forum forum ) {
        return sujetDao.trouverDernier( forum );
    }
}