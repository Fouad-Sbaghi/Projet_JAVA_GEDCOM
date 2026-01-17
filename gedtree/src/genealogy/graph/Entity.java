package genealogy.graph;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import genealogy.interfaces.Identifiable;
import genealogy.interfaces.Displayable;
import genealogy.tags.Tag;
import genealogy.exceptions.exceptfile.DupTagErr;

/**
 * Classe abstraite représentant une entité GEDCOM.
 * Elle met en commun aux classes et Individual et Family.
 * Elle gère l'identifiant ainsi que la liste des tags associés à l'entité.
 */
public abstract class Entity implements Identifiable, Displayable, Serializable {

    private static final long serialVersionUID = 1L;

    /** * Identifiant unique de l'entité
     */
    protected String id;

    /** * Liste contenant tous les tags associés à cette entité.
     */
    protected List<Tag> properties; 

    /**
     * Constructeur de base.
     * Initialise l'identifiant et crée une liste vide pour les propriétés.
     * @param id L'identifiant unique de l'entité.
     */
    public Entity(String id) {
        this.id = id;
        this.properties = new ArrayList<>();
    }

    /**
     * Retourne l'identifiant unique de l'entité.
     * @return La chaîne de caractères représentant l'ID
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * Ajoute un Tag à la liste des propriétés de l'entité en vérifiant les règles de duplication.
     * Certains tags sont répétables (FAMS, CHIL, NOTE), d'autres doivent être uniques (SEX, BIRT, DEAT...).
     * Si on tente d'ajouter un tag unique qui existe déjà, une exception est levée.
     * </p>
     * @param tag Le tag à ajouter.
     * @throws DupTagErr Si le tag n'est pas répétable et qu'il est déjà présent dans l'entité.
     */
    public void addProperty(Tag tag) throws DupTagErr {
        String nom = tag.getName();
        // Liste blanche des tags autorisés à être en double
        boolean estRepetable = nom.equals("FAMS") || nom.equals("CHIL") || nom.equals("NOTE");

        if (!estRepetable) {
            // Vérification : est-ce que ce tag existe déjà ?
            for (Tag t : properties) {
                if (t.getName().equals(nom)) {
                    throw new DupTagErr(nom, "Entity " + id);
                }
            }
        }
        this.properties.add(tag);
    }

    /**
     * Méthode pour récupérer la valeur textuelle d'un tag simple (ex: "NAME", "SEX").
     * Parcourt la liste des propriétés pour trouver le premier tag correspondant.
     * @param tagName Le nom du tag recherché 
     * @return La valeur du tag si trouvé, sinon la chaîne "Unknown".
     */
    public String getTagValue(String tagName) {
        for (Tag t : properties) {
            if (t.getName().equals(tagName)) {
                // On utilise getValueOrNull() défini dans Tag et surchargé dans SimpleTag
                String value = t.getValueOrNull(); 
                if (value != null) {
                    return value;
                }
            }
        }
        return "Unknown";
    }

    /**
     * Affiche l'entité sur la sortie.
     * Affiche d'abord l'ID, puis demande récursivement à chaque propriété de s'afficher.
     * @param indentation Le niveau d'indentation actuel (pour l'affichage hiérarchique).
     */
    @Override
    public void display(int indentation) {
        System.out.println("ID: " + id);
        // Délégation de l'affichage à chaque tag enfant
        for (Tag t : properties) {
            t.display(indentation + 1);
        }
    }
}