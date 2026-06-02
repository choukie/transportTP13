<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List, com.agence.transport.model.Voyage, com.agence.transport.model.Vehicule" %>
<%
    request.setAttribute("currentPage", "voyages");
    List<Voyage> voyages = (List<Voyage>) request.getAttribute("voyages");
    List<Vehicule> vehicules = (List<Vehicule>) request.getAttribute("vehicules");
    String message = (String) request.getAttribute("message");
    String erreur  = (String) request.getAttribute("erreur");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Voyages - TransitPro</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<%@ include file="sidebar.jsp" %>

<div class="main-content">
    <div class="topbar">
        <div class="topbar-title">
            <h1>&#128506; Voyages</h1>
            <p>Gestion des voyages programmes</p>
        </div>
        <div class="topbar-actions">
            <a href="${pageContext.request.contextPath}/export?type=voyages" class="btn btn-secondary">
                &#128194; Export CSV
            </a>
            <button class="btn btn-primary" onclick="ouvrirModalAjout()">&#43; Nouveau voyage</button>
        </div>
    </div>

    <div class="page-content">

        <% if (message != null) { %>
            <div class="alert alert-success">&#10003; <%= message %></div>
        <% } %>
        <% if (erreur != null) { %>
            <div class="alert alert-danger">&#9888; <%= erreur %></div>
        <% } %>

        <div class="card">
            <div class="card-header">
                <h3>Liste des voyages</h3>
                <span style="font-size:13px; color:var(--text-light);">
                    <%= voyages != null ? voyages.size() : 0 %> voyage(s)
                </span>
            </div>
            <div class="card-body" style="padding:0;">
                <div class="table-wrapper">
                    <table>
                        <thead>
                            <tr>
                                <th>#</th>
                                <th>Trajet</th>
                                <th>Vehicule</th>
                                <th>Chauffeur</th>
                                <th>Depart</th>
                                <th>Prix (FCFA)</th>
                                <th>Places dispo</th>
                                <th>Statut</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                        <% if (voyages != null && !voyages.isEmpty()) {
                               for (Voyage v : voyages) {
                                   String dateStr = v.getDateHeureDepart() != null ? v.getDateHeureDepart().toString().replace("T", " ") : "-";
                                   String statut = v.getStatut();
                                   String badgeClass = "badge-secondary";
                                   if ("PROGRAMME".equals(statut))     badgeClass = "badge-info";
                                   else if ("EN_COURS".equals(statut)) badgeClass = "badge-warning";
                                   else if ("ARRIVE".equals(statut))   badgeClass = "badge-success";
                                   else if ("ANNULE".equals(statut))   badgeClass = "badge-danger";
                        %>
                            <tr>
                                <td><strong>#<%= v.getId() %></strong></td>
                                <td>
                                    <strong><%= v.getVilleDepart() %></strong>
                                    <span style="color:var(--accent);margin:0 6px;">&#8594;</span>
                                    <strong><%= v.getVilleArrivee() %></strong>
                                </td>
                                <td><span style="font-family:'Space Mono',monospace;font-size:12px;"><%= v.getVehiculeImmatriculation() %></span></td>
                                <td><%= v.getChauffeur() %></td>
                                <td style="font-size:13px;"><%= dateStr %></td>
                                <td><strong><%= String.format("%,.0f", v.getPrixPlaceFcfa()) %></strong></td>
                                <td><span style="font-weight:600;">&#128693; <%= v.getNbPlacesDispo() %></span></td>
                                <td><span class="badge <%= badgeClass %>"><%= statut %></span></td>
                                <td>
                                    <div style="display:flex;gap:6px;">
                                        <button class="btn btn-warning btn-sm"
                                            onclick="ouvrirModalModif(
                                                <%= v.getId() %>,
                                                <%= v.getVehiculeId() %>,
                                                '<%= v.getChauffeur() %>',
                                                '<%= v.getVilleDepart() %>',
                                                '<%= v.getVilleArrivee() %>',
                                                '<%= v.getDateHeureDepart() != null ? v.getDateHeureDepart().toString() : "" %>',
                                                '<%= v.getPrixPlaceFcfa() %>',
                                                '<%= v.getNbPlacesDispo() %>',
                                                '<%= v.getStatut() %>')">
                                            &#9998; Modifier
                                        </button>
                                        <form method="post" action="${pageContext.request.contextPath}/voyages"
                                              onsubmit="return confirm('Supprimer ce voyage ?');" style="display:inline;">
                                            <input type="hidden" name="action" value="supprimer">
                                            <input type="hidden" name="id" value="<%= v.getId() %>">
                                            <button type="submit" class="btn btn-danger btn-sm">&#128465; Supprimer</button>
                                        </form>
                                    </div>
                                </td>
                            </tr>
                        <% } } else { %>
                            <tr>
                                <td colspan="9" style="text-align:center;padding:40px;color:var(--text-light);">
                                    Aucun voyage enregistre
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

<!-- MODAL AJOUT -->
<div class="modal-overlay" id="modalAjout">
    <div class="modal">
        <div class="modal-header">
            <h3>&#43; Nouveau voyage</h3>
            <button class="modal-close" onclick="fermerModal('modalAjout')">&#10005;</button>
        </div>
        <form method="post" action="${pageContext.request.contextPath}/voyages">
            <input type="hidden" name="action" value="ajouter">
            <div class="modal-body">
                <div class="form-grid">
                    <div class="form-group">
                        <label>Vehicule *</label>
                        <select name="vehiculeId" required>
                            <option value="">-- Selectionnez --</option>
                            <% if (vehicules != null) { for (Vehicule veh : vehicules) { %>
                                <option value="<%= veh.getId() %>"><%= veh.getImmatriculation() %> - <%= veh.getMarque() %> <%= veh.getModele() %></option>
                            <% } } %>
                        </select>
                    </div>
                    <div class="form-group">
                        <label>Chauffeur *</label>
                        <input type="text" name="chauffeur" placeholder="Nom du chauffeur" required>
                    </div>
                    <div class="form-group">
                        <label>Ville de depart *</label>
                        <input type="text" name="villeDepart" placeholder="Ex: Yaounde" required>
                    </div>
                    <div class="form-group">
                        <label>Ville d'arrivee *</label>
                        <input type="text" name="villeArrivee" placeholder="Ex: Douala" required>
                    </div>
                    <div class="form-group">
                        <label>Date et heure de depart *</label>
                        <input type="datetime-local" name="dateHeureDepart" required>
                    </div>
                    <div class="form-group">
                        <label>Prix par place (FCFA) *</label>
                        <input type="number" name="prixPlaceFcfa" min="0" step="100" placeholder="Ex: 5000" required>
                    </div>
                    <div class="form-group">
                        <label>Nombre de places *</label>
                        <input type="number" name="nbPlacesDispo" min="1" placeholder="Ex: 45" required>
                    </div>
                    <div class="form-group">
                        <label>Statut *</label>
                        <select name="statut" required>
                            <option value="PROGRAMME">PROGRAMME</option>
                            <option value="EN_COURS">EN_COURS</option>
                            <option value="ARRIVE">ARRIVE</option>
                            <option value="ANNULE">ANNULE</option>
                        </select>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-outline" onclick="fermerModal('modalAjout')">Annuler</button>
                <button type="submit" class="btn btn-primary">&#128190; Enregistrer</button>
            </div>
        </form>
    </div>
</div>

<!-- MODAL MODIFICATION -->
<div class="modal-overlay" id="modalModif">
    <div class="modal">
        <div class="modal-header">
            <h3>&#9998; Modifier le voyage</h3>
            <button class="modal-close" onclick="fermerModal('modalModif')">&#10005;</button>
        </div>
        <form method="post" action="${pageContext.request.contextPath}/voyages">
            <input type="hidden" name="action" value="modifier">
            <input type="hidden" name="id" id="modif-id">
            <div class="modal-body">
                <div class="form-grid">
                    <div class="form-group">
                        <label>Vehicule *</label>
                        <select name="vehiculeId" id="modif-vehiculeId" required>
                            <% if (vehicules != null) { for (Vehicule veh : vehicules) { %>
                                <option value="<%= veh.getId() %>"><%= veh.getImmatriculation() %> - <%= veh.getMarque() %></option>
                            <% } } %>
                        </select>
                    </div>
                    <div class="form-group">
                        <label>Chauffeur *</label>
                        <input type="text" name="chauffeur" id="modif-chauffeur" required>
                    </div>
                    <div class="form-group">
                        <label>Ville de depart *</label>
                        <input type="text" name="villeDepart" id="modif-villeDepart" required>
                    </div>
                    <div class="form-group">
                        <label>Ville d'arrivee *</label>
                        <input type="text" name="villeArrivee" id="modif-villeArrivee" required>
                    </div>
                    <div class="form-group">
                        <label>Date et heure de depart *</label>
                        <input type="datetime-local" name="dateHeureDepart" id="modif-dateHeureDepart" required>
                    </div>
                    <div class="form-group">
                        <label>Prix par place (FCFA) *</label>
                        <input type="number" name="prixPlaceFcfa" id="modif-prix" min="0" step="100" required>
                    </div>
                    <div class="form-group">
                        <label>Nombre de places *</label>
                        <input type="number" name="nbPlacesDispo" id="modif-places" min="1" required>
                    </div>
                    <div class="form-group">
                        <label>Statut *</label>
                        <select name="statut" id="modif-statut" required>
                            <option value="PROGRAMME">PROGRAMME</option>
                            <option value="EN_COURS">EN_COURS</option>
                            <option value="ARRIVE">ARRIVE</option>
                            <option value="ANNULE">ANNULE</option>
                        </select>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-outline" onclick="fermerModal('modalModif')">Annuler</button>
                <button type="submit" class="btn btn-primary">&#128190; Mettre a jour</button>
            </div>
        </form>
    </div>
</div>

<script>
function ouvrirModalAjout() {
    document.getElementById('modalAjout').classList.add('show');
}
function ouvrirModalModif(id, vehiculeId, chauffeur, villeDepart, villeArrivee, dateHeure, prix, places, statut) {
    document.getElementById('modif-id').value = id;
    document.getElementById('modif-vehiculeId').value = vehiculeId;
    document.getElementById('modif-chauffeur').value = chauffeur;
    document.getElementById('modif-villeDepart').value = villeDepart;
    document.getElementById('modif-villeArrivee').value = villeArrivee;
    document.getElementById('modif-dateHeureDepart').value = dateHeure;
    document.getElementById('modif-prix').value = prix;
    document.getElementById('modif-places').value = places;
    document.getElementById('modif-statut').value = statut;
    document.getElementById('modalModif').classList.add('show');
}
function fermerModal(id) {
    document.getElementById(id).classList.remove('show');
}
document.querySelectorAll('.modal-overlay').forEach(m => {
    m.addEventListener('click', function(e) {
        if (e.target === this) this.classList.remove('show');
    });
});
</script>
</body>
</html>
