package com.agence.transport.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.agence.transport.model.Passager;
import com.agence.transport.util.DBConnection;

public class PassagerDAOImpl implements PassagerDAO {
    
    @Override
    public void ajouter(Passager passager) {
        String sql = "INSERT INTO passager (nom, prenom, telephone, piece_identite, email) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, passager.getNom());
            pstmt.setString(2, passager.getPrenom());
            pstmt.setString(3, passager.getTelephone());
            pstmt.setString(4, passager.getPieceIdentite());
            pstmt.setString(5, passager.getEmail());
            
            pstmt.executeUpdate();
            System.out.println("Passager ajouté !");
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public List<Passager> listerTous() {
        List<Passager> passagers = new ArrayList<>();
        String sql = "SELECT * FROM passager";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Passager p = new Passager();
                p.setId(rs.getLong("id"));
                p.setNom(rs.getString("nom"));
                p.setPrenom(rs.getString("prenom"));
                p.setTelephone(rs.getString("telephone"));
                p.setPieceIdentite(rs.getString("piece_identite"));
                p.setEmail(rs.getString("email"));
                passagers.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return passagers;
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
                passager = new Passager();
                passager.setId(rs.getLong("id"));
                passager.setNom(rs.getString("nom"));
                passager.setPrenom(rs.getString("prenom"));
                passager.setTelephone(rs.getString("telephone"));
                passager.setPieceIdentite(rs.getString("piece_identite"));
                passager.setEmail(rs.getString("email"));
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
                passager = new Passager();
                passager.setId(rs.getLong("id"));
                passager.setNom(rs.getString("nom"));
                passager.setPrenom(rs.getString("prenom"));
                passager.setTelephone(rs.getString("telephone"));
                passager.setPieceIdentite(rs.getString("piece_identite"));
                passager.setEmail(rs.getString("email"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return passager;
    }
}