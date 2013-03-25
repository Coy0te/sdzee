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
    private static final String SESSION_MEMBRE = "membre";

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter( ServletRequest request, ServletResponse response, FilterChain chain ) throws ServletException, IOException {
        HttpServletRequest req = (HttpServletRequest) request;
        Membre membre = (Membre) req.getSession().getAttribute( SESSION_MEMBRE );

        if ( membre != null ) {
            // Membre connecté, visite autorisée.
            chain.doFilter( request, response );
        } else {
            // Membre non connecté, redirection vers la page de connexion.
            HttpServletResponse res = (HttpServletResponse) response;
            res.sendRedirect( req.getContextPath() + "/connexion.xhtml" );
        }
    }

    @Override
    public void init( FilterConfig filterConfig ) throws ServletException {
    }

}
