package com.agence.transport.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.agence.transport.model.Vehicule;
import com.agence.transport.util.DBConnection;

public class VehiculeDAOImpl implements VehiculeDAO {

    @Override
    public void ajouter(Vehicule vehicule) {
        String sql = "INSERT INTO vehicule (immatriculation, marque, modele, capacite, statut, date_derniere_revision) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, vehicule.getImmatriculation());
            pstmt.setString(2, vehicule.getMarque());
            pstmt.setString(3, vehicule.getModele());
            pstmt.setInt(4, vehicule.getCapacite());
            pstmt.setString(5, vehicule.getStatut());
            pstmt.setDate(6, vehicule.getDateDerniereRevision() != null ? Date.valueOf(vehicule.getDateDerniereRevision()) : null);
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @Override
    public List<Vehicule> listerTous() {
        List<Vehicule> vehicules = new ArrayList<>();
        String sql = "SELECT * FROM vehicule ORDER BY id DESC";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) vehicules.add(mapper(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return vehicules;
    }

    // ===== RECHERCHE AVEC PAGINATION =====
    @Override
    public List<Vehicule> rechercher(String motCle, int page, int taille) {
        List<Vehicule> vehicules = new ArrayList<>();
        String like = "%" + (motCle != null ? motCle.trim() : "") + "%";
        int offset = (page - 1) * taille;

        String sql = "SELECT * FROM vehicule WHERE " +
                     "immatriculation LIKE ? OR marque LIKE ? OR modele LIKE ? OR statut LIKE ? " +
                     "ORDER BY id DESC LIMIT ? OFFSET ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, like);
            pstmt.setString(2, like);
            pstmt.setString(3, like);
            pstmt.setString(4, like);
            pstmt.setInt(5, taille);
            pstmt.setInt(6, offset);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) vehicules.add(mapper(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return vehicules;
    }

    // ===== COMPTER RESULTATS POUR PAGINATION =====
    @Override
    public int compterRecherche(String motCle) {
        String like = "%" + (motCle != null ? motCle.trim() : "") + "%";
        String sql = "SELECT COUNT(*) FROM vehicule WHERE " +
                     "immatriculation LIKE ? OR marque LIKE ? OR modele LIKE ? OR statut LIKE ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, like);
            pstmt.setString(2, like);
            pstmt.setString(3, like);
            pstmt.setString(4, like);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return 0;
    }

    @Override
    public void modifier(Vehicule vehicule) {
        String sql = "UPDATE vehicule SET immatriculation=?, marque=?, modele=?, capacite=?, statut=?, date_derniere_revision=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, vehicule.getImmatriculation());
            pstmt.setString(2, vehicule.getMarque());
            pstmt.setString(3, vehicule.getModele());
            pstmt.setInt(4, vehicule.getCapacite());
            pstmt.setString(5, vehicule.getStatut());
            pstmt.setDate(6, vehicule.getDateDerniereRevision() != null ? Date.valueOf(vehicule.getDateDerniereRevision()) : null);
            pstmt.setLong(7, vehicule.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @Override
    public void supprimer(Long id) {
        String sql = "DELETE FROM vehicule WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @Override
    public Vehicule trouverParId(Long id) {
        String sql = "SELECT * FROM vehicule WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return mapper(rs);
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    // ===== MAPPER ResultSet -> Vehicule =====
    private Vehicule mapper(ResultSet rs) throws SQLException {
        Vehicule v = new Vehicule();
        v.setId(rs.getLong("id"));
        v.setImmatriculation(rs.getString("immatriculation"));
        v.setMarque(rs.getString("marque"));
        v.setModele(rs.getString("modele"));
        v.setCapacite(rs.getInt("capacite"));
        v.setStatut(rs.getString("statut"));
        Date dateSql = rs.getDate("date_derniere_revision");
        v.setDateDerniereRevision(dateSql != null ? dateSql.toLocalDate() : null);
        return v;
    }
}
