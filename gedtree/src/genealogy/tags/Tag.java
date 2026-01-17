package genealogy.tags;

import genealogy.interfaces.Displayable;

/**
 * Classe mère pour les Tags. Implémente les fonctions communes aux Tags simples et composés.
 */
public abstract class Tag implements Displayable {

    private String name; // Ex: "NAME"
    private int level;   // Ex: 1

    public Tag(String name, int level) {
        this.name = name;
        this.level = level;
    }

    /**
     * Méthode abstraite : chaque tag doit savoir lire son contenu.
     * @param content Le contenu.
     */
    public abstract void parse(String content);
    
    /**
     * Renvoie le nom du tag.
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Renvoie le niveau d'indentation du Tag.
     * @return
     */
    public int getLevel() {
        return level;
    }
    
    @Override
    public String toString() {
        return level + " " + name;
    }
    
    /**
     * Tente de retourner la valeur  du Tag. 
     * Renvois La valeur du Tag (String) si elle existe, ou null.
     */
    public  String getValueOrNull(){
    	return null;
    }
    
    @Override
    public abstract void display(int indentation);
}