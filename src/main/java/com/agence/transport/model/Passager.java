package com.agence.transport.model;

public class Passager {
    private Long id;
    private String nom;
    private String prenom;
    private String telephone;
    private String pieceIdentite;
    private String email;
    private Long utilisateurId;
    
    public Passager() {}
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    
    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
    
    public String getPieceIdentite() { return pieceIdentite; }
    public void setPieceIdentite(String pieceIdentite) { this.pieceIdentite = pieceIdentite; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Long getUtilisateurId() { return utilisateurId; }
    public void setUtilisateurId(Long utilisateurId) { this.utilisateurId = utilisateurId; }
}