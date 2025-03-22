package base;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Database {

    private String nom;
    private HashMap<String, Relation> relations;
    private Map<String, View> views = new HashMap<>();

    public String getNom() {
        return nom;
    }

    public Database(String nom) throws IOException {
        this.nom = nom;
        this.relations = new HashMap<>();
        File fichierDatabase = new File("databases/" + nom + ".txt");

        if (!fichierDatabase.exists()) {
            if (!fichierDatabase.getParentFile().exists()) {
                fichierDatabase.getParentFile().mkdirs();
            }
            fichierDatabase.createNewFile();
        } else {
            chargerRelations();
        }
    }

    // Charger relations et vues depuis le fichier
    private void chargerRelations() throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader("databases/" + nom + ".txt"))) {
            String ligne;
            boolean inViewsSection = false;

            while ((ligne = br.readLine()) != null) {
                if (ligne.equals("[VIEWS]")) {
                    inViewsSection = true;
                    continue;
                }

                if (inViewsSection) {
                    // Chargement des vues
                    String[] elements = ligne.split(":");
                    String viewName = elements[0];
                    String viewQuery = elements[1];
                    View view = new View(viewName, viewQuery);
                    views.put(viewName, view);
                } else {
                    // Chargement des relations
                    String[] elements = ligne.split(":");
                    String nomRelation = elements[0];

                    // Recuperation des attributs
                    String[] attributsBruts = elements[1].split(",");
                    Attribut[] attributs = new Attribut[attributsBruts.length];

                    for (int i = 0; i < attributsBruts.length; i++) {
                        String[] nomEtType = attributsBruts[i].split(" ");
                        String nomAttribut = nomEtType[0];
                        String typeClasse = nomEtType[2];

                        // Creation du domaine à partir du type
                        Ensemble domaine = new Ensemble(new Object[]{Class.forName(typeClasse)});
                        attributs[i] = new Attribut(nomAttribut, domaine);
                    }

                    // Initialisation des valeurs
                    Object[][] valeurs;
                    boolean aValeur = false;

                    if (elements.length > 2 && !elements[2].isBlank()) {
                        String[] lignesValeurs = elements[2].split("\\|");
                        valeurs = new Object[lignesValeurs.length][attributs.length];

                        for (int i = 0; i < lignesValeurs.length; i++) {
                            String[] valeursBrutes = lignesValeurs[i].split(",");
                            for (int j = 0; j < valeursBrutes.length; j++) {
                                valeurs[i][j] = parseValeur(valeursBrutes[j], attributs[j].getDomaine().getElements()[0].toString().split(" ")[1]);
                            }
                        }
                        aValeur = true;
                    } else {
                        valeurs = new Object[0][attributs.length];
                    }

                    Relation relation = new Relation(nomRelation, attributs);
                    if (aValeur) {
                        for (Object[] objects : valeurs) {
                            relation.insererDonnees(objects);
                        }
                    }
                    relations.put(nomRelation, relation);
                }
            }
        } catch (Exception e) {
            throw new IOException("Erreur lors du chargement des relations et des vues : " + e.getMessage());
        }
    }

