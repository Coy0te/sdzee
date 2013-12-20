package com.sdzee.membres.beans;

import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.sdzee.breadcrumb.beans.BreadCrumbHelper;
import com.sdzee.breadcrumb.beans.BreadCrumbItem;
import com.sdzee.membres.dao.MemberDao;
import com.sdzee.membres.entities.Member;

@ManagedBean
@RequestScoped
@URLMapping( id = "profile", pattern = "/members/profile/#{memberId : profileBean.memberId}/", viewId = "/profile.jsf" )
public class ProfileBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private int               memberId;
    private Member            member;

    @EJB
    private MemberDao         memberDao;

    public void init() {
        member = memberDao.find( memberId );
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId( int memberId ) {
        this.memberId = memberId;
    }

    public Member getMember() {
        return member;
    }

    /**
     * Cette méthode génère le fil d'Ariane pour la page d'un membre donné.
     * 
     * @param memberId l'id du membre dont il faut générer le fil.
     * @return la liste des items à afficher dans le fil d'Ariane.
     */
    public List<BreadCrumbItem> getBreadCrumb( int memberId ) {
        String path = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
        List<BreadCrumbItem> breadCrumb = BreadCrumbHelper.initBreadCrumb( path );
        BreadCrumbHelper.addMembersItem( breadCrumb, path, true );
        BreadCrumbHelper.addItem( breadCrumb, member.getNickName(), null );
        return breadCrumb;
    }
}