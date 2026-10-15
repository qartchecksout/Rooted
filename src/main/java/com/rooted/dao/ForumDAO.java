package com.rooted.dao;

import com.rooted.model.Forum;
import com.rooted.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object for Forum boards.
 * Maps to the `forums` table and computes thread counts.
 */
public class ForumDAO {

    /**
     * Retrieves all forums along with the computed count of threads in each forum.
     */
    public List<Forum> findAll() throws SQLException {
        List<Forum> forums = new ArrayList<>();
        String sql = "SELECT f.id, f.name, f.slug, f.description, f.created_at, " +
                     "       COUNT(t.id) AS thread_count " +
                     "FROM forums f " +
                     "LEFT JOIN threads t ON f.id = t.forum_id " +
                     "GROUP BY f.id, f.name, f.slug, f.description, f.created_at " +
                     "ORDER BY f.id ASC";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                forums.add(mapRowToForum(rs));
            }
        }
        return forums;
    }

    /**
     * Finds a single forum by its ID, including its thread count.
     */
    public Optional<Forum> findById(int id) throws SQLException {
        String sql = "SELECT f.id, f.name, f.slug, f.description, f.created_at, " +
                     "       COUNT(t.id) AS thread_count " +
                     "FROM forums f " +
                     "LEFT JOIN threads t ON f.id = t.forum_id " +
                     "WHERE f.id = ? " +
                     "GROUP BY f.id, f.name, f.slug, f.description, f.created_at";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToForum(rs));
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Finds a single forum by its URL slug.
     */
    public Optional<Forum> findBySlug(String slug) throws SQLException {
        String sql = "SELECT f.id, f.name, f.slug, f.description, f.created_at, " +
                     "       COUNT(t.id) AS thread_count " +
                     "FROM forums f " +
                     "LEFT JOIN threads t ON f.id = t.forum_id " +
                     "WHERE f.slug = ? " +
                     "GROUP BY f.id, f.name, f.slug, f.description, f.created_at";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, slug);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToForum(rs));
                }
            }
        }
        return Optional.empty();
    }

    private Forum mapRowToForum(ResultSet rs) throws SQLException {
        Forum forum = new Forum();
        forum.setId(rs.getInt("id"));
        forum.setName(rs.getString("name"));
        forum.setSlug(rs.getString("slug"));
        forum.setDescription(rs.getString("description"));
        forum.setThreadCount(rs.getInt("thread_count"));

        Timestamp ts = rs.getTimestamp("created_at");
        if (ts != null) {
            forum.setCreatedAt(ts.toLocalDateTime());
        }
        return forum;
    }
}
