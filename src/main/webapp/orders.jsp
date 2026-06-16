<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.smartcafe.model.User" %>
<%@ page import="com.smartcafe.model.InventoryItem" %>

<%
    User loggedUser = (User) session.getAttribute("user");
    if (loggedUser == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    List<InventoryItem> menuList = (List<InventoryItem>) request.getAttribute("menuList");
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Smart Cafe & Hotel - Order</title>
    <link rel="stylesheet" href="css/style.css">

    <style>
        .order-layout{
            display:grid;
            grid-template-columns:2fr 1fr;
            gap:25px;
        }

        .menu-item-card{
            display:flex;
            justify-content:space-between;
            padding:15px;
            margin-bottom:10px;
            border:1px solid rgba(255,255,255,0.1);
            border-radius:10px;
        }

        .qty-controls{
            display:flex;
            gap:6px;
            align-items:center;
        }

        .qty-btn{
            width:30px;
            height:30px;
            border:none;
            cursor:pointer;
        }

        .qty-input{
            width:50px;
            text-align:center;
        }
    </style>
</head>

<body>

<nav class="navbar">
    <div>☕ Smart Cafe</div>

    <div>
        <span><%= loggedUser.getName() %> (<%= loggedUser.getRole() %>)</span>
    </div>
</nav>

<div class="container">

<h1>Place Order</h1>

<form action="orders" method="post">

    <!-- IMPORTANT -->
    <input type="hidden" id="finalTotal" name="totalAmount" value="0">

    <div class="order-layout">

        <!-- MENU -->
        <div>

            <% if (menuList != null) {
                for (InventoryItem item : menuList) { %>

                <div class="menu-item-card">

                    <div>
                        <h3><%= item.getItemName() %></h3>
                        <small>₹<%= item.getUnitPrice() %></small>
                    </div>

                    <div class="qty-controls">

                        <input type="hidden" name="itemIds" value="<%= item.getId() %>">

                        <button type="button"
                                onclick="changeQty(<%= item.getId() %>, -1, <%= item.getQuantity() %>)">-</button>

                        <input type="number"
       id="qty-<%= item.getId() %>"
       name="quantity"
       class="qty-input"
       value="0"
       min="0"
       max="<%= item.getQuantity() %>"
       data-price="<%= item.getUnitPrice() %>"
       onchange="updateTotal()">
       <input type="hidden" name="itemId" value="<%= item.getId() %>">
<input type="hidden" name="itemName" value="<%= item.getItemName() %>">
<input type="hidden" name="price" value="<%= item.getUnitPrice() %>">

                        <button type="button"
                                onclick="changeQty(<%= item.getId() %>, 1, <%= item.getQuantity() %>)">+</button>

                    </div>

                </div>

            <% } } %>

        </div>

        <!-- BILL -->
        <div>

            <h2>Billing</h2>

            <label>Customer</label>
            <input type="text"
       name="customerName"
       placeholder="Customer Name or Table No.">

            <label>Payment</label>
            <select name="paymentMethod">
                <option>Cash</option>
                <option>Card</option>
                <option>UPI</option>
            </select>

            <hr>

            <h3>Total</h3>

            <div id="grand-total" style="font-size:28px;color:green;">
                ₹0.00
            </div>

            <button type="submit" style="width:100%;margin-top:10px;">
                Confirm Order
            </button>

        </div>

    </div>

</form>

</div>

<script>
console.log("ORDER JS LOADED");

function changeQty(id, change, max) {

    const input = document.getElementById("qty-" + id);
    if (!input) return;

    let val = parseInt(input.value || "0");
    val = val + change;

    if (val < 0) val = 0;
    if (val > max) val = max;

    input.value = val;

    updateTotal();
}

function updateTotal() {

    let total = 0;

    const inputs = document.querySelectorAll(".qty-input");

    console.log("FOUND INPUTS:", inputs.length);

    inputs.forEach(i => {

        let qty = Number(i.value || 0);
        let price = Number(i.dataset.price || 0);

        total += qty * price;
    });

    document.getElementById("grand-total").innerText =
        "₹ " + total.toFixed(2);
}

window.onload = function () {
    updateTotal();
};
</script>

</body>
<footer class="footer">
         Smart Cafe & Hotel Management System
    </footer>

</html>