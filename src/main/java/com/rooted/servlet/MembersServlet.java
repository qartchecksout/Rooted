package com.rooted.servlet;

import com.rooted.dao.UserDAO;
import com.rooted.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Controller for browsing the community members directory.
 */
@WebServlet({"/members", "/member-list"})
public class MembersServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            List<User> members = userDAO.findAll();
            request.setAttribute("members", members);
            request.getRequestDispatcher("/WEB-INF/views/members.jsp").forward(request, response);

        } catch (SQLException e) {
            getServletContext().log("Database error loading member list", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to load community members.");
        }
    }
}
