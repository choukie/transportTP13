package com.agence.transport.servlet;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.agence.transport.dao.PassagerDAO;
import com.agence.transport.dao.PassagerDAOImpl;
import com.agence.transport.model.Passager;

public class PassagerServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private PassagerDAO passagerDAO = new PassagerDAOImpl();
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

        String motCle = request.getParameter("recherche");
        if (motCle == null) motCle = "";

        int page = 1;
        try {
            String pageParam = request.getParameter("page");
            if (pageParam != null) page = Integer.parseInt(pageParam);
        } catch (NumberFormatException e) { page = 1; }

        int total = passagerDAO.compterRecherche(motCle);
        int totalPages = (int) Math.ceil((double) total / TAILLE_PAGE);
        if (totalPages == 0) totalPages = 1;
        if (page < 1) page = 1;
        if (page > totalPages) page = totalPages;

        List<Passager> passagers = passagerDAO.rechercher(motCle, page, TAILLE_PAGE);

        request.setAttribute("passagers", passagers);
        request.setAttribute("recherche", motCle);
        request.setAttribute("page", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("total", total);
        request.getRequestDispatcher("/WEB-INF/vues/passagers.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!estConnecte(request, response)) return;

        String action = request.getParameter("action");

        try {
            if ("ajouter".equals(action)) {
                Passager p = extrairePassager(request);
                passagerDAO.ajouter(p);
                request.setAttribute("message", "Passager ajoute avec succes !");

            } else if ("modifier".equals(action)) {
                Long id = Long.parseLong(request.getParameter("id"));
                Passager p = extrairePassager(request);
                p.setId(id);
                passagerDAO.modifier(p);
                request.setAttribute("message", "Passager modifie avec succes !");

            } else if ("supprimer".equals(action)) {
                Long id = Long.parseLong(request.getParameter("id"));
                passagerDAO.supprimer(id);
                request.setAttribute("message", "Passager supprime avec succes !");
            }

        } catch (Exception e) {
            request.setAttribute("erreur", "Erreur : " + e.getMessage());
            e.printStackTrace();
        }

        // Recharger avec pagination
        String motCle = "";
        int total = passagerDAO.compterRecherche(motCle);
        int totalPages = (int) Math.ceil((double) total / TAILLE_PAGE);
        if (totalPages == 0) totalPages = 1;

        request.setAttribute("passagers", passagerDAO.rechercher(motCle, 1, TAILLE_PAGE));
        request.setAttribute("recherche", motCle);
        request.setAttribute("page", 1);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("total", total);
        request.getRequestDispatcher("/WEB-INF/vues/passagers.jsp").forward(request, response);
    }

    private Passager extrairePassager(HttpServletRequest request) {
        Passager p = new Passager();
        p.setNom(request.getParameter("nom").trim());
        p.setPrenom(request.getParameter("prenom").trim());
        p.setTelephone(request.getParameter("telephone").trim());
        p.setPieceIdentite(request.getParameter("pieceIdentite").trim());
        String email = request.getParameter("email");
        p.setEmail(email != null ? email.trim() : "");
        return p;
    }
}
