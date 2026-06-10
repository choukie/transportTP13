package com.agence.transport.servlet;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.agence.transport.dao.UtilisateurDAO;
import com.agence.transport.dao.UtilisateurDAOImpl;
import com.agence.transport.dao.PassagerDAO;
import com.agence.transport.dao.PassagerDAOImpl;
import com.agence.transport.model.Utilisateur;
import com.agence.transport.model.Passager;
import com.agence.transport.util.PasswordUtil;

public class InscriptionServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UtilisateurDAO utilisateurDAO = new UtilisateurDAOImpl();
    private PassagerDAO passagerDAO = new PassagerDAOImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/vues/inscription.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String nom = request.getParameter("nom").trim();
        String prenom = request.getParameter("prenom").trim();
        String telephone = request.getParameter("telephone").trim();
        String email = request.getParameter("email").trim();
        String login = request.getParameter("login").trim();
        String motDePasse = request.getParameter("motDePasse");
        String confirmation = request.getParameter("confirmation");

        // Validation
        if (!motDePasse.equals(confirmation)) {
            request.setAttribute("erreur", "Les mots de passe ne correspondent pas !");
            request.getRequestDispatcher("/WEB-INF/vues/inscription.jsp").forward(request, response);
            return;
        }

        if (motDePasse.length() < 6) {
            request.setAttribute("erreur", "Le mot de passe doit contenir au moins 6 caracteres !");
            request.getRequestDispatcher("/WEB-INF/vues/inscription.jsp").forward(request, response);
            return;
        }

        // Verifier si login existe deja
        Utilisateur existant = utilisateurDAO.trouverParLogin(login);
        if (existant != null) {
            request.setAttribute("erreur", "Ce login est deja utilise ! Choisissez un autre.");
            request.getRequestDispatcher("/WEB-INF/vues/inscription.jsp").forward(request, response);
            return;
        }

        try {
            // Creer l'utilisateur d'abord
            Utilisateur utilisateur = new Utilisateur();
            utilisateur.setLogin(login);
            utilisateur.setMotDePasse(PasswordUtil.hasher(motDePasse));
            utilisateur.setRole("CLIENT");
            utilisateur.setActif(true);
            utilisateurDAO.ajouter(utilisateur);

            // Recuperer l'utilisateur pour avoir son ID
            Utilisateur utilisateurCree = utilisateurDAO.trouverParLogin(login);

            // Creer le passager lie a l'utilisateur
            Passager passager = new Passager();
            passager.setNom(nom);
            passager.setPrenom(prenom);
            passager.setTelephone(telephone);
            passager.setEmail(email);
            passager.setUtilisateurId(utilisateurCree.getId());
            passagerDAO.ajouter(passager);

            request.setAttribute("succes", "Compte cree avec succes ! Vous pouvez vous connecter.");
            request.getRequestDispatcher("/WEB-INF/vues/inscription.jsp").forward(request, response);

        } catch (Exception e) {
            request.setAttribute("erreur", "Erreur lors de la creation du compte : " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/vues/inscription.jsp").forward(request, response);
        }
    }
}
