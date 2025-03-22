package main;

import base.*;

public class Main {
    public static void main(String[] args) {
        try {

            DatabaseManager dbManager = new DatabaseManager();

            // Tester la creation d'une base de donnees
            System.out.println("=== Creation d'une base de donnees ===");
            String createDbRequete = "CREATE DATABASE testDB";
            System.out.println(dbManager.executerRequete(createDbRequete));

            // Tester l'affichage des listes de bases
            System.out.println("\n=== Affichage des bases ===");
            String showRequete3 = "SHOW DATABASES";
            System.out.println(dbManager.executerRequete(showRequete3));

            System.out.println("=== Utilisation d'une base de donnees ===");
            String useDbRequete = "USE testDB";
            System.out.println(dbManager.executerRequete(useDbRequete));

            // Tester la creation d'une table
            System.out.println("\n=== Creation d'une table ===");
            String createTableRequete = "CREATE TABLE users (nom varchar, age int, isAdmin boolean)";
            System.out.println(dbManager.executerRequete(createTableRequete));

            System.out.println("\n=== Description d'une table ===");
            String description = "DESCRIBE users";
            System.out.println(dbManager.executerRequete(description));

            // Tester l'insertion de donnees
            System.out.println("\n=== Insertion de donnees ===");
            String insertRequete = "INSERT INTO users (nom, age, isAdmin) VALUES ('Alice', 25, true)";
            System.out.println(dbManager.executerRequete(insertRequete));
            
            String insertRequete2 = "INSERT INTO users (nom, age, isAdmin) VALUES ('Kevin', 30, false)";
            System.out.println(dbManager.executerRequete(insertRequete2));
            
            insertRequete = "INSERT INTO users (nom, age, isAdmin) VALUES ('Bob', 30, false)";
            System.out.println(dbManager.executerRequete(insertRequete));

            // Afficher la table
            System.out.println("\n=== Affichage de la table ===");
            String selectRequete = "SELECT * FROM users";
            System.out.println(dbManager.executerRequete(selectRequete));

            // Tester des requêtes SELECT avec differentes operations relationnelles

            // UNION
            System.out.println("\n=== Test UNION ===");
            String createTable2Requete = "CREATE TABLE users2 (nom varchar, age int, isAdmin boolean)";
            System.out.println(dbManager.executerRequete(createTable2Requete));
            String insertRequete3 = "INSERT INTO users2 (nom, age, isAdmin) VALUES ('Alice', 25, true)";
            System.out.println(dbManager.executerRequete(insertRequete3));
            String insertRequete4 = "INSERT INTO users2 (nom, age, isAdmin) VALUES ('Zoe', 22, true)";
            System.out.println(dbManager.executerRequete(insertRequete4));
            String selectUnionRequete = "SELECT * FROM users UNION SELECT * FROM users2";
            System.out.println(dbManager.executerRequete(selectUnionRequete));

            // INTERSECTION
            System.out.println("\n=== Test INTERSECTION ===");
            String selectIntersectionRequete = "SELECT * FROM users INTERSECTION SELECT * FROM users2";
            System.out.println(dbManager.executerRequete(selectIntersectionRequete));

            // DIFFERENCE
            System.out.println("\n=== Test DIFFERENCE ===");
            String selectDifferenceRequete = "SELECT * FROM users DIFFERENCE SELECT * FROM users2";
            System.out.println(dbManager.executerRequete(selectDifferenceRequete));

            // PROJECTION
            System.out.println("\n=== Test PROJECTION ===");
            String selectProjectionRequete = "SELECT nom, age FROM users";
            System.out.println(dbManager.executerRequete(selectProjectionRequete));

            // PRODUIT CARTESIEN
            System.out.println("\n=== Test PRODUIT CARTESIEN ===");
            String selectProduitCartesienRequete = "SELECT * FROM users PRODUITCARTESIEN SELECT * FROM users2";
            System.out.println(dbManager.executerRequete(selectProduitCartesienRequete));

            // SELECTION (avec condition)
            System.out.println("\n=== Test SELECTION ===");
            String selectSelectionRequete = "SELECT * FROM users WHERE age > 25";
            System.out.println(dbManager.executerRequete(selectSelectionRequete));

            // JOINTURE NATURELLE
            System.out.println("\n=== Test JOINTURE NATURELLE ===");
            String selectJointureNaturelleRequete = "SELECT * FROM users JOINTURE NATURELLE users2 ON nom";
            System.out.println(dbManager.executerRequete(selectJointureNaturelleRequete));

            // JOINTURE TETA
            System.out.println("\n=== Test JOINTURE TETA ===");
            String selectJointureTetaRequete = "SELECT * FROM users JOINTURE TETA users2 ON users.age = users2.age";
            System.out.println(dbManager.executerRequete(selectJointureTetaRequete));

            // JOINTURE EXTERNE COMPLETE
            System.out.println("\n=== Test JOINTURE EXTERNE COMPLETE ===");
            String selectJointureExterneCompleteRequete = "SELECT * FROM users JOINTURE EXTERNE_COMPLETE users2 ON users.nom = users2.nom";
            System.out.println(dbManager.executerRequete(selectJointureExterneCompleteRequete));

            // JOINTURE EXTERNE GAUCHE
            System.out.println("\n=== Test JOINTURE EXTERNE GAUCHE ===");
            String selectJointureExterneGaucheRequete = "SELECT * FROM users JOINTURE EXTERNE_GAUCHE users2 ON users.users.nom = users2.users2.nom";
            System.out.println(dbManager.executerRequete(selectJointureExterneGaucheRequete));

            // JOINTURE EXTERNE DROITE
            System.out.println("\n=== Test JOINTURE EXTERNE DROITE ===");
            String selectJointureExterneDroiteRequete = "SELECT * FROM users JOINTURE EXTERNE_DROITE users2 ON users.users.users.nom = users2.users2.users2.nom";
            System.out.println(dbManager.executerRequete(selectJointureExterneDroiteRequete));

            // Tester l'affichage des listes de tables
            System.out.println("\n=== Affichage des tables ===");
            String showRequete = "SHOW TABLES";
            System.out.println(dbManager.executerRequete(showRequete));

            // Tester la creation d'une vue
            System.out.println("\n===  Creation de vue ===");
            String createView = "CREATE VIEW user_ages AS SELECT id, age FROM users";
            System.out.println(dbManager.executerRequete(createView));

            // tester l'affichage des vues
            System.out.println("\n=== Affichages des vues ===");
            String affView = "SHOW VIEWS";
            System.out.println(dbManager.executerRequete(affView));

            // tester selection à partir de la vue
            System.out.println("\n=== Selection a partir de vue ===");
            String selectView = "SELECT * FROM user_ages";
            System.out.println(dbManager.executerRequete(selectView));

            // tester suppression de la vue
            System.out.println("\n=== Suppression de vue ===");
            dbManager.executerRequete("DELETE VIEW user_ages");

            // Tester la suppression de la table
            System.out.println("\n=== Suppression de la table ===");
            String deleteTableRequete = "DELETE TABLE users";
            System.out.println(dbManager.executerRequete(deleteTableRequete));

            // Tester la suppression de la base de donnees
            System.out.println("\n=== Suppression de la base de donnees ===");
            String deleteDbRequete = "DELETE DATABASE testDB";
            System.out.println(dbManager.executerRequete(deleteDbRequete));

            // Tester l'affichage des listes de bases
            System.out.println("\n=== Affichage des bases ===");
            String showRequete2 = "SHOW DATABASES";
            System.out.println(dbManager.executerRequete(showRequete2));
        } catch (Exception e) {
            System.err.println("Erreur : " + e.getMessage());
            e.printStackTrace();
        }
    }
}
