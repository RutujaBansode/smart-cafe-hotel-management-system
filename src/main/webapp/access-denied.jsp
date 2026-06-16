<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Access Denied</title>

<link rel="stylesheet" href="css/style.css">

<style>

.access-container{
    display:flex;
    justify-content:center;
    align-items:center;
    min-height:80vh;
}

.access-card{
    background:white;
    padding:40px;
    border-radius:15px;
    text-align:center;
    box-shadow:0 5px 20px rgba(0,0,0,0.15);
    max-width:500px;
    width:100%;
}

.access-card h1{
    color:#ef4444;
    margin-bottom:20px;
}

.access-card p{
    color:#555;
    margin-bottom:25px;
}

.back-btn{
    display:inline-block;
    background:#2563eb;
    color:white;
    padding:12px 25px;
    border-radius:8px;
    text-decoration:none;
    font-weight:bold;
}

.back-btn:hover{
    opacity:0.9;
}

</style>

</head>

<body>

<div class="access-container">

    <div class="access-card">

        <h1>⛔ Access Denied</h1>

        <p>
            You do not have permission to access this page.
        </p>

        <a href="dashboard" class="back-btn">
            Back To Dashboard
        </a>

    </div>

</div>
<footer class="footer">
         Smart Cafe & Hotel Management System
    </footer>


</body>
</html>