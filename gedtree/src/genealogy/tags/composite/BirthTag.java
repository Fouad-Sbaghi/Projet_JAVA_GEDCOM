package genealogy.tags.composite;

import genealogy.tags.CompositeTag;

/**
 * Représente le Tag GEDCOM "BIRT" (Naissance).
 */
public class BirthTag extends CompositeTag {

    /**
     * Constructeur. 
     */
    public BirthTag() {
        super("BIRT", 1); 
    }
}