package com.agence.transport.dao;

import java.util.List;
import com.agence.transport.model.Passager;

public class TestPassager {
    public static void main(String[] args) {
        PassagerDAO dao = new PassagerDAOImpl();
        
        System.out.println("=== Liste des passagers ===\n");
        
        List<Passager> liste = dao.listerTous();
        
        if (liste.isEmpty()) {
            System.out.println("Aucun passager trouvé.");
        } else {
            for (Passager p : liste) {
                System.out.println("ID: " + p.getId() + 
                                   " | Nom: " + p.getNom() + 
                                   " | Prénom: " + p.getPrenom() +
                                   " | Tél: " + p.getTelephone());
            }
        }
    }
}