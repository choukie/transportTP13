package com.agence.transport.servlet;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import com.agence.transport.dao.UtilisateurDAO;
import com.agence.transport.dao.UtilisateurDAOImpl;
import com.agence.transport.model.Utilisateur;

public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UtilisateurDAO utilisateurDAO = new UtilisateurDAOImpl();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/vues/login.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String login = request.getParameter("login");
        String motDePasse = request.getParameter("motDePasse");

        boolean authentifie = utilisateurDAO.verifierAuthentification(login, motDePasse);

        if (authentifie) {
            Utilisateur utilisateur = utilisateurDAO.trouverParLogin(login);

            HttpSession session = request.getSession();
            session.setAttribute("user", utilisateur);
            session.setAttribute("role", utilisateur.getRole());
            session.setAttribute("login", login);

            // Redirection selon le role
            if ("CLIENT".equals(utilisateur.getRole())) {
                response.sendRedirect(request.getContextPath() + "/espace-client");
            } else {
                response.sendRedirect(request.getContextPath() + "/dashboard");
            }
        } else {
            request.setAttribute("erreur", "Login ou mot de passe incorrect");
            request.getRequestDispatcher("/WEB-INF/vues/login.jsp").forward(request, response);
        }
    }
}
