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
        return get("db.host");
    }

    public static String getDbPort() {
        return get("db.port");
    }

    public static String getUsername() {
        return get("db.username");
    }

    public static String getPassword() {
        return get("db.password");
    }

    public static String getDbName() {
        return get("db.dbName");
    }

    public static String getDbOption() {
        return get("db.dbOption");
    }

    public static String get(String key) {
        String value = System.getProperty(key);
        if (value == null || value.isBlank()) {
            value = System.getenv(toEnvName(key));
        }
        if (value == null || value.isBlank()) {
            value = prop.getProperty(key);
        }
        return value == null ? "" : value.trim();
    }

    private static String toEnvName(String key) {
        return key.toUpperCase().replace('.', '_').replace('-', '_');
    }
}

