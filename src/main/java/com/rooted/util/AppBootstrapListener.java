package com.rooted.util;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Automatically initializes the database schema and starter data when the
 * web application starts up (works on both MySQL and Embedded H2 fallback).
 */
@WebListener
public class AppBootstrapListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        DBUtil.init();
        sce.getServletContext().setAttribute("dbMode", DBUtil.getDbMode());

        try (Connection conn = DBUtil.getConnection()) {
            createSchemaIfNeeded(conn);
            seedForumsIfEmpty(conn);
            seedStarterContentIfNoUsers(conn);
        } catch (SQLException e) {
            sce.getServletContext().log("[Rooted] Failed to initialize database schema", e);
            System.err.println("[Rooted] Database bootstrap error: " + e.getMessage());
        }
    }

    private void createSchemaIfNeeded(Connection conn) throws SQLException {
        String[] ddlStatements = {
            "CREATE TABLE IF NOT EXISTS users (" +
            "    id            INT          NOT NULL AUTO_INCREMENT," +
            "    username      VARCHAR(50)  NOT NULL UNIQUE," +
            "    email         VARCHAR(150) NOT NULL UNIQUE," +
            "    password_hash VARCHAR(255) NOT NULL," +
            "    bio           TEXT         DEFAULT NULL," +
            "    created_at    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP," +
            "    PRIMARY KEY (id)" +
            ")",

            "CREATE TABLE IF NOT EXISTS forums (" +
            "    id          INT          NOT NULL AUTO_INCREMENT," +
            "    name        VARCHAR(100) NOT NULL," +
            "    slug        VARCHAR(100) NOT NULL UNIQUE," +
            "    description VARCHAR(255) DEFAULT NULL," +
            "    created_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP," +
            "    PRIMARY KEY (id)" +
            ")",

            "CREATE TABLE IF NOT EXISTS threads (" +
            "    id         INT          NOT NULL AUTO_INCREMENT," +
            "    forum_id   INT          NOT NULL," +
            "    user_id    INT          NOT NULL," +
            "    title      VARCHAR(255) NOT NULL," +
            "    created_at DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP," +
            "    PRIMARY KEY (id)," +
            "    FOREIGN KEY (forum_id) REFERENCES forums(id) ON DELETE CASCADE," +
            "    FOREIGN KEY (user_id)  REFERENCES users(id)  ON DELETE CASCADE" +
            ")",

            "CREATE TABLE IF NOT EXISTS posts (" +
            "    id         INT      NOT NULL AUTO_INCREMENT," +
            "    thread_id  INT      NOT NULL," +
            "    user_id    INT      NOT NULL," +
            "    body       TEXT     NOT NULL," +
            "    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP," +
            "    PRIMARY KEY (id)," +
            "    FOREIGN KEY (thread_id) REFERENCES threads(id) ON DELETE CASCADE," +
            "    FOREIGN KEY (user_id)   REFERENCES users(id)   ON DELETE CASCADE" +
            ")",

            "CREATE TABLE IF NOT EXISTS journals (" +
            "    id         INT          NOT NULL AUTO_INCREMENT," +
            "    user_id    INT          NOT NULL," +
            "    title      VARCHAR(255) NOT NULL," +
            "    body       TEXT         NOT NULL," +
            "    created_at DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP," +
            "    PRIMARY KEY (id)," +
            "    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE" +
            ")"
        };

        try (Statement stmt = conn.createStatement()) {
            for (String ddl : ddlStatements) {
                stmt.execute(ddl);
            }
        }
    }

    private void seedForumsIfEmpty(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM forums")) {
            if (rs.next() && rs.getInt(1) > 0) {
                return;
            }
        }

        String insertForum = "INSERT INTO forums (name, slug, description) VALUES (?, ?, ?)";
        String[][] defaults = {
            {"The Potting Shed", "potting-shed", "Beginner questions and growing advice"},
            {"Plant SOS", "plant-sos", "Pests, yellow leaves and plant troubles"},
            {"Show Your Patch", "show-your-patch", "Garden tours, projects and little victories"}
        };

        try (PreparedStatement ps = conn.prepareStatement(insertForum)) {
            for (String[] row : defaults) {
                ps.setString(1, row[0]);
                ps.setString(2, row[1]);
                ps.setString(3, row[2]);
                ps.executeUpdate();
            }
        }
        System.out.println("[Rooted] Seeded default discussion boards.");
    }

    private void seedStarterContentIfNoUsers(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM users")) {
            if (rs.next() && rs.getInt(1) > 0) {
                return;
            }
        }

        // Create a starter community member so a brand-new clone has real clickable threads & journals
        int userId = -1;
        String demoHash = PasswordUtil.hashPassword("garden123");
        String insertUser = "INSERT INTO users (username, email, password_hash, bio) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(insertUser, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, "FernAndFound");
            ps.setString(2, "fern@rooted.garden");
            ps.setString(3, demoHash);
            ps.setString(4, "Balcony cherry tomatoes, basil pots, and learning something new every season.");
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    userId = keys.getInt(1);
                }
            }
        }

        if (userId <= 0) {
            return;
        }

        int firstForumId = 1;
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT id FROM forums ORDER BY id ASC LIMIT 1")) {
            if (rs.next()) {
                firstForumId = rs.getInt(1);
            }
        }

        int threadId = -1;
        String insertThread = "INSERT INTO threads (forum_id, user_id, title) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(insertThread, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, firstForumId);
            ps.setInt(2, userId);
            ps.setString(3, "Welcome to Rooted! What are you growing right now?");
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    threadId = keys.getInt(1);
                }
            }
        }

        if (threadId > 0) {
            String insertPost = "INSERT INTO posts (thread_id, user_id, body) VALUES (?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(insertPost)) {
                ps.setInt(1, threadId);
                ps.setInt(2, userId);
                ps.setString(3, "Pull up a chair and tell us about your current plants, windowsills, or backyard beds. Everyone starts somewhere!");
                ps.executeUpdate();
            }
        }

        String insertJournal = "INSERT INTO journals (user_id, title, body) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(insertJournal)) {
            ps.setInt(1, userId);
            ps.setString(2, "My first balcony tomato plant");
            ps.setString(3, "Three weeks in, and I finally have my first tiny green tomatoes!\n\nWatering every morning before the sun hits the railing has made a huge difference, and pinching off the lower suckers kept the plant nice and airy.");
            ps.executeUpdate();
        }

        System.out.println("[Rooted] Seeded starter gardener (FernAndFound / garden123) and welcome posts.");
    }
}
