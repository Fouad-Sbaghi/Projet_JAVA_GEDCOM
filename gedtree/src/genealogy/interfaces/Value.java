package genealogy.interfaces;

/**
 * Interface pour les Tags Simples qui contiennent une valeur de type String
 * (ex: le nom "Doe", le sexe "M", la date "1990").
 */
public interface Value {

    /**
     * Récupère la valeur du Tag.
     * @return La valeur sous forme de chaîne.
     */
    String getValue();

    /**
     * Définit la valeur du Tag.
     * @param value La valeur à stocker.
     */
    void setValue(String value);
}