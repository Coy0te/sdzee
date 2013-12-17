package com.sdzee.admin.beans;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import com.sdzee.breadcrumb.beans.BreadCrumbHelper;
import com.sdzee.breadcrumb.beans.BreadCrumbItem;
import com.sdzee.dao.DAOException;
import com.sdzee.forums.dao.ForumCategoryDao;
import com.sdzee.forums.dao.ForumDao;
import com.sdzee.forums.entities.Forum;
import com.sdzee.forums.entities.ForumCategory;
import com.sdzee.membres.entities.Member;

@ManagedBean( name = "adminForumsBean" )
@RequestScoped
public class AdminForumsBackingBean implements Serializable {
    private static final long   serialVersionUID          = 1L;
    private static final String URL_PAGE_ADMIN_CATEGORIES = "/admin/admin_categories.jsf";
    private static final String URL_PAGE_ADMIN_FORUMS     = "/admin/admin_forums.jsf";
    private static final String URL_404                   = "/404.jsf";

    private List<ForumCategory> categories;
    private ForumCategory       category;
    private ForumCategory       categorieDeplacement;
    private Forum               forum;

    @EJB
    private ForumDao            forumDao;
    @EJB
    private ForumCategoryDao    categoryDao;

    // Récupération de la liste des catégories à l'initialisation du bean
    @PostConstruct
    public void init() {
        category = new ForumCategory();
        categorieDeplacement = new ForumCategory();
        forum = new Forum();
        categories = categoryDao.list();
    }

    public List<ForumCategory> getCategories() {
        return categories;
    }

    public void editCategory( Member member, ForumCategory category ) {
        if ( member != null && member.getRights() >= 4 ) {
            try {
                categoryDao.update( category );
                // TODO : logger la modification faite par l'admin (date, membre, changement)
            } catch ( DAOException e ) {
                // TODO: logger l'échec de la mise à jour en base de la réponse
                e.printStackTrace();
            }
        } else {
            // TODO: logger l'intrus qui essaie de jouer aux admins...
        }
    }

    public String editForum( Member member, Forum forum ) {
        if ( member != null && member.getRights() >= 4 ) {
            try {
                forumDao.update( forum );
                // TODO : logger la modification faite par l'admin (date, membre, changement)
                return URL_PAGE_ADMIN_FORUMS + "?faces-redirect=true";
            } catch ( DAOException e ) {
                // TODO: logger l'échec de la mise à jour en base de la réponse
                e.printStackTrace();
                return URL_404;
            }
        } else {
            // TODO: logger l'intrus qui essaie de jouer aux admins...
            return URL_404;
        }
    }

    public String moveForum( Member member, Forum forum, ForumCategory category ) {
        if ( member != null && member.getRights() >= 4 && categorieDeplacement != null
                && category != categorieDeplacement ) {
            try {
                int positionOrigine = forum.getPosition();
                forum.setCategory( categorieDeplacement );
                forum.setPosition( forumDao.list( categorieDeplacement ).size() + 1 );
                forumDao.update( forum );
                // tentative hasardeuse de refresh (ça a l'air de bien fonctionner)
                category.removeForum( forum );
                categoryDao.update( category );
                // KO ici : il faut refresh la categorie pour avoir une liste à jour
                for ( Forum f : category.getForums() ) {
                    if ( f.getPosition() > positionOrigine ) {
                        f.setPosition( f.getPosition() - 1 );
                        forumDao.update( f );
                    }
                }
                categorieDeplacement = new ForumCategory();
                // TODO : logger la modification faite par l'admin (date, membre, changement)
                return URL_PAGE_ADMIN_FORUMS + "?faces-redirect=true";
            } catch ( DAOException e ) {
                // TODO: logger l'échec de la mise à jour en base de la réponse
                return URL_404;
            }
        } else {
            // TODO: logger l'intrus qui essaie de jouer aux admins...
            return URL_PAGE_ADMIN_FORUMS + "?faces-redirect=true";
        }
    }

