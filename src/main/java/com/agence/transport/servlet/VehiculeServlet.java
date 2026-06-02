package com.agence.transport.servlet;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.agence.transport.dao.VehiculeDAO;
import com.agence.transport.dao.VehiculeDAOImpl;
import com.agence.transport.model.Vehicule;

public class VehiculeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private VehiculeDAO vehiculeDAO = new VehiculeDAOImpl();
    private static final int TAILLE_PAGE = 5;

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

        // Recherche et pagination
        String motCle = request.getParameter("recherche");
        if (motCle == null) motCle = "";

        int page = 1;
        try {
            String pageParam = request.getParameter("page");
            if (pageParam != null) page = Integer.parseInt(pageParam);
        } catch (NumberFormatException e) { page = 1; }

        int total = vehiculeDAO.compterRecherche(motCle);
        int totalPages = (int) Math.ceil((double) total / TAILLE_PAGE);
        if (totalPages == 0) totalPages = 1;
        if (page < 1) page = 1;
        if (page > totalPages) page = totalPages;

        List<Vehicule> vehicules = vehiculeDAO.rechercher(motCle, page, TAILLE_PAGE);

        request.setAttribute("vehicules", vehicules);
        request.setAttribute("recherche", motCle);
        request.setAttribute("page", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("total", total);
        request.getRequestDispatcher("/WEB-INF/vues/vehicules.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!estConnecte(request, response)) return;

        String action = request.getParameter("action");

        try {
            if ("ajouter".equals(action)) {
                vehiculeDAO.ajouter(extraireVehicule(request));
                request.setAttribute("message", "Vehicule ajoute avec succes !");
            } else if ("modifier".equals(action)) {
                Long id = Long.parseLong(request.getParameter("id"));
                Vehicule v = extraireVehicule(request);
                v.setId(id);
                vehiculeDAO.modifier(v);
                request.setAttribute("message", "Vehicule modifie avec succes !");
            } else if ("supprimer".equals(action)) {
                vehiculeDAO.supprimer(Long.parseLong(request.getParameter("id")));
                request.setAttribute("message", "Vehicule supprime avec succes !");
            }
        } catch (Exception e) {
            request.setAttribute("erreur", "Erreur : " + e.getMessage());
        }

        // Recharger avec pagination
        String motCle = "";
        int total = vehiculeDAO.compterRecherche(motCle);
        int totalPages = (int) Math.ceil((double) total / TAILLE_PAGE);
        if (totalPages == 0) totalPages = 1;

        request.setAttribute("vehicules", vehiculeDAO.rechercher(motCle, 1, TAILLE_PAGE));
        request.setAttribute("recherche", motCle);
        request.setAttribute("page", 1);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("total", total);
        request.getRequestDispatcher("/WEB-INF/vues/vehicules.jsp").forward(request, response);
    }

    private Vehicule extraireVehicule(HttpServletRequest request) {
        Vehicule v = new Vehicule();
        v.setImmatriculation(request.getParameter("immatriculation").trim());
        v.setMarque(request.getParameter("marque").trim());
        v.setModele(request.getParameter("modele").trim());
        v.setCapacite(Integer.parseInt(request.getParameter("capacite")));
        v.setStatut(request.getParameter("statut"));
        String dateStr = request.getParameter("dateDerniereRevision");
        if (dateStr != null && !dateStr.isEmpty())
            v.setDateDerniereRevision(LocalDate.parse(dateStr));
        return v;
    }
}
