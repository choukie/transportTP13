package com.agence.transport.dao;

import java.util.List;
import com.agence.transport.model.Voyage;

public interface VoyageDAO {
    void ajouter(Voyage voyage);
    void modifier(Voyage voyage);
    void supprimer(Long id);
    Voyage trouverParId(Long id);
    List<Voyage> listerTous();
    List<Voyage> listerParVehicule(Long vehiculeId);
}