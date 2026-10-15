package com.rooted.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Filter that protects routes requiring an authenticated member session.
 * If not logged in, redirects to /login with the intended redirect URL.
 */
@WebFilter(urlPatterns = {"/threads/new", "/reply", "/journals/new", "/profile/edit"})
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        HttpSession session = request.getSession(false);
        boolean isLoggedIn = (session != null && session.getAttribute("userId") != null);

        if (isLoggedIn) {
            chain.doFilter(req, res);
        } else {
            String uri = request.getRequestURI();
            String contextPath = request.getContextPath();
            String relative = uri.substring(contextPath.length());
            String query = request.getQueryString();
            if (query != null && !query.isEmpty()) {
                relative += "?" + query;
            }

            String target = URLEncoder.encode(relative, StandardCharsets.UTF_8);
            response.sendRedirect(contextPath + "/login?redirect=" + target);
        }
    }
}
