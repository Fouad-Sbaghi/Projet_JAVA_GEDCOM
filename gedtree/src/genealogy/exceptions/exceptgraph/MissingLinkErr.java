package genealogy.exceptions.exceptgraph;

import genealogy.exceptions.GenealogyErr;

/**
 * Exception pour le cas d'un lien asymétrique entre deux entités.
 */
public class MissingLinkErr extends GenealogyErr {
	/**
	 * Lève une erreur s'il existe un lien venant d'un Entity A vers un Entity B,
	 * mais pas de B vers A.
	 * @param entityId1 L'identifiant de l'Entity A.
	 * @param entityId2 L'identifiant de l'Entity B.
	 * @param type Le type de lien (entre un enfant et une famille, entre conjoints, etc.).
	 */
    public MissingLinkErr(String entityId1, String entityId2, String type) {
        super("Lien incohérent : " + entityId1 + " et " + entityId2 + ". Lien " + type + " manquant.");
    }
}