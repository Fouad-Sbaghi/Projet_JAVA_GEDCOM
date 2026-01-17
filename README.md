# GedTree - GEDCOM Reader & Explorer

**GedTree** est une application Java permettant de lire, analyser et naviguer dans des fichiers généalogiques au format standard GEDCOM (`.ged`).

## Fonctionnalités

- **Parsing Robuste :** Lecture et analyse syntaxique des fichiers GEDCOM (Niveaux 0, 1, 2).
- **Structure en Graphe :** Modélisation des relations entre `Individus` et `Familles`.
- **Interface Console (CLI) :** Navigation interactive dans l'arbre.
- **Requêtes Généalogiques :**
  - Recherche d'individus par ID ou Nom.
  - Affichage des parents (`FAMC`), enfants (`CHILD`) et fratries (`SIBLINGS`).
  - Vérification de liens de mariage (`MARRIED`).
- **Validation :** Détection d'incohérences (dates, genres, cycles).
- **Sauvegarde :** Sérialisation du graphe en binaire (`SAVE`).

## Architecture Technique

- **Langage :** Java (JDK 8+)
- **Concepts :** POO, Polymorphisme, Design Pattern Factory & Composite.
- **Gestion des erreurs :** Exceptions personnalisées hiérarchisées.

## Utilisation

1. Compilez le projet.
2. Lancez `Main.java`.
3. Utilisez les commandes :
   ```bash
   > LOAD mon_arbre.ged
   > INFO Jean Dupont
   > CHILD Jean Dupont
   > SAVE backup.ser
   > EXIT
## Auteurs
**Fouad SBAGHI**
**Mathis HERNANDEZ**
