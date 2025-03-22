package main;

import base.*;

public class MainRelation {

    public static void main(String[] args) throws Exception {
        try {
            // Definir les attributs pour deux relations
            Attribut[] attributsR1 = {
                new Attribut("nom", new Ensemble(new Object[]{"Koto", "Rana", "Ali", "Sara"})),
                new Attribut("age", new Ensemble(new Object[]{30, 25, 40, 13, 20, 22})),
                new Attribut("etat", new Ensemble(new Object[]{true, false}))
            };
            Attribut[] attributsR2 = {
                new Attribut("nom", new Ensemble(new Object[]{"Ali", "Sara", "Paul"})),
                new Attribut("age", new Ensemble(new Object[]{30, 25, 40, 13, 20, 22})),
                new Attribut("etat", new Ensemble(new Object[]{false, true}))
            };

            // Creer deux relations et inserer des donnees
            Relation r1 = new Relation("Personnes1", attributsR1);
            r1.insererDonnees(new Object[]{"Koto", 20, true});
            r1.insererDonnees(new Object[]{"Rana", 13, false});
            r1.insererDonnees(new Object[]{"Ali", 30, false});
            r1.insererDonnees(new Object[]{"Sara", 25, true});

            Relation r2 = new Relation("Personnes2", attributsR2);
            r2.insererDonnees(new Object[]{"Ali", 30, false});
            r2.insererDonnees(new Object[]{"Sara", 25, true});
            r2.insererDonnees(new Object[]{"Paul", 40, true});

            // Afficher les relations initiales
            System.out.println("Relation R1:");
            r1.afficherValeurs();
            System.out.println("\nRelation R2:");
            r2.afficherValeurs();

            // Test de l'union
            Relation unionResultat = r1.union(r2, new String[]{"nom", "age", "etat"}, "UnionRelation");
            System.out.println("\nResultat de l'union:");
            unionResultat.afficherValeurs();

            // Test de l'intersection
            Relation intersectionResultat = r1.intersection(r2, new String[]{"nom", "age", "etat"}, "IntersectionRelation");
            System.out.println("\nResultat de l'intersection:");
            intersectionResultat.afficherValeurs();

            // Test de la difference
            Relation differenceResultat = r1.difference(r2, new String[]{"nom", "age", "etat"}, "DifferenceRelation");
            System.out.println("\nResultat de la difference:");
            differenceResultat.afficherValeurs();

            // Test de la projection
            Relation projectionResultat = r1.projection(new String[]{"nom", "etat"});
            System.out.println("\nResultat de la projection:");
            projectionResultat.afficherValeurs();

            // Test du produit cartesien
            Relation produitCartesienResultat = r1.produitCartesien(r2);
            System.out.println("\nResultat du produit cartesien:");
            produitCartesienResultat.afficherValeurs();

            // Test de la jointure naturelle
            Relation jointureNaturelleResultat = r1.jointureNaturelle(r2, "JointureNaturelle");
            System.out.println("\nResultat de la jointure naturelle:");
            jointureNaturelleResultat.afficherValeurs();

            // Test de la teta-jointure
            Relation tetaJointureResultat = r1.tetaJointure(r2, "R1.age = R2.age", "TetaJointureRelation");
            System.out.println("\nResultat de la teta-jointure:");
            tetaJointureResultat.afficherValeurs();

            // Test de la selection simple
            Relation selectionResultat = r1.selection("age > 20");
            System.out.println("\nResultat de la selection simple:");
            selectionResultat.afficherValeurs();

            // Test de la selection multiple
            Relation selectionMultipleResultat = r1.selectionMultiple("age > 20 and ( etat is true");
            System.out.println("\nResultat de la selection multiple:");
            selectionMultipleResultat.afficherValeurs();

            Attribut[] attributsHomme = {
                new Attribut("nom", new Ensemble(new Object[]{"Dupont", "Durand", "Martin"})),
                new Attribut("prenom", new Ensemble(new Object[]{"Pierre", "Jean", "Georges"})),
                new Attribut("age", new Ensemble(new Object[]{20, 30, 40}))
            };

            Attribut[] attributsVoiture = {
                new Attribut("proprietaire", new Ensemble(new Object[]{"Dupont", "Durand", null})),
                new Attribut("marque", new Ensemble(new Object[]{"Tesla", "Citroen", "Citroen"})),
                new Attribut("modele", new Ensemble(new Object[]{"Model X", "2 CV", "3 CV"}))
            };

            Relation homme = new Relation("homme", attributsHomme);
            homme.insererDonnees(new Object[]{"Dupont", "Pierre", 20});
            homme.insererDonnees(new Object[]{"Durand", "Jean", 30});
            homme.insererDonnees(new Object[]{"Martin", "Georges", 40});

            Relation voiture = new Relation("voiture", attributsVoiture);
            voiture.insererDonnees(new Object[]{"Dupont", "Tesla", "Model X"});
            voiture.insererDonnees(new Object[]{"Durand", "Citroen", "2 CV"});
            voiture.insererDonnees(new Object[]{null, "Citroen", "3 CV"});

            System.out.println("Relation Homme:");
            homme.afficherValeurs();

            System.out.println("\nRelation Voiture:");
            voiture.afficherValeurs();

            Relation resultat = homme.jointureExterneDroite(homme, voiture, "homme.nom = voiture.proprietaire");

            System.out.println("\nResultat de la jointure externe droite:");
            resultat.afficherValeurs();

        } catch (Exception e) {
            System.out.println("New Exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
