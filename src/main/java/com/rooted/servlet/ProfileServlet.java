package com.rooted.servlet;

import com.rooted.dao.ForumThreadDAO;
import com.rooted.dao.JournalDAO;
import com.rooted.dao.UserDAO;
import com.rooted.model.ForumThread;
import com.rooted.model.Journal;
import com.rooted.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Controller for viewing a member's public profile, garden journals, and forum topics.
 */
@WebServlet({"/profile", "/user"})
public class ProfileServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();
    private final ForumThreadDAO threadDAO = new ForumThreadDAO();
    private final JournalDAO journalDAO = new JournalDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idParam = request.getParameter("id");
        int targetUserId = -1;

        if (idParam != null && !idParam.isEmpty()) {
            try {
                targetUserId = Integer.parseInt(idParam);
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid member ID.");
                return;
            }
        } else {
            // Default to current logged-in user if available
            HttpSession session = request.getSession(false);
            if (session != null && session.getAttribute("userId") != null) {
                targetUserId = (Integer) session.getAttribute("userId");
            } else {
                response.sendRedirect(request.getContextPath() + "/members");
                return;
            }
        }

        try {
            Optional<User> userOpt = userDAO.findById(targetUserId);
            if (userOpt.isEmpty()) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Member not found.");
                return;
            }

            User profileUser = userOpt.get();
            List<ForumThread> userThreads = threadDAO.findByUserId(profileUser.getId());
            List<Journal> userJournals = journalDAO.findByUserId(profileUser.getId());

            HttpSession session = request.getSession(false);
            boolean isOwnProfile = (session != null &&
                    session.getAttribute("userId") != null &&
                    (Integer) session.getAttribute("userId") == profileUser.getId());

            request.setAttribute("profileUser", profileUser);
            request.setAttribute("userThreads", userThreads);
            request.setAttribute("userJournals", userJournals);
            request.setAttribute("isOwnProfile", isOwnProfile);

            request.getRequestDispatcher("/WEB-INF/views/profile.jsp").forward(request, response);

        } catch (SQLException e) {
            getServletContext().log("Database error in ProfileServlet", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to load member profile.");
        }
    }
}
