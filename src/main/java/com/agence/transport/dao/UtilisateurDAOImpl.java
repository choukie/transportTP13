package com.agence.transport.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.agence.transport.model.Utilisateur;
import com.agence.transport.util.DBConnection;
import com.agence.transport.util.PasswordUtil;

public class UtilisateurDAOImpl implements UtilisateurDAO {

    @Override
    public Utilisateur trouverParLogin(String login) {
        Utilisateur utilisateur = null;
        String sql = "SELECT * FROM utilisateur WHERE login = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, login);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                utilisateur = new Utilisateur();
                utilisateur.setId(rs.getLong("id"));
                utilisateur.setLogin(rs.getString("login"));
                utilisateur.setMotDePasse(rs.getString("mdp_hash"));
                utilisateur.setRole(rs.getString("role"));
                utilisateur.setActif(rs.getBoolean("actif"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return utilisateur;
    }

    @Override
    public boolean verifierAuthentification(String login, String motDePasse) {
        System.out.println("=== DEBUT AUTH ===");
        System.out.println("Login: " + login);

        Utilisateur utilisateur = trouverParLogin(login);

        if (utilisateur == null) {
            System.out.println("Utilisateur introuvable");
            return false;
        }

        System.out.println("Hash base: " + utilisateur.getMotDePasse());

        boolean resultat = PasswordUtil.verifier(motDePasse, utilisateur.getMotDePasse());

        System.out.println("Resultat: " + resultat);
        System.out.println("=== FIN AUTH ===");

        return resultat;
    }

    @Override
    public void ajouter(Utilisateur utilisateur) {
        String sql = "INSERT INTO utilisateur (login, mdp_hash, role, actif) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, utilisateur.getLogin());
            pstmt.setString(2, utilisateur.getMotDePasse());
            pstmt.setString(3, utilisateur.getRole());
            pstmt.setBoolean(4, utilisateur.isActif());

            pstmt.executeUpdate();
            System.out.println("Utilisateur cree : " + utilisateur.getLogin());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
