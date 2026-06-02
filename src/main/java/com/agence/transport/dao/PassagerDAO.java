package com.agence.transport.dao;

import java.util.List;
import com.agence.transport.model.Passager;

public interface PassagerDAO {
    void ajouter(Passager passager);
    void modifier(Passager passager);
    void supprimer(Long id);
    Passager trouverParId(Long id);
    List<Passager> listerTous();
    Passager trouverParTelephone(String telephone);
}