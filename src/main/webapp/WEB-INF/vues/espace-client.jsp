<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List, com.agence.transport.model.*" %>
<%
    String vue = (String) request.getAttribute("vue");
    if (vue == null) vue = "accueil";
    List<Voyage> voyages = (List<Voyage>) request.getAttribute("voyages");
    List<ReservationTransport> mesReservations = (List<ReservationTransport>) request.getAttribute("mesReservations");
    String message = (String) request.getAttribute("message");
    String erreur  = (String) request.getAttribute("erreur");
    String clientLogin = (String) session.getAttribute("login");
    int page = request.getAttribute("page") != null ? (Integer) request.getAttribute("page") : 1;
    int totalPages = request.getAttribute("totalPages") != null ? (Integer) request.getAttribute("totalPages") : 1;
    String initiale = (clientLogin != null && !clientLogin.isEmpty()) ? String.valueOf(clientLogin.charAt(0)).toUpperCase() : "C";
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Espace Client - TransitPro</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<!-- SIDEBAR CLIENT -->
<aside class="sidebar">
    <div class="sidebar-brand">
        <div class="brand-icon">&#128652;</div>
        <h2>TransitPro</h2>
        <p>Espace Client</p>
    </div>
    <nav class="sidebar-nav">
        <div class="nav-section">Menu</div>
        <a href="${pageContext.request.contextPath}/espace-client" class="nav-item <%= "accueil".equals(vue) ? "active" : "" %>">
            <span class="nav-icon">&#127968;</span> Accueil
        </a>
        <a href="${pageContext.request.contextPath}/espace-client?action=reserver" class="nav-item <%= "reserver".equals(vue) ? "active" : "" %>">
            <span class="nav-icon">&#127915;</span> Reserver un voyage
        </a>
        <a href="${pageContext.request.contextPath}/espace-client?action=mes-reservations" class="nav-item <%= "mes-reservations".equals(vue) ? "active" : "" %>">
            <span class="nav-icon">&#128196;</span> Mes reservations
        </a>
        <a href="${pageContext.request.contextPath}/rapports" class="nav-item">
            <span class="nav-icon">&#128196;</span> Mes billets PDF
        </a>
    </nav>
    <div class="sidebar-footer">
        <div class="user-info">
            <div class="user-avatar"><%= initiale %></div>
            <div>
                <div class="user-name"><%= clientLogin %></div>
                <div class="user-role">CLIENT</div>
            </div>
        </div>
        <a href="${pageContext.request.contextPath}/login" class="logout-btn">Deconnexion</a>
    </div>
</aside>

