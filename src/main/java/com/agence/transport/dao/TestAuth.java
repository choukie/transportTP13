package com.agence.transport.dao;

public class TestAuth {
    public static void main(String[] args) {
        UtilisateurDAO dao = new UtilisateurDAOImpl();
        
        System.out.println("=== Test Authentification ===\n");
        
        // Test avec admin
        String login1 = "admin";
        String mdp1 = "admin123";
        boolean resultat1 = dao.verifierAuthentification(login1, mdp1);
        System.out.println("Login: " + login1 + " | Mot de passe: " + mdp1 + " | Résultat: " + resultat1);
        
        // Test avec mauvais mot de passe
        String login2 = "admin";
        String mdp2 = "wrongpassword";
        boolean resultat2 = dao.verifierAuthentification(login2, mdp2);
        System.out.println("Login: " + login2 + " | Mot de passe: " + mdp2 + " | Résultat: " + resultat2);
        
        // Test avec agent
        String login3 = "agent1";
        String mdp3 = "agent123";
        boolean resultat3 = dao.verifierAuthentification(login3, mdp3);
        System.out.println("Login: " + login3 + " | Mot de passe: " + mdp3 + " | Résultat: " + resultat3);
        
        // Test avec client
        String login4 = "client1";
        String mdp4 = "client123";
        boolean resultat4 = dao.verifierAuthentification(login4, mdp4);
        System.out.println("Login: " + login4 + " | Mot de passe: " + mdp4 + " | Résultat: " + resultat4);
    }
}