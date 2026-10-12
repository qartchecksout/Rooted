package com.rooted.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

// Tells Tomcat: requests to /hello go to this class
@WebServlet("/hello")

public class HelloServlet extends HttpServlet 
{

    // doGet runs when a browser opens the URL (an HTTP GET request)
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException 
            {

                response.setContentType("text/html;charset=UTF-8");
                PrintWriter out = response.getWriter();
                out.println("<h1>Aur BKL</h1>");
                out.println("<p>tomcat localhost karwaa deta hai, java se banaaya hai oye hoye</p>");
            }
}