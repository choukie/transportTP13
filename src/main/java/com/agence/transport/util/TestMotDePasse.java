package com.agence.transport.util;

import org.mindrot.jbcrypt.BCrypt;

public class TestMotDePasse {
    public static void main(String[] args) {
        String motDePasse = "admin123";
        String hashStocke = "$2a$10$vytycPhdgSAot/GausFHpevq5HwHKsMIOpowXDQnmdtYdW/NCRTEq";
        
        boolean resultat = BCrypt.checkpw(motDePasse, hashStocke);
        
        System.out.println("Mot de passe admin123 : " + motDePasse);
        System.out.println("Hash stocké : " + hashStocke);
        System.out.println("Résultat vérification : " + resultat);
        
        if (resultat) {
            System.out.println("✅ Le mot de passe est CORRECT !");
        } else {
            System.out.println("❌ Le mot de passe est INCORRECT !");
        }
    }
}