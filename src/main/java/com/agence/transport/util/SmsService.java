package com.agence.transport.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Service SMS - Simulation SMSLib
 * En production, remplacer par une vraie API SMS (Twilio, Orange SMS, etc.)
 */
public class SmsService {

    // Historique des SMS envoyes (simulation)
    private static final List<String[]> historiqueSms = new ArrayList<>();

    /**
     * Envoyer un SMS de confirmation de billet
     */
    public static boolean envoyerConfirmationBillet(
            String telephone,
            String numeroBillet,
            String villeDepart,
            String villeArrivee,
            LocalDateTime dateDepart) {

        String dateStr = dateDepart != null
            ? dateDepart.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
            : "N/A";
        String heureStr = dateDepart != null
            ? dateDepart.format(DateTimeFormatter.ofPattern("HH:mm"))
            : "N/A";

        String message = "Billet N°" + numeroBillet + " confirme. "
            + villeDepart + "->" + villeArrivee
            + " le " + dateStr + " a " + heureStr;

        return envoyerSms(telephone, message, "CONFIRMATION");
    }

    /**
     * Envoyer une alerte retard
     */
    public static boolean envoyerAlerteRetard(
            String telephone,
            String villeDepart,
            String villeArrivee,
            LocalDateTime dateVoyage,
            int heuresRetard) {

        String dateStr = dateVoyage != null
            ? dateVoyage.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
            : "N/A";

        String trajet = villeDepart + "->" + villeArrivee;
        String message = "Votre voyage " + trajet
            + " du " + dateStr
            + " a " + heuresRetard + "h de retard";

        return envoyerSms(telephone, message, "RETARD");
    }

    /**
     * Methode principale d'envoi SMS
     */
    private static boolean envoyerSms(String telephone, String message, String type) {
        try {
            // === SIMULATION SMSLib ===
            // En production, utiliser :
            // org.smslib.Service, org.smslib.OutboundMessage
            // Service.getInstance().sendMessage(new OutboundMessage(telephone, message));

            String dateEnvoi = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));

            // Log console
            System.out.println("=== SMS ENVOYE ===");
            System.out.println("Type    : " + type);
            System.out.println("Tel     : " + telephone);
            System.out.println("Message : " + message);
            System.out.println("Date    : " + dateEnvoi);
            System.out.println("Statut  : SIMULE (SMSLib)");
            System.out.println("==================");

            // Sauvegarder dans l'historique
            historiqueSms.add(new String[]{dateEnvoi, telephone, type, message, "ENVOYE"});

            return true;

        } catch (Exception e) {
            System.err.println("Erreur SMS : " + e.getMessage());
            historiqueSms.add(new String[]{
                LocalDateTime.now().toString(), telephone, type, message, "ECHEC"
            });
            return false;
        }
    }

    /**
     * Recuperer l'historique des SMS
     */
    public static List<String[]> getHistoriqueSms() {
        return historiqueSms;
    }

    /**
     * Vider l'historique
     */
    public static void viderHistorique() {
        historiqueSms.clear();
    }
}
