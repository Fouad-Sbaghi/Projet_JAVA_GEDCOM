package genealogy.tags.simple;

import genealogy.tags.SimpleTag;

/**
 * Représente le Tag GEDCOM "FILE" (Chemin ou URL du Fichier Multimédia).
 */
public class FileURLTag extends SimpleTag {

    /**
     * Constructeur.
     */
    public FileURLTag() {
        super("FILE", 2);
    }
}