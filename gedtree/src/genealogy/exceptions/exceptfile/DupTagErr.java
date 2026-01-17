package genealogy.exceptions.exceptfile;

import genealogy.exceptions.GenealogyErr;

/**
 * Exception dans le cas de la répétition d'un Tag non répétable dans la description d'une entité.
 */
public class DupTagErr extends GenealogyErr {
	/**
	 * Lève une erreur si un Tag unique apparaît plusieurs fois à la même indentation dans une Entity ou un CompositeTag.
	 * @param tagName Le nom du Tag dupliqué dans le fichier.
	 * @param parentName Le nom de l'Entity ou TagComposite dans lequel le Tag est présent.
	 */
    public DupTagErr(String tagName, String parentName) {
        super("Le tag '" + tagName + "' ne peut pas être dupliqué dans l'entité/tag : " + parentName);
    }
}