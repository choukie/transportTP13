package com.agence.transport.dao;

import java.util.List;
import com.agence.transport.model.ReservationTransport;

public class TestReservation {
    public static void main(String[] args) {
        ReservationDAO dao = new ReservationDAOImpl();
        
        System.out.println("=== Liste des réservations ===\n");
        
        List<ReservationTransport> liste = dao.listerTous();
        
        if (liste.isEmpty()) {
            System.out.println("Aucune réservation trouvée.");
        } else {
            for (ReservationTransport r : liste) {
                System.out.println("Billet: " + r.getNumeroBillet() + 
                                   " | Passager: " + r.getPassagerPrenom() + " " + r.getPassagerNom() +
                                   " | Trajet: " + r.getVilleDepart() + " → " + r.getVilleArrivee() +
                                   " | Places: " + r.getNbPlaces() +
                                   " | Montant: " + r.getMontantTotal() + " FCFA");
            }
        }
    }
}