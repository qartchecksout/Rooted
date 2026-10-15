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
import java.util.Optional;

/**
 * Handles member authentication.
 * GET: Displays login form.
 * POST: Verifies credentials, initializes session, and redirects to destination or home.
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("userId") != null) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }

        // Pass optional redirect parameter if user was redirected from a protected page
        String redirect = request.getParameter("redirect");
        if (redirect != null && !redirect.isEmpty()) {
            request.setAttribute("redirect", redirect);
        }

        request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String usernameOrEmail = request.getParameter("usernameOrEmail");
        String password = request.getParameter("password");
        String redirect = request.getParameter("redirect");

        usernameOrEmail = (usernameOrEmail != null) ? usernameOrEmail.trim() : "";
        request.setAttribute("usernameOrEmail", usernameOrEmail);
        request.setAttribute("redirect", redirect);

        if (usernameOrEmail.isEmpty() || password == null || password.isEmpty()) {
            request.setAttribute("error", "Please provide both your username/email and password.");
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
            return;
        }

        try {
            // Check if input matches an email or username
            Optional<User> userOpt;
            if (usernameOrEmail.contains("@")) {
                userOpt = userDAO.findByEmail(usernameOrEmail);
            } else {
                userOpt = userDAO.findByUsername(usernameOrEmail);
            }

            if (userOpt.isEmpty()) {
                request.setAttribute("error", "Invalid username/email or password.");
                request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
                return;
            }

            User user = userOpt.get();

            // Verify password hash
            boolean matches = PasswordUtil.verifyPassword(password, user.getPasswordHash());
            if (!matches) {
                request.setAttribute("error", "Invalid username/email or password.");
                request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
                return;
            }

            // Authentication successful: reset session to prevent session fixation attacks
            HttpSession oldSession = request.getSession(false);
            if (oldSession != null) {
                oldSession.invalidate();
            }

            HttpSession session = request.getSession(true);
            session.setAttribute("userId", user.getId());
            session.setAttribute("username", user.getUsername());
            session.setAttribute("email", user.getEmail());

            if (redirect != null && !redirect.isEmpty() && redirect.startsWith("/")) {
                response.sendRedirect(request.getContextPath() + redirect);
            } else {
                response.sendRedirect(request.getContextPath() + "/");
            }

        } catch (SQLException e) {
            getServletContext().log("Database error during login", e);
            request.setAttribute("error", "A database error occurred. Please verify database connectivity.");
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
        }
    }
}
