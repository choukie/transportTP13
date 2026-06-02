<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List, com.agence.transport.model.Voyage, com.agence.transport.model.ReservationTransport" %>
<%
    request.setAttribute("currentPage", "rapports");
    List<Voyage> voyages = (List<Voyage>) request.getAttribute("voyages");
    List<ReservationTransport> reservations = (List<ReservationTransport>) request.getAttribute("reservations");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Rapports PDF - TransitPro</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<%@ include file="sidebar.jsp" %>

<div class="main-content">
    <div class="topbar">
        <div class="topbar-title">
            <h1>&#128196; Rapports PDF</h1>
            <p>Generation de documents officiels</p>
        </div>
    </div>

    <div class="page-content">

        <!-- Rapport mensuel -->
        <div class="card" style="margin-bottom:24px;">
            <div class="card-header">
                <h3>&#128200; Rapport mensuel de frequentation</h3>
            </div>
            <div class="card-body">
                <p style="color:var(--text-light);margin-bottom:16px;">
                    Genere un rapport complet de toutes les reservations avec les recettes totales.
                </p>
                <a href="${pageContext.request.contextPath}/pdf?type=rapport" 
                   class="btn btn-primary" target="_blank">
                    &#128196; Generer le rapport mensuel
                </a>
            </div>
        </div>

        <!-- Billets de voyage -->
        <div class="card" style="margin-bottom:24px;">
            <div class="card-header">
                <h3>&#127915; Billets de voyage</h3>
            </div>
            <div class="card-body" style="padding:0;">
                <div class="table-wrapper">
                    <table>
                        <thead>
                            <tr>
                                <th>Billet</th>
                                <th>Passager</th>
                                <th>Trajet</th>
                                <th>Montant</th>
                                <th>Statut</th>
                                <th>Action</th>
                            </tr>
                        </thead>
                        <tbody>
                        <% if (reservations != null && !reservations.isEmpty()) {
                               for (ReservationTransport r : reservations) {
                                   String statut = r.getStatut();
                                   String badgeClass = "badge-secondary";
                                   if ("CONFIRMEE".equals(statut))    badgeClass = "badge-success";
                                   else if ("ANNULEE".equals(statut)) badgeClass = "badge-danger";
                                   else if ("EMBARQUEE".equals(statut)) badgeClass = "badge-info";
                        %>
                            <tr>
                                <td>
                                    <span style="font-family:'Space Mono',monospace;font-size:11px;background:#f0f2f8;padding:4px 8px;border-radius:6px;font-weight:700;">
                                        <%= r.getNumeroBillet() %>
                                    </span>
                                </td>
                                <td><%= r.getPassagerNom() %> <%= r.getPassagerPrenom() %></td>
                                <td>
                                    <strong><%= r.getVilleDepart() %></strong>
                                    <span style="color:var(--accent);margin:0 4px;">&#8594;</span>
                                    <strong><%= r.getVilleArrivee() %></strong>
                                </td>
                                <td><strong><%= String.format("%,.0f", r.getMontantTotal()) %></strong> FCFA</td>
                                <td><span class="badge <%= badgeClass %>"><%= statut %></span></td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/pdf?type=billet&id=<%= r.getId() %>"
                                       class="btn btn-primary btn-sm" target="_blank">
                                        &#128196; Imprimer billet
                                    </a>
                                </td>
                            </tr>
                        <% } } else { %>
                            <tr>
                                <td colspan="6" style="text-align:center;padding:30px;color:var(--text-light);">
                                    Aucune reservation disponible
                                </td>
                            </tr>
                        <% } %>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

        <!-- Manifestes de voyage -->
        <div class="card">
            <div class="card-header">
                <h3>&#128652; Manifestes de voyage</h3>
            </div>
            <div class="card-body" style="padding:0;">
                <div class="table-wrapper">
                    <table>
                        <thead>
                            <tr>
                                <th>#</th>
                                <th>Trajet</th>
                                <th>Chauffeur</th>
                                <th>Date depart</th>
                                <th>Places dispo</th>
                                <th>Action</th>
                            </tr>
                        </thead>
                        <tbody>
                        <% if (voyages != null && !voyages.isEmpty()) {
                               for (Voyage v : voyages) {
                                   String dateStr = v.getDateHeureDepart() != null ? v.getDateHeureDepart().toString().replace("T"," ") : "-";
                        %>
                            <tr>
                                <td><strong>#<%= v.getId() %></strong></td>
                                <td>
                                    <strong><%= v.getVilleDepart() %></strong>
                                    <span style="color:var(--accent);margin:0 4px;">&#8594;</span>
                                    <strong><%= v.getVilleArrivee() %></strong>
                                </td>
                                <td><%= v.getChauffeur() %></td>
                                <td style="font-size:12px;"><%= dateStr %></td>
                                <td>&#128693; <%= v.getNbPlacesDispo() %></td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/pdf?type=manifeste&id=<%= v.getId() %>"
                                       class="btn btn-secondary btn-sm" target="_blank">
                                        &#128196; Manifeste
                                    </a>
                                </td>
                            </tr>
                        <% } } else { %>
                            <tr>
                                <td colspan="6" style="text-align:center;padding:30px;color:var(--text-light);">
                                    Aucun voyage disponible
                                </td>
                            </tr>
                        <% } %>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

    </div>
</div>
</body>
</html>
