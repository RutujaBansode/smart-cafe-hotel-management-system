<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.smartcafe.model.User" %>
<%@ page import="com.smartcafe.model.InventoryItem" %>
<%@ page import="com.smartcafe.model.Order" %>
<%@ page import="com.smartcafe.service.ForecastService.Prediction" %>

<%
    // Session Verification
    User loggedUser = (User) session.getAttribute("user");
    if (loggedUser == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    
    String role = loggedUser.getRole();

    // Retrieve data from Servlet attributes
    int totalItems = (Integer) request.getAttribute("totalItems");
    int lowStockCount = (Integer) request.getAttribute("lowStockCount");
    double recentRevenue = (Double) request.getAttribute("recentRevenue");
    Prediction topSeller = (Prediction) request.getAttribute("topSeller");
    List<InventoryItem> lowStockList = (List<InventoryItem>) request.getAttribute("lowStockList");
    List<Order> recentOrders = (List<Order>) request.getAttribute("recentOrders");
    List<Prediction> predictions = (List<Prediction>) request.getAttribute("predictions");
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Smart Cafe & Hotel - Dashboard</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>

    <!-- Header Navigation -->
    <nav class="navbar">
        <div class="nav-brand">
            <span>☕</span> Smart Cafe & Hotel
        </div>
        <ul class="nav-links">

    <li>
        <a href="dashboard" class="nav-item active">
            Dashboard
        </a>
    </li>

    <% if("admin".equalsIgnoreCase(role)){ %>

        <li>
            <a href="inventory" class="nav-item">
                Inventory
            </a>
        </li>

        <li>
            <a href="reports.jsp" class="nav-item">
                Reports
            </a>
        </li>

        <li>
            <a href="manage-staff.jsp" class="nav-item">
                Manage Staff
            </a>
        </li>

    <% } %>

    <% if("staff".equalsIgnoreCase(role)){ %>

        <li>
            <a href="orders" class="nav-item">
                Place Order
            </a>
        </li>

        <li>
            <a href="orders" class="nav-item">
                View Menu
            </a>
        </li>

    <% } %>

</ul>
        <div class="nav-user">
            <span class="user-badge"><%= loggedUser.getName() %> (<%= loggedUser.getRole().toUpperCase() %>)</span>
            <a href="login?logout=true" class="logout-btn">Log Out</a>
        </div>
    </nav>

    <div class="container">
        <!-- Dashboard Greeting -->
        <div class="dashboard-header">
            <h1>Analytics & Operations Dashboard</h1>
            <p>Real-time inventory thresholds, mock transaction logs, and AI next-day sales projections.</p>
        </div>

        <!-- Metric KPI Cards -->
        <div class="card-grid-4">
            <!-- Metric 1: Total Menu Items -->
            <div class="metric-card">
                <span class="label">Total Products</span>
                <span class="value"><%= totalItems %></span>
                <span class="footer-text">Items registered in Cafe</span>
            </div>

            <!-- Metric 2: Critical Alerts -->
            <div class="metric-card <%= lowStockCount > 0 ? "alert-active" : "" %>">
                <span class="label" style="<%= lowStockCount > 0 ? "color: #feb139;" : "" %>">
                    <%= lowStockCount > 0 ? "⚠️ Low Stock Alerts" : "Stock Warnings" %>
                </span>
                <span class="value" style="<%= lowStockCount > 0 ? "color: #feb139;" : "" %>"><%= lowStockCount %></span>
                <span class="footer-text"><%= lowStockCount > 0 ? "Action required to restock" : "All levels above thresholds" %></span>
            </div>

            <!-- Metric 3: Recent Order Sales Sum -->
            <div class="metric-card">
                <span class="label">Recent Revenue</span>
                <span class="value">₹<%= recentRevenue %></span>
                <span class="footer-text">Sum of recent orders</span>
            </div>

            <!-- Metric 4: AI Top Seller Prediction -->
            <div class="metric-card" style="border-color: rgba(0, 242, 254, 0.25);">
                <span class="label" style="color: var(--accent-glow)">🧠 AI Top Seller Prediction</span>
                <span class="value" style="font-size: 20px; margin: 18px 0; color: var(--accent-glow)">
                    <%= topSeller != null ? topSeller.getItemName() : "No Sales Records" %>
                </span>
                <span class="footer-text">Predicted top demand tomorrow</span>
            </div>
        </div>

        <!-- Main Dashboard Split Content -->
        <div class="main-grid">
            
            <!-- Left Side: AI Sales Projections & Stock Alerts -->
            <div class="left-column">
                <% if("admin".equalsIgnoreCase(role)){ %>
                <!-- Section 1: AI Forecast Predictions -->
                <div class="content-block">
                    <div class="block-header">
                        <h2><span>🔮</span> Next-Day Sales Demand Forecast (AI Analysis)</h2>
                        <span class="badge-glow">AI Forecast Engine Active</span>
                    </div>
                    <p style="font-size: 13px; color: var(--text-secondary); margin-bottom: 20px;">
                        Calculated using a hybrid model: 70% weighted moving average (heavier weight on recent sales) and 30% day-of-week historical seasonality patterns.
                    </p>
                    <div class="table-container">
                        <table>
                            <thead>
                                <tr>
                                    <th>Menu Item</th>
                                    <th>Recent Daily Avg</th>
                                    <th>AI Predicted Demand (Units)</th>
                                    <th>Projected Trend</th>
                                    <th>Smart Recommendation</th>
                                </tr>
                            </thead>
                            <tbody>
                                <% if (predictions != null && !predictions.isEmpty()) {
                                    for (Prediction p : predictions) { %>
                                        <tr>
                                            <td style="font-weight: 600;"><%= p.getItemName() %></td>
                                            <td><%= p.getRecentAverage() %> units</td>
                                            <td style="font-size: 15px; font-weight: 700; color: var(--accent-glow);"><%= p.getPredictedQuantity() %></td>
                                            <td>
                                                <% if ("UP".equals(p.getTrend())) { %>
                                                    <span class="badge badge-success">📈 UP</span>
                                                <% } else if ("DOWN".equals(p.getTrend())) { %>
                                                    <span class="badge badge-danger">📉 DOWN</span>
                                                <% } else { %>
                                                    <span class="badge badge-info">➖ STABLE</span>
                                                <% } %>
                                            </td>
                                            <td style="font-size: 13px; color: var(--text-secondary); font-style: italic;">
                                                <%= p.getRecommendation() %>
                                            </td>
                                        </tr>
                                    <% }
                                } else { %>
                                    <tr>
                                        <td colspan="5" style="text-align: center; color: var(--text-muted);">No sales data available to calculate predictions. Please create orders first.</td>
                                    </tr>
                                <% } %>
                            </tbody>
                        </table>
                    </div>
                </div>
                <% } %>
                
                
                <% if("admin".equalsIgnoreCase(role)){ %>

                <!-- Section 2: Critical Stock Levels -->
                <div class="content-block">
                    <div class="block-header">
                        <h2><span>⚠️</span> Critical Stock Replenishment Warnings</h2>
                        <span class="badge badge-danger"><%= lowStockCount %> Alert<%= lowStockCount == 1 ? "" : "s" %></span>
                    </div>
                    <div class="table-container">
                        <table>
                            <thead>
                                <tr>
                                    <th>Item Name</th>
                                    <th>Category</th>
                                    <th>Available Stock</th>
                                    <th>Safety Threshold</th>
                                    <th>Status Level</th>
                                </tr>
                            </thead>
                            <tbody>
                                <% if (lowStockList != null && !lowStockList.isEmpty()) {
                                    for (InventoryItem item : lowStockList) { %>
                                        <tr>
                                            <td style="font-weight: 600;"><%= item.getItemName() %></td>
                                            <td><%= item.getCategory() %></td>
                                            <td style="font-weight: 700; color: var(--danger-glow);"><%= item.getQuantity() %></td>
                                            <td><%= item.getThreshold() %></td>
                                            <td>
                                                <% if (item.getQuantity() == 0) { %>
                                                    <span class="badge badge-danger">OUT OF STOCK</span>
                                                <% } else { %>
                                                    <span class="badge badge-warning">LOW STOCK ALERT</span>
                                                <% } %>
                                            </td>
                                        </tr>
                                    <% }
                                } else { %>
                                    <tr>
                                        <td colspan="5" style="text-align: center; color: var(--success-glow); font-weight: 600; padding: 20px 0;">
                                            🎉 Excellent! All inventory stock levels are safely above safety thresholds.
                                        </td>
                                    </tr>
                                <% } %>
                            </tbody>
                        </table>
                    </div>
                </div>
                    <% } %>

            </div>
            
            <!-- Right Side: Recent Transactions Logs -->
            <div class="right-column">
                
                <div class="content-block">
                    <div class="block-header">
                        <h2><span>📝</span> Recent Customer Bills</h2>
                    </div>
                    
                    <% if (recentOrders != null && !recentOrders.isEmpty()) {
                        for (Order order : recentOrders) { %>
                            <div style="background: rgba(255, 255, 255, 0.02); border: 1px solid var(--border-color); border-radius: 10px; padding: 15px; margin-bottom: 15px; transition: var(--transition-smooth);">
                                <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 8px;">
                                    <span style="font-weight: 600; font-size: 14px; color: white;"><%= order.getCustomerName() %></span>
                                    <span style="font-weight: 700; color: var(--success-glow); font-size: 15px;">₹<%= order.getTotalAmount() %></span>
                                </div>
                                <div style="display: flex; justify-content: space-between; font-size: 12px; color: var(--text-secondary);">
                                    <span>Method: <%= order.getPaymentMethod() %></span>
                                    <span><%= order.getOrderDate().toString().substring(0, 16) %></span>
                                </div>
                            </div>
                        <% }
                    } else { %>
                        <p style="text-align: center; color: var(--text-muted); padding: 20px 0;">No transactions recorded yet.</p>
                    <% } %>
                    
                    <a href="orders" class="btn-primary" style="width: 100%; justify-content: center; margin-top: 10px;">
                        <span>🛒</span> Add Cafe Transaction
                    </a>
                </div>

            </div>

        </div>
    </div>
    <footer class="footer">
         Smart Cafe & Hotel Management System
    </footer>
    

</body>
</html>
