package genealogy.tags.simple;

import genealogy.tags.SimpleTag;

/**
 * Représente le Tag GEDCOM "DATE" (Date d'un événement).
 */
public class DateTag extends SimpleTag {

    /**
     * Constructeur.
     */
    public DateTag() {
        // Le niveau 2 est standard pour un sous-tag de BIRT/MARR
        super("DATE", 2); 
    }
}