    public String createCategory( Member member ) {
        if ( member != null && member.getRights() >= 4 ) {
            try {
                category.setPosition( categoryDao.list().size() + 1 );
                categoryDao.create( category );
                // TODO : logger la modification faite par l'admin (date, membre, changement)
                return URL_PAGE_ADMIN_CATEGORIES + "?faces-redirect=true";
            } catch ( DAOException e ) {
                // TODO: logger l'échec de la mise à jour en base de la réponse
                return URL_404;
            }
        } else {
            // TODO: logger l'intrus qui essaie de jouer aux admins...
            return URL_404;
        }
    }

    public String createForum( Member member ) {
        if ( member != null && member.getRights() >= 4 ) {
            try {
                forum.setPosition( forumDao.list( forum.getCategory() ).size() + 1 );
                forumDao.create( forum );
                // TODO : logger la modification faite par l'admin (date, membre, changement)
                return URL_PAGE_ADMIN_FORUMS + "?faces-redirect=true";
            } catch ( DAOException e ) {
                // TODO: logger l'échec de la mise à jour en base de la réponse
                return URL_404;
            }
        } else {
            // TODO: logger l'intrus qui essaie de jouer aux admins...
            return URL_404;
        }
    }

    public String deleteCategory( Member member, ForumCategory category ) {
        if ( member != null && member.getRights() >= 4 ) {
            try {
                categoryDao.delete( category );
                // TODO : logger la modification faite par l'admin (date, membre, changement)
                return URL_PAGE_ADMIN_CATEGORIES + "?faces-redirect=true";
            } catch ( DAOException e ) {
                // TODO: logger l'échec de la mise à jour en base de la réponse
                return URL_404; // TODO : gérer le cas de la catégorie non-supprimable parce que non vide
            }
        } else {
            // TODO: logger l'intrus qui essaie de jouer aux admins...
            return URL_404;
        }
    }

    public String deleteForum( Member member, Forum forum, ForumCategory category ) {
        if ( member != null && member.getRights() >= 4 ) {
            try {
                int position = forum.getPosition();
                // C'est moche et gourmand, mais ça marche et on ne le fait que très rarement
                for ( Forum f : category.getForums() ) {
                    if ( f.getPosition() > position ) {
                        f.setPosition( f.getPosition() - 1 );
                        forumDao.update( f );
                    }
                }
                forumDao.delete( forum );
                // TODO : logger la modification faite par l'admin (date, membre, changement)
                return URL_PAGE_ADMIN_FORUMS + "?faces-redirect=true";
            } catch ( DAOException e ) {
                // TODO: logger l'échec de la mise à jour en base de la réponse
                return URL_404;
            }
        } else {
            // TODO: logger l'intrus qui essaie de jouer aux admins...
            return URL_404;
        }
    }

    public String moveUpCategory( Member member, ForumCategory category ) {
        if ( member != null && member.getRights() >= 4 ) {
            try {
                ForumCategory categorieDejaPositionnee = categoryDao.find( category.getPosition() - 1 );
                // Si aucune catégorie positionnée au dessus, alors ça ne sert à rien de faire un traitement
                if ( categorieDejaPositionnee != null ) {
                    categorieDejaPositionnee.setPosition( categorieDejaPositionnee.getPosition() + 1 );
                    categoryDao.update( categorieDejaPositionnee );
                    category.setPosition( category.getPosition() - 1 );
                    categoryDao.update( category );
                    return URL_PAGE_ADMIN_CATEGORIES + "?faces-redirect=true";
                }
                return null;
                // TODO : logger la modification faite par l'admin (date, membre, changement)
            } catch ( DAOException e ) {
                // TODO: logger l'échec de la mise à jour en base de la réponse
                return URL_404;
            }
        } else {
            // TODO: logger l'intrus qui essaie de jouer aux admins...
            return URL_404;
        }
    }

