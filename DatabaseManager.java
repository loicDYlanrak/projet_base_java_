package base;

import java.util.HashMap;

public class DatabaseManager {
    private HashMap<String, Database> databases;
    private Database databaseActuelle;

    public DatabaseManager() {
        this.databases = new HashMap<>();
    }

    public void connecter(String nomDatabase) throws Exception {
        if (!databases.containsKey(nomDatabase)) {
            databases.put(nomDatabase, new Database(nomDatabase));
        }
        this.databaseActuelle = databases.get(nomDatabase);
    }

    public boolean isUseDatabase(String requete) {
        String[] parts = requete.split(" ");
        return parts[0].compareToIgnoreCase("USE")==0;
    }

    public boolean isCreateDatabase(String requete) {
        String[] parts = requete.split(" ");
        return parts[0].compareToIgnoreCase("CREATE")==0 && parts[1].compareToIgnoreCase("DATABASE")==0;
    }

    public boolean isShowDatabase(String requete) {
        String[] parts = requete.split(" ");
        return parts[0].compareToIgnoreCase("SHOW")==0 && parts[1].compareToIgnoreCase("DATABASES")==0;
    }

    public String executerRequete(String requete) throws Exception {
        if (databaseActuelle == null && isCreateDatabase(requete)==false && isShowDatabase(requete)==false && isUseDatabase(requete)==false) {
            throw new IllegalStateException("Aucune base de donnees connectee !");
        }

        String[] parts = requete.split(" ");

        if (isUseDatabase(requete)==true) {
            return setDatabaseActuelle(new Database(parts[1]));
        }

        if (isCreateDatabase(requete)==true || isShowDatabase(requete)==true ) {
            Requete req = new Requete(new Database("default"));
            return req.executer(requete);
        } else {
            Requete req = new Requete(databaseActuelle);
            return req.executer(requete);
        }
    }

    public void afficherBasesExistantes() {
        System.out.println("Bases existantes : " + databases.keySet());
    }

    public String setDatabaseActuelle(Database databaseActu) {
        this.databaseActuelle = databaseActu;
        if (databaseActuelle.databaseExiste()) {
            return "Database changer : "+databaseActuelle.getNom();
        }
        return "Database non trouver "+databaseActuelle.getNom();
    }
}
