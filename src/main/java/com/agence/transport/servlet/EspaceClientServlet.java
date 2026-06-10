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

    private static final int TAILLE_PAGE = 5;

    private boolean estClient(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return false;
        }
        return true;
    }

    private Passager getPassagerClient(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Utilisateur user = (Utilisateur) session.getAttribute("user");
        return passagerDAO.trouverParUtilisateurId(user.getId());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!estClient(request, response)) return;

        String action = request.getParameter("action");
        List<Voyage> voyages = voyageDAO.listerTous();

        if ("reserver".equals(action)) {
            request.setAttribute("voyages", voyages);
            request.setAttribute("vue", "reserver");

        } else if ("mes-reservations".equals(action)) {
            // Pagination des reservations du client
            Passager passagerClient = getPassagerClient(request);
            List<ReservationTransport> mesReservations = null;
            int total = 0;

            if (passagerClient != null) {
                int page = 1;
                try {
                    String pageParam = request.getParameter("page");
                    if (pageParam != null) page = Integer.parseInt(pageParam);
                } catch (NumberFormatException e) { page = 1; }

                total = reservationDAO.compterParPassager(passagerClient.getId());
                int totalPages = (int) Math.ceil((double) total / TAILLE_PAGE);
                if (totalPages == 0) totalPages = 1;
                if (page < 1) page = 1;
                if (page > totalPages) page = totalPages;

                mesReservations = reservationDAO.listerParPassager(passagerClient.getId(), page, TAILLE_PAGE);
                request.setAttribute("page", page);
                request.setAttribute("totalPages", totalPages);
            }

            request.setAttribute("mesReservations", mesReservations);
            request.setAttribute("total", total);
            request.setAttribute("vue", "mes-reservations");

        } else {
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
                Passager passagerClient = getPassagerClient(request);

                if (passagerClient == null) {
                    request.setAttribute("erreur", "Profil passager introuvable. Contactez l'administration.");
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
