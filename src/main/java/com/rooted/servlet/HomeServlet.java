package com.rooted.servlet;

import com.rooted.dao.ForumDAO;
import com.rooted.dao.JournalDAO;
import com.rooted.model.Forum;
import com.rooted.model.Journal;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Controller for the Rooted community homepage.
 * Queries active forums with live thread counts and recent garden journals.
 */
@WebServlet({"/home", "/index"})
public class HomeServlet extends HttpServlet {

    private final ForumDAO forumDAO = new ForumDAO();
    private final JournalDAO journalDAO = new JournalDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            List<Forum> forums = forumDAO.findAll();
            List<Journal> recentJournals = journalDAO.findAll(5);

            request.setAttribute("forums", forums);
            request.setAttribute("recentJournals", recentJournals);

        } catch (SQLException e) {
            getServletContext().log("Failed to load home data from database", e);
            request.setAttribute("error", "Unable to retrieve community data. Please check database connection.");
        }

        request.getRequestDispatcher("/WEB-INF/views/home.jsp").forward(request, response);
    }
}
