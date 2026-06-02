<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*, java.text.DecimalFormat" %>
<%
    request.setAttribute("currentPage", "statistiques");
    List<Map<String,Object>> traficParTrajet = (List<Map<String,Object>>) request.getAttribute("traficParTrajet");
    List<Map<String,Object>> remplissageVehicule = (List<Map<String,Object>>) request.getAttribute("remplissageVehicule");
    List<Map<String,Object>> trajetsPopulaires = (List<Map<String,Object>>) request.getAttribute("trajetsPopulaires");
    double totalRecettes = request.getAttribute("totalRecettes") != null ? (Double) request.getAttribute("totalRecettes") : 0;
    int totalReservations = request.getAttribute("totalReservations") != null ? (Integer) request.getAttribute("totalReservations") : 0;
    String erreur = (String) request.getAttribute("erreur");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Statistiques - TransitPro</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<%@ include file="sidebar.jsp" %>

<div class="main-content">
    <div class="topbar">
        <div class="topbar-title">
            <h1>&#128200; Statistiques</h1>
            <p>Analyse des performances de l'agence</p>
        </div>
    </div>

    <div class="page-content">

        <% if (erreur != null) { %>
            <div class="alert alert-danger">&#9888; <%= erreur %></div>
        <% } %>

        <!-- Totaux -->
        <div class="stats-grid" style="grid-template-columns: repeat(2,1fr); margin-bottom:28px;">
            <div class="stat-card">
                <div class="stat-icon green">&#128181;</div>
                <div class="stat-info">
                    <div class="stat-number"><%= String.format("%,.0f", totalRecettes) %></div>
                    <div class="stat-label">Total recettes (FCFA)</div>
                </div>
            </div>
            <div class="stat-card">
                <div class="stat-icon orange">&#127915;</div>
                <div class="stat-info">
                    <div class="stat-number"><%= totalReservations %></div>
                    <div class="stat-label">Total reservations</div>
                </div>
            </div>
        </div>

        <!-- Trafic par trajet -->
        <div class="card" style="margin-bottom:24px;">
            <div class="card-header">
                <h3>&#128506; Trafic et recettes par trajet</h3>
            </div>
            <div class="card-body" style="padding:0;">
                <div class="table-wrapper">
                    <table>
                        <thead>
                            <tr>
                                <th>Trajet</th>
                                <th>Nb reservations</th>
                                <th>Total recettes (FCFA)</th>
                                <th>Performance</th>
                            </tr>
                        </thead>
                        <tbody>
                        <% if (traficParTrajet != null && !traficParTrajet.isEmpty()) {
                               for (Map<String,Object> row : traficParTrajet) {
                                   int nb = (Integer) row.get("nbReservations");
                                   double recettes = (Double) row.get("totalRecettes");
                                   int maxNb = (Integer) ((Map<String,Object>)traficParTrajet.get(0)).get("nbReservations");
                                   int pct = maxNb > 0 ? (nb * 100 / maxNb) : 0;
                        %>
                            <tr>
                                <td>
                                    <strong><%= row.get("villeDepart") %></strong>
                                    <span style="color:var(--accent);margin:0 6px;">&#8594;</span>
                                    <strong><%= row.get("villeArrivee") %></strong>
                                </td>
                                <td><strong><%= nb %></strong> reservation(s)</td>
                                <td><strong><%= String.format("%,.0f", recettes) %></strong> FCFA</td>
                                <td style="width:200px;">
                                    <div style="background:#f0f2f8;border-radius:20px;height:8px;overflow:hidden;">
                                        <div style="background:var(--accent);height:100%;width:<%= pct %>%;border-radius:20px;transition:width 0.5s;"></div>
                                    </div>
                                </td>
                            </tr>
                        <% } } else { %>
                            <tr><td colspan="4" style="text-align:center;padding:30px;color:var(--text-light);">Aucune donnee disponible</td></tr>
                        <% } %>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

        <!-- Taux de remplissage -->
        <div class="card" style="margin-bottom:24px;">
            <div class="card-header">
                <h3>&#128652; Taux de remplissage par vehicule</h3>
            </div>
            <div class="card-body" style="padding:0;">
                <div class="table-wrapper">
                    <table>
                        <thead>
                            <tr>
                                <th>Vehicule</th>
                                <th>Capacite</th>
                                <th>Places vendues</th>
                                <th>Taux de remplissage</th>
                            </tr>
                        </thead>
                        <tbody>
                        <% if (remplissageVehicule != null && !remplissageVehicule.isEmpty()) {
                               for (Map<String,Object> row : remplissageVehicule) {
                                   double taux = (Double) row.get("taux");
                                   String couleur = taux >= 75 ? "var(--success)" : taux >= 40 ? "var(--warning)" : "var(--danger)";
                        %>
                            <tr>
                                <td>
                                    <span style="font-family:'Space Mono',monospace;font-weight:700;"><%= row.get("immatriculation") %></span>
                                    <span style="color:var(--text-light);font-size:13px;"> - <%= row.get("marque") %> <%= row.get("modele") %></span>
                                </td>
                                <td><%= row.get("capacite") %> places</td>
                                <td><%= row.get("placesVendues") %> places</td>
                                <td style="width:250px;">
                                    <div style="display:flex;align-items:center;gap:10px;">
                                        <div style="flex:1;background:#f0f2f8;border-radius:20px;height:10px;overflow:hidden;">
                                            <div style="background:<%= couleur %>;height:100%;width:<%= (int)taux %>%;border-radius:20px;"></div>
                                        </div>
                                        <span style="font-weight:700;color:<%= couleur %>;min-width:45px;"><%= String.format("%.0f", taux) %>%</span>
                                    </div>
                                </td>
                            </tr>
                        <% } } else { %>
                            <tr><td colspan="4" style="text-align:center;padding:30px;color:var(--text-light);">Aucune donnee disponible</td></tr>
                        <% } %>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

        <!-- Trajets populaires -->
        <div class="card">
            <div class="card-header">
                <h3>&#127942; Top 5 trajets les plus demandes</h3>
            </div>
            <div class="card-body">
                <% if (trajetsPopulaires != null && !trajetsPopulaires.isEmpty()) {
                       int rank = 1;
                       for (Map<String,Object> row : trajetsPopulaires) {
                           String medal = rank == 1 ? "&#127945;" : rank == 2 ? "&#129352;" : rank == 3 ? "&#129353;" : "#" + rank;
                %>
                    <div style="display:flex;align-items:center;justify-content:space-between;padding:14px 0;border-bottom:1px solid var(--border);">
                        <div style="display:flex;align-items:center;gap:16px;">
                            <span style="font-size:22px;width:36px;text-align:center;"><%= medal %></span>
                            <div>
                                <strong><%= row.get("villeDepart") %></strong>
                                <span style="color:var(--accent);margin:0 8px;">&#8594;</span>
                                <strong><%= row.get("villeArrivee") %></strong>
                            </div>
                        </div>
                        <span class="badge badge-info"><%= row.get("nb") %> reservation(s)</span>
                    </div>
                <%     rank++;
                   }
                } else { %>
                    <p style="text-align:center;color:var(--text-light);padding:20px;">Aucune donnee disponible</p>
                <% } %>
            </div>
        </div>

    </div>
</div>
</body>
</html>
