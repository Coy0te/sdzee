package com.sdzee.admin.beans;

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

@ManagedBean( name = "adminMembersBean" )
@ViewScoped
@URLMapping( id = "adminMembers", pattern = "/admin/members/", viewId = "/admin/admin_membres.jsf" )
public class AdminMembresBackingBean implements Serializable {
    private static final long   serialVersionUID         = 1L;
    private static final String TITRE_PAGE_ADMIN         = "Administration";
    private static final String URL_PAGE_ADMIN           = "/admin/admin.jsf";
    private static final String TITRE_PAGE_ADMIN_MEMBRES = "Membres";

    @EJB
    private MemberDao           memberDao;

    private List<Member>        members;
    private List<Member>        filteredMembers;

    @PostConstruct
    public void init() {
        members = memberDao.list();
    }

    public List<Member> getMembers() {
        return members;
    }

    public List<Member> getFilteredMembers() {
        return filteredMembers;
    }

    public List<BreadCrumbItem> getBreadCrumb() {
        String path = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
        List<BreadCrumbItem> breadCrumb = BreadCrumbHelper.initBreadCrumb( path );
        BreadCrumbHelper.addItem( breadCrumb, TITRE_PAGE_ADMIN, path + URL_PAGE_ADMIN );
        BreadCrumbHelper.addItem( breadCrumb, TITRE_PAGE_ADMIN_MEMBRES, null );
        return breadCrumb;
    }
}