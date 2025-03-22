package jdbc;

import base.DatabaseManager;

public class Statement {
    private DatabaseManager dbManager;

    public Statement(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    public ResultSet executeQuery(String sql) throws Exception {
        String response = dbManager.executerRequete(sql);
        return new ResultSet(response);
    }

    public int executeUpdate(String sql) throws Exception {
        @SuppressWarnings("unused")
        String response = dbManager.executerRequete(sql);
        //return Integer.parseInt(response.split(" ")[0]); // Ex: "1 row(s) affected"
        return 0;
    }

    public boolean execute(String sql) throws Exception {
        String response = dbManager.executerRequete(sql);
        return !response.isBlank();
    }

    public void close() {
        // No resources to close in this simple implementation
    }
}
