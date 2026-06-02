package com.agence.transport.dao;

import java.util.List;
import com.agence.transport.model.Vehicule;

public class TestVehicule {
    public static void main(String[] args) {
        VehiculeDAO dao = new VehiculeDAOImpl();
        
        System.out.println("=== Liste des véhicules ===\n");
        
        List<Vehicule> liste = dao.listerTous();
        
        if (liste.isEmpty()) {
            System.out.println("Aucun véhicule trouvé dans la base.");
        } else {
            for (Vehicule v : liste) {
                System.out.println("ID: " + v.getId() + 
                                   " | Immat: " + v.getImmatriculation() + 
                                   " | Marque: " + v.getMarque() + 
                                   " | Modèle: " + v.getModele() +
                                   " | Capacité: " + v.getCapacite());
            }
        }
    }
}