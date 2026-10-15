package com.rooted.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * DBUtil — central JDBC helper for Rooted.
 *
 * Reads database credentials from db.properties on the classpath
 * (src/main/resources/db.properties).  Never hardcode credentials here.
 *
 * Usage:
 *   try (Connection conn = DBUtil.getConnection()) {
 *       // use conn ...
 *   }
 */
public final class DBUtil {

    private static final Properties PROPS = new Properties();

    /*
     * Static initialiser: runs once when the class is first loaded.
     * Loads db.properties and registers the JDBC driver.
     */
    static {
        try (InputStream in = DBUtil.class
                .getClassLoader()
                .getResourceAsStream("db.properties")) {

            if (in == null) {
                throw new ExceptionInInitializerError(
                    "db.properties not found on the classpath. " +
                    "Copy db.properties.example to db.properties and fill in your credentials."
                );
            }

            PROPS.load(in);

            // Explicitly load the MySQL driver class
            Class.forName(PROPS.getProperty("db.driver"));

        } catch (IOException | ClassNotFoundException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    /**
     * Returns a new JDBC {@link Connection} from the configured database.
     *
     * <p>Always use this in a try-with-resources block so the connection
     * is closed automatically:
     * <pre>
     *   try (Connection conn = DBUtil.getConnection()) {
     *       // ...
     *   }
     * </pre>
     *
     * @return an open Connection
     * @throws SQLException if the connection cannot be established
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
            PROPS.getProperty("db.url"),
            PROPS.getProperty("db.username"),
            PROPS.getProperty("db.password")
        );
    }

    /**
     * Quietly closes a Connection, ignoring any SQLException.
     * Useful in finally blocks where you cannot propagate exceptions.
     *
     * @param conn the Connection to close (null-safe)
     */
    public static void closeQuietly(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException ignored) {
                // nothing to do — we're in cleanup code
            }
        }
    }

    // Prevent instantiation — this is a pure utility class
    private DBUtil() {}
}
