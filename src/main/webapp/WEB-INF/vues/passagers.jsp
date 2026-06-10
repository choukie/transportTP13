<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List, com.agence.transport.model.Passager" %>
<%
    request.setAttribute("currentPage", "passagers");
    List<Passager> passagers = (List<Passager>) request.getAttribute("passagers");
    String message = (String) request.getAttribute("message");
    String erreur  = (String) request.getAttribute("erreur");
    String recherche = request.getAttribute("recherche") != null ? (String) request.getAttribute("recherche") : "";
    int pageNum = request.getAttribute("page") != null ? (Integer) request.getAttribute("page") : 1;
    int totalPages = request.getAttribute("totalPages") != null ? (Integer) request.getAttribute("totalPages") : 1;
    int total = request.getAttribute("total") != null ? (Integer) request.getAttribute("total") : 0;
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Passagers - TransitPro</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<%@ include file="sidebar.jsp" %>

<div class="main-content">
    <div class="topbar">
        <div class="topbar-title">
            <h1>&#128101; Passagers</h1>
            <p>Gestion des passagers enregistres</p>
        </div>
        <div class="topbar-actions">
            <a href="${pageContext.request.contextPath}/export?type=passagers" class="btn btn-secondary">
                &#128194; Export CSV
            </a>
            <button class="btn btn-primary" onclick="ouvrirModalAjout()">&#43; Nouveau passager</button>
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
        <form method="get" action="${pageContext.request.contextPath}/passagers" style="margin-bottom:20px;">
            <div class="search-bar">
                <div class="search-input-wrap">
                    <span class="search-icon">&#128269;</span>
                    <input type="text" name="recherche" value="<%= recherche %>"
                           placeholder="Rechercher par nom, prenom, telephone, email...">
                </div>
                <button type="submit" class="btn btn-primary">Rechercher</button>
                <% if (!recherche.isEmpty()) { %>
                    <a href="${pageContext.request.contextPath}/passagers" class="btn btn-outline">&#10005; Effacer</a>
                <% } %>
            </div>
        </form>

        <div class="card">
            <div class="card-header">
                <h3>Liste des passagers</h3>
                <span style="font-size:13px; color:var(--text-light);">
                    <%= total %> passager(s)
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
                                <th>Nom complet</th>
                                <th>Telephone</th>
                                <th>Piece d'identite</th>
                                <th>Email</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                        <% if (passagers != null && !passagers.isEmpty()) {
                               for (Passager p : passagers) { %>
                            <tr>
                                <td><strong>#<%= p.getId() %></strong></td>
                                <td>
                                    <div style="display:flex;align-items:center;gap:10px;">
                                        <div style="width:36px;height:36px;background:var(--accent);border-radius:50%;display:flex;align-items:center;justify-content:center;color:#fff;font-weight:700;font-size:14px;flex-shrink:0;">
                                            <%= p.getNom().charAt(0) %>
                                        </div>
                                        <div style="font-weight:600;"><%= p.getNom() %> <%= p.getPrenom() %></div>
                                    </div>
                                </td>
                                <td>&#128222; <%= p.getTelephone() %></td>
                                <td><span style="font-family:'Space Mono',monospace;font-size:12px;"><%= p.getPieceIdentite() != null ? p.getPieceIdentite() : "-" %></span></td>
                                <td><%= p.getEmail() != null && !p.getEmail().isEmpty() ? p.getEmail() : "-" %></td>
                                <td>
                                    <div style="display:flex;gap:6px;">
                                        <button class="btn btn-warning btn-sm"
                                            onclick="ouvrirModalModif(
                                                <%= p.getId() %>,
                                                '<%= p.getNom() %>',
                                                '<%= p.getPrenom() %>',
                                                '<%= p.getTelephone() %>',
                                                '<%= p.getPieceIdentite() != null ? p.getPieceIdentite() : "" %>',
                                                '<%= p.getEmail() != null ? p.getEmail() : "" %>')">
                                            &#9998; Modifier
                                        </button>
                                        <form method="post" action="${pageContext.request.contextPath}/passagers"
                                              onsubmit="return confirm('Supprimer ce passager ?');" style="display:inline;">
                                            <input type="hidden" name="action" value="supprimer">
                                            <input type="hidden" name="id" value="<%= p.getId() %>">
                                            <button type="submit" class="btn btn-danger btn-sm">&#128465; Supprimer</button>
                                        </form>
                                    </div>
                                </td>
                            </tr>
                        <% } } else { %>
                            <tr>
                                <td colspan="6" style="text-align:center;padding:40px;color:var(--text-light);">
                                    Aucun passager enregistre
                                </td>
                            </tr>
                        <% } %>
                        </tbody>
                    </table>
                </div>

                <!-- PAGINATION -->
                <% if (totalPages > 1) { %>
                <div class="pagination" style="padding:16px;">
                    <% if (pageNum > 1) { %>
                        <a href="${pageContext.request.contextPath}/passagers?recherche=<%= recherche %>&page=<%= pageNum-1 %>" class="page-link">&#8592;</a>
                    <% } %>
                    <% for (int i = 1; i <= totalPages; i++) { %>
                        <a href="${pageContext.request.contextPath}/passagers?recherche=<%= recherche %>&page=<%= i %>"
                           class="page-link <%= i == pageNum ? "active" : "" %>"><%= i %></a>
                    <% } %>
                    <% if (pageNum < totalPages) { %>
                        <a href="${pageContext.request.contextPath}/passagers?recherche=<%= recherche %>&page=<%= pageNum+1 %>" class="page-link">&#8594;</a>
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
            <h3>&#43; Nouveau passager</h3>
            <button class="modal-close" onclick="fermerModal('modalAjout')">&#10005;</button>
        </div>
        <form method="post" action="${pageContext.request.contextPath}/passagers">
            <input type="hidden" name="action" value="ajouter">
            <div class="modal-body">
                <div class="form-grid">
                    <div class="form-group">
                        <label>Nom *</label>
                        <input type="text" name="nom" placeholder="Ex: MBARGA" required>
                    </div>
                    <div class="form-group">
                        <label>Prenom *</label>
                        <input type="text" name="prenom" placeholder="Ex: Jean" required>
                    </div>
                    <div class="form-group">
                        <label>Telephone *</label>
                        <input type="text" name="telephone" placeholder="Ex: 699000000" required>
                    </div>
                    <div class="form-group">
                        <label>Piece d'identite</label>
                        <input type="text" name="pieceIdentite" placeholder="Ex: CNI-123456">
                    </div>
                    <div class="form-group full-width">
                        <label>Email</label>
                        <input type="email" name="email" placeholder="Ex: jean@email.com">
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
            <h3>&#9998; Modifier le passager</h3>
            <button class="modal-close" onclick="fermerModal('modalModif')">&#10005;</button>
        </div>
        <form method="post" action="${pageContext.request.contextPath}/passagers">
            <input type="hidden" name="action" value="modifier">
            <input type="hidden" name="id" id="modif-id">
            <div class="modal-body">
                <div class="form-grid">
                    <div class="form-group">
                        <label>Nom *</label>
                        <input type="text" name="nom" id="modif-nom" required>
                    </div>
                    <div class="form-group">
                        <label>Prenom *</label>
                        <input type="text" name="prenom" id="modif-prenom" required>
                    </div>
                    <div class="form-group">
                        <label>Telephone *</label>
                        <input type="text" name="telephone" id="modif-telephone" required>
                    </div>
                    <div class="form-group">
                        <label>Piece d'identite</label>
                        <input type="text" name="pieceIdentite" id="modif-piece">
                    </div>
                    <div class="form-group full-width">
                        <label>Email</label>
                        <input type="email" name="email" id="modif-email">
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
function ouvrirModalModif(id, nom, prenom, telephone, piece, email) {
    document.getElementById('modif-id').value = id;
    document.getElementById('modif-nom').value = nom;
    document.getElementById('modif-prenom').value = prenom;
    document.getElementById('modif-telephone').value = telephone;
    document.getElementById('modif-piece').value = piece;
    document.getElementById('modif-email').value = email;
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
