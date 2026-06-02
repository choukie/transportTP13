package com.agence.transport.dao;

import java.util.List;
import com.agence.transport.model.Voyage;

public class TestVoyage {
    public static void main(String[] args) {
        VoyageDAO dao = new VoyageDAOImpl();
        
        System.out.println("=== Liste des voyages ===\n");
        
        List<Voyage> liste = dao.listerTous();
        
        if (liste.isEmpty()) {
            System.out.println("Aucun voyage trouvé dans la base.");
        } else {
            for (Voyage v : liste) {
                System.out.println("ID: " + v.getId() + 
                                   " | Chauffeur: " + v.getChauffeur() + 
                                   " | Trajet: " + v.getVilleDepart() + " → " + v.getVilleArrivee() +
                                   " | Véhicule: " + v.getVehiculeImmatriculation() +
                                   " | Prix: " + v.getPrixPlaceFcfa() + " FCFA");
            }
        }
    }
}