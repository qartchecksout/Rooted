-- ============================================================
-- Rooted — Gardening Community Database Schema
-- Run this script in MySQL to create the database and tables.
-- Usage: mysql -u root -p < database/schema.sql
-- ============================================================

CREATE DATABASE IF NOT EXISTS rooted_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE rooted_db;

-- --------------------------------------------------------
-- Table: users
-- Stores all registered members of the community.
-- --------------------------------------------------------
CREATE TABLE IF NOT EXISTS users (
    id            INT          NOT NULL AUTO_INCREMENT,
    username      VARCHAR(50)  NOT NULL UNIQUE,
    email         VARCHAR(150) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,           -- BCrypt hash, never plain text
    bio           TEXT         DEFAULT NULL,       -- Optional member bio
    created_at    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------
-- Table: forums
-- The top-level discussion boards (e.g. "The Potting Shed").
-- --------------------------------------------------------
CREATE TABLE IF NOT EXISTS forums (
    id          INT          NOT NULL AUTO_INCREMENT,
    name        VARCHAR(100) NOT NULL,
    slug        VARCHAR(100) NOT NULL UNIQUE,      -- URL-friendly name e.g. "potting-shed"
    description VARCHAR(255) DEFAULT NULL,
    created_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------
-- Table: threads
-- A discussion thread inside a forum, started by a user.
-- --------------------------------------------------------
CREATE TABLE IF NOT EXISTS threads (
    id         INT          NOT NULL AUTO_INCREMENT,
    forum_id   INT          NOT NULL,
    user_id    INT          NOT NULL,
    title      VARCHAR(255) NOT NULL,
    created_at DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    FOREIGN KEY (forum_id) REFERENCES forums(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id)  REFERENCES users(id)  ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------
-- Table: posts
-- Individual replies inside a thread.
-- --------------------------------------------------------
CREATE TABLE IF NOT EXISTS posts (
    id         INT      NOT NULL AUTO_INCREMENT,
    thread_id  INT      NOT NULL,
    user_id    INT      NOT NULL,
    body       TEXT     NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    FOREIGN KEY (thread_id) REFERENCES threads(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id)   REFERENCES users(id)   ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------
-- Table: journals
-- Personal garden journal entries written by members.
-- --------------------------------------------------------
CREATE TABLE IF NOT EXISTS journals (
    id         INT          NOT NULL AUTO_INCREMENT,
    user_id    INT          NOT NULL,
    title      VARCHAR(255) NOT NULL,
    body       TEXT         NOT NULL,
    created_at DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------
-- Seed Data — default forums so the site isn't empty
-- --------------------------------------------------------
INSERT IGNORE INTO forums (name, slug, description) VALUES
    ('🌱 The Potting Shed', 'potting-shed',  'Beginner questions and growing advice'),
    ('🍂 Plant SOS',        'plant-sos',     'Pests, yellow leaves and plant troubles'),
    ('🌻 Show Your Patch',  'show-your-patch','Garden tours, projects and little victories');
