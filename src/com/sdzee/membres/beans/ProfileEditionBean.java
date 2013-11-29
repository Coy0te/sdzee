package com.sdzee.membres.beans;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import com.sdzee.breadcrumb.beans.BreadCrumbHelper;
import com.sdzee.breadcrumb.beans.BreadCrumbItem;
import com.sdzee.dao.DAOException;
import com.sdzee.membres.dao.MemberDao;
import com.sdzee.membres.entities.Member;

@ManagedBean
@ViewScoped
public class ProfileEditionBean implements Serializable {
    private static final long   serialVersionUID = 1L;
    private static final String SESSION_MEMBER   = "member";
    private static final int    STAFF_RIGHTS     = 3;
    private static final String URL_PROFILE_PAGE = "/profile.jsf?memberId=";
    private static final String URL_404          = "/404.jsf";

    private int                 memberId;
    private Member              member;
    private String              oldPassword;

    @EJB
    private MemberDao           memberDao;

    public void init() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        Member loggedInMember = (Member) externalContext.getSessionMap().get( SESSION_MEMBER );
        if ( loggedInMember == null ) {
            // si le visiteur n'est pas connecté, on le redirige
            try {
                externalContext.redirect( "connection.jsf" );
                return;
            } catch ( IOException e ) {
            }
        } else if ( loggedInMember.getId() != memberId && loggedInMember.getRights() < STAFF_RIGHTS ) {
            // si le visiteur est connecté mais n'est pas autorisé, on le redirige
            try {
                externalContext.redirect( "restricted.jsf" );
                return;
            } catch ( IOException e ) {
            }
        } else {
            // sinon tout va bien, et on récupère les infos du membre dont le profil doit être modifié
            member = memberDao.find( memberId );
        }
    }

    public String editProfile( Member viewMember ) {
        if ( viewMember != null
                && ( viewMember.getRights() >= 3 || viewMember.getNickName().equals( member.getNickName() ) ) ) {
            try {
                memberDao.update( member );
                return URL_PROFILE_PAGE + member.getId() + "&faces-redirect=true";
            } catch ( DAOException e ) {
                // TODO: logger l'échec de la mise à jour en base de la réponse
                return URL_404;
            }
        } else {
            // TODO: logger l'intrus qui essaie d'éditer un profil sans y être autorisé...
            return URL_404;
        }
    }

    public String editParameters( Member viewMember ) {
        // Si on arrive là, on est déjà passés par les différents validator JSF, donc tout est ok.
        // Modifications faisable uniquement par l'utilisateur, le Staff n'a pas à y avoir accès
        if ( viewMember != null && viewMember.getNickName().equals( member.getNickName() ) ) {
            try {
                memberDao.update( member );
                return URL_PROFILE_PAGE + member.getId() + "&faces-redirect=true";
            } catch ( DAOException e ) {
                // TODO: logger l'échec de la mise à jour en base de la réponse
                return URL_404;
            }
        } else {
            // TODO: logger l'intrus qui essaie d'éditer un profil sans y être autorisé...
            return URL_404;
        }
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

    public Member setMember() {
        return member;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword( String oldPassword ) {
        this.oldPassword = oldPassword;
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
        BreadCrumbHelper.addItem( breadCrumb, member.getNickName(), path + URL_PROFILE_PAGE + memberId );
        BreadCrumbHelper.addItem( breadCrumb, "Edition", null );
        return breadCrumb;
    }
}