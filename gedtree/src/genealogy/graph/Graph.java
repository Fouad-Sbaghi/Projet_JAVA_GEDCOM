package genealogy.graph;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import genealogy.exceptions.exceptfile.DuplicationErr;
import genealogy.exceptions.exceptfile.MissingEntityErr;
import genealogy.exceptions.exceptgraph.GenderErr;
import genealogy.exceptions.exceptgraph.CycleErr;
import genealogy.exceptions.exceptgraph.MissingLinkErr;

/**
 * Représente le graphe généalogique complet.
 * elle stocke tous les individus et toutes les familles.
 * Elle implémente la sérialisation pour permettre la sauvegarde de l'arbre sur le disque.
 */
public class Graph implements Serializable {

    private static final long serialVersionUID = 1L;

    /** * Stockage des individus.
     * La clé est l'ID GEDCOM et la valeur est l'objet individu.
     */
    private Map<String, Individual> individuals;

    /** * Stockage des familles.
     * La clé est l'ID GEDCOM et la valeur est l'objet famille.
     */
    private Map<String, Family> families;

    /**
     * Constructeur par défaut.
     * Initialise les structures de données (HashMap) pour stocker les entités.
     */
    public Graph() {
        this.individuals = new HashMap<>();
        this.families = new HashMap<>();
    }

    /**
     * Ajoute un nouvel individu au graphe.
     * Vérifie d'abord si l'identifiant n'est pas déjà présent pour éviter les doublons.
     * @param ind L'individu à ajouter.
     * @throws DuplicationErr Si un individu avec le même ID existe déjà dans le graphe.
     */
    public void addIndividual(Individual ind) throws DuplicationErr {
        if (individuals.containsKey(ind.getId())) {
            throw new DuplicationErr(ind.getId());
        }
        individuals.put(ind.getId(), ind);
    }

    /**
     * Ajoute une nouvelle famille au graphe.
     * Vérifie d'abord si l'identifiant n'est pas déjà présent pour éviter les doublons.
     * @param fam L'objet famille à ajouter.
     * @throws DuplicationErr Si une famille avec le même ID existe déjà dans le graphe.
     */
    public void addFamily(Family fam) throws DuplicationErr {
        if (families.containsKey(fam.getId())) {
            throw new DuplicationErr(fam.getId());
        }
        families.put(fam.getId(), fam);
    }

    /**
     * Récupère un individu à partir de son identifiant unique.
     * @param id L'identifiant GEDCOM de l'individu'.
     * @return L'objet individu correspondant, ou null si l'ID n'existe pas.
     */
    public Individual getIndividual(String id) {
        return individuals.get(id);
    }
    
    /**
     * Récupère une famille à partir de son identifiant unique.
     * @param id L'identifiant GEDCOM famille
     * @return L'objet famille correspondant, ou null si l'ID n'existe pas.
     */
    public Family getFamily(String id) {
        return families.get(id);
    }
    
    /**
     * Retourne la map complète des individus.
     * Utile pour parcourir tous les membres de l'arbre (ex: pour la recherche par nom).
     * @return La Map contenant tous les individus.
     */
    public Map<String, Individual> getIndividuals() { 
        return individuals; 
    }

