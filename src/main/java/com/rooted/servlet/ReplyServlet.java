package com.rooted.servlet;

import com.rooted.dao.PostDAO;
import com.rooted.model.Post;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Controller for posting a reply to an existing discussion thread.
 * Protected by AuthFilter.
 */
@WebServlet("/reply")
public class ReplyServlet extends HttpServlet {

    private final PostDAO postDAO = new PostDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        int userId = (Integer) session.getAttribute("userId");
        String threadIdStr = request.getParameter("threadId");
        String body = request.getParameter("body");

        body = (body != null) ? body.trim() : "";

        if (threadIdStr == null || threadIdStr.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/forums");
            return;
        }

        try {
            int threadId = Integer.parseInt(threadIdStr);

            if (body.isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/threads?id=" + threadId);
                return;
            }

            Post post = new Post(threadId, userId, body);
            postDAO.save(post);

            response.sendRedirect(request.getContextPath() + "/threads?id=" + threadId);

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid thread ID.");
        } catch (SQLException e) {
            getServletContext().log("Failed to save post reply", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error saving reply.");
        }
    }
}
