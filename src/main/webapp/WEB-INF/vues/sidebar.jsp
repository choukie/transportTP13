<%
    String currentPage = (String) request.getAttribute("currentPage");
    if (currentPage == null) currentPage = "";
    String sidebarLogin = (String) session.getAttribute("login");
    String sidebarRole  = (String) session.getAttribute("role");
    String initiale = (sidebarLogin != null && !sidebarLogin.isEmpty()) 
                      ? String.valueOf(sidebarLogin.charAt(0)).toUpperCase() : "U";
%>
<aside class="sidebar">
    <div class="sidebar-brand">
        <div class="brand-icon" style="font-size:22px;">&#128652;</div>
        <h2>TransitPro</h2>
        <p>Gestion Transport Interurbain</p>
    </div>

    <nav class="sidebar-nav">
        <div class="nav-section">Principal</div>
        <a href="${pageContext.request.contextPath}/dashboard"
           class="nav-item <%= "dashboard".equals(currentPage) ? "active" : "" %>">
            <span class="nav-icon">&#128202;</span> Tableau de bord
        </a>

        <div class="nav-section">Gestion</div>
        <% if ("ADMIN".equals(sidebarRole)) { %>
        <a href="${pageContext.request.contextPath}/vehicules"
           class="nav-item <%= "vehicules".equals(currentPage) ? "active" : "" %>">
            <span class="nav-icon">&#128652;</span> Vehicules
        </a>
        <% } %>
        <a href="${pageContext.request.contextPath}/voyages"
           class="nav-item <%= "voyages".equals(currentPage) ? "active" : "" %>">
            <span class="nav-icon">&#128506;</span> Voyages
        </a>
        <a href="${pageContext.request.contextPath}/passagers"
           class="nav-item <%= "passagers".equals(currentPage) ? "active" : "" %>">
            <span class="nav-icon">&#128101;</span> Passagers
        </a>
        <a href="${pageContext.request.contextPath}/reservations"
           class="nav-item <%= "reservations".equals(currentPage) ? "active" : "" %>">
            <span class="nav-icon">&#127915;</span> Reservations
        </a>

        <div class="nav-section">Rapports</div>
        <% if ("ADMIN".equals(sidebarRole)) { %>
        <a href="${pageContext.request.contextPath}/statistiques"
           class="nav-item <%= "statistiques".equals(currentPage) ? "active" : "" %>">
            <span class="nav-icon">&#128200;</span> Statistiques
        </a>
        <% } %>
        <a href="${pageContext.request.contextPath}/rapports"
           class="nav-item <%= "rapports".equals(currentPage) ? "active" : "" %>">
            <span class="nav-icon">&#128196;</span> Rapports PDF
        </a>
    </nav>

    <div class="sidebar-footer">
        <div class="user-info">
            <div class="user-avatar"><%= initiale %></div>
            <div>
                <div class="user-name"><%= sidebarLogin != null ? sidebarLogin : "Utilisateur" %></div>
                <div class="user-role"><%= sidebarRole != null ? sidebarRole : "" %></div>
            </div>
        </div>
        <a href="${pageContext.request.contextPath}/login" class="logout-btn">Deconnexion</a>
    </div>
</aside>
