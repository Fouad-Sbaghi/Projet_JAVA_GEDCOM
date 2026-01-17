package genealogy.exceptions.exceptfile;

import genealogy.exceptions.GenealogyErr;

/**
 * Exception dans le cas d'une entité évoquée par une autre entité mais manquante dans le graphe.
 */
public class MissingEntityErr extends GenealogyErr {

    private static final long serialVersionUID = 1L;

    public MissingEntityErr(String idManquant, String contexte) {
        super("Incohérence du graphe : L'entité " + idManquant + " est introuvable (" + contexte + ").");
    }
}