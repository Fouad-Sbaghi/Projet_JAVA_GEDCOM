package genealogy.exceptions.exceptgraph;

import genealogy.exceptions.GenealogyErr;

/**
 * Exception dans le cas où il existe un cycle dans le graphe.
 */
public class CycleErr extends GenealogyErr {
	/**
	 * Lève une erreur lorsq'un cycle est détecte (Ex : A est le père de B, qui est le père de A).
	 * @param idIndividu L'identifiant de l'individu de départ du cycle.
	 */
    public CycleErr(String idIndividu) {
        super("Cycle généalogique détecté pour l'individu : " + idIndividu);
    }
}