package com.agence.transport.model;

import java.time.LocalDate;

public class Vehicule {
    private Long id;
    private String immatriculation;
    private String marque;
    private String modele;
    private int capacite;
    private String statut;
    private LocalDate dateDerniereRevision;
    
    public Vehicule() {}
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getImmatriculation() { return immatriculation; }
    public void setImmatriculation(String immatriculation) { this.immatriculation = immatriculation; }
    
    public String getMarque() { return marque; }
    public void setMarque(String marque) { this.marque = marque; }
    
    public String getModele() { return modele; }
    public void setModele(String modele) { this.modele = modele; }
    
    public int getCapacite() { return capacite; }
    public void setCapacite(int capacite) { this.capacite = capacite; }
    
    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
    
    public LocalDate getDateDerniereRevision() { return dateDerniereRevision; }
    public void setDateDerniereRevision(LocalDate dateDerniereRevision) { this.dateDerniereRevision = dateDerniereRevision; }
}