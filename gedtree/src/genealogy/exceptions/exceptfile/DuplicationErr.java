package genealogy.exceptions.exceptfile;

import genealogy.exceptions.GenealogyErr;

/**
 * Exception dans le cas d'une entité en double dans le graphe.
 */
public class DuplicationErr extends GenealogyErr {
	/**
	 * Lève une erreur si une nouvelle instance Individuel ou Family a un identifiant déjà existant dans le graphe.
	 * @param id L'identifiant déjà utilisé.
	 */
    public DuplicationErr(String id) {
        super("L'identifiant '" + id + "' est déjà utilisé.");
    }
}