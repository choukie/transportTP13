package com.agence.transport.dao;

import java.util.List;
import com.agence.transport.model.Vehicule;

public interface VehiculeDAO {
    void ajouter(Vehicule vehicule);
    List<Vehicule> listerTous();
    List<Vehicule> rechercher(String motCle, int page, int taille);
    int compterRecherche(String motCle);
    void modifier(Vehicule vehicule);
    void supprimer(Long id);
    Vehicule trouverParId(Long id);
}
