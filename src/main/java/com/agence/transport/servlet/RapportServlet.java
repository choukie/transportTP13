package com.agence.transport.servlet;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.agence.transport.dao.*;
import com.agence.transport.model.*;

public class RapportServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private boolean estConnecte(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return false;
        }
        return true;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!estConnecte(request, response)) return;

        List<Voyage> voyages = new VoyageDAOImpl().listerTous();
        List<ReservationTransport> reservations = new ReservationDAOImpl().listerTous();

        request.setAttribute("voyages", voyages);
        request.setAttribute("reservations", reservations);
        request.getRequestDispatcher("/WEB-INF/vues/rapports.jsp").forward(request, response);
    }
}
