package com.sdzee.forums.beans;

import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import com.sdzee.forums.dao.ForumDao;
import com.sdzee.forums.dao.SujetDao;
import com.sdzee.forums.entities.Forum;
import com.sdzee.forums.entities.Sujet;

@ManagedBean
@RequestScoped
public class ListerSujetsBean implements Serializable {
    private static final long serialVersionUID = 1L;

    @EJB
    private SujetDao          sujetDao;
    @EJB
    private ForumDao          forumDao;

    public List<Sujet> getSujetsParForum( int forumId ) {
        return sujetDao.lister( forumDao.trouver( forumId ) );
    }

    public Sujet getDernierSujetParForum( Forum forum ) {
        return sujetDao.trouverDernier( forum );
    }
}