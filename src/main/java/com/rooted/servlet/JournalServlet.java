package com.rooted.servlet;

import com.rooted.dao.JournalDAO;
import com.rooted.model.Journal;

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
 * Controller for browsing community garden journals or viewing an individual entry.
 */
@WebServlet({"/journals", "/journal"})
public class JournalServlet extends HttpServlet {

    private final JournalDAO journalDAO = new JournalDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idParam = request.getParameter("id");

        try {
            if (idParam != null && !idParam.isEmpty()) {
                // View single journal entry
                int journalId = Integer.parseInt(idParam);
                Optional<Journal> journalOpt = journalDAO.findById(journalId);

                if (journalOpt.isEmpty()) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Garden journal entry not found.");
                    return;
                }

                request.setAttribute("journal", journalOpt.get());
                request.getRequestDispatcher("/WEB-INF/views/journal.jsp").forward(request, response);

            } else {
                // View all community journals
                List<Journal> journals = journalDAO.findAll(50);
                request.setAttribute("journals", journals);
                request.getRequestDispatcher("/WEB-INF/views/journals.jsp").forward(request, response);
            }

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid journal entry ID.");
        } catch (SQLException e) {
            getServletContext().log("Database error in JournalServlet", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to load garden journals.");
        }
    }
}
