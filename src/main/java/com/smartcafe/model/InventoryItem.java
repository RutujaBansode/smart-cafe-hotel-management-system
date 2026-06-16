package com.smartcafe.model;

public class InventoryItem {
    private int id;
    private String itemName;
    private String category;
    private int quantity;
    private int threshold;
    private double unitPrice;

    public InventoryItem() {}

    public InventoryItem(int id, String itemName, String category, int quantity, int threshold, double unitPrice) {
        this.id = id;
        this.itemName = itemName;
        this.category = category;
        this.quantity = quantity;
        this.threshold = threshold;
        this.unitPrice = unitPrice;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public int getThreshold() { return threshold; }
    public void setThreshold(int threshold) { this.threshold = threshold; }

    public double getUnitPrice() { return unitPrice; }
    public void setUnitPrice(double unitPrice) { this.unitPrice = unitPrice; }

    // Helper method to check if stock level is low
    public boolean isLowStock() {
        return this.quantity <= this.threshold;
    }
}
