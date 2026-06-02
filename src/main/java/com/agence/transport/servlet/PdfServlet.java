package com.agence.transport.servlet;

import java.io.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.agence.transport.dao.*;
import com.agence.transport.model.*;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.io.font.constants.StandardFonts;

import java.util.List;

public class PdfServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final DeviceRgb ROUGE      = new DeviceRgb(233, 69, 96);
    private static final DeviceRgb BLEU_FONCE = new DeviceRgb(26, 26, 46);
    private static final DeviceRgb GRIS_CLAIR = new DeviceRgb(240, 242, 248);
    private static final DeviceRgb BLANC      = new DeviceRgb(255, 255, 255);

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
        if (type == null) type = "billet";

        response.setContentType("application/pdf");

        switch (type) {
            case "billet":
                genererBillet(request, response);
                break;
            case "manifeste":
                genererManifeste(request, response);
                break;
            case "rapport":
                genererRapport(request, response);
                break;
            default:
                genererBillet(request, response);
        }
    }

    // ===== BILLET DE VOYAGE =====
    private void genererBillet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        Long reservationId = Long.parseLong(request.getParameter("id"));
        ReservationTransport r = new ReservationDAOImpl().trouverParId(reservationId);

        if (r == null) {
            response.getWriter().write("Reservation introuvable");
            return;
        }

        response.setHeader("Content-Disposition", "inline; filename=billet_" + r.getNumeroBillet() + ".pdf");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdf = new PdfDocument(writer);
        Document doc = new Document(pdf);
        doc.setMargins(40, 40, 40, 40);

        PdfFont fontBold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
        PdfFont fontNormal = PdfFontFactory.createFont(StandardFonts.HELVETICA);

        // En-tete
        Table header = new Table(UnitValue.createPercentArray(new float[]{1})).useAllAvailableWidth();
        Cell headerCell = new Cell()
            .add(new Paragraph("TransitPro").setFont(fontBold).setFontSize(22).setFontColor(BLANC))
            .add(new Paragraph("BILLET DE VOYAGE").setFont(fontBold).setFontSize(14).setFontColor(BLANC))
            .setBackgroundColor(BLEU_FONCE)
            .setPadding(20)
            .setTextAlignment(TextAlignment.CENTER)
            .setBorder(null);
        header.addCell(headerCell);
        doc.add(header);

        doc.add(new Paragraph("\n"));

        // Numero billet
        Table billetNum = new Table(UnitValue.createPercentArray(new float[]{1})).useAllAvailableWidth();
        Cell billetCell = new Cell()
            .add(new Paragraph("N° " + r.getNumeroBillet()).setFont(fontBold).setFontSize(16).setFontColor(ROUGE))
            .setBackgroundColor(GRIS_CLAIR)
            .setPadding(12)
            .setTextAlignment(TextAlignment.CENTER)
            .setBorder(null);
        billetNum.addCell(billetCell);
        doc.add(billetNum);

        doc.add(new Paragraph("\n"));

        // Trajet
        Table trajet = new Table(UnitValue.createPercentArray(new float[]{1, 0.3f, 1})).useAllAvailableWidth();
        trajet.addCell(creerCellule("DEPART\n" + r.getVilleDepart(), fontBold, 14, BLEU_FONCE, TextAlignment.CENTER));
        trajet.addCell(creerCellule(">>>", fontBold, 16, ROUGE, TextAlignment.CENTER));
        trajet.addCell(creerCellule("ARRIVEE\n" + r.getVilleArrivee(), fontBold, 14, BLEU_FONCE, TextAlignment.CENTER));
        doc.add(trajet);

        doc.add(new Paragraph("\n"));

        // Infos passager et voyage
        Table infos = new Table(UnitValue.createPercentArray(new float[]{1, 1})).useAllAvailableWidth();

        infos.addCell(creerInfoCell("PASSAGER", r.getPassagerNom() + " " + r.getPassagerPrenom(), fontBold, fontNormal));
        infos.addCell(creerInfoCell("DATE DE DEPART",
            r.getDateHeureDepart() != null ? r.getDateHeureDepart().toString().replace("T", " ") : "-",
            fontBold, fontNormal));
        infos.addCell(creerInfoCell("NOMBRE DE PLACES", String.valueOf(r.getNbPlaces()), fontBold, fontNormal));
        infos.addCell(creerInfoCell("MONTANT TOTAL", String.format("%,.0f FCFA", r.getMontantTotal()), fontBold, fontNormal));
        infos.addCell(creerInfoCell("STATUT", r.getStatut(), fontBold, fontNormal));
        infos.addCell(creerInfoCell("CHAUFFEUR", r.getChauffeur() != null ? r.getChauffeur() : "-", fontBold, fontNormal));

        doc.add(infos);

        doc.add(new Paragraph("\n"));

        // Pied de page
        doc.add(new Paragraph("Merci de voyager avec TransitPro. Bon voyage !")
            .setFont(fontNormal).setFontSize(10)
            .setFontColor(new DeviceRgb(99, 110, 114))
            .setTextAlignment(TextAlignment.CENTER));

        doc.close();
        response.getOutputStream().write(baos.toByteArray());
    }

    // ===== MANIFESTE DE VOYAGE =====
    private void genererManifeste(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        Long voyageId = Long.parseLong(request.getParameter("id"));
        List<ReservationTransport> reservations = new ReservationDAOImpl().listerParVoyage(voyageId);

        response.setHeader("Content-Disposition", "inline; filename=manifeste_voyage_" + voyageId + ".pdf");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdf = new PdfDocument(writer);
        Document doc = new Document(pdf);
        doc.setMargins(40, 40, 40, 40);

        PdfFont fontBold   = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
        PdfFont fontNormal = PdfFontFactory.createFont(StandardFonts.HELVETICA);

        // En-tete
        doc.add(new Paragraph("TransitPro - MANIFESTE DE VOYAGE")
            .setFont(fontBold).setFontSize(18).setFontColor(BLEU_FONCE)
            .setTextAlignment(TextAlignment.CENTER));
        doc.add(new Paragraph("Voyage #" + voyageId)
            .setFont(fontNormal).setFontSize(12)
            .setFontColor(new DeviceRgb(99, 110, 114))
            .setTextAlignment(TextAlignment.CENTER));

        doc.add(new Paragraph("\n"));

        if (!reservations.isEmpty()) {
            ReservationTransport first = reservations.get(0);
            doc.add(new Paragraph("Trajet : " + first.getVilleDepart() + " -> " + first.getVilleArrivee())
                .setFont(fontBold).setFontSize(12).setFontColor(ROUGE));
            if (first.getDateHeureDepart() != null) {
                doc.add(new Paragraph("Date : " + first.getDateHeureDepart().toString().replace("T", " "))
                    .setFont(fontNormal).setFontSize(11));
            }
            doc.add(new Paragraph("Nombre de passagers : " + reservations.size())
                .setFont(fontBold).setFontSize(11));
            doc.add(new Paragraph("\n"));
        }

        // Tableau des passagers
        Table table = new Table(UnitValue.createPercentArray(new float[]{0.5f, 2, 1.5f, 1, 1.5f})).useAllAvailableWidth();

        // En-tetes colonnes
        String[] headers = {"#", "Passager", "Billet", "Places", "Statut"};
        for (String h : headers) {
            table.addHeaderCell(new Cell()
                .add(new Paragraph(h).setFont(fontBold).setFontSize(10).setFontColor(BLANC))
                .setBackgroundColor(BLEU_FONCE).setPadding(8).setBorder(null));
        }

        int i = 1;
        for (ReservationTransport r : reservations) {
            DeviceRgb bg = (i % 2 == 0) ? GRIS_CLAIR : BLANC;
            table.addCell(celluleTable(String.valueOf(i), fontNormal, bg));
            table.addCell(celluleTable(r.getPassagerNom() + " " + r.getPassagerPrenom(), fontNormal, bg));
            table.addCell(celluleTable(r.getNumeroBillet(), fontNormal, bg));
            table.addCell(celluleTable(String.valueOf(r.getNbPlaces()), fontNormal, bg));
            table.addCell(celluleTable(r.getStatut(), fontNormal, bg));
            i++;
        }

        doc.add(table);
        doc.add(new Paragraph("\nDocument genere par TransitPro")
            .setFont(fontNormal).setFontSize(9)
            .setFontColor(new DeviceRgb(99, 110, 114))
            .setTextAlignment(TextAlignment.RIGHT));

        doc.close();
        response.getOutputStream().write(baos.toByteArray());
    }

    // ===== RAPPORT MENSUEL =====
    private void genererRapport(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setHeader("Content-Disposition", "inline; filename=rapport_mensuel.pdf");

        List<ReservationTransport> reservations = new ReservationDAOImpl().listerTous();
        double totalRecettes = reservations.stream().mapToDouble(ReservationTransport::getMontantTotal).sum();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdf = new PdfDocument(writer);
        Document doc = new Document(pdf);
        doc.setMargins(40, 40, 40, 40);

        PdfFont fontBold   = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
        PdfFont fontNormal = PdfFontFactory.createFont(StandardFonts.HELVETICA);

        // En-tete
        doc.add(new Paragraph("TransitPro")
            .setFont(fontBold).setFontSize(24).setFontColor(BLEU_FONCE)
            .setTextAlignment(TextAlignment.CENTER));
        doc.add(new Paragraph("RAPPORT MENSUEL DE FREQUENTATION")
            .setFont(fontBold).setFontSize(14).setFontColor(ROUGE)
            .setTextAlignment(TextAlignment.CENTER));
        doc.add(new Paragraph("\n"));

        // Stats globales
        Table stats = new Table(UnitValue.createPercentArray(new float[]{1, 1})).useAllAvailableWidth();
        stats.addCell(creerInfoCell("TOTAL RESERVATIONS", String.valueOf(reservations.size()), fontBold, fontNormal));
        stats.addCell(creerInfoCell("TOTAL RECETTES", String.format("%,.0f FCFA", totalRecettes), fontBold, fontNormal));
        doc.add(stats);
        doc.add(new Paragraph("\n"));

        // Liste des reservations
        doc.add(new Paragraph("Detail des reservations")
            .setFont(fontBold).setFontSize(13).setFontColor(BLEU_FONCE));
        doc.add(new Paragraph("\n"));

        Table table = new Table(UnitValue.createPercentArray(new float[]{1.5f, 2, 2, 1, 1.5f, 1})).useAllAvailableWidth();
        String[] headers = {"Billet", "Passager", "Trajet", "Places", "Montant", "Statut"};
        for (String h : headers) {
            table.addHeaderCell(new Cell()
                .add(new Paragraph(h).setFont(fontBold).setFontSize(9).setFontColor(BLANC))
                .setBackgroundColor(BLEU_FONCE).setPadding(6).setBorder(null));
        }

        int i = 1;
        for (ReservationTransport r : reservations) {
            DeviceRgb bg = (i % 2 == 0) ? GRIS_CLAIR : BLANC;
            table.addCell(celluleTable(r.getNumeroBillet(), fontNormal, bg));
            table.addCell(celluleTable(r.getPassagerNom() + " " + r.getPassagerPrenom(), fontNormal, bg));
            table.addCell(celluleTable(r.getVilleDepart() + "->" + r.getVilleArrivee(), fontNormal, bg));
            table.addCell(celluleTable(String.valueOf(r.getNbPlaces()), fontNormal, bg));
            table.addCell(celluleTable(String.format("%,.0f", r.getMontantTotal()), fontNormal, bg));
            table.addCell(celluleTable(r.getStatut(), fontNormal, bg));
            i++;
        }
        doc.add(table);

        doc.add(new Paragraph("\nDocument genere par TransitPro")
            .setFont(fontNormal).setFontSize(9)
            .setFontColor(new DeviceRgb(99, 110, 114))
            .setTextAlignment(TextAlignment.RIGHT));

        doc.close();
        response.getOutputStream().write(baos.toByteArray());
    }

    // ===== HELPERS =====
    private Cell creerCellule(String text, PdfFont font, float size, DeviceRgb color, TextAlignment align) {
        return new Cell()
            .add(new Paragraph(text).setFont(font).setFontSize(size).setFontColor(color))
            .setTextAlignment(align).setPadding(10).setBorder(null)
            .setBackgroundColor(GRIS_CLAIR);
    }

    private Cell creerInfoCell(String label, String value, PdfFont fontBold, PdfFont fontNormal) {
        return new Cell()
            .add(new Paragraph(label).setFont(fontBold).setFontSize(9).setFontColor(new DeviceRgb(99, 110, 114)))
            .add(new Paragraph(value).setFont(fontBold).setFontSize(12).setFontColor(BLEU_FONCE))
            .setBackgroundColor(GRIS_CLAIR).setPadding(12).setBorder(null);
    }

    private Cell celluleTable(String text, PdfFont font, DeviceRgb bg) {
        return new Cell()
            .add(new Paragraph(text).setFont(font).setFontSize(9))
            .setBackgroundColor(bg).setPadding(6).setBorder(null);
    }
}
