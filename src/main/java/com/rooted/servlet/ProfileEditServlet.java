package com.rooted.servlet;

import com.rooted.dao.UserDAO;
import com.rooted.model.User;

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
 * Controller for allowing a member to edit their personal bio and garden notes.
 * Protected by AuthFilter.
 */
@WebServlet("/profile/edit")
public class ProfileEditServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        int userId = (Integer) session.getAttribute("userId");

        try {
            Optional<User> userOpt = userDAO.findById(userId);
            if (userOpt.isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/home");
                return;
            }

            request.setAttribute("user", userOpt.get());
            request.getRequestDispatcher("/WEB-INF/views/edit_profile.jsp").forward(request, response);

        } catch (SQLException e) {
            getServletContext().log("Failed to load user for profile edit", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error loading profile.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        int userId = (Integer) session.getAttribute("userId");
        String bio = request.getParameter("bio");

        bio = (bio != null) ? bio.trim() : "";

        try {
            userDAO.updateBio(userId, bio);
            response.sendRedirect(request.getContextPath() + "/profile?id=" + userId + "&updated=true");

        } catch (SQLException e) {
            getServletContext().log("Failed to update user bio", e);
            request.setAttribute("error", "Failed to update profile. Please try again.");
            request.getRequestDispatcher("/WEB-INF/views/edit_profile.jsp").forward(request, response);
        }
    }
}
