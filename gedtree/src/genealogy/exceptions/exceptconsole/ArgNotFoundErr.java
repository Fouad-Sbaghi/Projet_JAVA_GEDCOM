package genealogy.exceptions.exceptconsole;

import genealogy.exceptions.GenealogyErr;

/**
 * Exception dans le cas d'un argument de commande non trouvé.
 */
public class ArgNotFoundErr extends GenealogyErr {
    /**
     * Levée si la cible d'une commande (un nom/ID) n'est pas trouvée dans le graphe.
     * @param target La personne cible.
     */
    public ArgNotFoundErr(String target) {
        super("Entité non trouvée : " + target + ". Veuillez vérifier l'orthographe.");
    }
}