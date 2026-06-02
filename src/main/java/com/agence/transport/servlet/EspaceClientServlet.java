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

public class EspaceClientServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private VoyageDAO voyageDAO = new VoyageDAOImpl();
    private ReservationDAO reservationDAO = new ReservationDAOImpl();
    private PassagerDAO passagerDAO = new PassagerDAOImpl();

    private boolean estClient(HttpServletRequest request, HttpServletResponse response)
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
        if (!estClient(request, response)) return;

        String action = request.getParameter("action");

        if ("reserver".equals(action)) {
            // Afficher formulaire de reservation
            List<Voyage> voyages = voyageDAO.listerTous();
            request.setAttribute("voyages", voyages);
            request.setAttribute("vue", "reserver");
        } else if ("mes-reservations".equals(action)) {
            // Afficher les reservations du client
            HttpSession session = request.getSession(false);
            String loginClient = (String) session.getAttribute("login");
            List<ReservationTransport> mesReservations = reservationDAO.listerTous();
            request.setAttribute("mesReservations", mesReservations);
            request.setAttribute("vue", "mes-reservations");
        } else {
            // Page d'accueil espace client
            List<Voyage> voyages = voyageDAO.listerTous();
            request.setAttribute("voyages", voyages);
            request.setAttribute("vue", "accueil");
        }

        request.getRequestDispatcher("/WEB-INF/vues/espace-client.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!estClient(request, response)) return;

        String action = request.getParameter("action");

        if ("reserver".equals(action)) {
            try {
                HttpSession session = request.getSession(false);
                String loginClient = (String) session.getAttribute("login");

                // Trouver le passager correspondant
                List<Passager> passagers = passagerDAO.listerTous();
                Passager passagerClient = null;
                for (Passager p : passagers) {
                    if (p.getTelephone() != null) {
                        passagerClient = p;
                        break;
                    }
                }

                if (passagerClient == null) {
                    request.setAttribute("erreur", "Profil passager introuvable.");
                } else {
                    ReservationTransport r = new ReservationTransport();
                    r.setPassagerId(passagerClient.getId());
                    r.setVoyageId(Long.parseLong(request.getParameter("voyageId")));
                    r.setNbPlaces(Integer.parseInt(request.getParameter("nbPlaces")));

                    Voyage voyage = voyageDAO.trouverParId(r.getVoyageId());
                    r.setMontantTotal(voyage.getPrixPlaceFcfa() * r.getNbPlaces());
                    r.setNumeroBillet("BIL-" + java.util.UUID.randomUUID().toString().substring(0, 8).toUpperCase());
                    r.setStatut("CONFIRMEE");

                    reservationDAO.ajouter(r);
                    request.setAttribute("message", "Reservation confirmee ! Votre billet : " + r.getNumeroBillet());
                }
            } catch (Exception e) {
                request.setAttribute("erreur", "Erreur : " + e.getMessage());
            }
        }

        List<Voyage> voyages = voyageDAO.listerTous();
        request.setAttribute("voyages", voyages);
        request.setAttribute("vue", "accueil");
        request.getRequestDispatcher("/WEB-INF/vues/espace-client.jsp").forward(request, response);
    }
}
