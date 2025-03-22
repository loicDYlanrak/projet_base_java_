package jdbc;

import base.DatabaseManager;

public class PreparedStatement extends Statement {
    private String sql;
    private Object[] parameters;

    public PreparedStatement(DatabaseManager dbManager, String sql) {
        super(dbManager);
        this.sql = sql;
    }

    public void setParameter(int parameterIndex, Object value) {
        if (parameters == null) {
            parameters = new Object[sql.split("\\?").length - 1];
        }
        parameters[parameterIndex - 1] = value;
    }

    public ResultSet execute() throws Exception {
        String executedSql = sql;
        for (Object param : parameters) {
            executedSql = executedSql.replaceFirst("\\?", param.toString());
        }
        return super.executeQuery(executedSql);
    }
}
