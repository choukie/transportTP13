package com.agence.transport.model;

import java.time.LocalDateTime;

public class Voyage {
    private Long id;
    private Long vehiculeId;
    private String chauffeur;
    private String villeDepart;
    private String villeArrivee;
    private LocalDateTime dateHeureDepart;
    private LocalDateTime dateHeureArriveePrevue;
    private double prixPlaceFcfa;
    private int nbPlacesDispo;
    private String statut;
    
    // Champs additionnels (pour jointure)
    private String vehiculeImmatriculation;
    
    public Voyage() {}
    
    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getVehiculeId() { return vehiculeId; }
    public void setVehiculeId(Long vehiculeId) { this.vehiculeId = vehiculeId; }
    
    public String getChauffeur() { return chauffeur; }
    public void setChauffeur(String chauffeur) { this.chauffeur = chauffeur; }
    
    public String getVilleDepart() { return villeDepart; }
    public void setVilleDepart(String villeDepart) { this.villeDepart = villeDepart; }
    
    public String getVilleArrivee() { return villeArrivee; }
    public void setVilleArrivee(String villeArrivee) { this.villeArrivee = villeArrivee; }
    
    public LocalDateTime getDateHeureDepart() { return dateHeureDepart; }
    public void setDateHeureDepart(LocalDateTime dateHeureDepart) { this.dateHeureDepart = dateHeureDepart; }
    
    public LocalDateTime getDateHeureArriveePrevue() { return dateHeureArriveePrevue; }
    public void setDateHeureArriveePrevue(LocalDateTime dateHeureArriveePrevue) { this.dateHeureArriveePrevue = dateHeureArriveePrevue; }
    
    public double getPrixPlaceFcfa() { return prixPlaceFcfa; }
    public void setPrixPlaceFcfa(double prixPlaceFcfa) { this.prixPlaceFcfa = prixPlaceFcfa; }
    
    public int getNbPlacesDispo() { return nbPlacesDispo; }
    public void setNbPlacesDispo(int nbPlacesDispo) { this.nbPlacesDispo = nbPlacesDispo; }
    
    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
    
    public String getVehiculeImmatriculation() { return vehiculeImmatriculation; }
    public void setVehiculeImmatriculation(String vehiculeImmatriculation) { this.vehiculeImmatriculation = vehiculeImmatriculation; }
}