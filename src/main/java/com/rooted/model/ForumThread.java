package com.rooted.model;

import java.time.LocalDateTime;

/**
 * Represents a discussion thread inside a forum.
 * Maps to the `threads` table in the database.
 *
 * Named ForumThread (not Thread) to avoid conflict with java.lang.Thread.
 */
public class ForumThread {

    private int           id;
    private int           forumId;
    private int           userId;
    private String        title;
    private LocalDateTime createdAt;

    // Computed fields — populated by DAO via JOINs, not stored in DB
    private String        authorName;     // username from users table
    private String        forumName;      // name from forums table
    private int           postCount;      // COUNT of posts in this thread

    public ForumThread() {}

    public ForumThread(int forumId, int userId, String title) {
        this.forumId = forumId;
        this.userId  = userId;
        this.title   = title;
    }

    public int getId()                         { return id; }
    public void setId(int id)                  { this.id = id; }

    public int getForumId()                    { return forumId; }
    public void setForumId(int forumId)        { this.forumId = forumId; }

    public int getUserId()                     { return userId; }
    public void setUserId(int userId)          { this.userId = userId; }

    public String getTitle()                   { return title; }
    public void setTitle(String title)         { this.title = title; }

    public LocalDateTime getCreatedAt()                   { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt)     { this.createdAt = createdAt; }

    public String getAuthorName()                         { return authorName; }
    public void setAuthorName(String authorName)          { this.authorName = authorName; }

    public String getForumName()                          { return forumName; }
    public void setForumName(String forumName)            { this.forumName = forumName; }

    public int getPostCount()                  { return postCount; }
    public void setPostCount(int postCount)    { this.postCount = postCount; }

    @Override
    public String toString() {
        return "ForumThread{id=" + id + ", title='" + title + "', forumId=" + forumId + "}";
    }
}
