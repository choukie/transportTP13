package com.agence.transport.servlet;

import java.io.IOException;
import java.sql.*;
import java.util.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.agence.transport.util.DBConnection;

public class StatistiqueServlet extends HttpServlet {
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

        try (Connection conn = DBConnection.getConnection()) {

            // 1. Trafic et recettes par trajet
            String sql1 = "SELECT v.ville_depart, v.ville_arrivee, " +
                          "COUNT(r.id) AS nb_reservations, " +
                          "SUM(r.montant_total) AS total_recettes " +
                          "FROM reservation_transport r " +
                          "JOIN voyage v ON r.voyage_id = v.id " +
                          "GROUP BY v.ville_depart, v.ville_arrivee " +
                          "ORDER BY nb_reservations DESC";
            List<Map<String,Object>> traficParTrajet = new ArrayList<>();
            try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql1)) {
                while (rs.next()) {
                    Map<String,Object> row = new LinkedHashMap<>();
                    row.put("villeDepart", rs.getString("ville_depart"));
                    row.put("villeArrivee", rs.getString("ville_arrivee"));
                    row.put("nbReservations", rs.getInt("nb_reservations"));
                    row.put("totalRecettes", rs.getDouble("total_recettes"));
                    traficParTrajet.add(row);
                }
            }

            // 2. Taux de remplissage par vehicule
            String sql2 = "SELECT ve.immatriculation, ve.marque, ve.modele, ve.capacite, " +
                          "COUNT(r.id) AS nb_reservations, " +
                          "SUM(r.nb_places) AS places_vendues " +
                          "FROM vehicule ve " +
                          "LEFT JOIN voyage voy ON ve.id = voy.vehicule_id " +
                          "LEFT JOIN reservation_transport r ON voy.id = r.voyage_id " +
                          "GROUP BY ve.id, ve.immatriculation, ve.marque, ve.modele, ve.capacite";
            List<Map<String,Object>> remplissageVehicule = new ArrayList<>();
            try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql2)) {
                while (rs.next()) {
                    Map<String,Object> row = new LinkedHashMap<>();
                    row.put("immatriculation", rs.getString("immatriculation"));
                    row.put("marque", rs.getString("marque"));
                    row.put("modele", rs.getString("modele"));
                    row.put("capacite", rs.getInt("capacite"));
                    int placesVendues = rs.getInt("places_vendues");
                    int capacite = rs.getInt("capacite");
                    double taux = capacite > 0 ? (placesVendues * 100.0 / capacite) : 0;
                    row.put("placesVendues", placesVendues);
                    row.put("taux", Math.min(taux, 100));
                    remplissageVehicule.add(row);
                }
            }

            // 3. Trajets les plus demandes
            String sql3 = "SELECT v.ville_depart, v.ville_arrivee, COUNT(r.id) AS nb " +
                          "FROM reservation_transport r " +
                          "JOIN voyage v ON r.voyage_id = v.id " +
                          "GROUP BY v.ville_depart, v.ville_arrivee " +
                          "ORDER BY nb DESC LIMIT 5";
            List<Map<String,Object>> trajetsPopulaires = new ArrayList<>();
            try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql3)) {
                while (rs.next()) {
                    Map<String,Object> row = new LinkedHashMap<>();
                    row.put("villeDepart", rs.getString("ville_depart"));
                    row.put("villeArrivee", rs.getString("ville_arrivee"));
                    row.put("nb", rs.getInt("nb"));
                    trajetsPopulaires.add(row);
                }
            }

            // 4. Totaux generaux
            double totalRecettes = 0;
            int totalReservations = 0;
            try (Statement st = conn.createStatement();
                 ResultSet rs = st.executeQuery("SELECT COUNT(*) as nb, SUM(montant_total) as total FROM reservation_transport")) {
                if (rs.next()) {
                    totalReservations = rs.getInt("nb");
                    totalRecettes = rs.getDouble("total");
                }
            }

            request.setAttribute("traficParTrajet", traficParTrajet);
            request.setAttribute("remplissageVehicule", remplissageVehicule);
            request.setAttribute("trajetsPopulaires", trajetsPopulaires);
            request.setAttribute("totalRecettes", totalRecettes);
            request.setAttribute("totalReservations", totalReservations);

        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("erreur", "Erreur base de donnees : " + e.getMessage());
        }

        request.getRequestDispatcher("/WEB-INF/vues/statistiques.jsp").forward(request, response);
    }
}
