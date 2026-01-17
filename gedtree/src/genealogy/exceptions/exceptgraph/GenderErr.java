package genealogy.exceptions.exceptgraph;

import genealogy.exceptions.GenealogyErr;

/**
 * Exception dans le cas d'une erreur de genre : un homme a le role WIFE, ou inversement.
 */
public class GenderErr extends GenealogyErr {
    /**
     * Levée si un Individual avec SEX M est listé comme WIFE, ou F comme HUSB.
     */
	/**
	 * Lève une erreur si un Individual est décrit comme une femme mais référencié comme mari
	 * dans la famille, et inversement.
	 * @param individualId L'identifiant de l'Individual.
	 * @param role Le rôle qui lui est attribué dans la famille, incompatible avec son sexe.
	 */
    public GenderErr(String individualId, String role) {
        super("Incohérence de genre : " + individualId + " est de genre incompatible avec le rôle de " + role + ".");
    }
}