// Sauvegarder relations et vues dans le fichier
    public void sauvegarderRelations() throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("databases/" + nom + ".txt"))) {
            // Sauvegarder les relations
            for (Relation relation : relations.values()) {
                StringBuilder sb = new StringBuilder();

                // Nom de la relation
                sb.append(relation.getNom()).append(":");

                // Liste des attributs
                for (Attribut attribut : relation.getListAttribut()) {
                    sb.append(attribut.getNom()).append(" ").append(attribut.getDomaine().getElements()[0].toString()).append(",");
                }
                sb.setLength(sb.length() - 1); // Supprimer la derniere virgule
                sb.append(":");

                // Valeurs de la relation
                Object[][] valeurs = relation.getValeurs();
                for (int i = 0; i < relation.getNombreDeLignes(); i++) {
                    for (int j = 0; j < valeurs[i].length; j++) {
                        sb.append(valeurs[i][j]).append(",");
                    }
                    sb.setLength(sb.length() - 1); // Supprimer la derniere virgule
                    sb.append("|");
                }
                if (relation.getNombreDeLignes() > 0) {
                    sb.setLength(sb.length() - 1); // Supprimer le dernier |
                }

                bw.write(sb.toString());
                bw.newLine();
            }

            // Sauvegarder les vues
            bw.write("[VIEWS]");
            bw.newLine();
            for (View view : views.values()) {
                StringBuilder sb = new StringBuilder();

                // Nom de la vue et requête associee
                sb.append(view.getNom()).append(":").append(view.getRequete());
                bw.write(sb.toString());
                bw.newLine();
            }
        } catch (Exception e) {
            throw new IOException("Erreur lors de la sauvegarde des relations et des vues : " + e.getMessage());
        }
    }

    // Methode utilitaire pour parser une valeur selon son type
    private Object parseValeur(String valeur, String type) throws ClassNotFoundException {
        //System.out.println("valeur: "+valeur+" , type: "+type);

        switch (type) {
            case "java.lang.String":
                return valeur;
            case "java.lang.Integer":
                return Integer.parseInt(valeur);
            case "java.lang.Double":
                return Double.parseDouble(valeur);
            case "java.lang.Boolean":
                return Boolean.parseBoolean(valeur);
            default:
                throw new ClassNotFoundException("Type non pris en charge : " + type);
        }
    }

    public boolean databaseExiste() {
        File databaseFile = new File("databases/" + this.nom + ".txt");
        return databaseFile.exists();
    }

    public boolean deleteDatabase(String nom) {
        File databaseFile = new File("databases/" + nom + ".txt");

        if (databaseFile.exists()) {
            return databaseFile.delete(); // Supprime le fichier
        } else {
            System.out.println("La base de donnees " + nom + " n'existe pas.");
            return false;
        }
    }

    public String afficherRelationsExistantes() {
        if (relations.isEmpty()) {
            System.out.println("Aucune relation existante dans la base de donnees \"" + nom + "\".");
            return "Aucune relation existante dans la base de donnees \"" + nom + "\".";
        } else {
            System.out.println("Relations existantes dans la base de donnees \"" + nom + "\":");
            String result = "Relations existantes dans la base de donnees \"" + nom + "\":";
            for (String nomRelation : relations.keySet()) {
                System.out.println("- " + nomRelation);
                result = result + ", " + nomRelation;
            }
            return result;
        }
    }

    public String afficherDatabasesExistantes() {
        File dossierDatabases = new File("databases/");

        if (!dossierDatabases.exists() || !dossierDatabases.isDirectory()) {
            System.out.println("Aucune base de donnees trouvee.");
            return "Aucune base de donnees trouvee.";
        }

        String[] fichiers = dossierDatabases.list((dir, name) -> name.endsWith(".txt"));
        String result = " ";
        if (fichiers == null || fichiers.length == 0) {
            System.out.println("Aucune base de donnees trouvee.");
            result = "Aucune base de donnees trouvee.";
        } else {
            System.out.println("Bases de donnees existantes :");
            result = "Bases de donnees existantes :\n";
            for (String fichier : fichiers) {
                System.out.println("- " + fichier.replace(".txt", ""));
                result = result + ",  " + fichier.replace(".txt", "");
            }
        }
        return result;
    }

    public String creerRelation(Relation relation) throws IOException {
        if (relations.containsKey(relation.getNom())) {
            return ("Relation existe deja !");
        }
        relations.put(relation.getNom(), relation);
        sauvegarderRelations();
        return "Table '" + relation.getNom() + "' cree avec succes.";
    }

    public Relation getRelation(String nom) {
        return relations.get(nom);
    }

    public HashMap<String, Relation> getRelations() {
        return relations;
    }

    public void modifierRelation(String nom, Relation nouvelleRelation) throws IOException {
        if (!relations.containsKey(nom)) {
            throw new IllegalArgumentException("Relation introuvable !");
        }
        relations.put(nom, nouvelleRelation);
        sauvegarderRelations();
    }

    public boolean supprimerRelation(String nom) throws IOException {
        if (!relations.containsKey(nom)) {
            throw new IllegalArgumentException("Relation introuvable !");
        }
        relations.remove(nom);
        sauvegarderRelations();
        return true;
    }

    public String describe(String tableName) {
        Relation re = getRelation(tableName);

        if (re == null) {
            return "La relation n existe pas : " + tableName + " not found ";
        }
        return re.describe();
    }

    ///resaka vue :
    
    // Ajouter une vue
public void ajouterView(String nom, String requete) throws  Exception {
        if (views.containsKey(nom)) {
            throw new IllegalArgumentException("View '" + nom + "' existe dejà.");
        }
        views.put(nom, new View(nom, requete));
        sauvegarderRelations();
    }

// Supprimer une vue
    public void supprimerView(String nom) throws  Exception {
        if (!views.containsKey(nom)) {
            throw new IllegalArgumentException("View '" + nom + "' introuvable.");
        }
        views.remove(nom);
        sauvegarderRelations();
    }

// Obtenir une vue
    public View getView(String nom) {
        return views.get(nom);
    }

// Verifier si une vue existe
    public boolean contientView(String nom) {
        return views.containsKey(nom);
    }

    public String afficherVues() {
        if (views.isEmpty()) {
            return "Aucune vue existante.";
        }
        StringBuilder sb = new StringBuilder("Vues existantes :\n");
        for (String nom : views.keySet()) {
            sb.append("  - ").append(nom).append(", ");
        }
        return sb.toString();
    }

    public String afficherVue(String nom) {
        if (!views.containsKey(nom)) {
            throw new IllegalArgumentException("View '" + nom + "' introuvable.");
        }
        return views.get(nom).toString();
    }

}
