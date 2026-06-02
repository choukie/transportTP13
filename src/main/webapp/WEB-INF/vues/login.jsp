<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Connexion - TransitPro</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="login-page">
    <div class="login-container">
        <div class="logo">&#128652;</div>
        <h2>Agence de Transport Interurbain</h2>
        <p class="subtitle">Gestion Transport Interurbain</p>
        <h3>Connexion</h3>

        <%
            String erreur = (String) request.getAttribute("erreur");
            if (erreur != null) {
        %>
            <div class="error-message">&#9888; <%= erreur %></div>
        <% } %>

        <form action="${pageContext.request.contextPath}/login" method="post">
            <div class="form-group">
                <label for="login">Login</label>
                <input type="text" id="login" name="login" placeholder="Entrez votre login" required>
            </div>
            <div class="form-group">
                <label for="motDePasse">Mot de passe</label>
                <input type="password" id="motDePasse" name="motDePasse" placeholder="&#8226;&#8226;&#8226;&#8226;&#8226;&#8226;&#8226;&#8226;" required>
            </div>
            <button type="submit" class="btn btn-primary">&#128272; Se connecter</button>
        </form>

        <a href="${pageContext.request.contextPath}/inscription" class="forgot-link" style="margin-top:12px;display:block;text-align:center;font-weight:600;color:var(--accent);">
            &#43; Pas encore de compte ? S'inscrire
        </a>
        <a href="#" class="forgot-link">Mot de passe oublie ?</a>
    </div>
</div>
</body>
</html>
