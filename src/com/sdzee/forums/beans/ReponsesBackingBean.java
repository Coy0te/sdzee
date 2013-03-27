package com.sdzee.forums.beans;

import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import com.sdzee.forums.dao.ReponseDao;
import com.sdzee.forums.dao.SujetDao;
import com.sdzee.forums.entities.Reponse;
import com.sdzee.forums.entities.Sujet;

@ManagedBean( name = "reponsesBean" )
@ViewScoped
public class ReponsesBackingBean implements Serializable {
    private static final long   serialVersionUID     = 1L;
    private static final String HEADER_REQUETE_PROXY = "X-FORWARDED-FOR";

    private String              texteReponse;

    @EJB
    private ReponseDao          reponseDao;
    @EJB
    private SujetDao            sujetDao;

    public Sujet getSujet( int sujetId ) {
        return sujetDao.trouver( sujetId );
    }

    public List<Reponse> getReponses( int sujetId ) {
        return reponseDao.lister( sujetDao.trouver( sujetId ) );
    }

    public void repondre() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
                .getRequest();
        String adresseIP = request.getHeader( HEADER_REQUETE_PROXY );
        if ( adresseIP == null ) {
            adresseIP = request.getRemoteAddr();
        }

        // TODO: reponseDao.creer( texteReponse, adresseIP );
    }

    public String getTexteReponse() {
        return texteReponse;
    }

    public void setTexteReponse( String texteReponse ) {
        this.texteReponse = texteReponse;
    }
}