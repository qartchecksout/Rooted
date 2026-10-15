package com.rooted.dao;

import com.rooted.model.Post;
import com.rooted.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for forum posts/replies.
 * Maps to the `posts` table.
 */
public class PostDAO {

    /**
     * Inserts a new reply/post into a thread.
     */
    public boolean save(Post post) throws SQLException {
        String sql = "INSERT INTO posts (thread_id, user_id, body) VALUES (?, ?, ?)";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, post.getThreadId());
            stmt.setInt(2, post.getUserId());
            stmt.setString(3, post.getBody());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        post.setId(rs.getInt(1));
                    }
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Retrieves all posts in a thread in chronological order (oldest first).
     */
    public List<Post> findByThreadId(int threadId) throws SQLException {
        List<Post> posts = new ArrayList<>();
        String sql = "SELECT p.id, p.thread_id, p.user_id, p.body, p.created_at, " +
                     "       u.username AS author_name " +
                     "FROM posts p " +
                     "JOIN users u ON p.user_id = u.id " +
                     "WHERE p.thread_id = ? " +
                     "ORDER BY p.created_at ASC";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, threadId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    posts.add(mapRowToPost(rs));
                }
            }
        }
        return posts;
    }

    /**
     * Returns the total count of posts in a thread.
     */
    public int countByThreadId(int threadId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM posts WHERE thread_id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, threadId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    private Post mapRowToPost(ResultSet rs) throws SQLException {
        Post post = new Post();
        post.setId(rs.getInt("id"));
        post.setThreadId(rs.getInt("thread_id"));
        post.setUserId(rs.getInt("user_id"));
        post.setBody(rs.getString("body"));
        post.setAuthorName(rs.getString("author_name"));

        Timestamp ts = rs.getTimestamp("created_at");
        if (ts != null) {
            post.setCreatedAt(ts.toLocalDateTime());
        }
        return post;
    }
}
