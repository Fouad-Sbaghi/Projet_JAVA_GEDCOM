package genealogy.interfaces;

/**
 * Interface pour les entités du graphe (Individual, Family) qui possèdent
 * un identifiant GEDCOM unique (ex: "@I1@", "@F2@").
 */
public interface Identifiable {

    /**
     * Retourne l'identifiant unique de l'entité.
     * @return L'identifiant (String).
     */
    String getId();
}