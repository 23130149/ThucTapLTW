package dao;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class DBProperties {
    private static Properties prop = new Properties();

    static {
        try (InputStream input = DBProperties.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (input == null) {
                throw new RuntimeException("db.properties not found");
            }
            prop.load(new InputStreamReader(input, StandardCharsets.UTF_8));
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

    public static String get(String key) {
        return prop.getProperty(key);
    }

}

