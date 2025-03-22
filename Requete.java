package base;

import java.util.ArrayList;
import java.util.List;

import base.Attribut;
import base.Database;
import base.Ensemble;
import base.Relation;

public class Requete {

    private Database database;

    public Requete(Database database) {
        this.database = database;
    }

    public String executer(String requete) throws Exception {
        String[] parts = requete.split(" ");
        String operation = parts[0].toUpperCase();

        switch (operation) {
            case "SHOW":
                return show(requete);
            case "CREATE":
                return create(requete);
            case "SELECT":
                return select(requete);
            case "INSERT":
                return insert(requete);
            case "DELETE":
                return delete(requete);
            case "DESCRIBE":
                return describe(requete);
            default:
                throw new UnsupportedOperationException("Requete non supportee : " + operation);
        }
    }

    public String describe(String req) {
        String[] parts = req.split(" ");
        return database.describe(parts[1]);
    }

    private String show(String req) {
        String[] parts = req.split(" ", 3);

        if (parts.length < 2) {
            throw new IllegalArgumentException("Requete SHOW invalide.");
        }

        switch (parts[1].toUpperCase()) {
            case "DATABASES":
                return database.afficherDatabasesExistantes();
            case "TABLES":
                return database.afficherRelationsExistantes();
            case "VIEWS":
                return database.afficherVues();
            default:
                if (parts.length == 2) {
                    return database.afficherVue(parts[1]);
                } else {
                    throw new IllegalArgumentException("Format SHOW non pris en charge.");
                }
        }
    }

    public String create(String req) throws Exception {
        String[] parts = req.split(" ", 3);
        if (parts.length < 3) {
            throw new IllegalArgumentException("Requete CREATE invalide.");
        }

        switch (parts[1].toUpperCase()) {
            case "DATABASE":
                String dbName = parts[2];
                Database newDatabase = new Database(dbName);
                if (newDatabase.databaseExiste()) {
                    return "Base de donnees '" + dbName + "' cree avec succes.";
                }
                return "Probleme lors de la creation de la base : " + dbName;

            case "TABLE":
                String[] tableParts = parts[2].split("\\(", 2);
                String tableName = tableParts[0].trim();
                String attributsPart = tableParts[1].replace(")", "").trim();
                Attribut[] attributs = parseAttributs(attributsPart);
                return database.creerRelation(new Relation(tableName, attributs));

            case "VIEW":
                String[] viewParts = parts[2].split("AS", 2);
                if (viewParts.length < 2) {
                    throw new IllegalArgumentException("Requete CREATE VIEW invalide. Format attendu : CREATE VIEW nomView AS requete");
                }
                String viewName = viewParts[0].trim();
                String viewQuery = viewParts[1].trim();
                database.ajouterView(viewName, viewQuery);
                return "Vue '" + viewName + "' creee avec succes.";

            default:
                throw new IllegalArgumentException("Format CREATE non pris en charge.");
        }
    }

    private String insert(String req) throws Exception {
        String[] parts = req.split("\\(", 2);
        String tableName = parts[0].split(" ")[2];
        Relation relation = database.getRelation(tableName);

        if (relation == null) {
            throw new IllegalArgumentException("Table '" + tableName + "' introuvable.");
        }

        String colonnesPart = parts[1].split("\\)", 2)[0];
        String valeursPart = parts[1].split("VALUES")[1].trim().replace("(", "").replace(")", "");

        Object[] donnees = parseDonnees(relation, colonnesPart, valeursPart);
        relation.insererDonnees(donnees);
        database.sauvegarderRelations();
        return "Donnees inserees dans la table '" + tableName + "' avec succes.";
    }

    private String delete(String req) throws Exception {
        String[] parts = req.split(" ", 3);

        if (parts.length < 3) {
            throw new IllegalArgumentException("Requete DELETE invalide.");
        }

        switch (parts[1].toUpperCase()) {
            case "DATABASE":
                String dbName = parts[2];
                if (database.deleteDatabase(dbName)) {
                    return "Base de donnees '" + dbName + "' supprimee avec succes.";
                } else {
                    throw new IllegalArgumentException("echec de la suppression de la base de donnees '" + dbName + "'.");
                }

            case "TABLE":
                String tableName = parts[2];
                if (database.supprimerRelation(tableName)) {
                    return "Table '" + tableName + "' supprimee avec succes.";
                } else {
                    throw new IllegalArgumentException("Table '" + tableName + "' introuvable.");
                }

            default:
                throw new IllegalArgumentException("Format DELETE non pris en charge.");
        }
    }

