package com.agence.transport.servlet;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.agence.transport.dao.VehiculeDAO;
import com.agence.transport.dao.VehiculeDAOImpl;
import com.agence.transport.dao.VoyageDAO;
import com.agence.transport.dao.VoyageDAOImpl;
import com.agence.transport.model.Vehicule;
import com.agence.transport.model.Voyage;

public class VoyageServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private VoyageDAO voyageDAO = new VoyageDAOImpl();
    private VehiculeDAO vehiculeDAO = new VehiculeDAOImpl();

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

        List<Voyage> voyages = voyageDAO.listerTous();
        List<Vehicule> vehicules = vehiculeDAO.listerTous();
        request.setAttribute("voyages", voyages);
        request.setAttribute("vehicules", vehicules);
        request.getRequestDispatcher("/WEB-INF/vues/voyages.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!estConnecte(request, response)) return;

        String action = request.getParameter("action");

        try {
            if ("ajouter".equals(action)) {
                Voyage v = extraireVoyage(request);
                voyageDAO.ajouter(v);
                request.setAttribute("message", "Voyage ajouté avec succès !");

            } else if ("modifier".equals(action)) {
                Long id = Long.parseLong(request.getParameter("id"));
                Voyage v = extraireVoyage(request);
                v.setId(id);
                voyageDAO.modifier(v);
                request.setAttribute("message", "Voyage modifié avec succès !");

            } else if ("supprimer".equals(action)) {
                Long id = Long.parseLong(request.getParameter("id"));
                voyageDAO.supprimer(id);
                request.setAttribute("message", "Voyage supprimé avec succès !");
            }

        } catch (Exception e) {
            request.setAttribute("erreur", "Erreur : " + e.getMessage());
            e.printStackTrace();
        }

        List<Voyage> voyages = voyageDAO.listerTous();
        List<Vehicule> vehicules = vehiculeDAO.listerTous();
        request.setAttribute("voyages", voyages);
        request.setAttribute("vehicules", vehicules);
        request.getRequestDispatcher("/WEB-INF/vues/voyages.jsp").forward(request, response);
    }

    private Voyage extraireVoyage(HttpServletRequest request) {
        Voyage v = new Voyage();
        v.setVehiculeId(Long.parseLong(request.getParameter("vehiculeId")));
        v.setChauffeur(request.getParameter("chauffeur").trim());
        v.setVilleDepart(request.getParameter("villeDepart").trim());
        v.setVilleArrivee(request.getParameter("villeArrivee").trim());
        v.setDateHeureDepart(LocalDateTime.parse(request.getParameter("dateHeureDepart")));
        v.setPrixPlaceFcfa(Double.parseDouble(request.getParameter("prixPlaceFcfa")));
        v.setNbPlacesDispo(Integer.parseInt(request.getParameter("nbPlacesDispo")));
        v.setStatut(request.getParameter("statut"));
        return v;
    }
}
