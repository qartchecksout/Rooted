package com.rooted.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * DBUtil — central JDBC helper for Rooted.
 *
 * Resolution order:
 *   1. Environment variables (ROOTED_DB_URL, ROOTED_DB_USER, ROOTED_DB_PASSWORD)
 *   2. src/main/resources/db.properties (MySQL) — if present and reachable
 *   3. Automatic fallback to Embedded H2 (MySQL compatibility mode) so the app
 *      works out of the box on any laptop with zero MySQL setup.
 */
public final class DBUtil {

    private static final String DEFAULT_MYSQL_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String H2_DRIVER = "org.h2.Driver";

    private static volatile boolean initialized = false;
    private static String activeUrl;
    private static String activeUser;
    private static String activePassword;
    private static String dbMode = "Uninitialized";
    private static boolean usingEmbeddedDb = false;

    private static synchronized void ensureInitialized() {
        if (initialized) {
            return;
        }

        // 1. Check environment variables first
        String envUrl = System.getenv("ROOTED_DB_URL");
        String envUser = System.getenv("ROOTED_DB_USER");
        String envPass = System.getenv("ROOTED_DB_PASSWORD");

        if (envUrl != null && !envUrl.isBlank()) {
            String driver = System.getenv("ROOTED_DB_DRIVER");
            if (tryConfigureMySql(
                    (driver != null && !driver.isBlank()) ? driver : DEFAULT_MYSQL_DRIVER,
                    envUrl.trim(),
                    envUser != null ? envUser : "root",
                    envPass != null ? envPass : "")) {
                initialized = true;
                return;
            }
        }

        // 2. Check db.properties on classpath
        Properties props = new Properties();
        try (InputStream in = DBUtil.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (in != null) {
                props.load(in);
                String driver = props.getProperty("db.driver", DEFAULT_MYSQL_DRIVER).trim();
                String url = props.getProperty("db.url", "").trim();
                String user = props.getProperty("db.username", "root").trim();
                String pass = props.getProperty("db.password", "");

                if (!url.isEmpty() && !"YOUR_PASSWORD_HERE".equals(pass)) {
                    if (tryConfigureMySql(driver, url, user, pass)) {
                        initialized = true;
                        return;
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("[Rooted] Could not read db.properties: " + e.getMessage());
        }

        // 3. Fallback to Embedded H2 (MySQL compatibility mode)
        configureEmbeddedH2();
        initialized = true;
    }

    private static boolean tryConfigureMySql(String driver, String url, String user, String pass) {
        try {
            Class.forName(driver);
            String targetUrl = ensureUtf8Params(url);

            try (Connection conn = DriverManager.getConnection(targetUrl, user, pass)) {
                activeUrl = targetUrl;
                activeUser = user;
                activePassword = pass;
                usingEmbeddedDb = false;
                dbMode = "MySQL";
                System.out.println("[Rooted] Connected to MySQL database.");
                return true;
            } catch (SQLException firstErr) {
                // If database doesn't exist yet (Unknown database 'rooted_db'), create it automatically
                if (firstErr.getMessage() != null && firstErr.getMessage().contains("Unknown database")) {
                    int slashIdx = targetUrl.indexOf('/', targetUrl.indexOf("://") + 3);
                    int qIdx = targetUrl.indexOf('?', slashIdx);
                    if (slashIdx > 0) {
                        String dbName = (qIdx > 0)
                                ? targetUrl.substring(slashIdx + 1, qIdx)
                                : targetUrl.substring(slashIdx + 1);
                        String serverUrl = targetUrl.substring(0, slashIdx + 1)
                                + (qIdx > 0 ? targetUrl.substring(qIdx) : "");

                        try (Connection serverConn = DriverManager.getConnection(serverUrl, user, pass);
                             Statement stmt = serverConn.createStatement()) {
                            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS `" + dbName
                                    + "` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci");
                        }
                        try (Connection conn = DriverManager.getConnection(targetUrl, user, pass)) {
                            activeUrl = targetUrl;
                            activeUser = user;
                            activePassword = pass;
                            usingEmbeddedDb = false;
                            dbMode = "MySQL";
                            System.out.println("[Rooted] Created & connected to MySQL database '" + dbName + "'.");
                            return true;
                        }
                    }
                }
                System.err.println("[Rooted] MySQL unavailable (" + firstErr.getMessage()
                        + "). Falling back to embedded local database...");
                return false;
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("[Rooted] MySQL setup skipped (" + e.getMessage()
                    + "). Falling back to embedded local database...");
            return false;
        }
    }

    private static String ensureUtf8Params(String url) {
        if (!url.startsWith("jdbc:mysql:")) {
            return url;
        }
        StringBuilder sb = new StringBuilder(url);
        if (!url.contains("?")) {
            sb.append("?useUnicode=true&characterEncoding=UTF-8");
        } else if (!url.contains("characterEncoding=")) {
            sb.append("&useUnicode=true&characterEncoding=UTF-8");
        }
        return sb.toString();
    }

    private static void configureEmbeddedH2() {
        try {
            Class.forName(H2_DRIVER);
            String dataDir = System.getProperty("user.home") + File.separator + ".rooted";
            new File(dataDir).mkdirs();
            String dbPath = new File(dataDir, "rooted_db").getAbsolutePath();

            activeUrl = "jdbc:h2:file:" + dbPath
                    + ";MODE=MySQL;DATABASE_TO_LOWER=TRUE;CASE_INSENSITIVE_IDENTIFIERS=TRUE";
            activeUser = "sa";
            activePassword = "";
            usingEmbeddedDb = true;
            dbMode = "Embedded Local DB (H2)";
            System.out.println("[Rooted] Using Embedded Local DB (H2 MySQL mode) at: " + dbPath);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Neither MySQL nor H2 JDBC driver could be loaded.", e);
        }
    }

    /**
     * Initializes database selection early (typically called by AppBootstrapListener).
     */
    public static void init() {
        ensureInitialized();
    }

    /**
     * Returns a new JDBC {@link Connection} from the active database.
     */
    public static Connection getConnection() throws SQLException {
        ensureInitialized();
        return DriverManager.getConnection(activeUrl, activeUser, activePassword);
    }

    public static String getDbMode() {
        ensureInitialized();
        return dbMode;
    }

    public static boolean isUsingEmbeddedDb() {
        ensureInitialized();
        return usingEmbeddedDb;
    }

    public static void closeQuietly(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException ignored) {
            }
        }
    }

    private DBUtil() {}
}