    private Attribut[] parseAttributs(String attributsStr) throws Exception {
        String[] attributs = attributsStr.split(",");
        List<Attribut> attributList = new ArrayList<>();

        for (String attr : attributs) {
            String[] parts = attr.trim().split(" ");
            if (parts.length != 2) {
                throw new IllegalArgumentException("Format d'attribut invalide : " + attr);
            }
            String nom = parts[0].trim();
            String type = parts[1].trim();
            Ensemble domaine = new Ensemble();
            switch (type.toLowerCase()) {
                case "varchar":
                    domaine.ajouter(String.class);
                    break;
                case "int":
                    domaine.ajouter(Integer.class);
                    break;
                case "double":
                    domaine.ajouter(Double.class);
                    break;
                case "boolean":
                    domaine.ajouter(Boolean.class);
                    break;
                default:
                    throw new IllegalArgumentException("Type non supporte : " + type);
            }
            attributList.add(new Attribut(nom, domaine));
        }

        return attributList.toArray(new Attribut[0]);
    }

    private Object[] parseDonnees(Relation relation, String colonnesPart, String valeursPart) throws Exception {
        String[] colonnes = colonnesPart.split(",");
        String[] valeurs = valeursPart.split(",");
        if (colonnes.length != valeurs.length) {
            throw new IllegalArgumentException("Les colonnes et les valeurs ne correspondent pas.");
        }

        Object[] result = new Object[colonnes.length];
        Attribut[] attributs = relation.getListAttribut();

        for (int i = 0; i < colonnes.length; i++) {
            String colonne = colonnes[i].trim();
            String valeur = valeurs[i].trim().replace("'", "");
            Attribut attribut = null;

            for (Attribut attr : attributs) {
                if (attr.getNom().equalsIgnoreCase(colonne)) {
                    attribut = attr;
                    break;
                }
            }

            if (attribut == null) {
                throw new IllegalArgumentException("Colonne introuvable : " + colonne);
            }

            Ensemble domaine = attribut.getDomaine();
            //System.out.println(domaine.toString());
            if (domaine.appartientEnsemble(String.class)) {
                result[i] = valeur;
            } else if (domaine.appartientEnsemble(Integer.class)) {
                result[i] = Integer.parseInt(valeur);
            } else if (domaine.appartientEnsemble(Double.class)) {
                result[i] = Double.parseDouble(valeur);
            } else if (domaine.appartientEnsemble(Boolean.class)) {
                result[i] = Boolean.parseBoolean(valeur);
            } else {
                throw new IllegalArgumentException("Type non supporte pour la colonne : " + colonne);
            }
        }

        return result;
    }

    private String select(String req) throws Exception {
        // Normaliser la requête en divisant par des espaces multiples
        String[] parts = req.trim().split("\\s+");

        // Extraire le nom de la table
        int fromIndex = -1;
        for (int i = 0; i < parts.length; i++) {
            if (parts[i].equalsIgnoreCase("FROM")) {
                fromIndex = i;
                break;
            }
        }

        if (fromIndex == -1 || fromIndex + 1 >= parts.length) {
            throw new IllegalArgumentException("Requête SELECT invalide : 'FROM' manquant ou incomplet.");
        }

        String tableName = parts[fromIndex + 1];

        // Verifier si c'est une vue
        if (database.contientView(tableName)) {
            View view = database.getView(tableName);
            return executer(view.getRequete());
        }

        Relation relation = database.getRelation(tableName);
        if (relation == null) {
            throw new IllegalArgumentException("Table '" + tableName + "' introuvable.");
        }

        // Verifier s'il existe une operation apres le nom de la table
        if (fromIndex + 2 < parts.length) {
            String operation = parts[fromIndex + 2].toUpperCase();

            // Traiter les differentes operations
            switch (operation) {
                case "UNION":
                    return handleUnion(req, relation);
                case "INTERSECTION":
                    return handleIntersection(req, relation);
                case "DIFFERENCE":
                    return handleDifference(req, relation);
                case "PROJECTION":
                    return handleProjection(req, relation);
                case "PRODUITCARTESIEN":
                    return handleProduitCartesien(req, relation);
                case "WHERE":
                    return handleSelection(req, relation);
                case "JOINTURE":
                    return handleJointure(req, relation);
                default:
                    throw new IllegalArgumentException("Operation non supportee : " + operation);
            }
        }

        // Par defaut, afficher la table si aucune operation specifique n'est demandee
        return relation.toString();
    }

