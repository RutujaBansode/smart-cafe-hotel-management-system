package com.smartcafe.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import com.smartcafe.model.InventoryItem;
import com.smartcafe.util.DBConnection;

public class InventoryDAO {

    // Retrieve all inventory items
    public List<InventoryItem> getAllInventory() {
        List<InventoryItem> list = new ArrayList<>();
        String sql = "SELECT * FROM `inventory` ORDER BY item_name ASC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new InventoryItem(
                    rs.getInt("id"),
                    rs.getString("item_name"),
                    rs.getString("category"),
                    rs.getInt("quantity"),
                    rs.getInt("threshold"),
                    rs.getDouble("unit_price")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // Get single item by ID
    public InventoryItem getInventoryItemById(int id) {
        InventoryItem item = null;
        String sql = "SELECT * FROM `inventory` WHERE id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    item = new InventoryItem(
                        rs.getInt("id"),
                        rs.getString("item_name"),
                        rs.getString("category"),
                        rs.getInt("quantity"),
                        rs.getInt("threshold"),
                        rs.getDouble("unit_price")
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return item;
    }

    // Add new inventory item
    public boolean addInventoryItem(InventoryItem item) {
        boolean status = false;
        String sql = "INSERT INTO `inventory`(item_name, category, quantity, threshold, unit_price) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, item.getItemName().trim());
            ps.setString(2, item.getCategory().trim());
            ps.setInt(3, item.getQuantity());
            ps.setInt(4, item.getThreshold());
            ps.setDouble(5, item.getUnitPrice());

            int rows = ps.executeUpdate();
            if (rows > 0) status = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return status;
    }

    // Update inventory details
    public boolean updateInventoryItem(InventoryItem item) {
        boolean status = false;
        String sql = "UPDATE `inventory` SET item_name=?, category=?, quantity=?, threshold=?, unit_price=? WHERE id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, item.getItemName().trim());
            ps.setString(2, item.getCategory().trim());
            ps.setInt(3, item.getQuantity());
            ps.setInt(4, item.getThreshold());
            ps.setDouble(5, item.getUnitPrice());
            ps.setInt(6, item.getId());

            int rows = ps.executeUpdate();
            if (rows > 0) status = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return status;
    }

    // Delete item from inventory
    public boolean deleteInventoryItem(int id) {
        boolean status = false;
        String sql = "DELETE FROM `inventory` WHERE id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            if (rows > 0) status = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return status;
    }

    // Get items with stock levels below or equal to threshold
    public List<InventoryItem> getLowStockItems() {
        List<InventoryItem> list = new ArrayList<>();
        String sql = "SELECT * FROM `inventory` WHERE quantity <= threshold ORDER BY quantity ASC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new InventoryItem(
                    rs.getInt("id"),
                    rs.getString("item_name"),
                    rs.getString("category"),
                    rs.getInt("quantity"),
                    rs.getInt("threshold"),
                    rs.getDouble("unit_price")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
