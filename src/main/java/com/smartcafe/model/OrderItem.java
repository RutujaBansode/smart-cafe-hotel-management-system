package com.smartcafe.model;

public class OrderItem {
    private int id;
    private int orderId;
    private int itemId;
    private String itemName; // Helper field for UI display
    private int quantity;
    private double price;

    public OrderItem() {}

    public OrderItem(int id, int orderId, int itemId, String itemName, int quantity, double price) {
        this.id = id;
        this.orderId = orderId;
        this.itemId = itemId;
        this.itemName = itemName;
        this.quantity = quantity;
        this.price = price;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }

    public int getItemId() { return itemId; }
    public void setItemId(int itemId) { this.itemId = itemId; }

    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
}
