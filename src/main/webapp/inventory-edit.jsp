<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.smartcafe.model.User" %>
<%@ page import="com.smartcafe.model.InventoryItem" %>

<%
    // Session Verification
    User loggedUser = (User) session.getAttribute("user");
    if (loggedUser == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    // Retrieve item details to edit
    InventoryItem item = (InventoryItem) request.getAttribute("item");
    if (item == null) {
        response.sendRedirect("inventory?action=list");
        return;
    }
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Smart Cafe & Hotel - Edit Inventory</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>

    <!-- Header Navigation -->
    <nav class="navbar">
        <div class="nav-brand">
            <span>☕</span> Smart Cafe & Hotel
        </div>
        <ul class="nav-links">
            <li><a href="dashboard" class="nav-item">Dashboard</a></li>
            <li><a href="inventory" class="nav-item active">Inventory Stock</a></li>
            <li><a href="orders" class="nav-item">New Order</a></li>
        </ul>
        <div class="nav-user">
            <span class="user-badge"><%= loggedUser.getName() %> (<%= loggedUser.getRole().toUpperCase() %>)</span>
            <a href="login?logout=true" class="logout-btn">Log Out</a>
        </div>
    </nav>

    <div class="container" style="max-width: 600px;">
        <div class="dashboard-header">
            <h1>Edit Inventory Item</h1>
            <p>Update properties for item: <strong><%= item.getItemName() %></strong></p>
        </div>

        <div class="content-block">
            <form action="inventory" method="post">
                <input type="hidden" name="action" value="update">
                <input type="hidden" name="id" value="<%= item.getId() %>">
                
                <div class="form-group">
                    <label for="itemName">Product / Item Name</label>
                    <input type="text" id="itemName" name="itemName" class="input-glow" value="<%= item.getItemName() %>" required>
                </div>

                <div class="form-group">
                    <label for="category">Category Classification</label>
                    <select id="category" name="category" class="input-glow" style="background-color: #0f172a; cursor: pointer;">
                        <option value="Beverages" <%= "Beverages".equals(item.getCategory()) ? "selected" : "" %>>Beverages</option>
                        <option value="Bakery" <%= "Bakery".equals(item.getCategory()) ? "selected" : "" %>>Bakery</option>
                        <option value="Snacks" <%= "Snacks".equals(item.getCategory()) ? "selected" : "" %>>Snacks</option>
                        <option value="Main Course" <%= "Main Course".equals(item.getCategory()) ? "selected" : "" %>>Main Course</option>
                        <option value="Rooms / Amenities" <%= "Rooms / Amenities".equals(item.getCategory()) ? "selected" : "" %>>Rooms / Amenities</option>
                    </select>
                </div>

                <div class="form-group">
                    <label for="quantity">Stock Quantity Available</label>
                    <input type="number" id="quantity" name="quantity" class="input-glow" value="<%= item.getQuantity() %>" min="0" required>
                </div>

                <div class="form-group">
                    <label for="threshold">Safety Alert Threshold</label>
                    <input type="number" id="threshold" name="threshold" class="input-glow" value="<%= item.getThreshold() %>" min="0" required>
                </div>

                <div class="form-group">
                    <label for="unitPrice">Unit Selling Price ($)</label>
                    <input type="number" id="unitPrice" name="unitPrice" class="input-glow" value="<%= item.getUnitPrice() %>" min="0" step="0.01" required>
                </div>

                <div style="display: flex; gap: 15px; margin-top: 20px;">
                    <button type="submit" class="btn-primary" style="flex: 1; justify-content: center;">
                        Save Changes
                    </button>
                    <a href="inventory?action=list" class="btn-secondary" style="flex: 1; text-align: center; display: inline-block;">
                        Cancel
                    </a>
                </div>
            </form>
        </div>
    </div>
    <footer class="footer">
       Smart Cafe & Hotel Management System
    </footer>
    

</body>
</html>
