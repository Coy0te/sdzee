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
import com.sdzee.membres.entities.Membre;

@ManagedBean( name = "notificationsBean" )
@RequestScoped
public class NotificationsBackingBean implements Serializable {
    private static final long   serialVersionUID = 1L;

    private static final String SESSION_MEMBRE   = "membre";

    private List<Notification>  notifications;

    @EJB
    private NotificationDao     notificationDao;

    // Récupération de la liste des notifications à l'initialisation du bean
    @PostConstruct
    public void init() {
        Membre membre = (Membre) FacesContext.getCurrentInstance().getExternalContext()
                .getSessionMap()
                .get( SESSION_MEMBRE );
        if ( membre != null ) {
            notifications = notificationDao.lister( membre );
        } else {
            notifications = new ArrayList<Notification>();
        }
        System.out.println( "Notifs du membre >>> " + notifications );
    }

    public List<Notification> getNotifications() {
        return notifications;
    }
}