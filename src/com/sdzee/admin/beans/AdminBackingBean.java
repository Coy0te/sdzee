package com.sdzee.admin.beans;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.component.menuitem.MenuItem;
import org.primefaces.component.submenu.Submenu;
import org.primefaces.model.DefaultMenuModel;
import org.primefaces.model.MenuModel;

import com.sdzee.forums.dao.CategorieForumsDao;
import com.sdzee.forums.dao.ForumDao;
import com.sdzee.forums.entities.CategorieForum;
import com.sdzee.forums.entities.Forum;

@ManagedBean( name = "adminBean" )
@ViewScoped
public class AdminBackingBean implements Serializable {
    private static final long    serialVersionUID = 1L;

    @EJB
    private ForumDao             forumDao;
    @EJB
    private CategorieForumsDao   categorieDao;

    private List<CategorieForum> categories;
    private List<Forum>          forums;
    private MenuModel            model;

    @PostConstruct
    public void init() {
        categories = categorieDao.lister();
        forums = forumDao.lister();

        model = new DefaultMenuModel();

        // Menu catégories
        Submenu submenu = new Submenu();
        submenu.setLabel( "Catégories" );

        MenuItem item = new MenuItem();
        item.setValue( "Créer" );
        item.setUrl( "#" );
        submenu.getChildren().add( item );

        item = new MenuItem();
        item.setValue( "Modifier" );
        item.setUrl( "#" );
        submenu.getChildren().add( item );

        item = new MenuItem();
        item.setValue( "Supprimer" );
        item.setUrl( "#" );
        submenu.getChildren().add( item );

        model.addSubmenu( submenu );

        // Menu forums
        submenu = new Submenu();
        submenu.setLabel( "Forums" );

        item = new MenuItem();
        item.setValue( "Créer" );
        item.setUrl( "#" );
        submenu.getChildren().add( item );

        item = new MenuItem();
        item.setValue( "Modifier" );
        item.setUrl( "#" );
        submenu.getChildren().add( item );

        item = new MenuItem();
        item.setValue( "Supprimer" );
        item.setUrl( "#" );
        submenu.getChildren().add( item );

        model.addSubmenu( submenu );

        // Menu membres
        submenu = new Submenu();
        submenu.setLabel( "Membres" );

        item = new MenuItem();
        item.setValue( "Modifier" );
        item.setUrl( "#" );
        submenu.getChildren().add( item );

        item = new MenuItem();
        item.setValue( "Supprimer" );
        item.setUrl( "#" );
        submenu.getChildren().add( item );

        model.addSubmenu( submenu );
    }

    public List<CategorieForum> getCategories() {
        return categories;
    }

    public List<Forum> getForums() {
        return forums;
    }

    public List<Forum> getForums( CategorieForum categorie ) {
        return forumDao.lister( categorie );
    }

    public MenuModel getModel() {
        return model;
    }

    public void creer() {
        addMessage( "Données créées." );
    }

    public void modifier() {
        addMessage( "Données mises à jour." );
    }

    public void supprimer() {
        addMessage( "Données supprimées." );
    }

    public void addMessage( String summary ) {
        FacesMessage message = new FacesMessage( FacesMessage.SEVERITY_INFO, summary, null );
        FacesContext.getCurrentInstance().addMessage( null, message );
    }

}