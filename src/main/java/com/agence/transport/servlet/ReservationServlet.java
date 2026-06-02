package com.agence.transport.servlet;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.agence.transport.dao.*;
import com.agence.transport.model.*;
import com.agence.transport.util.SmsService;

public class ReservationServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ReservationDAO reservationDAO = new ReservationDAOImpl();
    private PassagerDAO passagerDAO = new PassagerDAOImpl();
    private VoyageDAO voyageDAO = new VoyageDAOImpl();

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

        List<ReservationTransport> reservations = reservationDAO.listerTous();
        List<Passager> passagers = passagerDAO.listerTous();
        List<Voyage> voyages = voyageDAO.listerTous();
        request.setAttribute("reservations", reservations);
        request.setAttribute("passagers", passagers);
        request.setAttribute("voyages", voyages);
        request.getRequestDispatcher("/WEB-INF/vues/reservations.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!estConnecte(request, response)) return;

        String action = request.getParameter("action");

        try {
            if ("ajouter".equals(action)) {
                ReservationTransport r = new ReservationTransport();
                r.setPassagerId(Long.parseLong(request.getParameter("passagerId")));
                r.setVoyageId(Long.parseLong(request.getParameter("voyageId")));
                r.setNbPlaces(Integer.parseInt(request.getParameter("nbPlaces")));

                // Calcul montant
                Voyage voyage = voyageDAO.trouverParId(r.getVoyageId());
                double montant = voyage.getPrixPlaceFcfa() * r.getNbPlaces();
                r.setMontantTotal(montant);

                // Generation numero billet
                r.setNumeroBillet("BIL-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
                r.setStatut(request.getParameter("statut") != null ? request.getParameter("statut") : "CONFIRMEE");

                reservationDAO.ajouter(r);

                // Envoi SMS confirmation
                Passager passager = passagerDAO.trouverParId(r.getPassagerId());
                if (passager != null && passager.getTelephone() != null) {
                    boolean smsSent = SmsService.envoyerConfirmationBillet(
                        passager.getTelephone(),
                        r.getNumeroBillet(),
                        voyage.getVilleDepart(),
                        voyage.getVilleArrivee(),
                        voyage.getDateHeureDepart()
                    );
                    String smsInfo = smsSent ? " | SMS envoye au " + passager.getTelephone() : "";
                    request.setAttribute("message", "Reservation confirmee ! Billet : " + r.getNumeroBillet() + smsInfo);
                } else {
                    request.setAttribute("message", "Reservation confirmee ! Billet : " + r.getNumeroBillet());
                }

            } else if ("modifier".equals(action)) {
                Long id = Long.parseLong(request.getParameter("id"));
                ReservationTransport r = new ReservationTransport();
                r.setId(id);
                r.setPassagerId(Long.parseLong(request.getParameter("passagerId")));
                r.setVoyageId(Long.parseLong(request.getParameter("voyageId")));
                r.setNbPlaces(Integer.parseInt(request.getParameter("nbPlaces")));
                Voyage voyage = voyageDAO.trouverParId(r.getVoyageId());
                r.setMontantTotal(voyage.getPrixPlaceFcfa() * r.getNbPlaces());
                r.setNumeroBillet(request.getParameter("numeroBillet"));
                r.setStatut(request.getParameter("statut"));
                reservationDAO.modifier(r);
                request.setAttribute("message", "Reservation modifiee avec succes !");

            } else if ("supprimer".equals(action)) {
                Long id = Long.parseLong(request.getParameter("id"));
                reservationDAO.supprimer(id);
                request.setAttribute("message", "Reservation supprimee avec succes !");

            } else if ("alerte_retard".equals(action)) {
                // Envoyer alerte retard a tous les passagers d'un voyage
                Long voyageId = Long.parseLong(request.getParameter("voyageId"));
                int heuresRetard = Integer.parseInt(request.getParameter("heuresRetard"));
                Voyage voyage = voyageDAO.trouverParId(voyageId);
                List<ReservationTransport> reservations = reservationDAO.listerParVoyage(voyageId);

                int nbEnvoyes = 0;
                for (ReservationTransport r : reservations) {
                    Passager passager = passagerDAO.trouverParId(r.getPassagerId());
                    if (passager != null && passager.getTelephone() != null) {
                        SmsService.envoyerAlerteRetard(
                            passager.getTelephone(),
                            voyage.getVilleDepart(),
                            voyage.getVilleArrivee(),
                            voyage.getDateHeureDepart(),
                            heuresRetard
                        );
                        nbEnvoyes++;
                    }
                }
                request.setAttribute("message", "Alerte retard envoyee a " + nbEnvoyes + " passager(s) !");
            }

        } catch (Exception e) {
            request.setAttribute("erreur", "Erreur : " + e.getMessage());
            e.printStackTrace();
        }

        List<ReservationTransport> reservations = reservationDAO.listerTous();
        List<Passager> passagers = passagerDAO.listerTous();
        List<Voyage> voyages = voyageDAO.listerTous();
        request.setAttribute("reservations", reservations);
        request.setAttribute("passagers", passagers);
        request.setAttribute("voyages", voyages);
        request.getRequestDispatcher("/WEB-INF/vues/reservations.jsp").forward(request, response);
    }
}
