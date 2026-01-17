package genealogy.console;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

import genealogy.graph.Graph;
import genealogy.graph.Individual;
import genealogy.graph.Family;
import genealogy.parsing.Parser;
import genealogy.exceptions.GenealogyErr;
import genealogy.exceptions.exceptconsole.CommandErr;
import genealogy.exceptions.exceptconsole.ArgNotFoundErr;


/**
 * Génère une interface utilisateur pour taper des commandes.
 */
public class Console {

	/** * graph généré à partir du fichier	 */
    private Graph graph;
    /** * création d'un buffer     */
    private BufferedReader buffer; 
    
    /**
     * Constructeur.
     * @param graph Instance de la classe Graph/
     */
    public Console(Graph graph) {
        this.graph = graph;
        // Configuration standard pour lire le clavier (System.in) avec un Buffer
        this.buffer = new BufferedReader(new InputStreamReader(System.in));
    }

    /**
     * Démarre l'interface graphique, boucle principale du programme.
     */
    public void start() {
        System.out.println("============= GedTree =============\n");
        System.out.println("Commandes disponibles :");
        System.out.println(" - LOAD <fichier.ged>     : Charger un arbre");
        System.out.println(" - INFO <id ou nom>       : Afficher les infos complètes d'une personne");
        System.out.println(" - CHILD <id ou nom>      : Afficher les enfants");
        System.out.println(" - SIBLINGS <id ou nom>   : Afficher les frères et soeurs");
        System.out.println(" - FAMC <id ou nom>       : Afficher la famille parentale");
        System.out.println(" - <Nom1> MARRIED <Nom2>  : Vérifier si deux personnes sont mariées");
        System.out.println(" - SAVE <fichier.ser>     : Sauvegarder le graphe (Sérialisation)");
        System.out.println(" - EXIT                   : Quitter");
        System.out.println("\n===================================");

        boolean running = true;
        while (running) {
            System.out.print("\n> ");
            
            String line = null;
            try {
                // Lecture via le buffer lorsque l'utilisateur appuie sur la touche Entrée.
                line = buffer.readLine();
                
            } catch (IOException e) { // Dans le cas où l'accès au clavier échoue.
                System.out.println("Erreur de lecture entrée clavier : " + e.getMessage());
                continue; // On recommence la boucle.
            }

            line = line.trim(); // Retire les espaces inutiles avant et après le texte.
            if (line.isEmpty()) continue; // Si la ligne est vide, on recommence (nouvelle itération de while(running)).

            // Si la ligne contient " MARRIED " (avec espaces), on la traite à part
            // car elle doit être découpée différemment des autres.
            // Les autres commandes sont traitées plus bas, dans le bloc switch-case.
            if (line.toUpperCase().contains(" MARRIED ")) {
                try {
                    checkMarried(line);
                } catch (Exception e) {
                    System.out.println("ERREUR : " + e.getMessage());
                }
                continue; // On passe à la boucle suivante, on ne traite pas le switch habituel
            }

            // Découpage de la commande en 2 morceaux dans le tableau parts :
            // parts[0] contient la partie de la ligne avant le premier espace (la commande).
            // parts[1] contient la partie de la ligne après le premier espace (le(s) argument(s)).
            String[] parts = line.split(" ", 2);
            
            // Commande
            // toUpperCase() met la chaîne de caractère en majuscules.
            String command = parts[0].toUpperCase();
            
            // Argument(s)
            String arguments;
            if (parts.length > 1) arguments = parts[1];
            else arguments = "";
            
            try {
                switch (command) {
                	// Commande de sortie, le programme s'arrête.
                    case "EXIT":
                        running = false;
                        System.out.println("Au revoir.");
                        break;
                    // Commande de chargement de fichier, appel à la fonction loadFile(String file).
                    case "LOAD":
                        loadFile(arguments);
                        break;
                     // Commande de demande d'informations sur une personne, appel à la fonction showInfo(String person).
                    case "INFO":
                        showInfo(arguments);
                        break;
                    // Affiche les enfants d'une personne.
                    case "CHILD":
                        showChildren(arguments);
                        break;
                    // Affiche les frères et soeurs d'une personne.
                    case "SIBLINGS":
                        showSiblings(arguments);
                        break;
                    // Affiche les parents d'une personne.
                    case "FAMC":
                        showFamc(arguments);
                        break;
                    // Sauvegarde le graphe par sérialisation.
                    case "SAVE":
                        saveGraph(arguments);
                        break;

                    // Si la commande n'est pas connue, renvoie une erreur de l'exception personnalisée CommandErr.
                    default:
                        throw new CommandErr(command);
                }
            } catch (GenealogyErr e) {
                System.out.println("ERREUR : " + e.getMessage());
            } catch (Exception e) {
                System.out.println("ERREUR SYSTEME : " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * Remplit le graphe à partir du fichier donné en argument.
     * @param file Fichier ou chemin du fichier à charger.
     * @throws Exception
     */
    private void loadFile(String file) throws Exception {
        if (file.isEmpty()) {
            System.out.println("Veuillez spécifier un nom de fichier.");
            return;
        }

        // Remplit le graphe grâce au parser.
        Parser parser = new Parser();
        parser.parse(file, this.graph);
        
        System.out.println("Fichier chargé. Validation de la cohérence...");
        this.graph.validate(); 
        
        System.out.println("Validation OK.");
        System.out.println("Statistiques : " + graph.getIndividuals().size() + " individus importés.");
    }

    /**
     * Affiche le rôle dans la famille, le nom et l'ID d'une personne.
     * @param role Role de la personne dans sa famille.
     * @param id Identifiant de la personne.
     */
    private void printPersonName(String role, String id) {
        if (id == null) return;
        Individual p = graph.getIndividual(id);
        if (p != null) {
            String nom = p.getTagValue("NAME");
            System.out.println("  " + role + " : " + nom + " (" + id + ")");
        } else {
            System.out.println("  " + role + " : Inconnu (" + id + ")");
        }
    }

    /**
     * Affiche les informations généalogiques de la personne en argument.
     * @param person Nom de la personne en argument de la commande entrée dans l'interface graphique.
     * @throws ArgNotFoundErr Exception personnalisée si la commande n'existe pas.
     */
    private void showInfo(String person) throws ArgNotFoundErr {
        if (person.isEmpty()) return;

        // Partie 1 : Recherche
        Individual ind = graph.getIndividual(person);
        
        if (ind == null) {
            String recherche = person.toLowerCase(); // On cherche la personne donnée en argument, en miniscule.
            for(Individual i : graph.getIndividuals().values()) { // On regarde chaque individu du graphe (Map).
                String nomIndividu = i.getTagValue("NAME");
                if (nomIndividu != null && nomIndividu.toLowerCase().contains(recherche)) {
                	// L'individu a été trouvé, on arrête la recherche.
                    ind = i;
                    break;
                }
            }
        }
        
        // Personne non trouvée, on renvoie une erreur.
        if (ind == null) {
            throw new ArgNotFoundErr(person);
        }

        // Partie 2 : Affichage
        System.out.println("\n========================================");
        System.out.println(" FICHE DE : " + ind.getTagValue("NAME"));
        System.out.println("========================================");
        
        // Affichage des informations de la personne
        ind.display(0);
        
        System.out.println("----------------------------------------");

        // Affichage des parents (s'ils sont référenciés dans le fichier)
        if (ind.getFamc() != null) {
            System.out.println(" SES PARENTS :");
            Family familleEnfant = graph.getFamily(ind.getFamc());
            
            if (familleEnfant != null) {
                printPersonName("Père", familleEnfant.getHusb());
                printPersonName("Mère", familleEnfant.getWife());
            }
        } else {
            System.out.println(" PARENTS : Inconnus dans la base (Souche)");
        }

        // Affichage de sa famille (ID de famille, conjoint(e) et enfant(s)) s'il en a une.
        if (ind.getFams().isEmpty()) {
            System.out.println("----------------------------------------");
            System.out.println(" Aucune famille (conjoint/enfants) enregistrée.");
        } 
        else {
            for (String famsId : ind.getFams()) { // Pour toutes les familles où la personne est un parent.
                System.out.println("----------------------------------------");
                System.out.println(" FAMILLE (Ref " + famsId + ") :");
                
                Family familleParent = graph.getFamily(famsId);
                
                if (familleParent != null) {
                	// Conjoint(e)
                    String idConjoint;
                    if (ind.getId().equals(familleParent.getHusb())) {
                        idConjoint = familleParent.getWife(); // Si la personne est un homme, on affiche sa femme.
                    } else {
                        idConjoint = familleParent.getHusb(); // Sinon, on affiche son mari.
                    }
                    printPersonName("Conjoint(e)", idConjoint);
                    
                    // Enfant(s) si la personne en a.
                    List<String> enfants = familleParent.getChildren();
                    if (enfants.isEmpty()) {
                        System.out.println("  Pas d'enfants.");
                    } else {
                        System.out.println("  Descendance (" + enfants.size() + " enfants directs) :");
                        
                        for (String enfantId : enfants) {
                            printPersonName("  -", enfantId);
                            
                            // Petits-Enfants (On cherche les enfants des enfants)
                            Individual objetEnfant = graph.getIndividual(enfantId);
                            if (objetEnfant != null && !objetEnfant.getFams().isEmpty()) {
                                for (String idFamilleEnfant : objetEnfant.getFams()) {
                                    Family famillePetitEnfant = graph.getFamily(idFamilleEnfant);
                                    if (famillePetitEnfant != null) {
                                        for (String peId : famillePetitEnfant.getChildren()) {
                                            printPersonName("      -> Petit-Enfant", peId);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        System.out.println("========================================\n");
    }

    private Individual findIndividual(String person) throws ArgNotFoundErr {
        Individual ind = graph.getIndividual(person);
        if (ind == null) {
            String recherche = person.toLowerCase();
            for(Individual i : graph.getIndividuals().values()) {
                String nomIndividu = i.getTagValue("NAME");
                if (nomIndividu != null && nomIndividu.toLowerCase().contains(recherche)) {
                    ind = i;
                    break;
                }
            }
        }
        if (ind == null) {
            throw new ArgNotFoundErr(person);
        }
        return ind;
    }

    /**
     * Affiche les enfants de la personne donnée en argument.
     * @param person Le nom de la personne ciblée.
     * @throws ArgNotFoundErr Erreur si la personne n'est pas trouvée dans le fichier chargé.
     */
    private void showChildren(String person) throws ArgNotFoundErr {
        Individual ind = findIndividual(person);
        System.out.println("ENFANTS DE : " + ind.getTagValue("NAME"));
        
        // Si la personne n'a pas d'enfant.
        if (ind.getFams().isEmpty()) {
            System.out.println(" -> Aucun enfant enregistré.");
            return;
        }
        // Si elle en a, on les affiche.
        for (String famsId : ind.getFams()) {
            Family fam = graph.getFamily(famsId);
            if (fam != null) {
                List<String> enfants = fam.getChildren();
                for (String enfantId : enfants) {
                    printPersonName(" -> Enfant", enfantId);
                }
            }
        }
    }

    /**
     * Affiche les frères et soeurs de la personne donnée en argument.
     * @param person Le nom de la personne ciblée.
     * @throws ArgNotFoundErr Erreur si la personne n'est pas trouvée dans le fichier chargé.
     */
    private void showSiblings(String person) throws ArgNotFoundErr {
        Individual ind = findIndividual(person);
        System.out.println("FRERES ET SOEURS DE : " + ind.getTagValue("NAME"));

        if (ind.getFamc() == null) {
            System.out.println(" -> Aucune famille parentale connue.");
            return;
        }
        Family fam = graph.getFamily(ind.getFamc());
        if (fam != null) {
            for (String enfantId : fam.getChildren()) {
                if (!enfantId.equals(ind.getId())) {
                    printPersonName(" -> Frère/Soeur", enfantId);
                }
            }
        }
    }

    /**
     * Affiche les parents de la personne donnée en argument.
     * @param person Le nom de la personne ciblée.
     * @throws ArgNotFoundErr Erreur si la personne n'est pas trouvée dans le fichier chargé.
     */
    private void showFamc(String person) throws ArgNotFoundErr {
        Individual ind = findIndividual(person);
        System.out.println("FAMILLE PARENTALE DE : " + ind.getTagValue("NAME"));

        if (ind.getFamc() == null) {
            System.out.println(" -> Inconnue.");
            return;
        }
        Family fam = graph.getFamily(ind.getFamc());
        if (fam != null) {
            printPersonName(" PERE", fam.getHusb());
            printPersonName(" MERE", fam.getWife());
        }
    }

    /**
     * Vérifie si deux personnes sont marriées, et affiche la réponse.
     * @param fullLine La ligne complète donnée en commande
     * @throws ArgNotFoundErr
     */
    private void checkMarried(String fullLine) throws ArgNotFoundErr {
        // Découpage insensible à la casse autour de " MARRIED "
        String[] parts = fullLine.split("(?i) MARRIED ");
        
        // Mauvaise syntaxe
        if (parts.length != 2) {
            System.out.println("Usage : <Personne1> MARRIED <Personne2>");
            return;
        }

        Individual p1 = findIndividual(parts[0].trim());
        Individual p2 = findIndividual(parts[1].trim());

        System.out.println("Vérification Mariage entre " + p1.getTagValue("NAME") + " et " + p2.getTagValue("NAME") + "...");

        boolean isMarried = false;
        for (String famsId : p1.getFams()) {
            Family fam = graph.getFamily(famsId);
            if (fam != null) {
                String epoux = fam.getHusb();
                String epouse = fam.getWife();
                
                if ((p1.getId().equals(epoux) && p2.getId().equals(epouse)) ||
                    (p1.getId().equals(epouse) && p2.getId().equals(epoux))) {
                    isMarried = true;
                    System.out.println(" -> OUI, ils sont mariés dans la famille " + famsId);
                    break;
                }
            }
        }

        if (!isMarried) {
            System.out.println(" -> NON, aucun lien de mariage trouvé.");
        }
    }

    /**
     * Sauvegarde le graphe par sérialisation à l'appel de la commande SAVE.
     * @param filename Le fichier à sauvegarder.
     * @throws Exception
     */
    private void saveGraph(String filename) throws Exception {
        if (filename.isEmpty()) {
            System.out.println("Veuillez spécifier un fichier.");
            return;
        }
        System.out.println("Sauvegarde dans " + filename + "...");
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(this.graph);
            System.out.println("Sauvegarde réussie !");
        }
    }
}