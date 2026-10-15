package com.rooted.model;

import java.time.LocalDateTime;

/**
 * Represents a registered member of the Rooted community.
 * Maps to the `users` table in the database.
 *
 * Note: passwordHash is never exposed in views — it is only
 * used internally by UserDAO and the auth layer.
 */
public class User {

    private int           id;
    private String        username;
    private String        email;
    private String        passwordHash;   // BCrypt hash — never plain text
    private String        bio;
    private LocalDateTime createdAt;

    // --- Constructors ---

    public User() {}

    /** Constructor used when creating a new user (before DB insert). */
    public User(String username, String email, String passwordHash) {
        this.username     = username;
        this.email        = email;
        this.passwordHash = passwordHash;
    }

    // --- Getters & Setters ---

    public int getId()                        { return id; }
    public void setId(int id)                 { this.id = id; }

    public String getUsername()               { return username; }
    public void setUsername(String username)  { this.username = username; }

    public String getEmail()                  { return email; }
    public void setEmail(String email)        { this.email = email; }

    public String getPasswordHash()                       { return passwordHash; }
    public void setPasswordHash(String passwordHash)      { this.passwordHash = passwordHash; }

    public String getBio()                    { return bio; }
    public void setBio(String bio)            { this.bio = bio; }

    public LocalDateTime getCreatedAt()                   { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt)     { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return "User{id=" + id + ", username='" + username + "'}";
    }
}
