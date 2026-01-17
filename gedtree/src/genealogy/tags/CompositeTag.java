package genealogy.tags;

import java.util.ArrayList;
import java.util.List;
import genealogy.exceptions.exceptfile.DupTagErr;

/**
 * Classe abstraite mère de tous les tags qui servent de conteneur
 * Un CompositeTag possède une liste d'enfants (sous-tags)
 */
public abstract class CompositeTag extends Tag {

    /**
     * Liste qui contient les tags de niveau inférieur (enfants).
     * Les enfants peuvent être des SimpleTag ou d'autres CompositeTag.
     */
    private List<Tag> children; 

    /**
     * Constructeur.
     * On lui donne comme parametre le nom du tag
     * et son niveau pour permmetre de savoir si c'est un enfant.
     */
    public CompositeTag(String name, int level) {
        super(name, level);
        this.children = new ArrayList<>();
    }

    /**
     * Un tag composite n'a pas de contenu texte à parser sur sa propre ligne de niveau.
     */
    @Override
    public void parse(String content) {
        // Ne fait rien. Le contenu sera stocké dans les tags enfants
    }

    /**
     * Ajoute un sous-tag à la liste des enfants.
     * Cette méthode permet la non-répétition des tags.
     * Et si le tag existe deja alors il renvois une erreur
     */
    public void addChild(Tag child) throws DupTagErr {
        // Tags qui SONT répétables dans un composite (ex: NOTE, CONT, CONC)
        String nom = child.getName();
        boolean estRepetable = nom.equals("NOTE") || nom.equals("CONT") || nom.equals("CONC");
        
        if (!estRepetable) {
            // Vérification si le tag existe déjà
            for (Tag t : children) {
                if (t.getName().equals(nom)) {
                    throw new DupTagErr(nom, this.getName());
                }
            }
        }
        this.children.add(child);
    }
    
    /**
     * Renvois La liste des sous-tags de ce tag composite.
     */
    public List<Tag> getChildren() {
        return children;
    }

    /**
     * Affiche le tag et ses enfants de manière récursive.
     * L'indentation permet un meilleur affichage sur la console
     */
    @Override
    public void display(int indentation) {
        // Affiche le tag avec l'indentation
        for(int i=0; i<indentation; i++) System.out.print("  ");
        System.out.println(getName());
        
        // Appel récursif 
        for (Tag t : children) {
            t.display(indentation + 1);
        }
    }

    /**
     * Un CompositeTag n'a pas de valeur de lui-même (il est un conteneur).
     * Il renvois toujours un null.
     */
    @Override
    public String getValueOrNull() {
        return null; 
    }
}