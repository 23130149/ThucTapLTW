package dao;


import com.mysql.cj.jdbc.MysqlDataSource;
import dao.DBProperties;
import org.jdbi.v3.core.Jdbi;

import java.sql.SQLException;

public class BaseDao {
    private Jdbi jdbi;

    public Jdbi getJdbi() {
        if (jdbi == null) makeConnect();
        return jdbi;
    }

    private void makeConnect() {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setURL("jdbc:mysql://" + DBProperties.getDbHost() + ":" + DBProperties.getDbPort() + "/" + DBProperties.getDbName());
        dataSource.setUser(DBProperties.getUsername());
        dataSource.setPassword(DBProperties.getPassword());
        try {
            dataSource.setUseCompression(true);
            dataSource.setAutoReconnect(true);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        jdbi = Jdbi.create(dataSource);

    }
}