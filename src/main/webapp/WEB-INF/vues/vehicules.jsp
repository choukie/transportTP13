<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List, com.agence.transport.model.Vehicule" %>
<%
    request.setAttribute("currentPage", "vehicules");
    List<Vehicule> vehicules = (List<Vehicule>) request.getAttribute("vehicules");
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
    <title>Vehicules - TransitPro</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<%@ include file="sidebar.jsp" %>

<div class="main-content">
    <div class="topbar">
        <div class="topbar-title">
            <h1>&#128652; Vehicules</h1>
            <p>Gestion de la flotte de bus</p>
        </div>
        <div class="topbar-actions">
            <a href="${pageContext.request.contextPath}/export?type=vehicules" class="btn btn-secondary">
                &#128194; Export CSV
            </a>
            <button class="btn btn-primary" onclick="ouvrirModalAjout()">&#43; Nouveau vehicule</button>
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
        <form method="get" action="${pageContext.request.contextPath}/vehicules" style="margin-bottom:20px;">
            <div class="search-bar">
                <div class="search-input-wrap">
                    <span class="search-icon">&#128269;</span>
                    <input type="text" name="recherche" value="<%= recherche %>"
                           placeholder="Rechercher par immatriculation, marque, modele, statut...">
                </div>
                <button type="submit" class="btn btn-primary">Rechercher</button>
                <% if (!recherche.isEmpty()) { %>
                    <a href="${pageContext.request.contextPath}/vehicules" class="btn btn-outline">&#10005; Effacer</a>
                <% } %>
            </div>
        </form>

        <div class="card">
            <div class="card-header">
                <h3>Liste des vehicules</h3>
                <span style="font-size:13px; color:var(--text-light);">
                    <%= total %> vehicule(s)
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
                                <th>#</th>
                                <th>Immatriculation</th>
                                <th>Marque</th>
                                <th>Modele</th>
                                <th>Capacite</th>
                                <th>Statut</th>
                                <th>Derniere revision</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                        <% if (vehicules != null && !vehicules.isEmpty()) {
                               for (Vehicule v : vehicules) {
                                   String statut = v.getStatut();
                                   String badgeClass = "badge-secondary";
                                   if ("DISPONIBLE".equals(statut))          badgeClass = "badge-success";
                                   else if ("EN_ROUTE".equals(statut))       badgeClass = "badge-info";
                                   else if ("EN_MAINTENANCE".equals(statut)) badgeClass = "badge-warning";
                                   else if ("RETIRE".equals(statut))         badgeClass = "badge-danger";
                        %>
                            <tr>
                                <td><strong>#<%= v.getId() %></strong></td>
                                <td><span style="font-family:'Space Mono',monospace;font-weight:700;color:var(--primary);"><%= v.getImmatriculation() %></span></td>
                                <td><%= v.getMarque() %></td>
                                <td><%= v.getModele() %></td>
                                <td><span style="font-weight:600;">&#128693; <%= v.getCapacite() %> places</span></td>
                                <td><span class="badge <%= badgeClass %>"><%= statut %></span></td>
                                <td><%= v.getDateDerniereRevision() != null ? v.getDateDerniereRevision().toString() : "&#8212;" %></td>
                                <td>
                                    <div style="display:flex;gap:6px;">
                                        <button class="btn btn-warning btn-sm"
                                            onclick="ouvrirModalModif(<%= v.getId() %>,
                                                '<%= v.getImmatriculation() %>',
                                                '<%= v.getMarque() %>',
                                                '<%= v.getModele() %>',
                                                '<%= v.getCapacite() %>',
                                                '<%= v.getStatut() %>',
                                                '<%= v.getDateDerniereRevision() != null ? v.getDateDerniereRevision().toString() : "" %>')">
                                            &#9998; Modifier
                                        </button>
                                        <form method="post" action="${pageContext.request.contextPath}/vehicules"
                                              onsubmit="return confirm('Supprimer ce vehicule ?');" style="display:inline;">
                                            <input type="hidden" name="action" value="supprimer">
                                            <input type="hidden" name="id" value="<%= v.getId() %>">
                                            <button type="submit" class="btn btn-danger btn-sm">&#128465; Supprimer</button>
                                        </form>
                                    </div>
                                </td>
                            </tr>
                        <% } } else { %>
                            <tr>
                                <td colspan="8" style="text-align:center;padding:40px;color:var(--text-light);">
                                    <% if (!recherche.isEmpty()) { %>
                                        Aucun vehicule trouve pour "<%= recherche %>"
                                    <% } else { %>
                                        Aucun vehicule enregistre
                                    <% } %>
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
                        <a href="${pageContext.request.contextPath}/vehicules?recherche=<%= recherche %>&page=<%= page-1 %>" class="page-link">&#8592;</a>
                    <% } %>
                    <% for (int i = 1; i <= totalPages; i++) { %>
                        <a href="${pageContext.request.contextPath}/vehicules?recherche=<%= recherche %>&page=<%= i %>"
                           class="page-link <%= i == page ? "active" : "" %>"><%= i %></a>
                    <% } %>
                    <% if (page < totalPages) { %>
                        <a href="${pageContext.request.contextPath}/vehicules?recherche=<%= recherche %>&page=<%= page+1 %>" class="page-link">&#8594;</a>
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
            <h3>&#43; Nouveau vehicule</h3>
            <button class="modal-close" onclick="fermerModal('modalAjout')">&#10005;</button>
        </div>
        <form method="post" action="${pageContext.request.contextPath}/vehicules">
            <input type="hidden" name="action" value="ajouter">
            <div class="modal-body">
                <div class="form-grid">
                    <div class="form-group">
                        <label>Immatriculation *</label>
                        <input type="text" name="immatriculation" placeholder="Ex: LT-1234-A" required>
                    </div>
                    <div class="form-group">
                        <label>Marque *</label>
                        <input type="text" name="marque" placeholder="Ex: Mercedes" required>
                    </div>
                    <div class="form-group">
                        <label>Modele *</label>
                        <input type="text" name="modele" placeholder="Ex: Sprinter 519" required>
                    </div>
                    <div class="form-group">
                        <label>Capacite (places) *</label>
                        <input type="number" name="capacite" min="1" max="100" placeholder="Ex: 45" required>
                    </div>
                    <div class="form-group">
                        <label>Statut *</label>
                        <select name="statut" required>
                            <option value="DISPONIBLE">DISPONIBLE</option>
                            <option value="EN_ROUTE">EN_ROUTE</option>
                            <option value="EN_MAINTENANCE">EN_MAINTENANCE</option>
                            <option value="RETIRE">RETIRE</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <label>Date derniere revision</label>
                        <input type="date" name="dateDerniereRevision">
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
            <h3>&#9998; Modifier le vehicule</h3>
            <button class="modal-close" onclick="fermerModal('modalModif')">&#10005;</button>
        </div>
        <form method="post" action="${pageContext.request.contextPath}/vehicules">
            <input type="hidden" name="action" value="modifier">
            <input type="hidden" name="id" id="modif-id">
            <div class="modal-body">
                <div class="form-grid">
                    <div class="form-group">
                        <label>Immatriculation *</label>
                        <input type="text" name="immatriculation" id="modif-immat" required>
                    </div>
                    <div class="form-group">
                        <label>Marque *</label>
                        <input type="text" name="marque" id="modif-marque" required>
                    </div>
                    <div class="form-group">
                        <label>Modele *</label>
                        <input type="text" name="modele" id="modif-modele" required>
                    </div>
                    <div class="form-group">
                        <label>Capacite (places) *</label>
                        <input type="number" name="capacite" id="modif-capacite" min="1" max="100" required>
                    </div>
                    <div class="form-group">
                        <label>Statut *</label>
                        <select name="statut" id="modif-statut" required>
                            <option value="DISPONIBLE">DISPONIBLE</option>
                            <option value="EN_ROUTE">EN_ROUTE</option>
                            <option value="EN_MAINTENANCE">EN_MAINTENANCE</option>
                            <option value="RETIRE">RETIRE</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <label>Date derniere revision</label>
                        <input type="date" name="dateDerniereRevision" id="modif-date">
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
function ouvrirModalAjout() { document.getElementById('modalAjout').classList.add('show'); }
function ouvrirModalModif(id, immat, marque, modele, capacite, statut, date) {
    document.getElementById('modif-id').value = id;
    document.getElementById('modif-immat').value = immat;
    document.getElementById('modif-marque').value = marque;
    document.getElementById('modif-modele').value = modele;
    document.getElementById('modif-capacite').value = capacite;
    document.getElementById('modif-statut').value = statut;
    document.getElementById('modif-date').value = date;
    document.getElementById('modalModif').classList.add('show');
}
function fermerModal(id) { document.getElementById(id).classList.remove('show'); }
document.querySelectorAll('.modal-overlay').forEach(m => {
    m.addEventListener('click', function(e) { if (e.target === this) this.classList.remove('show'); });
});
</script>
</body>
</html>