    public String moveDownCategory( Member member, ForumCategory category ) {
        if ( member != null && member.getRights() >= 4 ) {
            try {
                ForumCategory categorieDejaPositionnee = categoryDao.find( category.getPosition() + 1 );
                // Si aucune catégorie positionnée au dessus, alors ça ne sert à rien de faire un traitement
                if ( categorieDejaPositionnee != null ) {
                    categorieDejaPositionnee.setPosition( categorieDejaPositionnee.getPosition() - 1 );
                    categoryDao.update( categorieDejaPositionnee );
                    category.setPosition( category.getPosition() + 1 );
                    categoryDao.update( category );
                    return URL_PAGE_ADMIN_CATEGORIES + "?faces-redirect=true";
                }
                return null;
                // TODO : logger la modification faite par l'admin (date, membre, changement)
            } catch ( DAOException e ) {
                // TODO: logger l'échec de la mise à jour en base de la réponse
                return URL_404;
            }
        } else {
            // TODO: logger l'intrus qui essaie de jouer aux admins...
            return URL_404;
        }
    }

    public String moveUpForum( Member member, Forum forum ) {
        if ( member != null && member.getRights() >= 4 ) {
            try {
                Forum forumDejaPositionne = forumDao.find( forum.getCategory(), forum.getPosition() - 1 );
                // Si aucune catégorie positionnée au dessus, alors ça ne sert à rien de faire un traitement
                if ( forumDejaPositionne != null ) {
                    forumDejaPositionne.setPosition( forumDejaPositionne.getPosition() + 1 );
                    forumDao.update( forumDejaPositionne );
                    forum.setPosition( forum.getPosition() - 1 );
                    forumDao.update( forum );
                    return URL_PAGE_ADMIN_FORUMS + "?faces-redirect=true";
                }
                return null;
                // TODO : logger la modification faite par l'admin (date, membre, changement)
            } catch ( DAOException e ) {
                // TODO: logger l'échec de la mise à jour en base de la réponse
                return URL_404;
            }
        } else {
            // TODO: logger l'intrus qui essaie de jouer aux admins...
            return URL_404;
        }
    }

    public String moveDownForum( Member member, Forum forum ) {
        if ( member != null && member.getRights() >= 4 ) {
            try {
                Forum forumDejaPositionne = forumDao.find( forum.getCategory(), forum.getPosition() + 1 );
                // Si aucune catégorie positionnée au dessus, alors ça ne sert à rien de faire un traitement
                if ( forumDejaPositionne != null ) {
                    forumDejaPositionne.setPosition( forumDejaPositionne.getPosition() - 1 );
                    forumDao.update( forumDejaPositionne );
                    forum.setPosition( forum.getPosition() + 1 );
                    forumDao.update( forum );
                    return URL_PAGE_ADMIN_FORUMS + "?faces-redirect=true";
                }
                return null;
                // TODO : logger la modification faite par l'admin (date, membre, changement)
            } catch ( DAOException e ) {
                // TODO: logger l'échec de la mise à jour en base de la réponse
                return URL_404;
            }
        } else {
            // TODO: logger l'intrus qui essaie de jouer aux admins...
            return URL_404;
        }
    }

    public List<BreadCrumbItem> getBreadCrumb() {
        String path = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
        List<BreadCrumbItem> breadCrumb = BreadCrumbHelper.initBreadCrumb( path );
        BreadCrumbHelper.addForumsItem( breadCrumb, path, false );
        return breadCrumb;
    }

    public ForumCategory getCategory() {
        return category;
    }

    public void setCategory( ForumCategory category ) {
        this.category = category;
    }

    public Forum getForum() {
        return forum;
    }

    public void setForum( Forum forum ) {
        this.forum = forum;
    }

    public ForumCategory getCategorieDeplacement() {
        return categorieDeplacement;
    }

    public void setCategorieDeplacement( ForumCategory categorieDeplacement ) {
        this.categorieDeplacement = categorieDeplacement;
    }
}