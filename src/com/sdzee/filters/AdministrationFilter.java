package com.sdzee.filters;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.omnifaces.filter.HttpFilter;

import com.sdzee.membres.entities.Member;

/**
 * AdministrationFilter est le filtre de limitation d'accès à l'administration du site. Il contrôle toute URL suivant le pattern <code>/admin/*</code>
 * .
 * 
 * @author Médéric Munier
 * @version %I%, %G%
 */
@WebFilter( "/admin/*" )
public class AdministrationFilter extends HttpFilter {
    private static final String SESSION_MEMBER         = "member";
    // Rappel: visiteur < membre < staff < admin
    private static final int    ADMIN_RIGHTS           = 4;
    private static final String PARAM_URL_ORIGINE      = "?urlOrigine=";
    private static final String PAGE_CONNECTION        = "/connection.jsf";
    private static final String PAGE_RESTRICTED_ACCESS = "/restricted.jsf";

    @Override
    public void doFilter( HttpServletRequest request, HttpServletResponse response, HttpSession session,
            FilterChain chain ) throws ServletException, IOException {
        String urlOrigine = request.getRequestURI();
        Member member = (Member) request.getSession().getAttribute( SESSION_MEMBER );
        // Membre connecté
        if ( member != null ) {
            // Membre admin
            if ( member.getRights() == ADMIN_RIGHTS ) {
                chain.doFilter( request, response );
            } else {
                // Membre non admin, redirection vers la page d'avertissement.
                HttpServletResponse res = (HttpServletResponse) response;
                res.sendRedirect( request.getContextPath() + PAGE_RESTRICTED_ACCESS );
            }
        } else {
            // Membre non connecté, redirection vers la page de connexion.
            HttpServletResponse res = (HttpServletResponse) response;
            if ( urlOrigine != null && !urlOrigine.isEmpty() ) {
                res.sendRedirect( request.getContextPath() + PAGE_CONNECTION + PARAM_URL_ORIGINE + urlOrigine );
            } else {
                res.sendRedirect( request.getContextPath() + PAGE_CONNECTION );
            }
        }
    }
}
