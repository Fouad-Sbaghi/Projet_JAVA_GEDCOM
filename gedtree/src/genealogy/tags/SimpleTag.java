package genealogy.tags;

import genealogy.interfaces.Value;

/**
 * Classe abstraite mère de tous les tags qui représentent une valeur simple.
 */
public abstract class SimpleTag extends Tag implements Value {
	/** * La valeur du Tag	 */
    private String value;

    /**
     * Constructeur.
     * @param name Nom du tag
     * @param level Indentation
     */
    public SimpleTag(String name, int level) {
        super(name, level);
    }

    /**
     * Implémentation de la méthode de parsing.
     * Par défaut, stocke tout le contenu de la ligne comme valeur.
     */
    @Override
    public void parse(String content) {
        if (content != null) {
            this.value = content.trim(); // trim() permet de supprimer les espaces au début et à la fin d'une chaine
        } else {
            this.value = "";
        }
    }
    
    /**
     * Récupère la valeur du tag.
     */
    @Override
    public String getValue() {
        return value;
    }

    /**
     * Définit la valeur du tag.
     * @param value La valeur du tag.
     */
    @Override
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Affiche le nom du tag et sa valeur.
     * Ne fait pas d'appel récursif (c'est une feuille de l'arbre).
     */
    @Override
    public void display(int indentation) {
        // Affichage avec décalage
        for(int i=0; i<indentation; i++) System.out.print("  ");
        System.out.println(getName() + ": " + value);
    }
    
    /**
     * Implémentation du polymorphisme pour l'accès aux valeurs.
     * Et renvois la valeur du tag
     */
    @Override
    public String getValueOrNull() {
        return this.value; 
    }
}