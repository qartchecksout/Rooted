package com.rooted.model;

import java.time.LocalDateTime;

/**
 * Represents a top-level discussion board (e.g. "The Potting Shed").
 * Maps to the `forums` table in the database.
 *
 * threadCount is a computed field populated by ForumDAO via a COUNT query —
 * it is NOT a column in the forums table.
 */
public class Forum {

    private int           id;
    private String        name;
    private String        slug;            // URL-friendly name e.g. "potting-shed"
    private String        description;
    private LocalDateTime createdAt;

    // Computed field — populated by DAO via JOIN/COUNT, not stored in DB
    private int           threadCount;

    // --- Constructors ---

    public Forum() {}

    public Forum(String name, String slug, String description) {
        this.name        = name;
        this.slug        = slug;
        this.description = description;
    }

    // --- Getters & Setters ---

    public int getId()                        { return id; }
    public void setId(int id)                 { this.id = id; }

    public String getName()                   { return name; }
    public void setName(String name)          { this.name = name; }

    public String getSlug()                   { return slug; }
    public void setSlug(String slug)          { this.slug = slug; }

    public String getDescription()                    { return description; }
    public void setDescription(String description)    { this.description = description; }

    public LocalDateTime getCreatedAt()               { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public int getThreadCount()               { return threadCount; }
    public void setThreadCount(int count)     { this.threadCount = count; }

    @Override
    public String toString() {
        return "Forum{id=" + id + ", name='" + name + "', slug='" + slug + "'}";
    }
}
