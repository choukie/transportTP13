package com.agence.transport.model;

import java.time.LocalDateTime;

public class ReservationTransport {
    private Long id;
    private Long passagerId;
    private Long voyageId;
    private int nbPlaces;
    private double montantTotal;
    private String numeroBillet;
    private String statut;
    private LocalDateTime dateReservation;
    
    // Champs additionnels (pour jointures)
    private String passagerNom;
    private String passagerPrenom;
    private String villeDepart;
    private String villeArrivee;
    private String chauffeur;
    private LocalDateTime dateHeureDepart;
    
    public ReservationTransport() {}
    
    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getPassagerId() { return passagerId; }
    public void setPassagerId(Long passagerId) { this.passagerId = passagerId; }
    
    public Long getVoyageId() { return voyageId; }
    public void setVoyageId(Long voyageId) { this.voyageId = voyageId; }
    
    public int getNbPlaces() { return nbPlaces; }
    public void setNbPlaces(int nbPlaces) { this.nbPlaces = nbPlaces; }
    
    public double getMontantTotal() { return montantTotal; }
    public void setMontantTotal(double montantTotal) { this.montantTotal = montantTotal; }
    
    public String getNumeroBillet() { return numeroBillet; }
    public void setNumeroBillet(String numeroBillet) { this.numeroBillet = numeroBillet; }
    
    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
    
    public LocalDateTime getDateReservation() { return dateReservation; }
    public void setDateReservation(LocalDateTime dateReservation) { this.dateReservation = dateReservation; }
    
    public String getPassagerNom() { return passagerNom; }
    public void setPassagerNom(String passagerNom) { this.passagerNom = passagerNom; }
    
    public String getPassagerPrenom() { return passagerPrenom; }
    public void setPassagerPrenom(String passagerPrenom) { this.passagerPrenom = passagerPrenom; }
    
    public String getVilleDepart() { return villeDepart; }
    public void setVilleDepart(String villeDepart) { this.villeDepart = villeDepart; }
    
    public String getVilleArrivee() { return villeArrivee; }
    public void setVilleArrivee(String villeArrivee) { this.villeArrivee = villeArrivee; }
    
    public String getChauffeur() { return chauffeur; }
    public void setChauffeur(String chauffeur) { this.chauffeur = chauffeur; }
    
    public LocalDateTime getDateHeureDepart() { return dateHeureDepart; }
    public void setDateHeureDepart(LocalDateTime dateHeureDepart) { this.dateHeureDepart = dateHeureDepart; }
}