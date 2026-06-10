package com.agence.transport.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.agence.transport.model.Passager;
import com.agence.transport.util.DBConnection;

public class PassagerDAOImpl implements PassagerDAO {

    private Passager mapper(ResultSet rs) throws SQLException {
        Passager p = new Passager();
        p.setId(rs.getLong("id"));
        p.setNom(rs.getString("nom"));
        p.setPrenom(rs.getString("prenom"));
        p.setTelephone(rs.getString("telephone"));
        p.setPieceIdentite(rs.getString("piece_identite"));
        p.setEmail(rs.getString("email"));
        try { p.setUtilisateurId(rs.getLong("utilisateur_id")); } catch (SQLException ignored) {}
        return p;
    }
    
    @Override
    public void ajouter(Passager passager) {
        String sql = "INSERT INTO passager (nom, prenom, telephone, piece_identite, email, utilisateur_id) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, passager.getNom());
            pstmt.setString(2, passager.getPrenom());
            pstmt.setString(3, passager.getTelephone());
            pstmt.setString(4, passager.getPieceIdentite());
            pstmt.setString(5, passager.getEmail());
            if (passager.getUtilisateurId() != null) {
                pstmt.setLong(6, passager.getUtilisateurId());
            } else {
                pstmt.setNull(6, java.sql.Types.BIGINT);
            }
            
            pstmt.executeUpdate();
            System.out.println("Passager ajouté !");
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public List<Passager> listerTous() {
        List<Passager> passagers = new ArrayList<>();
        String sql = "SELECT * FROM passager ORDER BY id DESC";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                passagers.add(mapper(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return passagers;
    }

    @Override
    public List<Passager> rechercher(String motCle, int page, int taille) {
        List<Passager> passagers = new ArrayList<>();
        String like = "%" + (motCle != null ? motCle.trim() : "") + "%";
        int offset = (page - 1) * taille;
        String sql = "SELECT * FROM passager WHERE nom LIKE ? OR prenom LIKE ? OR telephone LIKE ? OR email LIKE ? ORDER BY id DESC LIMIT ? OFFSET ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, like);
            pstmt.setString(2, like);
            pstmt.setString(3, like);
            pstmt.setString(4, like);
            pstmt.setInt(5, taille);
            pstmt.setInt(6, offset);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) passagers.add(mapper(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return passagers;
    }

    @Override
    public int compterRecherche(String motCle) {
        String like = "%" + (motCle != null ? motCle.trim() : "") + "%";
        String sql = "SELECT COUNT(*) FROM passager WHERE nom LIKE ? OR prenom LIKE ? OR telephone LIKE ? OR email LIKE ?";
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
    public void modifier(Passager passager) {
        String sql = "UPDATE passager SET nom=?, prenom=?, telephone=?, piece_identite=?, email=? WHERE id=?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, passager.getNom());
            pstmt.setString(2, passager.getPrenom());
            pstmt.setString(3, passager.getTelephone());
            pstmt.setString(4, passager.getPieceIdentite());
            pstmt.setString(5, passager.getEmail());
            pstmt.setLong(6, passager.getId());
            
            pstmt.executeUpdate();
            System.out.println("Passager modifié !");
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void supprimer(Long id) {
        String sql = "DELETE FROM passager WHERE id=?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
            System.out.println("Passager supprimé !");
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public Passager trouverParId(Long id) {
        Passager passager = null;
        String sql = "SELECT * FROM passager WHERE id=?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                passager = mapper(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return passager;
    }
    
    @Override
    public Passager trouverParTelephone(String telephone) {
        Passager passager = null;
        String sql = "SELECT * FROM passager WHERE telephone=?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, telephone);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                passager = mapper(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return passager;
    }

    @Override
    public Passager trouverParUtilisateurId(Long utilisateurId) {
        Passager passager = null;
        String sql = "SELECT * FROM passager WHERE utilisateur_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, utilisateurId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                passager = mapper(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return passager;
    }
}