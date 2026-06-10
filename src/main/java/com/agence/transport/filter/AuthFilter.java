package com.agence.transport.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class AuthFilter implements Filter {

    private static final List<String> PAGES_ADMIN = Arrays.asList(
        "/dashboard", "/vehicules", "/voyages", "/passagers",
        "/reservations", "/statistiques", "/rapports", "/export", "/pdf"
    );
    private static final List<String> PAGES_AGENT = Arrays.asList(
        "/dashboard", "/voyages", "/passagers",
        "/reservations", "/rapports", "/export", "/pdf"
    );
    private static final String PAGE_CLIENT = "/espace-client";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String uri = httpRequest.getRequestURI();
        String contextPath = httpRequest.getContextPath();

        // Ressources publiques — toujours accessibles
        boolean estPublic = uri.equals(contextPath + "/login")
                || uri.equals(contextPath + "/inscription")
                || uri.startsWith(contextPath + "/css/")
                || uri.startsWith(contextPath + "/js/")
                || uri.startsWith(contextPath + "/images/");

        if (estPublic) {
            chain.doFilter(request, response);
            return;
        }

        // Verification session
        HttpSession session = httpRequest.getSession(false);
        boolean estConnecte = (session != null && session.getAttribute("user") != null);

        if (!estConnecte) {
            httpResponse.sendRedirect(contextPath + "/login");
            return;
        }

        httpResponse.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        httpResponse.setHeader("Pragma", "no-cache");
        httpResponse.setDateHeader("Expires", 0);

        // Controle d'acces par role
        String role = (String) session.getAttribute("role");
        String path = uri.substring(contextPath.length());

        if (path.isEmpty()) path = "/";

        boolean accesAutorise = false;

        if ("ADMIN".equals(role)) {
            accesAutorise = true;
        } else if ("AGENT".equals(role)) {
            accesAutorise = PAGES_AGENT.contains(path);
        } else if ("CLIENT".equals(role)) {
            accesAutorise = path.equals(PAGE_CLIENT) || path.startsWith(PAGE_CLIENT + "?");
        }

        if (accesAutorise) {
            chain.doFilter(request, response);
        } else {
            if ("CLIENT".equals(role)) {
                httpResponse.sendRedirect(contextPath + "/espace-client");
            } else {
                httpResponse.sendRedirect(contextPath + "/dashboard");
            }
        }
    }

    @Override
    public void destroy() {}
}