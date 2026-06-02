package com.agence.transport.dao;

import java.util.List;
import com.agence.transport.model.ReservationTransport;

public interface ReservationDAO {
    void ajouter(ReservationTransport reservation);
    void modifier(ReservationTransport reservation);
    void supprimer(Long id);
    ReservationTransport trouverParId(Long id);
    List<ReservationTransport> listerTous();
    List<ReservationTransport> listerParPassager(Long passagerId);
    List<ReservationTransport> listerParVoyage(Long voyageId);
}