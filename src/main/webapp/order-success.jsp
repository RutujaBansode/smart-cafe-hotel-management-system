<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>

<%
String amount = request.getParameter("amount");
String customer = request.getParameter("customer");
String payment = request.getParameter("payment");
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Smart Cafe & Hotel-Order Confirmed</title>

<link rel="stylesheet" href="style.css">

<style>

.success-card{
    max-width:700px;
    margin:80px auto;
    background:white;
    padding:40px;
    border-radius:20px;
    box-shadow:0 10px 30px rgba(0,0,0,.15);
    text-align:center;
}

.success-icon{
    font-size:80px;
    margin-bottom:20px;
}

.success-title{
    color:#10b981;
    font-size:38px;
    margin-bottom:25px;
}

.bill-info{
    font-size:22px;
    line-height:2;
}

.place-order-btn{
    display:inline-block;
    margin-top:30px;
    background:#2563eb;
    color:white;
    padding:15px 30px;
    border-radius:10px;
    text-decoration:none;
    font-weight:bold;
}

.place-order-btn:hover{
    opacity:.9;
}

</style>

</head>

<body>

<div class="success-card">

<div class="success-icon">
✅
</div>

<h1 class="success-title">
Order Confirmed
</h1>

<div class="bill-info">

<p>
<b>Customer / Table:</b>
<%= customer %>
</p>

<p>
<b>Payment:</b>
<%= payment %>
</p>

<p>
<b>Total Amount:</b>
₹<%= amount %>
</p>

</div>

<h3>
Thank You For Visiting Smart Cafe ☕
</h3>

<a href="orders" class="place-order-btn">
Place New Order
</a>

</div>
<footer class="footer">
         Smart Cafe & Hotel Management System
    </footer>

</body>
</html>