package base;

public class View extends Relation {
    private String requete;

    public View(String nom, String requete) {
        super(nom, new Attribut[]{new Attribut("requete", new Ensemble(new Object[]{String.class}))});
        this.requete = requete;
    }

    public String getRequete() {
        return requete;
    }

    public void setRequete(String requete) {
        this.requete = requete;
    }

    @Override
    public String toString() {
        return "View: " + getNom() + ", Requete: " + requete;
    }
}
