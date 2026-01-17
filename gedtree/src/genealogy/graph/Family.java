package genealogy.graph;

import java.util.ArrayList;
import java.util.List;

/**
 * Représente une famille.
 * Elle modélise la structure familiale en reliant
 * un époux (HUSB), une épouse (WIFE) et leurs enfants (CHIL).
 * Elle stocke uniquement les identifiants
 */
public class Family extends Entity {

    private static final long serialVersionUID = 1L;

    /** Identifiant de l'époux
     */
    private String husbId;

    /** * Identifiant de l'épouse 
     */
    private String wifeId;

    /** * Liste des identifiants des enfants issus de cette famille
     * Initialisée vide par défaut.
     */
    private List<String> childrenIds;

    /**
     * Constructeur d'une famille.
     * Initialise la liste des enfants.
     * @param id L'identifiant unique de la famille.
     */
    public Family(String id) {
        super(id); 
        this.childrenIds = new ArrayList<>(); 
    }

    /**
     * Définit l'identifiant du mari (HUSB).
     * @param id L'identifiant de l'individu.
     */
    public void setHusb(String id) { 
        this.husbId = id; 
    }

    /**
     * Définit l'identifiant de la femme.
     * @param id L'identifiant de l'individu.
     */
    public void setWife(String id) { 
        this.wifeId = id; 
    }

    /**
     * Ajoute un enfant à la liste des enfants de cette famille.
     * @param id L'identifiant de l'enfant à ajouter.
     */
    public void addChild(String id) { 
        this.childrenIds.add(id); 
    }

    /**
     * Récupère l'identifiant du mari.
     * @return L'ID du mari (String) ou null s'il n'est pas défini.
     */
    public String getHusb() { 
        return husbId; 
    }

    /**
     * Récupère l'identifiant de la femme.
     * @return L'ID de la femme (String) ou null s'il n'est pas défini.
     */
    public String getWife() { 
        return wifeId; 
    }

    /**
     * Récupère la liste des identifiants de tous les enfants de cette famille.
     * @return Une liste de Strings (IDs des enfants). La liste est vide s'il n'y a pas d'enfants.
     */
    public List<String> getChildren() { 
        return childrenIds; 
    }
}