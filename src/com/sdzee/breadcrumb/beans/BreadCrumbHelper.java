package com.sdzee.breadcrumb.beans;

import java.util.ArrayList;
import java.util.List;

public final class BreadCrumbHelper {
    private static final String TITRE_PAGE_ACCUEIL = "Accueil";
    private static final String URL_PAGE_ACCUEIL   = "/index.jsf";
    private static final String TITRE_PAGE_FORUMS  = "Forums";
    private static final String URL_PAGE_FORUMS    = "/forums.jsf";
    private static final String TITRE_PAGE_MPS     = "Messages Privés";
    private static final String URL_PAGE_MPS       = "/privateTopics.jsf";
    private static final String TITRE_PAGE_TUTOS   = "Tutoriels";
    private static final String URL_PAGE_TUTOS     = "/tutorials.jsf";
    private static final String TITRE_PAGE_MEMBRES = "Membres";
    private static final String URL_PAGE_MEMBRES   = "/members.jsf";

    /**
     * Initialise le breadCrumb avec la page d'accueil
     * 
     * @param path Le contextPath de l'application.
     * @return Le breadCrumb initialisé avec la page d'accueil.
     */
    public static List<BreadCrumbItem> initBreadCrumb( String path ) {
        List<BreadCrumbItem> list = new ArrayList<BreadCrumbItem>();
        BreadCrumbItem item = new BreadCrumbItem();
        item.setTitle( TITRE_PAGE_ACCUEIL );
        item.setUrl( path + URL_PAGE_ACCUEIL );
        list.add( item );
        return list;
    }

    /**
     * Réalise l'ajout de la page mère des forums au breadCrumb
     * 
     * @param list Le breadCrumb à modifier.
     * @param path Le contextPath de l'application.
     * @param url Un booléen à true pour inclure l'URL de la page des forums, et à false sinon.
     */
    public static void addForumsItem( List<BreadCrumbItem> list, String path, boolean url ) {
        BreadCrumbItem item = new BreadCrumbItem();
        item.setTitle( TITRE_PAGE_FORUMS );
        item.setUrl( url ? path + URL_PAGE_FORUMS : null );
        list.add( item );
    }

    /**
     * Réalise l'ajout de la page mère des MPs au breadCrumb
     * 
     * @param list Le breadCrumb à modifier.
     * @param path Le contextPath de l'application.
     * @param url Un booléen à true pour inclure l'URL de la page des MPs, et à false sinon.
     */
    public static void addPrivatesItem( List<BreadCrumbItem> list, String path, boolean url ) {
        BreadCrumbItem item = new BreadCrumbItem();
        item.setTitle( TITRE_PAGE_MPS );
        item.setUrl( url ? path + URL_PAGE_MPS : null );
        list.add( item );
    }

    /**
     * Réalise l'ajout de la page mère des tutos au breadCrumb
     * 
     * @param list Le breadCrumb à modifier.
     * @param path Le contextPath de l'application.
     * @param url Un booléen à true pour inclure l'URL de la page des tutos, et à false sinon.
     */
    public static void addTutosItem( List<BreadCrumbItem> list, String path, boolean url ) {
        BreadCrumbItem item = new BreadCrumbItem();
        item.setTitle( TITRE_PAGE_TUTOS );
        item.setUrl( url ? path + URL_PAGE_TUTOS : null );
        list.add( item );
    }

    /**
     * Réalise l'ajout de la page mère des membres au breadCrumb
     * 
     * @param list Le breadCrumb à modifier.
     * @param path Le contextPath de l'application.
     * @param url Un booléen à true pour inclure l'URL de la page des membres, et à false sinon.
     */
    public static void addMembersItem( List<BreadCrumbItem> list, String path, boolean url ) {
        BreadCrumbItem item = new BreadCrumbItem();
        item.setTitle( TITRE_PAGE_MEMBRES );
        item.setUrl( url ? path + URL_PAGE_MEMBRES : null );
        list.add( item );
    }

    /**
     * Réalise l'ajout d'un item au breadCrumb
     * 
     * @param list Le breadCrumb à modifier.
     * @param itemTitle Le titre de l'item à ajouter.
     * @param itemUrl L'url de l'item à ajouter.
     * */
    public static void addItem( List<BreadCrumbItem> list, String itemTitle, String itemUrl ) {
        BreadCrumbItem item = new BreadCrumbItem();
        item.setTitle( itemTitle );
        item.setUrl( itemUrl );
        list.add( item );
    }
}
