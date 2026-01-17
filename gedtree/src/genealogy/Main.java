package genealogy;

import genealogy.graph.Graph;
import genealogy.console.Console;

/**
 * Classe principale du programme, elle :
 * _ Crée un nouveau graphe qui sera rempli au chargement du fichier GEDCOM,
 * _ Crée et démarre l'interface graphique pour l'utilisateur.
 */

public class Main {
    public static void main(String[] args) {
        // 1. Création du modèle vide
        Graph graph = new Graph();
        
        // 2. Création de l'interface
        Console ui = new Console(graph);
        
        // 3. Lancement
        ui.start();
    }
}