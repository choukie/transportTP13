<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List, com.agence.transport.model.*" %>
<%
    request.setAttribute("currentPage", "reservations");
    List<ReservationTransport> reservations = (List<ReservationTransport>) request.getAttribute("reservations");
    List<Passager> passagers = (List<Passager>) request.getAttribute("passagers");
    List<Voyage> voyages = (List<Voyage>) request.getAttribute("voyages");
    String message = (String) request.getAttribute("message");
    String erreur  = (String) request.getAttribute("erreur");
    String recherche = request.getAttribute("recherche") != null ? (String) request.getAttribute("recherche") : "";
    int page = request.getAttribute("page") != null ? (Integer) request.getAttribute("page") : 1;
    int totalPages = request.getAttribute("totalPages") != null ? (Integer) request.getAttribute("totalPages") : 1;
    int total = request.getAttribute("total") != null ? (Integer) request.getAttribute("total") : 0;
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Reservations - TransitPro</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<%@ include file="sidebar.jsp" %>

<div class="main-content">
    <div class="topbar">
        <div class="topbar-title">
            <h1>&#127915; Reservations</h1>
            <p>Gestion des reservations et billets</p>
        </div>
        <div class="topbar-actions">
            <a href="${pageContext.request.contextPath}/export?type=reservations" class="btn btn-secondary">
                &#128194; Export CSV
            </a>
            <button class="btn btn-primary" onclick="ouvrirModalAjout()">&#43; Nouvelle reservation</button>
        </div>
    </div>

    <div class="page-content">

        <% if (message != null) { %>
            <div class="alert alert-success">&#10003; <%= message %></div>
        <% } %>
        <% if (erreur != null) { %>
            <div class="alert alert-danger">&#9888; <%= erreur %></div>
        <% } %>

        <!-- BARRE DE RECHERCHE -->
        <form method="get" action="${pageContext.request.contextPath}/reservations" style="margin-bottom:20px;">
            <div class="search-bar">
                <div class="search-input-wrap">
                    <span class="search-icon">&#128269;</span>
                    <input type="text" name="recherche" value="<%= recherche %>"
                           placeholder="Rechercher par billet, passager, trajet...">
                </div>
                <button type="submit" class="btn btn-primary">Rechercher</button>
                <% if (!recherche.isEmpty()) { %>
                    <a href="${pageContext.request.contextPath}/reservations" class="btn btn-outline">&#10005; Effacer</a>
                <% } %>
            </div>
        </form>

        <div class="card">
            <div class="card-header">
                <h3>Liste des reservations</h3>
                <span style="font-size:13px;color:var(--text-light);">
                    <%= total %> reservation(s)
                    <% if (!recherche.isEmpty()) { %>
                        pour "<strong><%= recherche %></strong>"
                    <% } %>
                </span>
            </div>
            <div class="card-body" style="padding:0;">
                <div class="table-wrapper">
                    <table>
                        <thead>
                            <tr>
                                <th>Billet</th>
                                <th>Passager</th>
                                <th>Trajet</th>
                                <th>Places</th>
                                <th>Montant (FCFA)</th>
                                <th>Date reservation</th>
                                <th>Statut</th>
                                <th>Actions</th>
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
                                   String dateRes = r.getDateReservation() != null ? r.getDateReservation().toString().replace("T"," ").substring(0,16) : "-";
                        %>
                            <tr>
                                <td>
                                    <span style="font-family:'Space Mono',monospace;font-size:11px;background:#f0f2f8;padding:4px 8px;border-radius:6px;font-weight:700;">
                                        <%= r.getNumeroBillet() %>
                                    </span>
                                </td>
                                <td>
                                    <div style="display:flex;align-items:center;gap:8px;">
                                        <div style="width:32px;height:32px;background:var(--accent);border-radius:50%;display:flex;align-items:center;justify-content:center;color:#fff;font-weight:700;font-size:13px;">
                                            <%= r.getPassagerNom() != null ? r.getPassagerNom().charAt(0) : "?" %>
                                        </div>
                                        <span><%= r.getPassagerNom() %> <%= r.getPassagerPrenom() %></span>
                                    </div>
                                </td>
                                <td>
                                    <strong><%= r.getVilleDepart() %></strong>
                                    <span style="color:var(--accent);margin:0 4px;">&#8594;</span>
                                    <strong><%= r.getVilleArrivee() %></strong>
                                </td>
                                <td><strong><%= r.getNbPlaces() %></strong> place(s)</td>
                                <td><strong><%= String.format("%,.0f", r.getMontantTotal()) %></strong></td>
                                <td style="font-size:12px;"><%= dateRes %></td>
                                <td><span class="badge <%= badgeClass %>"><%= statut %></span></td>
                                <td>
                                    <div style="display:flex;gap:6px;">
                                        <button class="btn btn-warning btn-sm"
                                            onclick="ouvrirModalModif(
                                                <%= r.getId() %>,
                                                <%= r.getPassagerId() %>,
                                                <%= r.getVoyageId() %>,
                                                <%= r.getNbPlaces() %>,
                                                '<%= r.getNumeroBillet() %>',
                                                '<%= r.getStatut() %>')">
                                            &#9998; Modifier
                                        </button>
                                        <form method="post" action="${pageContext.request.contextPath}/reservations"
                                              onsubmit="return confirm('Supprimer cette reservation ?');" style="display:inline;">
                                            <input type="hidden" name="action" value="supprimer">
                                            <input type="hidden" name="id" value="<%= r.getId() %>">
                                            <button type="submit" class="btn btn-danger btn-sm">&#128465; Supprimer</button>
                                        </form>
                                    </div>
                                </td>
                            </tr>
                        <% } } else { %>
                            <tr>
                                <td colspan="8" style="text-align:center;padding:40px;color:var(--text-light);">
                                    Aucune reservation enregistree
                                </td>
                            </tr>
                        <% } %>
                        </tbody>
                    </table>
                </div>

                <!-- PAGINATION -->
                <% if (totalPages > 1) { %>
                <div class="pagination" style="padding:16px;">
                    <% if (page > 1) { %>
                        <a href="${pageContext.request.contextPath}/reservations?recherche=<%= recherche %>&page=<%= page-1 %>" class="page-link">&#8592;</a>
                    <% } %>
                    <% for (int i = 1; i <= totalPages; i++) { %>
                        <a href="${pageContext.request.contextPath}/reservations?recherche=<%= recherche %>&page=<%= i %>"
                           class="page-link <%= i == page ? "active" : "" %>"><%= i %></a>
                    <% } %>
                    <% if (page < totalPages) { %>
                        <a href="${pageContext.request.contextPath}/reservations?recherche=<%= recherche %>&page=<%= page+1 %>" class="page-link">&#8594;</a>
                    <% } %>
                </div>
                <% } %>

            </div>
        </div>
    </div>
