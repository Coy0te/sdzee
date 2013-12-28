package com.sdzee.forums.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import com.sdzee.forums.dao.NotificationDao;
import com.sdzee.forums.entities.Notification;
import com.sdzee.membres.entities.Member;

/**
 * NotificationsBackingBean est le bean sur lequel s'appuie la page d'un sujet de forum pour travailler sur les notifications. Il s'agit
 * d'un ManagedBean JSF, ayant pour portée une requête.
 * 
 * @author Médéric Munier
 * @version %I%, %G%
 */
@ManagedBean( name = "notificationsBean" )
@RequestScoped
// TODO : pourquoi pas ViewScoped ?
public class NotificationsBackingBean implements Serializable {
    private static final long   serialVersionUID          = 1L;
    private static final String SESSION_MEMBER            = "member";
    private static final double NB_NOTIFICATIONS_PER_PAGE = 5;

    private List<Notification>  paginatedNotifications;
    private int                 pagesNumber;
    private int                 page                      = 1;
    private int                 countNotifications        = 0;

    @EJB
    private NotificationDao     notificationDao;

    /**
     * Cette méthode vérifie si le visiteur accédant à une page est connecté. Si oui, elle va charger la liste des notifications du membre,
     * et sinon elle initialise une liste de notifications vide.
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
        if ( member != null ) {
            countNotifications = notificationDao.count( member );
            pagesNumber = (int) Math.ceil( countNotifications / NB_NOTIFICATIONS_PER_PAGE );
            paginatedNotifications = notificationDao.list( member, page, (int) NB_NOTIFICATIONS_PER_PAGE );
        } else {
            paginatedNotifications = new ArrayList<Notification>();// TODO : pourquoi ? rien pour un visiteur non membre, ça va tres bien a
                                                                   // priori...
        }
    }

    /**
     * Cette méthode donne accès à la liste des {@link Notification} chargée à l'initialisation du bean.
     * 
     * @return la liste des notifications, ou une liste vide si aucune notification n'est trouvée.
     */
    public List<Notification> getPaginatedNotifications() {
        return paginatedNotifications;
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

    public int getCountNotifications() {
        return countNotifications;
    }

    public void setCountNotifications( int countNotifications ) {
        this.countNotifications = countNotifications;
    }
}