    private String handleUnion(String req, Relation relation) throws Exception {
        String[] parts = req.split(" ");

        // for (int i = 0; i < parts.length; i++) {
        //     System.out.println("parts["+i+"]: "+parts[i]);
        // }
        String table2Name = parts[8];
        Relation relation2 = database.getRelation(table2Name);
        if (relation2 == null) {
            throw new IllegalArgumentException("Table '" + table2Name + "' introuvable.");
        }
        // Appel de la methode union avec les noms de colonnes

        String[] ols = new String[relation.getListAttribut().length];

        for (int i = 0; i < ols.length; i++) {
            ols[i] = relation.getListAttribut()[i].getNom();
        }

        return relation.union(relation2, ols, "UnionRelation").toString();
    }

    private String handleIntersection(String req, Relation relation) throws Exception {
        String[] parts = req.split(" ");
        String table2Name = parts[8];
        Relation relation2 = database.getRelation(table2Name);
        if (relation2 == null) {
            throw new IllegalArgumentException("Table '" + table2Name + "' introuvable.");
        }

        String[] ols = new String[relation.getListAttribut().length];

        for (int i = 0; i < ols.length; i++) {
            ols[i] = relation.getListAttribut()[i].getNom();
        }

        // Appel de la methode intersection avec les noms de colonnes
        return relation.intersection(relation2, ols, "IntersectionRelation").toString();
    }

    private String handleDifference(String req, Relation relation) throws Exception {
        String[] parts = req.split(" ");
        String table2Name = parts[8];
        Relation relation2 = database.getRelation(table2Name);
        if (relation2 == null) {
            throw new IllegalArgumentException("Table '" + table2Name + "' introuvable.");
        }

        String[] ols = new String[relation.getListAttribut().length];

        for (int i = 0; i < ols.length; i++) {
            ols[i] = relation.getListAttribut()[i].getNom();
        }

        // Appel de la methode difference avec les noms de colonnes
        return relation.difference(relation2, ols, "DifferenceRelation").toString();
    }

    private String handleProjection(String req, Relation relation) throws Exception {
        String[] parts = req.split(" ");
        String[] nomsAttributs = parts[5].split(",");
        // Appel de la methode projection
        return relation.projection(nomsAttributs).toString();
    }

    private String handleProduitCartesien(String req, Relation relation) throws Exception {
        String[] parts = req.split(" ");
        String table2Name = parts[8];
        Relation relation2 = database.getRelation(table2Name);
        if (relation2 == null) {
            throw new IllegalArgumentException("Table '" + table2Name + "' introuvable.");
        }
        // Appel de la methode produitCartesien
        return relation.produitCartesien(relation2).toString();
    }

    private String handleSelection(String req, Relation relation) throws Exception {
        String[] parts = req.split(" ", 6);
        String condition = parts[5];
        // Appel de la methode selection
        return relation.selection(condition).toString();
    }

    private String handleJointure(String req, Relation relation) throws Exception {
        String[] parts = req.split(" ", 9);
        String table2Name = parts[6];

        Relation relation2 = database.getRelation(table2Name);
        if (relation2 == null) {
            throw new IllegalArgumentException("Table '" + table2Name + "' introuvable.");
        }
        String jointureType = parts[5].toUpperCase();
        String condition = parts[8];

        switch (jointureType) {
            case "NATURELLE":
                return relation.jointureNaturelle(relation2, "JointureNaturelle").toString();
            case "TETA":
                return relation.tetaJointure(relation2, condition, "TetaJointureRelation").toString();
            case "EXTERNE_COMPLETE":
                return relation.jointureExterneComplete(relation, relation2, condition).toString();
            case "EXTERNE_GAUCHE":
                return relation.jointureExterneGauche(relation, relation2, condition).toString();
            case "EXTERNE_DROITE":
                return relation.jointureExterneDroite(relation, relation2, condition).toString();
            default:
                throw new IllegalArgumentException("Type de jointure non supporte : " + jointureType);
        }
    }

    private String createView(String req) throws Exception {
        String[] parts = req.split(" ", 4);
        if (parts.length < 4) {
            throw new IllegalArgumentException("Requete CREATE VIEW invalide.");
        }
        String nomView = parts[2];
        String requete = parts[3];
        database.ajouterView(nomView, requete);
        return "Vue '" + nomView + "' creee avec succes.";
    }

    private String deleteView(String req) throws Exception {
        String[] parts = req.split(" ", 3);
        if (parts.length < 3) {
            throw new IllegalArgumentException("Requete DELETE VIEW invalide.");
        }
        String nomView = parts[2];
        database.supprimerView(nomView);
        return "Vue '" + nomView + "' supprimee avec succes.";
    }

    private String showViews() {
        return database.afficherVues();
    }

    private String showView(String req) {
        String[] parts = req.split(" ", 2);
        if (parts.length < 2) {
            throw new IllegalArgumentException("Requete SHOW VIEW invalide.");
        }
        return database.afficherVue(parts[1]);
    }

}
