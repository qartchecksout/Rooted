package com.rooted.servlet;

import com.rooted.dao.ForumDAO;
import com.rooted.dao.ForumThreadDAO;
import com.rooted.dao.PostDAO;
import com.rooted.model.Forum;
import com.rooted.model.ForumThread;
import com.rooted.model.Post;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Controller for creating a new discussion thread.
 * Protected by AuthFilter.
 */
@WebServlet("/threads/new")
public class NewThreadServlet extends HttpServlet {

    private final ForumDAO forumDAO = new ForumDAO();
    private final ForumThreadDAO threadDAO = new ForumThreadDAO();
    private final PostDAO postDAO = new PostDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            List<Forum> forums = forumDAO.findAll();
            request.setAttribute("forums", forums);

            String forumIdParam = request.getParameter("forumId");
            if (forumIdParam != null && !forumIdParam.isEmpty()) {
                request.setAttribute("selectedForumId", forumIdParam);
            }

            request.getRequestDispatcher("/WEB-INF/views/new_thread.jsp").forward(request, response);

        } catch (SQLException e) {
            getServletContext().log("Failed to load forums for new thread form", e);
            response.sendRedirect(request.getContextPath() + "/forums");
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
        String forumIdStr = request.getParameter("forumId");
        String title = request.getParameter("title");
        String body = request.getParameter("body");

        title = (title != null) ? title.trim() : "";
        body = (body != null) ? body.trim() : "";

        request.setAttribute("title", title);
        request.setAttribute("body", body);
        request.setAttribute("selectedForumId", forumIdStr);

        try {
            List<Forum> forums = forumDAO.findAll();
            request.setAttribute("forums", forums);

            if (forumIdStr == null || forumIdStr.isEmpty()) {
                request.setAttribute("error", "Please select a discussion board.");
                request.getRequestDispatcher("/WEB-INF/views/new_thread.jsp").forward(request, response);
                return;
            }

            int forumId = Integer.parseInt(forumIdStr);

            if (title.length() < 5 || title.length() > 255) {
                request.setAttribute("error", "Title must be between 5 and 255 characters long.");
                request.getRequestDispatcher("/WEB-INF/views/new_thread.jsp").forward(request, response);
                return;
            }

            if (body.length() < 5) {
                request.setAttribute("error", "Your post content must be at least 5 characters long.");
                request.getRequestDispatcher("/WEB-INF/views/new_thread.jsp").forward(request, response);
                return;
            }

            // Create thread
            ForumThread thread = new ForumThread(forumId, userId, title);
            boolean threadSaved = threadDAO.save(thread);

            if (!threadSaved) {
                request.setAttribute("error", "Failed to start the discussion. Please try again.");
                request.getRequestDispatcher("/WEB-INF/views/new_thread.jsp").forward(request, response);
                return;
            }

            // Create initial post for the thread
            Post post = new Post(thread.getId(), userId, body);
            postDAO.save(post);

            response.sendRedirect(request.getContextPath() + "/threads?id=" + thread.getId());

        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid discussion board selected.");
            request.getRequestDispatcher("/WEB-INF/views/new_thread.jsp").forward(request, response);
        } catch (SQLException e) {
            getServletContext().log("Database error creating thread", e);
            request.setAttribute("error", "Database error occurred while saving your discussion.");
            request.getRequestDispatcher("/WEB-INF/views/new_thread.jsp").forward(request, response);
        }
    }
}
