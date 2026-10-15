package com.rooted.servlet;

import com.rooted.dao.UserDAO;
import com.rooted.model.User;
import com.rooted.util.PasswordUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.regex.Pattern;

/**
 * Handles member registration.
 * GET: Displays registration form.
 * POST: Validates input, hashes password, saves user to DB, and starts a session.
 */
@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_]{3,30}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // If user is already logged in, redirect home
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("userId") != null) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }

        request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String bio = request.getParameter("bio");

        // Trim values
        username = (username != null) ? username.trim() : "";
        email = (email != null) ? email.trim() : "";
        bio = (bio != null) ? bio.trim() : "";

        // Keep values in request to repopulate on error
        request.setAttribute("username", username);
        request.setAttribute("email", email);
        request.setAttribute("bio", bio);

        // Validation
        if (!USERNAME_PATTERN.matcher(username).matches()) {
            request.setAttribute("error", "Username must be 3-30 characters long and contain only letters, numbers, and underscores.");
            request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
            return;
        }

        if (!EMAIL_PATTERN.matcher(email).matches()) {
            request.setAttribute("error", "Please enter a valid email address.");
            request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
            return;
        }

        if (password == null || password.length() < 6) {
            request.setAttribute("error", "Password must be at least 6 characters long.");
            request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
            return;
        }

        if (!password.equals(confirmPassword)) {
            request.setAttribute("error", "Passwords do not match.");
            request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
            return;
        }

        try {
            if (userDAO.findByUsername(username).isPresent()) {
                request.setAttribute("error", "The username '" + username + "' is already taken. Please pick another.");
                request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
                return;
            }

            if (userDAO.findByEmail(email).isPresent()) {
                request.setAttribute("error", "An account with email '" + email + "' already exists. Please login instead.");
                request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
                return;
            }

            // Hash password and persist
            String passwordHash = PasswordUtil.hashPassword(password);
            User newUser = new User(username, email, passwordHash);
            if (!bio.isEmpty()) {
                newUser.setBio(bio);
            }

            boolean saved = userDAO.save(newUser);
            if (!saved) {
                request.setAttribute("error", "Registration could not be completed. Please try again.");
                request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
                return;
            }

            // Create new session
            HttpSession session = request.getSession(true);
            session.setAttribute("userId", newUser.getId());
            session.setAttribute("username", newUser.getUsername());
            session.setAttribute("email", newUser.getEmail());

            response.sendRedirect(request.getContextPath() + "/");

        } catch (SQLException e) {
            getServletContext().log("Database error during registration", e);
            request.setAttribute("error", "A database error occurred. Please make sure the database is running and try again.");
            request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
        }
    }
}