<div class="main-content">
    <div class="topbar">
        <div class="topbar-title">
            <h1>
                <% if ("accueil".equals(vue)) { %>&#127968; Bienvenue <%= clientLogin %>
                <% } else if ("reserver".equals(vue)) { %>&#127915; Reserver un voyage
                <% } else { %>&#128196; Mes reservations
                <% } %>
            </h1>
            <p>Espace client TransitPro</p>
        </div>
    </div>

    <div class="page-content">

        <% if (message != null) { %>
            <div class="alert alert-success">&#10003; <%= message %></div>
        <% } %>
        <% if (erreur != null) { %>
            <div class="alert alert-danger">&#9888; <%= erreur %></div>
        <% } %>

        <% if ("accueil".equals(vue)) { %>
        <!-- ACCUEIL -->
        <div style="display:grid;grid-template-columns:repeat(3,1fr);gap:20px;margin-bottom:28px;">
            <a href="${pageContext.request.contextPath}/espace-client?action=reserver"
               style="text-decoration:none;">
                <div class="stat-card" style="cursor:pointer;flex-direction:column;align-items:center;padding:30px;text-align:center;">
                    <div class="stat-icon blue" style="margin-bottom:12px;">&#127915;</div>
                    <div class="stat-info">
                        <div style="font-weight:700;font-size:16px;color:var(--primary);">Reserver</div>
                        <div style="font-size:13px;color:var(--text-light);">un voyage</div>
                    </div>
                </div>
            </a>
            <a href="${pageContext.request.contextPath}/espace-client?action=mes-reservations"
               style="text-decoration:none;">
                <div class="stat-card" style="cursor:pointer;flex-direction:column;align-items:center;padding:30px;text-align:center;">
                    <div class="stat-icon green" style="margin-bottom:12px;">&#128196;</div>
                    <div class="stat-info">
                        <div style="font-weight:700;font-size:16px;color:var(--primary);">Mes reservations</div>
                        <div style="font-size:13px;color:var(--text-light);">Voir mes billets</div>
                    </div>
                </div>
            </a>
            <a href="${pageContext.request.contextPath}/rapports"
               style="text-decoration:none;">
                <div class="stat-card" style="cursor:pointer;flex-direction:column;align-items:center;padding:30px;text-align:center;">
                    <div class="stat-icon orange" style="margin-bottom:12px;">&#128221;</div>
                    <div class="stat-info">
                        <div style="font-weight:700;font-size:16px;color:var(--primary);">Billets PDF</div>
                        <div style="font-size:13px;color:var(--text-light);">Imprimer mes billets</div>
                    </div>
                </div>
            </a>
        </div>

        <!-- Voyages disponibles -->
        <div class="card">
            <div class="card-header">
                <h3>&#128652; Voyages disponibles</h3>
            </div>
            <div class="card-body" style="padding:0;">
                <table>
                    <thead>
                        <tr>
                            <th>Trajet</th>
                            <th>Date depart</th>
                            <th>Prix (FCFA)</th>
                            <th>Places dispo</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                    <% if (voyages != null && !voyages.isEmpty()) {
                           for (Voyage v : voyages) {
                               if ("PROGRAMME".equals(v.getStatut()) && v.getNbPlacesDispo() > 0) {
                                   String dateStr = v.getDateHeureDepart() != null ? v.getDateHeureDepart().toString().replace("T"," ") : "-";
                    %>
                        <tr>
                            <td>
                                <strong><%= v.getVilleDepart() %></strong>
                                <span style="color:var(--accent);margin:0 6px;">&#8594;</span>
                                <strong><%= v.getVilleArrivee() %></strong>
                            </td>
                            <td style="font-size:13px;"><%= dateStr %></td>
                            <td><strong><%= String.format("%,.0f", v.getPrixPlaceFcfa()) %></strong></td>
                            <td><span style="font-weight:600;">&#128693; <%= v.getNbPlacesDispo() %></span></td>
                            <td>
                                <a href="${pageContext.request.contextPath}/espace-client?action=reserver&voyageId=<%= v.getId() %>"
                                   class="btn btn-primary btn-sm">
                                    &#127915; Reserver
                                </a>
                            </td>
                        </tr>
                    <%     }
                       }
                    } else { %>
                        <tr><td colspan="5" style="text-align:center;padding:30px;color:var(--text-light);">Aucun voyage disponible</td></tr>
                    <% } %>
                    </tbody>
                </table>
            </div>
        </div>

        <% } else if ("reserver".equals(vue)) { %>
        <!-- FORMULAIRE RESERVATION -->
        <div class="card" style="max-width:600px;margin:0 auto;">
            <div class="card-header">
                <h3>&#127915; Nouvelle reservation</h3>
            </div>
            <div class="card-body">
                <form method="post" action="${pageContext.request.contextPath}/espace-client">
                    <input type="hidden" name="action" value="reserver">
                    <div class="form-grid">
                        <div class="form-group full-width">
                            <label>Voyage *</label>
                            <select name="voyageId" required>
                                <option value="">-- Selectionnez un voyage --</option>
                                <% if (voyages != null) { for (Voyage v : voyages) {
                                       if ("PROGRAMME".equals(v.getStatut()) && v.getNbPlacesDispo() > 0) {
                                           String sel = request.getParameter("voyageId") != null &&
                                                        request.getParameter("voyageId").equals(String.valueOf(v.getId())) ? "selected" : "";
                                %>
                                    <option value="<%= v.getId() %>" <%= sel %>>
                                        <%= v.getVilleDepart() %> &#8594; <%= v.getVilleArrivee() %>
                                        | <%= String.format("%,.0f", v.getPrixPlaceFcfa()) %> FCFA
                                        | <%= v.getNbPlacesDispo() %> places
                                    </option>
                                <%     }
                                   }
                                } %>
                            </select>
                        </div>
                        <div class="form-group full-width">
                            <label>Nombre de places *</label>
                            <input type="number" name="nbPlaces" min="1" max="10" value="1" required>
                        </div>
                    </div>
                    <div style="background:#f8f9ff;border-radius:10px;padding:14px;margin:16px 0;font-size:13px;color:var(--text-light);">
                        &#8505; Le montant total sera calcule automatiquement selon le prix du voyage.
                    </div>
                    <div style="display:flex;gap:12px;justify-content:flex-end;">
                        <a href="${pageContext.request.contextPath}/espace-client" class="btn btn-outline">Annuler</a>
                        <button type="submit" class="btn btn-primary">&#127915; Confirmer la reservation</button>
                    </div>
                </form>
            </div>
        </div>

        <% } else if ("mes-reservations".equals(vue)) { %>
        <!-- MES RESERVATIONS -->
        <div class="card">
            <div class="card-header">
                <h3>&#128196; Mes reservations</h3>
            </div>
            <div class="card-body" style="padding:0;">
                <table>
                    <thead>
                        <tr>
                            <th>Billet</th>
                            <th>Trajet</th>
                            <th>Places</th>
                            <th>Montant</th>
                            <th>Statut</th>
                            <th>PDF</th>
                        </tr>
                    </thead>
                    <tbody>
                    <% if (mesReservations != null && !mesReservations.isEmpty()) {
                           for (ReservationTransport r : mesReservations) {
                               String statut = r.getStatut();
                               String badgeClass = "badge-secondary";
                               if ("CONFIRMEE".equals(statut))    badgeClass = "badge-success";
                               else if ("ANNULEE".equals(statut)) badgeClass = "badge-danger";
                    %>
                        <tr>
                            <td><span style="font-family:'Space Mono',monospace;font-size:11px;background:#f0f2f8;padding:4px 8px;border-radius:6px;font-weight:700;"><%= r.getNumeroBillet() %></span></td>
                            <td>
                                <strong><%= r.getVilleDepart() %></strong>
                                <span style="color:var(--accent);margin:0 4px;">&#8594;</span>
                                <strong><%= r.getVilleArrivee() %></strong>
                            </td>
                            <td><%= r.getNbPlaces() %> place(s)</td>
                            <td><strong><%= String.format("%,.0f", r.getMontantTotal()) %></strong> FCFA</td>
                            <td><span class="badge <%= badgeClass %>"><%= statut %></span></td>
                            <td>
                                <a href="${pageContext.request.contextPath}/pdf?type=billet&id=<%= r.getId() %>"
                                   class="btn btn-primary btn-sm" target="_blank">
                                    &#128196; PDF
                                </a>
                            </td>
                        </tr>
                    <% } } else { %>
                        <tr><td colspan="6" style="text-align:center;padding:30px;color:var(--text-light);">Aucune reservation</td></tr>
                    <% } %>
                    </tbody>
                </table>
            </div>

            <% if (totalPages > 1) { %>
            <div class="pagination" style="padding:16px;">
                <% if (page > 1) { %>
                    <a href="${pageContext.request.contextPath}/espace-client?action=mes-reservations&page=<%= page-1 %>" class="page-link">&#8592;</a>
                <% } %>
                <% for (int i = 1; i <= totalPages; i++) { %>
                    <a href="${pageContext.request.contextPath}/espace-client?action=mes-reservations&page=<%= i %>"
                       class="page-link <%= i == page ? "active" : "" %>"><%= i %></a>
                <% } %>
                <% if (page < totalPages) { %>
                    <a href="${pageContext.request.contextPath}/espace-client?action=mes-reservations&page=<%= page+1 %>" class="page-link">&#8594;</a>
                <% } %>
            </div>
            <% } %>

        </div>
        <% } %>

    </div>
</div>
</body>
</html>
