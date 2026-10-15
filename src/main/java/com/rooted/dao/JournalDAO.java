package com.rooted.dao;

import com.rooted.model.Journal;
import com.rooted.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object for personal garden journals.
 * Maps to the `journals` table and joins with `users`.
 */
public class JournalDAO {

    /**
     * Persists a new garden journal entry.
     */
    public boolean save(Journal journal) throws SQLException {
        String sql = "INSERT INTO journals (user_id, title, body) VALUES (?, ?, ?)";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, journal.getUserId());
            stmt.setString(2, journal.getTitle());
            stmt.setString(3, journal.getBody());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        journal.setId(rs.getInt(1));
                    }
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Finds a single journal entry by ID.
     */
    public Optional<Journal> findById(int id) throws SQLException {
        String sql = "SELECT j.id, j.user_id, j.title, j.body, j.created_at, " +
                     "       u.username AS author_name " +
                     "FROM journals j " +
                     "JOIN users u ON j.user_id = u.id " +
                     "WHERE j.id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToJournal(rs));
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Returns recent journal entries across all members, ordered by creation date descending.
     *
     * @param limit maximum records to return (if <= 0, returns up to 100 entries)
     */
    public List<Journal> findAll(int limit) throws SQLException {
        int max = (limit > 0) ? limit : 100;
        List<Journal> list = new ArrayList<>();
        String sql = "SELECT j.id, j.user_id, j.title, j.body, j.created_at, " +
                     "       u.username AS author_name " +
                     "FROM journals j " +
                     "JOIN users u ON j.user_id = u.id " +
                     "ORDER BY j.created_at DESC LIMIT ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, max);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRowToJournal(rs));
                }
            }
        }
        return list;
    }

    /**
     * Returns all journal entries written by a specific user.
     */
    public List<Journal> findByUserId(int userId) throws SQLException {
        List<Journal> list = new ArrayList<>();
        String sql = "SELECT j.id, j.user_id, j.title, j.body, j.created_at, " +
                     "       u.username AS author_name " +
                     "FROM journals j " +
                     "JOIN users u ON j.user_id = u.id " +
                     "WHERE j.user_id = ? " +
                     "ORDER BY j.created_at DESC";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRowToJournal(rs));
                }
            }
        }
        return list;
    }

    private Journal mapRowToJournal(ResultSet rs) throws SQLException {
        Journal journal = new Journal();
        journal.setId(rs.getInt("id"));
        journal.setUserId(rs.getInt("user_id"));
        journal.setTitle(rs.getString("title"));
        journal.setBody(rs.getString("body"));
        journal.setAuthorName(rs.getString("author_name"));

        Timestamp ts = rs.getTimestamp("created_at");
        if (ts != null) {
            journal.setCreatedAt(ts.toLocalDateTime());
        }
        return journal;
    }
}
