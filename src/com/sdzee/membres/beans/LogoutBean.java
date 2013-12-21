package com.sdzee.membres.beans;

import java.io.IOException;
import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

@ManagedBean
@RequestScoped
public class LogoutBean implements Serializable {
    private static final long   serialVersionUID = 1L;
    private static final String PAGE_ACCUEIL     = "/";

    public void logout() throws IOException {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        externalContext.invalidateSession();
        externalContext.redirect( externalContext.getRequestContextPath() + PAGE_ACCUEIL );
    }
}