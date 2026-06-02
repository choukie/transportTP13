package com.agence.transport.servlet;

import java.io.*;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.agence.transport.dao.*;
import com.agence.transport.model.*;

public class ExportServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private boolean estConnecte(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return false;
        }
        return true;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!estConnecte(request, response)) return;

        String type = request.getParameter("type");
        if (type == null) type = "reservations";

        response.setContentType("text/csv;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        switch (type) {
            case "vehicules":   exportVehicules(response);   break;
            case "voyages":     exportVoyages(response);     break;
            case "passagers":   exportPassagers(response);   break;
            default:            exportReservations(response); break;
        }
    }

    private void exportVehicules(HttpServletResponse response) throws IOException {
        response.setHeader("Content-Disposition", "attachment; filename=vehicules.csv");
        PrintWriter pw = response.getWriter();
        pw.println("ID,Immatriculation,Marque,Modele,Capacite,Statut,Derniere Revision");
        try {
            for (Vehicule v : new VehiculeDAOImpl().listerTous()) {
                pw.printf("%d,%s,%s,%s,%d,%s,%s%n",
                    v.getId(), v.getImmatriculation(), v.getMarque(), v.getModele(),
                    v.getCapacite(), v.getStatut(),
                    v.getDateDerniereRevision() != null ? v.getDateDerniereRevision().toString() : "");
            }
        } catch (Exception e) { e.printStackTrace(); }
        pw.flush();
    }

    private void exportVoyages(HttpServletResponse response) throws IOException {
        response.setHeader("Content-Disposition", "attachment; filename=voyages.csv");
        PrintWriter pw = response.getWriter();
        pw.println("ID,Vehicule,Chauffeur,Ville Depart,Ville Arrivee,Date Depart,Prix FCFA,Places Dispo,Statut");
        try {
            for (Voyage v : new VoyageDAOImpl().listerTous()) {
                pw.printf("%d,%s,%s,%s,%s,%s,%.0f,%d,%s%n",
                    v.getId(), v.getVehiculeImmatriculation(), v.getChauffeur(),
                    v.getVilleDepart(), v.getVilleArrivee(),
                    v.getDateHeureDepart() != null ? v.getDateHeureDepart().toString() : "",
                    v.getPrixPlaceFcfa(), v.getNbPlacesDispo(), v.getStatut());
            }
        } catch (Exception e) { e.printStackTrace(); }
        pw.flush();
    }

    private void exportPassagers(HttpServletResponse response) throws IOException {
        response.setHeader("Content-Disposition", "attachment; filename=passagers.csv");
        PrintWriter pw = response.getWriter();
        pw.println("ID,Nom,Prenom,Telephone,Piece Identite,Email");
        try {
            for (Passager p : new PassagerDAOImpl().listerTous()) {
                pw.printf("%d,%s,%s,%s,%s,%s%n",
                    p.getId(), p.getNom(), p.getPrenom(), p.getTelephone(),
                    p.getPieceIdentite() != null ? p.getPieceIdentite() : "",
                    p.getEmail() != null ? p.getEmail() : "");
            }
        } catch (Exception e) { e.printStackTrace(); }
        pw.flush();
    }

    private void exportReservations(HttpServletResponse response) throws IOException {
        response.setHeader("Content-Disposition", "attachment; filename=reservations.csv");
        PrintWriter pw = response.getWriter();
        pw.println("ID,Numero Billet,Passager,Trajet,Nb Places,Montant FCFA,Statut,Date Reservation");
        try {
            for (ReservationTransport r : new ReservationDAOImpl().listerTous()) {
                String passager = r.getPassagerNom() + " " + r.getPassagerPrenom();
                String trajet = r.getVilleDepart() + " -> " + r.getVilleArrivee();
                String date = r.getDateReservation() != null ? r.getDateReservation().toString() : "";
                pw.printf("%d,%s,%s,%s,%d,%.0f,%s,%s%n",
                    r.getId(), r.getNumeroBillet(), passager, trajet,
                    r.getNbPlaces(), r.getMontantTotal(), r.getStatut(), date);
            }
        } catch (Exception e) { e.printStackTrace(); }
        pw.flush();
    }
}
