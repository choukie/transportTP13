package com.agence.transport.filter;

import java.io.IOException;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;

public class SecurityHeadersFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Empecher le clickjacking
        httpResponse.setHeader("X-Frame-Options", "DENY");

        // Empecher le sniffing de type MIME
        httpResponse.setHeader("X-Content-Type-Options", "nosniff");

        // Protection XSS
        httpResponse.setHeader("X-XSS-Protection", "1; mode=block");

        // Forcer HTTPS (optionnel en dev)
        // httpResponse.setHeader("Strict-Transport-Security", "max-age=31536000");

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {}
}
