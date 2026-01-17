package genealogy.tags.simple;

import genealogy.tags.SimpleTag;

/**
 * Représente un Tag GEDCOM qui stocke simplement une chaîne de caractères.
 * Elle permet de ne pas créer une classe pour chaque tag simple.
 */
public class StringTag extends SimpleTag {
    
    /**
     * Constructeur. Permet de définir le nom et le niveau dynamiquement lors de l'instanciation.
     * @param name Le nom du tag.
     * @param level Le niveau d'indentation du tag.
     */
    public StringTag(String name, int level) {
        super(name, level);
    }
}