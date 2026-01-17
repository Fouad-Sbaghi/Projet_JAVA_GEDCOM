package genealogy.tags.composite;

import genealogy.tags.CompositeTag;

/**
 * Représente le Tag GEDCOM "OBJE" (Objet Multimédia).
 * Ce tag composite sert à lier des fichiers externes (photos, documents) 
 * à l'individu ou à un événement. Il contient les tags enfants FILE et TITLE.
 */
public class ObjectTag extends CompositeTag {

    /**
     * Constructeur.
     */
    public ObjectTag() {
        super("OBJE", 1); 
    }
}