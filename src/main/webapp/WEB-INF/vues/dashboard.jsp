<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.agence.transport.dao.*" %>
<%
    request.setAttribute("currentPage", "dashboard");
    int nbVehicules = 0, nbVoyages = 0, nbPassagers = 0, nbReservations = 0;
    try {
        nbVehicules    = new VehiculeDAOImpl().listerTous().size();
        nbVoyages      = new VoyageDAOImpl().listerTous().size();
        nbPassagers    = new PassagerDAOImpl().listerTous().size();
        nbReservations = new ReservationDAOImpl().listerTous().size();
    } catch(Exception e) {}
    String userLogin = (String) session.getAttribute("login");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Tableau de bord - TransitPro</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<%@ include file="sidebar.jsp" %>

<div class="main-content">
    <div class="topbar">
        <div class="topbar-title">
            <h1>Tableau de bord</h1>
            <p>Bienvenue, <%= userLogin %> — Vue d'ensemble de l'agence</p>
        </div>
        <div class="topbar-actions">
            <a href="${pageContext.request.contextPath}/vehicules" class="btn btn-primary">
                ➕ Nouveau véhicule
            </a>
        </div>
    </div>

    <div class="page-content">
        <div class="stats-grid">
            <div class="stat-card">
                <div class="stat-icon red">🚌</div>
                <div class="stat-info">
                    <div class="stat-number"><%= nbVehicules %></div>
                    <div class="stat-label">Véhicules</div>
                </div>
            </div>
            <div class="stat-card">
                <div class="stat-icon blue">🗺️</div>
                <div class="stat-info">
                    <div class="stat-number"><%= nbVoyages %></div>
                    <div class="stat-label">Voyages</div>
                </div>
            </div>
            <div class="stat-card">
                <div class="stat-icon green">👥</div>
                <div class="stat-info">
                    <div class="stat-number"><%= nbPassagers %></div>
                    <div class="stat-label">Passagers</div>
                </div>
            </div>
            <div class="stat-card">
                <div class="stat-icon orange">🎫</div>
                <div class="stat-info">
                    <div class="stat-number"><%= nbReservations %></div>
                    <div class="stat-label">Réservations</div>
                </div>
            </div>
        </div>

        <div class="card">
            <div class="card-header">
                <h3>⚡ Accès rapide</h3>
            </div>
            <div class="card-body">
                <div style="display:grid; grid-template-columns:repeat(4,1fr); gap:16px;">
                    <a href="${pageContext.request.contextPath}/vehicules"
                       class="btn btn-outline"
                       style="justify-content:center;padding:24px;flex-direction:column;gap:10px;height:auto;text-align:center;">
                        <span style="font-size:32px;">🚌</span>
                        <span>Véhicules</span>
                    </a>
                    <a href="${pageContext.request.contextPath}/voyages"
                       class="btn btn-outline"
                       style="justify-content:center;padding:24px;flex-direction:column;gap:10px;height:auto;text-align:center;">
                        <span style="font-size:32px;">🗺️</span>
                        <span>Voyages</span>
                    </a>
                    <a href="${pageContext.request.contextPath}/passagers"
                       class="btn btn-outline"
                       style="justify-content:center;padding:24px;flex-direction:column;gap:10px;height:auto;text-align:center;">
                        <span style="font-size:32px;">👥</span>
                        <span>Passagers</span>
                    </a>
                    <a href="${pageContext.request.contextPath}/reservations"
                       class="btn btn-outline"
                       style="justify-content:center;padding:24px;flex-direction:column;gap:10px;height:auto;text-align:center;">
                        <span style="font-size:32px;">🎫</span>
                        <span>Réservations</span>
                    </a>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
