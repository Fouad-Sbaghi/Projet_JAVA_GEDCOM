package genealogy.tags.simple;

import genealogy.tags.SimpleTag;

/**
 * Représente le Tag GEDCOM "TITL" (Titre ou Description du Fichier).
 */
public class FileTitleTag extends SimpleTag {

    /**
     * Constructeur.
     */
    public FileTitleTag() {
        super("TITL", 2); 
    }
}