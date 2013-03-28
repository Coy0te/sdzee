package com.sdzee.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sdzee.membres.entities.Membre;

@WebFilter( "/admin/*" )
public class ConnexionRequiseFilter implements Filter {
    private static final String SESSION_MEMBRE      = "membre";
    // Rappel: visiteur < inscrit < modo < admin
    private static final int    DROITS_ADMIN        = 4;
    private static final String PARAM_URL_ORIGINE   = "?urlOrigine=";
    private static final String PAGE_CONNEXION      = "/connexion.jsf";
    private static final String PAGE_ACCES_INTERDIT = "/interdit.jsf";

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter( ServletRequest request, ServletResponse response, FilterChain chain ) throws ServletException, IOException {
        HttpServletRequest req = (HttpServletRequest) request;
        String urlOrigine = req.getRequestURI();
        Membre membre = (Membre) req.getSession().getAttribute( SESSION_MEMBRE );
        // Membre connecté
        if ( membre != null ) {
            // Membre admin
            if ( membre.getDroits() == DROITS_ADMIN ) {
                chain.doFilter( request, response );
            } else {
                // Membre non admin, redirection vers la page d'avertissement.
                HttpServletResponse res = (HttpServletResponse) response;
                res.sendRedirect( req.getContextPath() + PAGE_ACCES_INTERDIT );
            }
        } else {
            // Membre non connecté, redirection vers la page de connexion.
            HttpServletResponse res = (HttpServletResponse) response;
            if ( urlOrigine != null && !urlOrigine.isEmpty() ) {
                res.sendRedirect( req.getContextPath() + PAGE_CONNEXION + PARAM_URL_ORIGINE + urlOrigine );
            } else {
                res.sendRedirect( req.getContextPath() + PAGE_CONNEXION );
            }
        }
    }

    @Override
    public void init( FilterConfig filterConfig ) throws ServletException {
    }

}
