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

    List<InventoryItem> inventoryList =
        (List<InventoryItem>) request.getAttribute("inventoryList");
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Smart Cafe & Hotel-Inventory Management</title>
    <link rel="stylesheet" href="css/style.css">
</head>

<body>

<!-- NAVBAR -->
<nav class="navbar">
    <div class="nav-brand">☕ Smart Cafe</div>

    <ul class="nav-links">
        <li><a href="dashboard" class="nav-item">Dashboard</a></li>
        <li><a href="inventory" class="nav-item active">Inventory</a></li>
        <li><a href="orders" class="nav-item">Orders</a></li>
    </ul>

    <div class="nav-user">
        <span class="user-badge">
            <%= loggedUser.getName() %>
        </span>
        <a href="login?logout=true" class="logout-btn">Logout</a>
    </div>
</nav>

<!-- MAIN CONTAINER -->
<div class="container">

    <!-- PAGE HEADER -->
    <div class="dashboard-header">
        <h1>Inventory Management</h1>
        <p>Manage all stock items efficiently</p>
    </div>

    <!-- 🔥 REQUIRED STRUCTURE ADDED HERE -->
    <div class="content-block">

        <div class="block-header">
            <h2>📦 Inventory List</h2>
        </div>

        <div class="table-container">

            <table>

                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Name</th>
                        <th>Category</th>
                        <th>Qty</th>
                        <th>Price</th>
                        <th>Status</th>
                        <th>Actions</th>
                    </tr>
                </thead>

                <tbody>

                <%
                    if (inventoryList != null && !inventoryList.isEmpty()) {
                        for (InventoryItem item : inventoryList) {
                %>

                    <tr>
                        <td><%= item.getId() %></td>
                        <td><%= item.getItemName() %></td>
                        <td><%= item.getCategory() %></td>
                        <td><%= item.getQuantity() %></td>
                        <td>₹<%= item.getUnitPrice() %></td>

                        <td>
                            <% if(item.getQuantity() > item.getThreshold()) { %>
                                <span class="badge badge-success">OK</span>
                            <% } else { %>
                                <span class="badge badge-warning">LOW</span>
                            <% } %>
                        </td>

                        <td>
                            <a href="inventory?action=edit&id=<%= item.getId() %>" class="btn-primary">Edit</a>
                            <a href="inventory?action=delete&id=<%= item.getId() %>" class="btn-secondary"
                               onclick="return confirm('Delete item?');">
                                Delete
                            </a>
                        </td>
                    </tr>

                <%
                        }
                    } else {
                %>

                    <tr>
                        <td colspan="7" style="text-align:center; padding:20px;">
                            No inventory items found
                        </td>
                    </tr>

                <%
                    }
                %>

                </tbody>

            </table>

        </div>

    </div>

</div>
<footer class="footer">
         Smart Cafe & Hotel Management System
    </footer>


</body>
</html>