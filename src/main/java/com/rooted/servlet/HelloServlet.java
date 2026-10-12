package com.rooted.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/hello")
public class HelloServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Data the page will display
        request.setAttribute("greeting", "Hello from a Servlet, shown by a JSP!");
        request.setAttribute("plants", List.of("Tulsi", "Rose", "Money plant"));

        // Hand the request over to the JSP, which builds the actual page
        request.getRequestDispatcher("/WEB-INF/views/hello.jsp")
               .forward(request, response);
    }
}