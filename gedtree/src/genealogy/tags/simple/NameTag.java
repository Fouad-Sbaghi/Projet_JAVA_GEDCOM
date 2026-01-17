package genealogy.tags.simple;

import genealogy.tags.SimpleTag;

/**
 * Représente le Tag GEDCOM "NAME" (Nom complet).
 * Ce tag simple surcharge la méthode parse() de la classe mère pour nettoyer
 * la valeur et supprimer les barres obliques (ex: "/Dupont/Jean/" devient "Jean Dupont").
 */
public class NameTag extends SimpleTag {

    /**
     * Constructeur. Initialise le tag avec son nom et son niveau standard (1).
     */
    public NameTag() {
        super("NAME", 1);
    }

    /**
     * Surcharge la méthode de parsing pour nettoyer les noms GEDCOM.
     * Retire les slashs "/" autour du nom de famille.
     * @param content Le contenu textuel de la ligne GEDCOM.
     */
    @Override
    public void parse(String content) {
        // Appelle la méthode standard de SimpleTag pour le nettoyage de base
        super.parse(content); 
        
        // Logique spécifique : retire les /.../ autour du nom de famille
        String cleanValue = getValue().replaceAll("/", "");
        setValue(cleanValue.trim());
    }
}