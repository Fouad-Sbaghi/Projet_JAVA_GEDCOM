package genealogy.exceptions.exceptconsole;

import genealogy.exceptions.GenealogyErr;

/**
 * Exception pour le cas d'une commande inscrite non reconnue.
 */
public class CommandErr extends GenealogyErr {
    /**
     * Lève une erreur si l'utilisateur saisit une commande qui n'est pas reconnue.
     * @param command La commande entrée par l'utilisateur.
     */
    public CommandErr(String command) {
        super("Commande inconnue : '" + command + "'.");
    }
}