package com.agence.transport.dao;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import com.agence.transport.model.ReservationTransport;
import com.agence.transport.util.DBConnection;

public class ReservationDAOImpl implements ReservationDAO {
    
    @Override
    public void ajouter(ReservationTransport reservation) {
        String sql = "INSERT INTO reservation_transport (passager_id, voyage_id, nb_places, montant_total, numero_billet, statut) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, reservation.getPassagerId());
            pstmt.setLong(2, reservation.getVoyageId());
            pstmt.setInt(3, reservation.getNbPlaces());
            pstmt.setDouble(4, reservation.getMontantTotal());
            pstmt.setString(5, reservation.getNumeroBillet());
            pstmt.setString(6, reservation.getStatut() != null ? reservation.getStatut() : "CONFIRMEE");
            
            pstmt.executeUpdate();
            System.out.println("Réservation ajoutée !");
            
            // Mettre à jour les places disponibles
            String updateSql = "UPDATE voyage SET nb_places_dispo = nb_places_dispo - ? WHERE id = ?";
            try (PreparedStatement pstmt2 = conn.prepareStatement(updateSql)) {
                pstmt2.setInt(1, reservation.getNbPlaces());
                pstmt2.setLong(2, reservation.getVoyageId());
                pstmt2.executeUpdate();
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public List<ReservationTransport> listerTous() {
        List<ReservationTransport> reservations = new ArrayList<>();
        String sql = "SELECT r.*, p.nom, p.prenom, v.ville_depart, v.ville_arrivee, v.chauffeur, v.date_heure_depart " +
                     "FROM reservation_transport r " +
                     "JOIN passager p ON r.passager_id = p.id " +
                     "JOIN voyage v ON r.voyage_id = v.id";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                ReservationTransport r = new ReservationTransport();
                r.setId(rs.getLong("id"));
                r.setPassagerId(rs.getLong("passager_id"));
                r.setVoyageId(rs.getLong("voyage_id"));
                r.setNbPlaces(rs.getInt("nb_places"));
                r.setMontantTotal(rs.getDouble("montant_total"));
                r.setNumeroBillet(rs.getString("numero_billet"));
                r.setStatut(rs.getString("statut"));
                Timestamp ts = rs.getTimestamp("date_reservation");
                r.setDateReservation(ts != null ? ts.toLocalDateTime() : null);
                r.setPassagerNom(rs.getString("nom"));
                r.setPassagerPrenom(rs.getString("prenom"));
                r.setVilleDepart(rs.getString("ville_depart"));
                r.setVilleArrivee(rs.getString("ville_arrivee"));
                r.setChauffeur(rs.getString("chauffeur"));
                Timestamp ts2 = rs.getTimestamp("date_heure_depart");
                r.setDateHeureDepart(ts2 != null ? ts2.toLocalDateTime() : null);
                reservations.add(r);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservations;
    }
    
    @Override
    public void modifier(ReservationTransport reservation) {
        String sql = "UPDATE reservation_transport SET passager_id=?, voyage_id=?, nb_places=?, montant_total=?, numero_billet=?, statut=? WHERE id=?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, reservation.getPassagerId());
            pstmt.setLong(2, reservation.getVoyageId());
            pstmt.setInt(3, reservation.getNbPlaces());
            pstmt.setDouble(4, reservation.getMontantTotal());
            pstmt.setString(5, reservation.getNumeroBillet());
            pstmt.setString(6, reservation.getStatut());
            pstmt.setLong(7, reservation.getId());
            
            pstmt.executeUpdate();
            System.out.println("Réservation modifiée !");
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void supprimer(Long id) {
        String sql = "DELETE FROM reservation_transport WHERE id=?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
            System.out.println("Réservation supprimée !");
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public ReservationTransport trouverParId(Long id) {
        ReservationTransport reservation = null;
        String sql = "SELECT r.*, p.nom, p.prenom, v.ville_depart, v.ville_arrivee, v.chauffeur, v.date_heure_depart " +
                     "FROM reservation_transport r " +
                     "JOIN passager p ON r.passager_id = p.id " +
                     "JOIN voyage v ON r.voyage_id = v.id " +
                     "WHERE r.id=?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                reservation = new ReservationTransport();
                reservation.setId(rs.getLong("id"));
                reservation.setPassagerId(rs.getLong("passager_id"));
                reservation.setVoyageId(rs.getLong("voyage_id"));
                reservation.setNbPlaces(rs.getInt("nb_places"));
                reservation.setMontantTotal(rs.getDouble("montant_total"));
                reservation.setNumeroBillet(rs.getString("numero_billet"));
                reservation.setStatut(rs.getString("statut"));
                Timestamp ts = rs.getTimestamp("date_reservation");
                reservation.setDateReservation(ts != null ? ts.toLocalDateTime() : null);
                reservation.setPassagerNom(rs.getString("nom"));
                reservation.setPassagerPrenom(rs.getString("prenom"));
                reservation.setVilleDepart(rs.getString("ville_depart"));
                reservation.setVilleArrivee(rs.getString("ville_arrivee"));
                reservation.setChauffeur(rs.getString("chauffeur"));
                Timestamp ts2 = rs.getTimestamp("date_heure_depart");
                reservation.setDateHeureDepart(ts2 != null ? ts2.toLocalDateTime() : null);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservation;
    }
    
    @Override
    public List<ReservationTransport> listerParPassager(Long passagerId) {
        List<ReservationTransport> reservations = new ArrayList<>();
        String sql = "SELECT r.*, p.nom, p.prenom, v.ville_depart, v.ville_arrivee, v.chauffeur, v.date_heure_depart " +
                     "FROM reservation_transport r " +
                     "JOIN passager p ON r.passager_id = p.id " +
                     "JOIN voyage v ON r.voyage_id = v.id " +
                     "WHERE r.passager_id=?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, passagerId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                ReservationTransport r = new ReservationTransport();
                r.setId(rs.getLong("id"));
                r.setPassagerId(rs.getLong("passager_id"));
                r.setVoyageId(rs.getLong("voyage_id"));
                r.setNbPlaces(rs.getInt("nb_places"));
                r.setMontantTotal(rs.getDouble("montant_total"));
                r.setNumeroBillet(rs.getString("numero_billet"));
                r.setStatut(rs.getString("statut"));
                Timestamp ts = rs.getTimestamp("date_reservation");
                r.setDateReservation(ts != null ? ts.toLocalDateTime() : null);
                r.setPassagerNom(rs.getString("nom"));
                r.setPassagerPrenom(rs.getString("prenom"));
                r.setVilleDepart(rs.getString("ville_depart"));
                r.setVilleArrivee(rs.getString("ville_arrivee"));
                r.setChauffeur(rs.getString("chauffeur"));
                Timestamp ts2 = rs.getTimestamp("date_heure_depart");
                r.setDateHeureDepart(ts2 != null ? ts2.toLocalDateTime() : null);
                reservations.add(r);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservations;
    }
    
    @Override
    public List<ReservationTransport> listerParVoyage(Long voyageId) {
        List<ReservationTransport> reservations = new ArrayList<>();
        String sql = "SELECT r.*, p.nom, p.prenom, v.ville_depart, v.ville_arrivee, v.chauffeur, v.date_heure_depart " +
                     "FROM reservation_transport r " +
                     "JOIN passager p ON r.passager_id = p.id " +
                     "JOIN voyage v ON r.voyage_id = v.id " +
                     "WHERE r.voyage_id=?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, voyageId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                ReservationTransport r = new ReservationTransport();
                r.setId(rs.getLong("id"));
                r.setPassagerId(rs.getLong("passager_id"));
                r.setVoyageId(rs.getLong("voyage_id"));
                r.setNbPlaces(rs.getInt("nb_places"));
                r.setMontantTotal(rs.getDouble("montant_total"));
                r.setNumeroBillet(rs.getString("numero_billet"));
                r.setStatut(rs.getString("statut"));
                Timestamp ts = rs.getTimestamp("date_reservation");
                r.setDateReservation(ts != null ? ts.toLocalDateTime() : null);
                r.setPassagerNom(rs.getString("nom"));
                r.setPassagerPrenom(rs.getString("prenom"));
                r.setVilleDepart(rs.getString("ville_depart"));
                r.setVilleArrivee(rs.getString("ville_arrivee"));
                r.setChauffeur(rs.getString("chauffeur"));
                Timestamp ts2 = rs.getTimestamp("date_heure_depart");
                r.setDateHeureDepart(ts2 != null ? ts2.toLocalDateTime() : null);
                reservations.add(r);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservations;
    }
}