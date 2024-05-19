package com.example.hachikocoffee.Domain;

import java.util.List;
import java.util.Objects;

public class CartItem {
    private String productId;
    private String productName;
    private int quantity;
    private String size;
    private List<String> toppings;
    private int totalCost;
    private double cost;
    public CartItem(){}
    
    public CartItem(String productId, String productName, int quantity, String size, List<String> toppings, int totalCost, double cost) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.size = size;
        this.toppings = toppings;
        this.totalCost = totalCost;
        this.cost = cost;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public List<String> getToppings() {
        return toppings;
    }

    public void setToppings(List<String> toppings) {
        this.toppings = toppings;
    }

    public int getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(int totalCost) {
        this.totalCost = totalCost;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        CartItem other = (CartItem) obj;
        return this.productId.equals(other.productId)
                && this.size.equals(other.size)
                && this.quantity == other.quantity
                && this.totalCost == other.totalCost
                && Objects.equals(this.toppings, other.toppings);
    }
}
