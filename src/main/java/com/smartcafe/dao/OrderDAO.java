package com.smartcafe.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.smartcafe.model.Order;
import com.smartcafe.model.OrderItem;
import com.smartcafe.util.DBConnection;

public class OrderDAO {

    // Save a complete order with its items in a single Database Transaction
    public boolean saveOrder(Order order) {
        if (order == null || order.getItems().isEmpty()) return false;

        boolean success = false;
        String insertOrderSql = "INSERT INTO `orders`(customer_name, order_date, total_amount, payment_method) VALUES (?, ?, ?, ?)";
        String insertItemSql = "INSERT INTO `order_items`(order_id, item_id, quantity, price) VALUES (?, ?, ?, ?)";
        String updateStockSql = "UPDATE `inventory` SET quantity = quantity - ? WHERE id = ?";

        Connection con = null;
        try {
            con = DBConnection.getConnection();
            if (con == null) return false;

            // Start SQL transaction
            con.setAutoCommit(false);

            int orderId = -1;
            // 1. Insert order metadata
            try (PreparedStatement psOrder = con.prepareStatement(insertOrderSql, Statement.RETURN_GENERATED_KEYS)) {
                psOrder.setString(1, order.getCustomerName());
                psOrder.setTimestamp(2, order.getOrderDate() != null ? order.getOrderDate() : new Timestamp(System.currentTimeMillis()));
                psOrder.setDouble(3, order.getTotalAmount());
                psOrder.setString(4, order.getPaymentMethod());

                psOrder.executeUpdate();

                // Retrieve generated order ID
                try (ResultSet rsKeys = psOrder.getGeneratedKeys()) {
                    if (rsKeys.next()) {
                        orderId = rsKeys.getInt(1);
                    }
                }
            }

            if (orderId == -1) {
                con.rollback();
                return false;
            }

            // 2. Insert order items & 3. Update inventory levels
            try (PreparedStatement psItem = con.prepareStatement(insertItemSql);
                 PreparedStatement psStock = con.prepareStatement(updateStockSql)) {

                for (OrderItem item : order.getItems()) {
                    // Insert order item row
                    psItem.setInt(1, orderId);
                    psItem.setInt(2, item.getItemId());
                    psItem.setInt(3, item.getQuantity());
                    psItem.setDouble(4, item.getPrice());
                    psItem.addBatch();

                    // Decrement inventory stock
                    psStock.setInt(1, item.getQuantity());
                    psStock.setInt(2, item.getItemId());
                    psStock.addBatch();
                }

                psItem.executeBatch();
                psStock.executeBatch();
            }

            // Commit transaction
            con.commit();
            success = true;

        } catch (Exception e) {
            e.printStackTrace();
            if (con != null) {
                try {
                    con.rollback(); // Rollback if error occurs
                } catch (Exception rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
        } finally {
            if (con != null) {
                try {
                    con.setAutoCommit(true);
                    con.close();
                } catch (Exception closeEx) {
                    closeEx.printStackTrace();
                }
            }
        }
        return success;
    }

    // Get recently placed orders
    public List<Order> getRecentOrders(int limit) {
        List<Order> list = new ArrayList<>();
        String sql = "SELECT * FROM `orders` ORDER BY order_date DESC LIMIT ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Order(
                        rs.getInt("id"),
                        rs.getString("customer_name"),
                        rs.getTimestamp("order_date"),
                        rs.getDouble("total_amount"),
                        rs.getString("payment_method")
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // Retrieve historical daily sales quantity of an item for the past N days.
    // Returns Map: Key = Days Ago (0 = today, 1 = yesterday, etc.), Value = Quantity Sold
    public Map<Integer, Integer> getItemSalesHistory(int itemId, int daysLimit) {
        Map<Integer, Integer> salesHistory = new HashMap<>();
        
        // Pre-fill history map with 0s
        for (int i = 0; i <= daysLimit; i++) {
            salesHistory.put(i, 0);
        }

        String sql = "SELECT DATEDIFF(CURDATE(), DATE(o.order_date)) as days_ago, SUM(oi.quantity) as total_qty " +
                     "FROM order_items oi " +
                     "JOIN orders o ON oi.order_id = o.id " +
                     "WHERE oi.item_id = ? AND o.order_date >= DATE_SUB(CURDATE(), INTERVAL ? DAY) " +
                     "GROUP BY DATEDIFF(CURDATE(), DATE(o.order_date))";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, itemId);
            ps.setInt(2, daysLimit);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int daysAgo = rs.getInt("days_ago");
                    int qty = rs.getInt("total_qty");
                    if (daysAgo >= 0 && daysAgo <= daysLimit) {
                        salesHistory.put(daysAgo, qty);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return salesHistory;
    }
}
