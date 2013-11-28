package com.sdzee.privatemessages.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import com.sdzee.membres.entities.Member;
import com.sdzee.privatemessages.dao.PrivateNotificationDao;
import com.sdzee.privatemessages.entities.PrivateNotification;

/**
 * PrivateNotificationsBackingBean est le bean sur lequel s'appuie le header d'un member connecté pour lister les notifications. Il s'agit d'un
 * ManagedBean JSF, ayant pour portée une requête.
 * 
 * @author Médéric Munier
 * @version %I%, %G%
 */
@ManagedBean( name = "privateNotificationsBean" )
@RequestScoped
// TODO : pourquoi pas ViewScoped ?
public class PrivateNotificationsBackingBean implements Serializable {
    private static final long         serialVersionUID = 1L;
    private static final String       SESSION_MEMBER   = "member";

    private List<PrivateNotification> privateNotifications;

    @EJB
    private PrivateNotificationDao    privateNotificationDao;

    /**
     * Cette méthode vérifie si le visiteur accédant à une page est connecté. Si oui, elle va charger la liste des notifications du membre, et sinon
     * elle initialise une liste de notifications vide.
     * <p>
     * Elle est exécutée automatiquement par JSF, après le constructeur de la classe s'il existe. À l'appel du constructeur classique, le bean n'est
     * pas encore initialisé, et donc aucune dépendance n'est injectée. Cependant lorsque cette méthode est appelée, le bean est déjà initialisé et il
     * est donc possible de faire appel à des dépendances. Ici, c'est le DAO {@link PrivateNotificationDao} injecté via l'annotation <code>@EJB</code>
     * qui entre en jeu.
     * <p>
     * L'annotation <code>@PostConstruct</code> garantit que cette méthode ne sera appelée qu'une seule et unique fois durant le cycle de vie du bean,
     * c'est-à-dire une fois par requête, vue, session ou application selon le scope de définition du bean.
     */
    @PostConstruct
    public void init() {
        Member member = (Member) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
                .get( SESSION_MEMBER );
        if ( member != null ) {
            privateNotifications = privateNotificationDao.list( member );
        } else {
            privateNotifications = new ArrayList<PrivateNotification>();
        }
    }

    /**
     * Cette méthode donne accès à la liste des {@link PrivateNotification} chargée à l'initialisation du bean.
     * 
     * @return la liste des notifications, ou une liste vide si aucune notification n'est trouvée.
     */
    public List<PrivateNotification> getPrivateNotifications() {
        return privateNotifications;
    }
}