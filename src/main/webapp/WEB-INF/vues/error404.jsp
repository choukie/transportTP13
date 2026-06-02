<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Page introuvable - TransitPro</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div style="min-height:100vh;display:flex;align-items:center;justify-content:center;background:#f0f2f8;">
    <div style="text-align:center;padding:60px;">
        <div style="font-size:100px;">&#128652;</div>
        <h1 style="font-size:80px;font-weight:900;color:var(--accent);margin:0;">404</h1>
        <h2 style="font-size:24px;color:var(--primary);margin:10px 0;">Page introuvable</h2>
        <p style="color:var(--text-light);font-size:15px;margin-bottom:30px;">
            La page que vous cherchez n'existe pas ou a ete deplacee.
        </p>
        <a href="${pageContext.request.contextPath}/dashboard" class="btn btn-primary">
            &#127968; Retour au tableau de bord
        </a>
    </div>
</div>
</body>
</html>