    /**
     * Vérifie la cohérence du graphe.
     * Effectue les vérifications suivantes :
     * 1. Existence des familles parentales référencées.
     * 2. Symétrie des liens (Si A est enfant de Famille B, Famille B doit lister A).
     * 3. Cohérence des genres (HUSB = M, WIFE = F).
     * 4. Absence de cycles généalogiques (un enfant ne peut pas être son propre ancêtre).
     * * @throws MissingEntityErr Si une entité référencée est introuvable.
     * @throws GenderErr Si le sexe ne correspond pas au rôle.
     * @throws MissingLinkErr Si un lien n'est pas réciproque.
     * @throws CycleErr Si un cycle est détecté.
     */
    public void validate() throws MissingEntityErr, GenderErr, MissingLinkErr, CycleErr {
        
        // Vérifications centrées sur les individus
        for (Individual ind : individuals.values()) {
            
            // Vérification de la famille parentale (FAMC)
            if (ind.getFamc() != null) {
                Family fam = families.get(ind.getFamc());
                
                // Si la famille n'existe pas dans le graphe -> Erreur
                if (fam == null) {
                    throw new MissingEntityErr(ind.getId(), "Famille parentale " + ind.getFamc());
                }
                
                // Vérification de la symétrie du lien Enfant -> Famille
                if (!fam.getChildren().contains(ind.getId())) {
                    throw new MissingLinkErr(ind.getId(), fam.getId(), "Enfant -> Famille (L'enfant référence la famille, mais la famille ne le liste pas)");
                }
            }

            // Détection de Cycle
            // On vérifie qu'aucun ancêtre de cet individu n'est l'individu lui-même.
            if (hasCycle(ind, new java.util.HashSet<>())) {
                throw new CycleErr(ind.getId());
            }
        }

        // Vérifications centrées sur les FAMILLES
        for (Family fam : families.values()) {
            
            // Vérification du Mari (HUSB)
            if (fam.getHusb() != null) {
                Individual mari = individuals.get(fam.getHusb());
                if (mari != null) {
                    String sexe = mari.getTagValue("SEX");
                    if (sexe != null && !sexe.isEmpty() && !sexe.equals("M")) {
                        throw new GenderErr(mari.getId(), "HUSB (Mari)");
                    }
                } else {
                     // Si le mari est référencé mais n'existe pas
                     throw new MissingEntityErr(fam.getHusb(), "Mari de la famille " + fam.getId());
                }
            }

            // Vérification de la Femme (WIFE)
            if (fam.getWife() != null) {
                Individual femme = individuals.get(fam.getWife());
                if (femme != null) {
                    String sexe = femme.getTagValue("SEX");
                    if (sexe != null && !sexe.isEmpty() && !sexe.equals("F")) {
                        throw new GenderErr(femme.getId(), "WIFE (Femme)");
                    }
                } else {
                    // Si la femme est référencée mais n'existe pas
                    throw new MissingEntityErr(fam.getWife(), "Femme de la famille " + fam.getId());
                }
            }
            
            // Vérification de la symétrie Famille -> Enfant
            for (String childId : fam.getChildren()) {
                Individual child = individuals.get(childId);
                if (child == null) {
                    throw new MissingEntityErr(childId, "Enfant listé dans la famille " + fam.getId());
                }
                
                // Si l'enfant ne connait pas sa famille ou pointe vers une autre
                if (child.getFamc() == null || !child.getFamc().equals(fam.getId())) {
                     throw new MissingLinkErr(fam.getId(), childId, "Famille -> Enfant (La famille liste l'enfant, mais l'enfant ne référence pas cette famille)");
                }
            }
        }
    }

    /**
     * Méthode récursive pour détecter les cycles.
     * @param current L'individu en cours d'analyse.
     * @param visited La liste des IDs déjà visités dans cette branche de récursion.
     * @return true si un cycle est détecté.
     */
    private boolean hasCycle(Individual current, java.util.Set<String> visited) {
        // Si on retombe sur un ID déjà visité dans la branche courante, c'est un cycle.
        if (visited.contains(current.getId())) {
            return true;
        }

        // Si l'individu n'a pas de parents connus, la branche s'arrête (pas de cycle ici).
        if (current.getFamc() == null) {
            return false;
        }

        Family fam = families.get(current.getFamc());
        if (fam == null) return false; // Géré par MissingEntityErr ailleurs

        // On ajoute l'individu courant aux visités pour la suite de l'exploration
        visited.add(current.getId());

        // On remonte vers le père
        if (fam.getHusb() != null) {
            Individual father = individuals.get(fam.getHusb());
            if (father != null && hasCycle(father, new java.util.HashSet<>(visited))) {
                return true;
            }
        }

        // On remonte vers la mère
        if (fam.getWife() != null) {
            Individual mother = individuals.get(fam.getWife());
            if (mother != null && hasCycle(mother, new java.util.HashSet<>(visited))) {
                return true;
            }
        }

        return false;
    }
}