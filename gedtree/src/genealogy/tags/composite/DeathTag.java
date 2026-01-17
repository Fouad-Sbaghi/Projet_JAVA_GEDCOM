package genealogy.tags.composite;

import genealogy.tags.CompositeTag;

/**
 * Représente le Tag GEDCOM "DEAT" (Décès).
 */
public class DeathTag extends CompositeTag {

    /**
     * Constructeur. 
     */
    public DeathTag() {
        super("DEAT", 1);
    }
}