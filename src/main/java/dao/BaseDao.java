package dao;

import org.jdbi.v3.core.Jdbi;

public class BaseDao {
    private Jdbi jdbi;

    public Jdbi getJdbi() {
        if (jdbi == null) makeConnect();
        return jdbi;
    }

    private void makeConnect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL JDBC driver not found", e);
        }

        String url = "jdbc:mysql://"
                + DBProperties.getDbHost()
                + ":"
                + DBProperties.getDbPort()
                + "/"
                + DBProperties.getDbName();

        jdbi = Jdbi.create(url, DBProperties.getUsername(), DBProperties.getPassword());
    }
}
