package com.sdzee.forums.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import com.sdzee.forums.dao.AlertDao;
import com.sdzee.forums.dao.NotificationDao;
import com.sdzee.forums.entities.Alert;
import com.sdzee.membres.entities.Member;

/**
 * AlertsBackingBean est le bean sur lequel s'appuie le header d'un staff pour lister les alertes. Il s'agit d'un ManagedBean JSF, ayant
 * pour portée une requête.
 * 
 * @author Médéric Munier
 * @version %I%, %G%
 */
@ManagedBean( name = "alertsBean" )
@RequestScoped
// TODO : pourquoi pas ViewScoped ?
public class AlertsBackingBean implements Serializable {
    private static final long   serialVersionUID   = 1L;
    private static final String SESSION_MEMBER     = "member";
    private static final double NB_ALERTS_PER_PAGE = 5;

    private List<Alert>         paginatedAlerts;
    private int                 pagesNumber;
    private int                 page               = 1;
    private int                 countAlerts        = 0;

    @EJB
    private AlertDao            alertDao;

    /**
     * Cette méthode vérifie si le visiteur accédant à une page est connecté et est un staff. Si oui, elle va charger la liste des alertes
     * en cours sur le forum, et sinon elle initialise une liste d'alertes vide.
     * <p>
     * Elle est exécutée automatiquement par JSF, après le constructeur de la classe s'il existe. À l'appel du constructeur classique, le
     * bean n'est pas encore initialisé, et donc aucune dépendance n'est injectée. Cependant lorsque cette méthode est appelée, le bean est
     * déjà initialisé et il est donc possible de faire appel à des dépendances. Ici, c'est le DAO {@link NotificationDao} injecté via
     * l'annotation <code>@EJB</code> qui entre en jeu.
     * <p>
     * L'annotation <code>@PostConstruct</code> garantit que cette méthode ne sera appelée qu'une seule et unique fois durant le cycle de
     * vie du bean, c'est-à-dire une fois par requête, vue, session ou application selon le scope de définition du bean.
     */
    @PostConstruct
    public void init() {
        Member member = (Member) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
                .get( SESSION_MEMBER );
        if ( member != null && member.getRights() >= 3 ) {
            countAlerts = alertDao.count();
            pagesNumber = (int) Math.ceil( countAlerts / NB_ALERTS_PER_PAGE );
            paginatedAlerts = alertDao.list( page, (int) NB_ALERTS_PER_PAGE );
        } else {
            paginatedAlerts = new ArrayList<Alert>();
        }
    }

    /**
     * Cette méthode donne accès à la liste des {@link Alert} chargée à l'initialisation du bean.
     * 
     * @return la liste des alertes, ou une liste vide si aucune alerte n'est trouvée.
     */
    public List<Alert> getPaginatedAlerts() {
        return paginatedAlerts;
    }

    public int getPage() {
        return page;
    }

    public void setPage( int page ) {
        this.page = page;
    }

    public int getPagesNumber() {
        return pagesNumber;
    }

    public int getCountAlerts() {
        return countAlerts;
    }

    public void setCountAlerts( int countAlerts ) {
        this.countAlerts = countAlerts;
    }
}