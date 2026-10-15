package com.rooted.model;

import java.time.LocalDateTime;

/**
 * Represents an individual reply inside a forum thread.
 * Maps to the `posts` table in the database.
 *
 * authorName is a computed field populated by PostDAO via a JOIN
 * with the users table — it is NOT stored in the posts table.
 */
public class Post {

    private int           id;
    private int           threadId;
    private int           userId;
    private String        body;
    private LocalDateTime createdAt;

    // Computed field — populated by DAO via JOIN, not stored in DB
    private String        authorName;    // username from users table

    // --- Constructors ---

    public Post() {}

    /** Constructor used when creating a new post (before DB insert). */
    public Post(int threadId, int userId, String body) {
        this.threadId = threadId;
        this.userId   = userId;
        this.body     = body;
    }

    // --- Getters & Setters ---

    public int getId()                         { return id; }
    public void setId(int id)                  { this.id = id; }

    public int getThreadId()                   { return threadId; }
    public void setThreadId(int threadId)      { this.threadId = threadId; }

    public int getUserId()                     { return userId; }
    public void setUserId(int userId)          { this.userId = userId; }

    public String getBody()                    { return body; }
    public void setBody(String body)           { this.body = body; }

    public LocalDateTime getCreatedAt()                   { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt)     { this.createdAt = createdAt; }

    public String getAuthorName()                         { return authorName; }
    public void setAuthorName(String authorName)          { this.authorName = authorName; }

    @Override
    public String toString() {
        return "Post{id=" + id + ", threadId=" + threadId + ", author='" + authorName + "'}";
    }
}
