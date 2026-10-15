package com.rooted.util;

import at.favre.lib.crypto.bcrypt.BCrypt;

/**
 * Utility for hashing and verifying user passwords using BCrypt.
 */
public final class PasswordUtil {

    private static final int BCRYPT_COST = 12;

    /**
     * Hashes a plain-text password using BCrypt with a default salt and cost factor 12.
     *
     * @param plainPassword the plain-text password
     * @return BCrypt hash string
     */
    public static String hashPassword(String plainPassword) {
        if (plainPassword == null || plainPassword.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty.");
        }
        return BCrypt.withDefaults().hashToString(BCRYPT_COST, plainPassword.toCharArray());
    }

    /**
     * Verifies a plain-text password against a stored BCrypt hash.
     *
     * @param plainPassword the plain-text password
     * @param bcryptHash the stored BCrypt hash
     * @return true if password matches, false otherwise
     */
    public static boolean verifyPassword(String plainPassword, String bcryptHash) {
        if (plainPassword == null || bcryptHash == null) {
            return false;
        }
        BCrypt.Result result = BCrypt.verifyer().verify(plainPassword.toCharArray(), bcryptHash);
        return result.verified;
    }

    private PasswordUtil() {}
}