</div>

<!-- MODAL AJOUT -->
<div class="modal-overlay" id="modalAjout">
    <div class="modal">
        <div class="modal-header">
            <h3>&#43; Nouvelle reservation</h3>
            <button class="modal-close" onclick="fermerModal('modalAjout')">&#10005;</button>
        </div>
        <form method="post" action="${pageContext.request.contextPath}/reservations">
            <input type="hidden" name="action" value="ajouter">
            <div class="modal-body">
                <div class="form-grid">
                    <div class="form-group">
                        <label>Passager *</label>
                        <select name="passagerId" required>
                            <option value="">-- Selectionnez --</option>
                            <% if (passagers != null) { for (Passager p : passagers) { %>
                                <option value="<%= p.getId() %>"><%= p.getNom() %> <%= p.getPrenom() %> - <%= p.getTelephone() %></option>
                            <% } } %>
                        </select>
                    </div>
                    <div class="form-group">
                        <label>Voyage *</label>
                        <select name="voyageId" required>
                            <option value="">-- Selectionnez --</option>
                            <% if (voyages != null) { for (Voyage v : voyages) { %>
                                <option value="<%= v.getId() %>"><%= v.getVilleDepart() %> &#8594; <%= v.getVilleArrivee() %> | <%= String.format("%,.0f", v.getPrixPlaceFcfa()) %> FCFA</option>
                            <% } } %>
                        </select>
                    </div>
                    <div class="form-group">
                        <label>Nombre de places *</label>
                        <input type="number" name="nbPlaces" min="1" max="50" value="1" required>
                    </div>
                    <div class="form-group">
                        <label>Statut *</label>
                        <select name="statut" required>
                            <option value="CONFIRMEE">CONFIRMEE</option>
                            <option value="ANNULEE">ANNULEE</option>
                            <option value="EMBARQUEE">EMBARQUEE</option>
                        </select>
                    </div>
                </div>
                <div style="background:#f8f9ff;border-radius:10px;padding:14px;margin-top:12px;font-size:13px;color:var(--text-light);">
                    &#8505; Le montant total et le numero de billet seront calcules automatiquement.
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-outline" onclick="fermerModal('modalAjout')">Annuler</button>
                <button type="submit" class="btn btn-primary">&#127915; Confirmer la reservation</button>
            </div>
        </form>
    </div>
</div>

<!-- MODAL MODIFICATION -->
<div class="modal-overlay" id="modalModif">
    <div class="modal">
        <div class="modal-header">
            <h3>&#9998; Modifier la reservation</h3>
            <button class="modal-close" onclick="fermerModal('modalModif')">&#10005;</button>
        </div>
        <form method="post" action="${pageContext.request.contextPath}/reservations">
            <input type="hidden" name="action" value="modifier">
            <input type="hidden" name="id" id="modif-id">
            <input type="hidden" name="numeroBillet" id="modif-billet">
            <div class="modal-body">
                <div class="form-grid">
                    <div class="form-group">
                        <label>Passager *</label>
                        <select name="passagerId" id="modif-passagerId" required>
                            <% if (passagers != null) { for (Passager p : passagers) { %>
                                <option value="<%= p.getId() %>"><%= p.getNom() %> <%= p.getPrenom() %></option>
                            <% } } %>
                        </select>
                    </div>
                    <div class="form-group">
                        <label>Voyage *</label>
                        <select name="voyageId" id="modif-voyageId" required>
                            <% if (voyages != null) { for (Voyage v : voyages) { %>
                                <option value="<%= v.getId() %>"><%= v.getVilleDepart() %> &#8594; <%= v.getVilleArrivee() %></option>
                            <% } } %>
                        </select>
                    </div>
                    <div class="form-group">
                        <label>Nombre de places *</label>
                        <input type="number" name="nbPlaces" id="modif-nbPlaces" min="1" required>
                    </div>
                    <div class="form-group">
                        <label>Statut *</label>
                        <select name="statut" id="modif-statut" required>
                            <option value="CONFIRMEE">CONFIRMEE</option>
                            <option value="ANNULEE">ANNULEE</option>
                            <option value="EMBARQUEE">EMBARQUEE</option>
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
function ouvrirModalModif(id, passagerId, voyageId, nbPlaces, billet, statut) {
    document.getElementById('modif-id').value = id;
    document.getElementById('modif-billet').value = billet;
    document.getElementById('modif-passagerId').value = passagerId;
    document.getElementById('modif-voyageId').value = voyageId;
    document.getElementById('modif-nbPlaces').value = nbPlaces;
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
