package dao;

import java.io.IOException;
import java.util.Properties;

public class DBProperties {
    private static Properties prop = new Properties();

    static {
        try {
            prop.load(DBProperties.class.getClassLoader().getResourceAsStream("db.properties"));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static String getDbHost() {
        return prop.getProperty("db.host");
    }

    public static String getDbPort() {
        return prop.getProperty("db.port");
    }

    public static String getUsername() {
        return prop.getProperty("db.username");
    }

    public static String getPassword() {
        return prop.getProperty("db.password");
    }

    public static String getDbName() {
        return prop.getProperty("db.dbName");
    }

    public static String getDbOption() {
        return prop.getProperty("db.dbOption");
    }


}

