<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Inscription - TransitPro</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="login-page">
    <div class="login-container" style="width:480px;">
        <div class="logo">&#128101;</div>
        <h2>TransitPro</h2>
        <h3>Creer un compte</h3>

        <% String erreur = (String) request.getAttribute("erreur");
           String succes = (String) request.getAttribute("succes");
           if (erreur != null) { %>
            <div class="error-message">&#9888; <%= erreur %></div>
        <% } %>
        <% if (succes != null) { %>
            <div style="background:rgba(0,184,148,0.1);border-left:4px solid #00b894;color:#00856f;padding:12px 16px;border-radius:8px;font-size:14px;margin-bottom:20px;">
                &#10003; <%= succes %>
                <br><a href="${pageContext.request.contextPath}/login" style="color:#00b894;font-weight:700;">Se connecter maintenant</a>
            </div>
        <% } %>

        <form action="${pageContext.request.contextPath}/inscription" method="post">
            <div class="form-grid" style="grid-template-columns:1fr 1fr;gap:14px;">
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
                    <label>Email</label>
                    <input type="email" name="email" placeholder="Ex: jean@email.com">
                </div>
                <div class="form-group" style="grid-column:1/-1;">
                    <label>Login *</label>
                    <input type="text" name="login" placeholder="Choisissez un login unique" required>
                </div>
                <div class="form-group">
                    <label>Mot de passe *</label>
                    <input type="password" name="motDePasse" placeholder="Min. 6 caracteres" required>
                </div>
                <div class="form-group">
                    <label>Confirmation *</label>
                    <input type="password" name="confirmation" placeholder="Repetez le mot de passe" required>
                </div>
            </div>
            <button type="submit" class="btn btn-primary" style="width:100%;justify-content:center;padding:13px;margin-top:16px;">
                &#10003; Creer mon compte
            </button>
        </form>

        <a href="${pageContext.request.contextPath}/login" class="forgot-link" style="margin-top:16px;display:block;text-align:center;">
            Deja un compte ? Se connecter
        </a>
    </div>
</div>
</body>
</html>
