package com.sdzee.membres.beans;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.sdzee.breadcrumb.beans.BreadCrumbHelper;
import com.sdzee.breadcrumb.beans.BreadCrumbItem;
import com.sdzee.membres.dao.MemberDao;
import com.sdzee.membres.entities.Member;

@ManagedBean
@ViewScoped
@URLMapping( id = "members", pattern = "/members/", viewId = "/members.jsf" )
public class MembersBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<Member>      members;

    @EJB
    private MemberDao         memberDao;

    @PostConstruct
    public void init() {
        members = memberDao.list( 1, 50 );
    }

    public List<Member> getMembers( int pageNumber, int membersPerPage ) {
        return memberDao.list( pageNumber, membersPerPage );
    }

    public List<Member> getMembers() {
        return members;
    }

    public void setMembers( List<Member> members ) {
        this.members = members;
    }

    /**
     * Cette méthode génère le fil d'Ariane pour la page d'un membre donné.
     * 
     * @param memberId l'id du membre dont il faut générer le fil.
     * @return la liste des items à afficher dans le fil d'Ariane.
     */
    public List<BreadCrumbItem> getBreadCrumb() {
        String path = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
        List<BreadCrumbItem> breadCrumb = BreadCrumbHelper.initBreadCrumb( path );
        BreadCrumbHelper.addMembersItem( breadCrumb, path, false );
        return breadCrumb;
    }
}