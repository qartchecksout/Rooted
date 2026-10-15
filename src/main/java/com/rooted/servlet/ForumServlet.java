package com.rooted.servlet;

import com.rooted.dao.ForumDAO;
import com.rooted.dao.ForumThreadDAO;
import com.rooted.model.Forum;
import com.rooted.model.ForumThread;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Controller for forum boards.
 * Displays all discussion threads within a specific forum.
 */
@WebServlet({"/forums", "/forum"})
public class ForumServlet extends HttpServlet {

    private final ForumDAO forumDAO = new ForumDAO();
    private final ForumThreadDAO threadDAO = new ForumThreadDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idParam = request.getParameter("id");
        String slugParam = request.getParameter("slug");

        if ((idParam == null || idParam.isEmpty()) && (slugParam == null || slugParam.isEmpty())) {
            // No specific forum requested — show all forums
            try {
                List<Forum> forums = forumDAO.findAll();
                request.setAttribute("forums", forums);
                request.getRequestDispatcher("/WEB-INF/views/home.jsp").forward(request, response);
                return;
            } catch (SQLException e) {
                getServletContext().log("Failed to load forums", e);
                response.sendRedirect(request.getContextPath() + "/home");
                return;
            }
        }

        try {
            Optional<Forum> forumOpt = Optional.empty();

            if (idParam != null && !idParam.isEmpty()) {
                try {
                    int forumId = Integer.parseInt(idParam);
                    forumOpt = forumDAO.findById(forumId);
                } catch (NumberFormatException ignored) {
                }
            } else if (slugParam != null && !slugParam.isEmpty()) {
                forumOpt = forumDAO.findBySlug(slugParam);
            }

            if (forumOpt.isEmpty()) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "The requested forum board does not exist.");
                return;
            }

            Forum forum = forumOpt.get();
            List<ForumThread> threads = threadDAO.findByForumId(forum.getId());

            request.setAttribute("forum", forum);
            request.setAttribute("threads", threads);

            request.getRequestDispatcher("/WEB-INF/views/forum.jsp").forward(request, response);

        } catch (SQLException e) {
            getServletContext().log("Database error in ForumServlet", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to load forum threads.");
        }
    }
}
