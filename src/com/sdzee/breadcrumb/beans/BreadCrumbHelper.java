package com.sdzee.breadcrumb.beans;

import java.util.ArrayList;
import java.util.List;

public final class BreadCrumbHelper {
    private static final String TITRE_PAGE_ACCUEIL = "Accueil";
    private static final String URL_PAGE_ACCUEIL   = "/accueil.jsf";
    private static final String TITRE_PAGE_FORUMS  = "Forums";
    private static final String URL_PAGE_FORUMS    = "/categories.jsf";
    private static final String TITRE_PAGE_TUTOS   = "Tutoriels";
    private static final String URL_PAGE_TUTOS     = "/tutoriels.jsf";

    /**
     * Initialise le breadCrumb avec la page d'accueil
     * 
     * @param chemin Le contextPath de l'application.
     * @return Le breadCrumb initialisé avec la page d'accueil.
     */
    public static List<BreadCrumbItem> initBreadCrumb( String chemin ) {
        List<BreadCrumbItem> liste = new ArrayList<BreadCrumbItem>();
        BreadCrumbItem item = new BreadCrumbItem();
        item.setTitre( TITRE_PAGE_ACCUEIL );
        item.setUrl( chemin + URL_PAGE_ACCUEIL );
        liste.add( item );
        return liste;
    }

    /**
     * Réalise l'ajout de la page mère des forums au breadCrumb
     * 
     * @param liste Le breadCrumb à modifier.
     * @param chemin Le contextPath de l'application.
     * @param url Un booléen à true pour inclure l'URL de la page des forums, et à false sinon.
     */
    public static void addForumsItem( List<BreadCrumbItem> liste, String chemin, boolean url ) {
        BreadCrumbItem item = new BreadCrumbItem();
        item.setTitre( TITRE_PAGE_FORUMS );
        item.setUrl( url ? chemin + URL_PAGE_FORUMS : null );
        liste.add( item );
    }

    /**
     * Réalise l'ajout de la page mère des tutos au breadCrumb
     * 
     * @param liste Le breadCrumb à modifier.
     * @param chemin Le contextPath de l'application.
     * @param url Un booléen à true pour inclure l'URL de la page des forums, et à false sinon.
     */
    public static void addTutosItem( List<BreadCrumbItem> liste, String chemin, boolean url ) {
        BreadCrumbItem item = new BreadCrumbItem();
        item.setTitre( TITRE_PAGE_TUTOS );
        item.setUrl( url ? chemin + URL_PAGE_TUTOS : null );
        liste.add( item );
    }

    /**
     * Réalise l'ajout d'un item au breadCrumb
     * 
     * @param liste Le breadCrumb à modifier.
     * @param titreItem Le titre de l'item à ajouter.
     * @param urlItem L'url de l'item à ajouter.
     * */
    public static void addItem( List<BreadCrumbItem> liste, String titreItem, String urlItem ) {
        BreadCrumbItem item = new BreadCrumbItem();
        item.setTitre( titreItem );
        item.setUrl( urlItem );
        liste.add( item );
    }
}
