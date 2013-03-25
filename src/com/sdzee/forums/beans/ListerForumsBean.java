package com.sdzee.forums.beans;

import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import com.sdzee.forums.dao.ForumDao;
import com.sdzee.forums.entities.CategorieForum;
import com.sdzee.forums.entities.Forum;

@ManagedBean
@RequestScoped
public class ListerForumsBean implements Serializable {
    private static final long serialVersionUID = 1L;

    @EJB
    private ForumDao          forumDao;

    public List<Forum> getForumsParCategorie( CategorieForum categorie ) {
        return forumDao.lister( categorie );
    }
}