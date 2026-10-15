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

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Controller for displaying an individual discussion thread and its replies.
 */
@WebServlet({"/threads", "/thread"})
public class ThreadServlet extends HttpServlet {

    private final ForumThreadDAO threadDAO = new ForumThreadDAO();
    private final PostDAO postDAO = new PostDAO();
    private final ForumDAO forumDAO = new ForumDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idParam = request.getParameter("id");
        if (idParam == null || idParam.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/forums");
            return;
        }

        try {
            int threadId = Integer.parseInt(idParam);
            Optional<ForumThread> threadOpt = threadDAO.findById(threadId);

            if (threadOpt.isEmpty()) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Discussion thread not found.");
                return;
            }

            ForumThread thread = threadOpt.get();
            List<Post> posts = postDAO.findByThreadId(thread.getId());
            Optional<Forum> forumOpt = forumDAO.findById(thread.getForumId());

            request.setAttribute("thread", thread);
            request.setAttribute("posts", posts);
            forumOpt.ifPresent(forum -> request.setAttribute("forum", forum));

            request.getRequestDispatcher("/WEB-INF/views/thread.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid thread ID.");
        } catch (SQLException e) {
            getServletContext().log("Database error in ThreadServlet", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to load thread details.");
        }
    }
}
