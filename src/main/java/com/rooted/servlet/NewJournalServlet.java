package com.rooted.servlet;

import com.rooted.dao.JournalDAO;
import com.rooted.model.Journal;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Controller for creating a personal garden journal entry.
 * Protected by AuthFilter.
 */
@WebServlet("/journals/new")
public class NewJournalServlet extends HttpServlet {

    private final JournalDAO journalDAO = new JournalDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher("/WEB-INF/views/new_journal.jsp").forward(request, response);
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
        String title = request.getParameter("title");
        String body = request.getParameter("body");

        title = (title != null) ? title.trim() : "";
        body = (body != null) ? body.trim() : "";

        request.setAttribute("title", title);
        request.setAttribute("body", body);

        if (title.length() < 3 || title.length() > 255) {
            request.setAttribute("error", "Journal title must be between 3 and 255 characters.");
            request.getRequestDispatcher("/WEB-INF/views/new_journal.jsp").forward(request, response);
            return;
        }

        if (body.length() < 5) {
            request.setAttribute("error", "Journal entry content must be at least 5 characters.");
            request.getRequestDispatcher("/WEB-INF/views/new_journal.jsp").forward(request, response);
            return;
        }

        try {
            Journal journal = new Journal(userId, title, body);
            boolean saved = journalDAO.save(journal);

            if (!saved) {
                request.setAttribute("error", "Failed to save journal entry. Please try again.");
                request.getRequestDispatcher("/WEB-INF/views/new_journal.jsp").forward(request, response);
                return;
            }

            response.sendRedirect(request.getContextPath() + "/journals?id=" + journal.getId());

        } catch (SQLException e) {
            getServletContext().log("Database error saving journal entry", e);
            request.setAttribute("error", "Database error occurred while saving your journal.");
            request.getRequestDispatcher("/WEB-INF/views/new_journal.jsp").forward(request, response);
        }
    }
}
