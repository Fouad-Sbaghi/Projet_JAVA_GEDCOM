package genealogy.interfaces;

/**
 * Interface pour les objets qui doivent pouvoir s'afficher dans la console.
 * La méthode display est obligatoire pour tous les Tags, Individuals, et Families.
 */
public interface Displayable {

    /**
     * Affiche l'objet.
     * @param indentation Niveau de décalage pour visualiser la structure hiérarchique.
     */
    void display(int indentation);
}