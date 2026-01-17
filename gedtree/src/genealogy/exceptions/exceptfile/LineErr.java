package genealogy.exceptions.exceptfile;

import genealogy.exceptions.GenealogyErr;

/**
 * Exception dans le cas d'une ligne du fichier avec une erreur de syntaxe.
 */
public class LineErr extends GenealogyErr {

    private static final long serialVersionUID = 1L;

    /**
     * Constructeur pour une erreur liée à une ligne précise du fichier.
     * @param lineNum Le numéro de la ligne où l'erreur a eu lieu.
     * @param message Le détail de l'erreur.
     */
    public LineErr(int lineNum, String message) {
        // On formate le message pour qu'il soit clair dans la console
        super("Erreur de lecture (Ligne " + lineNum + ") : " + message);
    }
}