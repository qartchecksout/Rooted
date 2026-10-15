package com.rooted.dao;

import com.rooted.model.ForumThread;
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
 * Data Access Object for discussion threads.
 * Maps to the `threads` table and joins with `users` and `posts`.
 */
public class ForumThreadDAO {

    /**
     * Inserts a new thread into the database.
     */
    public boolean save(ForumThread thread) throws SQLException {
        String sql = "INSERT INTO threads (forum_id, user_id, title) VALUES (?, ?, ?)";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, thread.getForumId());
            stmt.setInt(2, thread.getUserId());
            stmt.setString(3, thread.getTitle());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        thread.setId(rs.getInt(1));
                    }
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Finds a thread by ID, including its author's username and post count.
     */
    public Optional<ForumThread> findById(int id) throws SQLException {
        String sql = "SELECT t.id, t.forum_id, t.user_id, t.title, t.created_at, " +
                     "       u.username AS author_name, " +
                     "       COUNT(p.id) AS post_count " +
                     "FROM threads t " +
                     "JOIN users u ON t.user_id = u.id " +
                     "LEFT JOIN posts p ON t.id = p.thread_id " +
                     "WHERE t.id = ? " +
                     "GROUP BY t.id, t.forum_id, t.user_id, t.title, t.created_at, u.username";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToThread(rs));
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Retrieves all threads in a forum, ordered by most recently created first.
     */
    public List<ForumThread> findByForumId(int forumId) throws SQLException {
        List<ForumThread> list = new ArrayList<>();
        String sql = "SELECT t.id, t.forum_id, t.user_id, t.title, t.created_at, " +
                     "       u.username AS author_name, " +
                     "       COUNT(p.id) AS post_count " +
                     "FROM threads t " +
                     "JOIN users u ON t.user_id = u.id " +
                     "LEFT JOIN posts p ON t.id = p.thread_id " +
                     "WHERE t.forum_id = ? " +
                     "GROUP BY t.id, t.forum_id, t.user_id, t.title, t.created_at, u.username " +
                     "ORDER BY t.created_at DESC";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, forumId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRowToThread(rs));
                }
            }
        }
        return list;
    }

    /**
     * Retrieves all threads created by a specific user.
     */
    public List<ForumThread> findByUserId(int userId) throws SQLException {
        List<ForumThread> list = new ArrayList<>();
        String sql = "SELECT t.id, t.forum_id, t.user_id, t.title, t.created_at, " +
                     "       u.username AS author_name, " +
                     "       COUNT(p.id) AS post_count " +
                     "FROM threads t " +
                     "JOIN users u ON t.user_id = u.id " +
                     "LEFT JOIN posts p ON t.id = p.thread_id " +
                     "WHERE t.user_id = ? " +
                     "GROUP BY t.id, t.forum_id, t.user_id, t.title, t.created_at, u.username " +
                     "ORDER BY t.created_at DESC";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRowToThread(rs));
                }
            }
        }
        return list;
    }

    private ForumThread mapRowToThread(ResultSet rs) throws SQLException {
        ForumThread thread = new ForumThread();
        thread.setId(rs.getInt("id"));
        thread.setForumId(rs.getInt("forum_id"));
        thread.setUserId(rs.getInt("user_id"));
        thread.setTitle(rs.getString("title"));
        thread.setAuthorName(rs.getString("author_name"));
        thread.setPostCount(rs.getInt("post_count"));

        Timestamp ts = rs.getTimestamp("created_at");
        if (ts != null) {
            thread.setCreatedAt(ts.toLocalDateTime());
        }
        return thread;
    }
}
