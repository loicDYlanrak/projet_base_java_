package jdbc;

import base.DatabaseManager;

public class Connection {
    private DatabaseManager dbManager;

    public Connection(String dbName) throws Exception {
        this.dbName = dbName;
        this.dbManager = new DatabaseManager();
        this.dbManager.executerRequete("USE " + dbName);
    }

    public Statement createStatement() {
        return new Statement(this.dbManager);
    }

    public PreparedStatement createPreparedStatement(String sql) {
        return new PreparedStatement(this.dbManager, sql);
    }

    public void close() throws Exception {
        //this.dbManager.executerRequete("DISCONNECT DATABASE " + dbName);
    }
}
