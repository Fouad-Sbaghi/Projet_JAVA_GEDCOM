package genealogy.parsing;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import genealogy.graph.Graph;
import genealogy.graph.Individual;
import genealogy.graph.Family;
import genealogy.graph.Entity;
import genealogy.tags.*;
import genealogy.tags.simple.*;
import genealogy.tags.composite.*;
import genealogy.exceptions.GenealogyErr;
import genealogy.exceptions.exceptfile.LineErr;

/**
 * Classe responsable de la lecture et de l'analyse d'un fichier GEDCOM.
 * Le parseur lit le fichier ligne par ligne, interprète le niveau hiérarchique
 * et instancie les objets correspondants entités ou tags pour remplir le graph.
 */
public class Parser {

    /**
     * Lit un fichier GEDCOM et remplit le graphe avec les données extraites.
     * La méthode utilise un BufferedReader pour lire le fichier ligne par ligne.
     * * @param filePath Le chemin absolu ou relatif vers le fichier .ged à lire.
     * @param graph L'instance du graphe à remplir. Les individus et familles créés y seront ajoutés.
     * @throws IOException En cas de problème d'accès au fichier (fichier introuvable, lecture impossible).
     * @throws GenealogyErr En cas d'erreur de formatage dans le fichier
     */
    public void parse(String filePath, Graph graph) throws IOException, GenealogyErr {
            
        // Utilisation de BufferedReader pour une lecture ligne par ligne
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;
        
        // Attributs d'état pour le parsing :
        Entity currentEntity = null;    // L'individu ou la famille en cours de construction (Niveau 0)
        CompositeTag currentTag = null; // Le tag composite en cours (Niveau 1) qui reçoit les sous-tags
        
        // Compteur de lignes pour le rapport d'erreurs
        int lineCount = 0; 

        while ((line = reader.readLine()) != null) {
            lineCount++; // On incrémente à chaque lecture
            
            line = line.trim(); 
            if (line.isEmpty()) continue;

            // Tentative de séparation : Niveau / Tag / Valeur
            String[] parts = line.split(" ", 3);
            int level;
            try {
                level = Integer.parseInt(parts[0]);
            } catch (NumberFormatException e) {
                // On passe le numéro de ligne ET le message pour l'erreur
                throw new LineErr(lineCount, "Le niveau n'est pas un entier valide : " + line);
            }

            // Niveau 0 
            if (level == 0) {
                currentEntity = null;
                currentTag = null;
                
                // Format attendu : 0 @ID@ TYPE
                if (parts.length >= 3 && parts[1].startsWith("@")) {
                    String id = parts[1];
                    String type = parts[2];

                    if (type.equals("INDI")) {
                        currentEntity = new Individual(id);
                        graph.addIndividual((Individual) currentEntity);
                    } else if (type.equals("FAM")) {
                        currentEntity = new Family(id);
                        graph.addFamily((Family) currentEntity);
                    }
                }
            }
            
            //Niveau 1 
            else if (level == 1 && currentEntity != null) {
                currentTag = null; // Fin du tag composite précédent éventuel

                String tagName = parts[1];
                String value = (parts.length > 2) ? parts[2] : null;

                Tag tag = createTag(tagName, level); // Appel à la Factory
                
                if (tag != null) {
                    tag.parse(value); 
                    currentEntity.addProperty(tag); 

                    // Mise à jour des références croisées (champs rapides)
                    updateLinks(currentEntity, tagName, value);

                    // Si c'est un tag composite, on le garde en mémoire pour y ajouter les enfants (niv 2)
                    if (tag instanceof CompositeTag) {
                        currentTag = (CompositeTag) tag;
                    }
                }
            }
            
            // Niveau 2 
            else if (level == 2 && currentTag != null) {
                String tagName = parts[1];
                String value = (parts.length > 2) ? parts[2] : null;

                Tag subTag = createTag(tagName, level);
                
                if (subTag != null) {
                    subTag.parse(value);
                    currentTag.addChild(subTag); 
                }
            }
        }
        reader.close();
    }

    /**
     * Crée le tag approprié en fonction du nom lu.
     * @param tagName Le code du tag 
     * @param level Le niveau hiérarchique du tag, utilisé pour les tags génériques.
     * @return Une instance concrète de Tag ou un StringTag si aucune classe spécifique n'existe.
     */
    private Tag createTag(String tagName, int level) {
        switch (tagName) {
            // Tags Simples 
            case "NAME": return new NameTag();
            case "SEX":  return new SexTag();
            
            // Tags Composites Spécifiques
            case "BIRT": return new BirthTag();
            case "DEAT": return new DeathTag();
            case "MARR": return new MarriageTag();
            case "OBJE": return new ObjectTag();
            
            // Sous-tags Simples
            case "DATE": return new DateTag();
            case "PLAC": return new PlaceTag();
            case "FORM": return new FileFormatTag();
            case "TITL": return new FileTitleTag();
            case "FILE": return new FileURLTag();

            // Tags Génériques
            // On utilise StringTag en utilisant le vrai niveau passé en paramètre
            case "FAMC": 
            case "FAMS": 
            case "HUSB": 
            case "WIFE": 
            case "CHIL": 
            case "NOTE": 
                return new StringTag(tagName, level);
            default: 
                // Pour les tags inconnus, on renvoie un tag pour ne pas bloquer le parsing
                return new StringTag(tagName, level); 
        }
    }

    /**
     * Met à jour les attributs spécifiques des classes Individual et Family 
     * Cela permet une navigation plus rapide dans le graphe sans devoir parcourir la liste des tags à chaque fois.
     * @param entity L'entité en cours de modification.
     * @param tag Le nom du tag rencontré.
     * @param value La valeur du tag.
     */
    private void updateLinks(Entity entity, String tag, String value) {
        if (entity instanceof Individual) {
            Individual ind = (Individual) entity;
            if (tag.equals("FAMC")) ind.setFamc(value);
            if (tag.equals("FAMS")) ind.addFams(value);
        } else if (entity instanceof Family) {
            Family fam = (Family) entity;
            if (tag.equals("HUSB")) fam.setHusb(value);
            if (tag.equals("WIFE")) fam.setWife(value);
            if (tag.equals("CHIL")) fam.addChild(value);
        }
    }
}