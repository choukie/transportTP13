package com.agence.transport.dao;
import com.agence.transport.model.Utilisateur;

public interface UtilisateurDAO {
    Utilisateur trouverParLogin(String login);
    boolean verifierAuthentification(String login, String motDePasse);
    void ajouter(Utilisateur utilisateur);
}
