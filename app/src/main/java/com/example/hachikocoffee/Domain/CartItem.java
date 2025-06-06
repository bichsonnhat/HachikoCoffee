package com.example.hachikocoffee.Domain;

import com.example.hachikocoffee.Management.ManagementCart;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class CartItem implements Serializable {
    private String productId;
    private String productName;
    private String cartItemId;
    private static int count = 0;
    private int quantity;
    private String size;
    private List<String> toppings;
    private int totalCost;
    private double cost;
    private String note;
    public CartItem(){}
    
    public CartItem(String productId, String productName, int quantity, String size, List<String> toppings, int totalCost, double cost, String note) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.size = size;
        this.toppings = toppings;
        this.totalCost = totalCost;
        this.cost = cost;
        this.cartItemId = String.valueOf(ManagementCart.getInstance().getNoId());
        this.note = note;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getCartItemId() {
        return cartItemId;
    }
    public void setCartItemId(String cartItemId) { this.cartItemId = cartItemId;}

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
