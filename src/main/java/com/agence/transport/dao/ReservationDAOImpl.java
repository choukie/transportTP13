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
                     "JOIN voyage v ON r.voyage_id = v.id " +
                     "ORDER BY r.id DESC";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                reservations.add(mapper(rs));
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
                reservation = mapper(rs);
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
                     "WHERE r.passager_id=? ORDER BY r.id DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, passagerId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                reservations.add(mapper(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservations;
    }
    
    @Override
    public List<ReservationTransport> listerTous(int page, int taille) {
        List<ReservationTransport> reservations = new ArrayList<>();
        int offset = (page - 1) * taille;
        String sql = "SELECT r.*, p.nom, p.prenom, v.ville_depart, v.ville_arrivee, v.chauffeur, v.date_heure_depart " +
                     "FROM reservation_transport r " +
                     "JOIN passager p ON r.passager_id = p.id " +
                     "JOIN voyage v ON r.voyage_id = v.id " +
                     "ORDER BY r.id DESC LIMIT ? OFFSET ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, taille);
            pstmt.setInt(2, offset);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) reservations.add(mapper(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return reservations;
    }

    @Override
    public int compterTous() {
        String sql = "SELECT COUNT(*) FROM reservation_transport";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return 0;
    }

    private ReservationTransport mapper(ResultSet rs) throws SQLException {
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
        return r;
    }

    @Override
    public List<ReservationTransport> listerParPassager(Long passagerId, int page, int taille) {
        List<ReservationTransport> reservations = new ArrayList<>();
        int offset = (page - 1) * taille;
        String sql = "SELECT r.*, p.nom, p.prenom, v.ville_depart, v.ville_arrivee, v.chauffeur, v.date_heure_depart " +
                     "FROM reservation_transport r " +
                     "JOIN passager p ON r.passager_id = p.id " +
                     "JOIN voyage v ON r.voyage_id = v.id " +
                     "WHERE r.passager_id=? ORDER BY r.id DESC LIMIT ? OFFSET ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, passagerId);
            pstmt.setInt(2, taille);
            pstmt.setInt(3, offset);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) reservations.add(mapper(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return reservations;
    }

    @Override
    public int compterParPassager(Long passagerId) {
        String sql = "SELECT COUNT(*) FROM reservation_transport WHERE passager_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, passagerId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return 0;
    }

    @Override
    public List<ReservationTransport> rechercher(String motCle, int page, int taille) {
        List<ReservationTransport> reservations = new ArrayList<>();
        String like = "%" + (motCle != null ? motCle.trim() : "") + "%";
        int offset = (page - 1) * taille;
        String sql = "SELECT r.*, p.nom, p.prenom, v.ville_depart, v.ville_arrivee, v.chauffeur, v.date_heure_depart " +
                     "FROM reservation_transport r " +
                     "JOIN passager p ON r.passager_id = p.id " +
                     "JOIN voyage v ON r.voyage_id = v.id " +
                     "WHERE r.numero_billet LIKE ? OR p.nom LIKE ? OR p.prenom LIKE ? OR v.ville_depart LIKE ? OR v.ville_arrivee LIKE ? " +
                     "ORDER BY r.id DESC LIMIT ? OFFSET ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, like);
            pstmt.setString(2, like);
            pstmt.setString(3, like);
            pstmt.setString(4, like);
            pstmt.setString(5, like);
            pstmt.setInt(6, taille);
            pstmt.setInt(7, offset);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) reservations.add(mapper(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return reservations;
    }

    @Override
    public int compterRecherche(String motCle) {
        String like = "%" + (motCle != null ? motCle.trim() : "") + "%";
        String sql = "SELECT COUNT(*) FROM reservation_transport r " +
                     "JOIN passager p ON r.passager_id = p.id " +
                     "JOIN voyage v ON r.voyage_id = v.id " +
                     "WHERE r.numero_billet LIKE ? OR p.nom LIKE ? OR p.prenom LIKE ? OR v.ville_depart LIKE ? OR v.ville_arrivee LIKE ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, like);
            pstmt.setString(2, like);
            pstmt.setString(3, like);
            pstmt.setString(4, like);
            pstmt.setString(5, like);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return 0;
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
                reservations.add(mapper(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservations;
    }
}