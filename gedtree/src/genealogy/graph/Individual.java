package genealogy.graph;

import java.util.ArrayList;
import java.util.List;

/**
 * Représente un individu (Tag GEDCOM : INDI).
 * Cette classe hérite de la classe Entity, ce qui lui permet de posséder un ID unique
 * et une liste de tags.
 */
public class Individual extends Entity {

    private static final long serialVersionUID = 1L;

    /**
     * Identifiant de la famille où l'individu est un enfant.
     * Stocké sous forme de String.
     */
    private String famcId; 

    /**
     * Liste des identifiants des familles où l'individu est un époux/parent.
     * Une liste est utilisée car un individu peut avoir plusieurs conjoints.
     */
    private List<String> famsIds; 

    /**
     * Constructeur d'un individu.
     * Initialise les listes de familles pour éviter les erreurs de type NullPointerException.
     * * @param id L'identifiant unique de l'individu.
     */
    public Individual(String id) {
        super(id); 
        this.famsIds = new ArrayList<>(); 
        this.famcId = null;
    }
    
    /**
     * Identifiant de la famille où l'individu est un enfant.
     * Stocké sous forme de String .
     * * @param famcId L'identifiant de la famille parentale.
     */
    public void setFamc(String famcId) {
        this.famcId = famcId;
    }
    
    /**
     * Ajoute une famille où cet individu est parent/époux.
     * * @param famsId L'identifiant de la famille fondée.
     */
    public void addFams(String famsId) {
        this.famsIds.add(famsId);
    }
    
    /**
    *L'ID de la famille parentale peut être null si inconnue.
     * * @return L'identifiant de la famille null si inconnu.
     */
    public String getFamc() { 
        return famcId; 
    }

    /**
     *La liste des IDs des familles fondées (peut être vide).
     * * @return Une liste de Strings contenant les IDs des familles.
     */
    public List<String> getFams() { 
        return famsIds; 
    }

    /**
      * Redéfinission de l'affichage de l'individu.
     * "Nom Prenom (@ID@)"
     * * @return Une chaîne de caractères combinant le nom (tag NAME) et l'ID.
     */
    @Override
    public String toString() {
        // Utilise la méthode getTagValue de la classe mère Entity pour trouver le nom
        return getTagValue("NAME") + " (" + getId() + ")";
    }
}