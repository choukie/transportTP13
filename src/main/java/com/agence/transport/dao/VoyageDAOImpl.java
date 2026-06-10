package com.agence.transport.dao;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import com.agence.transport.model.Voyage;
import com.agence.transport.util.DBConnection;

public class VoyageDAOImpl implements VoyageDAO {
    
    @Override
    public void ajouter(Voyage voyage) {
        String sql = "INSERT INTO voyage (vehicule_id, chauffeur, ville_depart, ville_arrivee, date_heure_depart, prix_place_fcfa, nb_places_dispo, statut) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, voyage.getVehiculeId());
            pstmt.setString(2, voyage.getChauffeur());
            pstmt.setString(3, voyage.getVilleDepart());
            pstmt.setString(4, voyage.getVilleArrivee());
            pstmt.setTimestamp(5, Timestamp.valueOf(voyage.getDateHeureDepart()));
            pstmt.setDouble(6, voyage.getPrixPlaceFcfa());
            pstmt.setInt(7, voyage.getNbPlacesDispo());
            pstmt.setString(8, voyage.getStatut() != null ? voyage.getStatut() : "PROGRAMME");
            
            pstmt.executeUpdate();
            System.out.println("Voyage ajouté !");
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public List<Voyage> listerTous() {
        List<Voyage> voyages = new ArrayList<>();
        String sql = "SELECT v.*, ve.immatriculation FROM voyage v JOIN vehicule ve ON v.vehicule_id = ve.id";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Voyage voyage = new Voyage();
                voyage.setId(rs.getLong("id"));
                voyage.setVehiculeId(rs.getLong("vehicule_id"));
                voyage.setChauffeur(rs.getString("chauffeur"));
                voyage.setVilleDepart(rs.getString("ville_depart"));
                voyage.setVilleArrivee(rs.getString("ville_arrivee"));
                Timestamp ts = rs.getTimestamp("date_heure_depart");
                voyage.setDateHeureDepart(ts != null ? ts.toLocalDateTime() : null);
                voyage.setPrixPlaceFcfa(rs.getDouble("prix_place_fcfa"));
                voyage.setNbPlacesDispo(rs.getInt("nb_places_dispo"));
                voyage.setStatut(rs.getString("statut"));
                voyage.setVehiculeImmatriculation(rs.getString("immatriculation"));
                voyages.add(voyage);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return voyages;
    }
    
    @Override
    public void modifier(Voyage voyage) {
        String sql = "UPDATE voyage SET vehicule_id=?, chauffeur=?, ville_depart=?, ville_arrivee=?, date_heure_depart=?, prix_place_fcfa=?, nb_places_dispo=?, statut=? WHERE id=?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, voyage.getVehiculeId());
            pstmt.setString(2, voyage.getChauffeur());
            pstmt.setString(3, voyage.getVilleDepart());
            pstmt.setString(4, voyage.getVilleArrivee());
            pstmt.setTimestamp(5, Timestamp.valueOf(voyage.getDateHeureDepart()));
            pstmt.setDouble(6, voyage.getPrixPlaceFcfa());
            pstmt.setInt(7, voyage.getNbPlacesDispo());
            pstmt.setString(8, voyage.getStatut());
            pstmt.setLong(9, voyage.getId());
            
            pstmt.executeUpdate();
            System.out.println("Voyage modifié !");
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void supprimer(Long id) {
        String sql = "DELETE FROM voyage WHERE id=?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
            System.out.println("Voyage supprimé !");
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public Voyage trouverParId(Long id) {
        Voyage voyage = null;
        String sql = "SELECT v.*, ve.immatriculation FROM voyage v JOIN vehicule ve ON v.vehicule_id = ve.id WHERE v.id=?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                voyage = new Voyage();
                voyage.setId(rs.getLong("id"));
                voyage.setVehiculeId(rs.getLong("vehicule_id"));
                voyage.setChauffeur(rs.getString("chauffeur"));
                voyage.setVilleDepart(rs.getString("ville_depart"));
                voyage.setVilleArrivee(rs.getString("ville_arrivee"));
                Timestamp ts = rs.getTimestamp("date_heure_depart");
                voyage.setDateHeureDepart(ts != null ? ts.toLocalDateTime() : null);
                voyage.setPrixPlaceFcfa(rs.getDouble("prix_place_fcfa"));
                voyage.setNbPlacesDispo(rs.getInt("nb_places_dispo"));
                voyage.setStatut(rs.getString("statut"));
                voyage.setVehiculeImmatriculation(rs.getString("immatriculation"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return voyage;
    }
    
    @Override
    public List<Voyage> listerParVehicule(Long vehiculeId) {
        List<Voyage> voyages = new ArrayList<>();
        String sql = "SELECT v.*, ve.immatriculation FROM voyage v JOIN vehicule ve ON v.vehicule_id = ve.id WHERE v.vehicule_id=?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, vehiculeId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Voyage voyage = new Voyage();
                voyage.setId(rs.getLong("id"));
                voyage.setVehiculeId(rs.getLong("vehicule_id"));
                voyage.setChauffeur(rs.getString("chauffeur"));
                voyage.setVilleDepart(rs.getString("ville_depart"));
                voyage.setVilleArrivee(rs.getString("ville_arrivee"));
                Timestamp ts = rs.getTimestamp("date_heure_depart");
                voyage.setDateHeureDepart(ts != null ? ts.toLocalDateTime() : null);
                voyage.setPrixPlaceFcfa(rs.getDouble("prix_place_fcfa"));
                voyage.setNbPlacesDispo(rs.getInt("nb_places_dispo"));
                voyage.setStatut(rs.getString("statut"));
                voyage.setVehiculeImmatriculation(rs.getString("immatriculation"));
                voyages.add(voyage);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return voyages;
    }

    @Override
    public List<Voyage> rechercher(String motCle, int page, int taille) {
        List<Voyage> voyages = new ArrayList<>();
        String like = "%" + (motCle != null ? motCle.trim() : "") + "%";
        int offset = (page - 1) * taille;
        String sql = "SELECT v.*, ve.immatriculation FROM voyage v " +
                     "JOIN vehicule ve ON v.vehicule_id = ve.id " +
                     "WHERE v.ville_depart LIKE ? OR v.ville_arrivee LIKE ? OR v.chauffeur LIKE ? OR ve.immatriculation LIKE ? " +
                     "ORDER BY v.id DESC LIMIT ? OFFSET ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, like);
            pstmt.setString(2, like);
            pstmt.setString(3, like);
            pstmt.setString(4, like);
            pstmt.setInt(5, taille);
            pstmt.setInt(6, offset);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Voyage voyage = new Voyage();
                voyage.setId(rs.getLong("id"));
                voyage.setVehiculeId(rs.getLong("vehicule_id"));
                voyage.setChauffeur(rs.getString("chauffeur"));
                voyage.setVilleDepart(rs.getString("ville_depart"));
                voyage.setVilleArrivee(rs.getString("ville_arrivee"));
                Timestamp ts = rs.getTimestamp("date_heure_depart");
                voyage.setDateHeureDepart(ts != null ? ts.toLocalDateTime() : null);
                voyage.setPrixPlaceFcfa(rs.getDouble("prix_place_fcfa"));
                voyage.setNbPlacesDispo(rs.getInt("nb_places_dispo"));
                voyage.setStatut(rs.getString("statut"));
                voyage.setVehiculeImmatriculation(rs.getString("immatriculation"));
                voyages.add(voyage);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return voyages;
    }

    @Override
    public int compterRecherche(String motCle) {
        String like = "%" + (motCle != null ? motCle.trim() : "") + "%";
        String sql = "SELECT COUNT(*) FROM voyage v " +
                     "JOIN vehicule ve ON v.vehicule_id = ve.id " +
                     "WHERE v.ville_depart LIKE ? OR v.ville_arrivee LIKE ? OR v.chauffeur LIKE ? OR ve.immatriculation LIKE ?";
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
}