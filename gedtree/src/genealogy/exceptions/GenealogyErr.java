package genealogy.exceptions;

/**
 * Classe mère pour toutes les exceptions spécifiques à l'application de généalogie.
 * Hérite de Exception pour pouvoir être attrapée.
 */
public class GenealogyErr extends Exception {
	
	/**
	 * Constructeur d'une nouvelle exception, sert de base pour les exceptions plus spécifiques.
	 * @param message Le message à afficher lors du déclenchement de l'erreur.
	 */
    public GenealogyErr(String message) {
        super(message);
    }
}