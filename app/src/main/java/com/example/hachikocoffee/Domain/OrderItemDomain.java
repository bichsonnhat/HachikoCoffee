package com.example.hachikocoffee.Domain;

import java.io.Serializable;

public class OrderItemDomain implements Serializable {
    private String OrderItemID;
    private String OrderID;
    private String ProductID;
    private int Quantity;
    private String Size;
    private String Topping;
    private String Note;
    private double TotalOrderItemPrice;

    public OrderItemDomain() {
    }

    public OrderItemDomain(String ordderItemID, String orderID, String productID, int quantity, String size, String topping, String note, double totalOrderItemPrice) {
        OrderItemID = ordderItemID;
        OrderID = orderID;
        ProductID = productID;
        Quantity = quantity;
        Size = size;
        Topping = topping;
        Note = note;
        TotalOrderItemPrice = totalOrderItemPrice;
    }

    public String getOrdderItemID() {
        return OrderItemID;
    }

    public void setOrdderItemID(String ordderItemID) {
        OrderItemID = ordderItemID;
    }

    public String getOrderID() {
        return OrderID;
    }

    public void setOrderID(String orderID) {
        OrderID = orderID;
    }

    public String getProductID() {
        return ProductID;
    }

    public void setProductID(String productID) {
        ProductID = productID;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    public String getSize() {
        return Size;
    }

    public void setSize(String size) {
        Size = size;
    }

    public String getTopping() {
        return Topping;
    }

    public void setTopping(String topping) {
        Topping = topping;
    }

    public String getNote() {
        return Note;
    }

    public void setNote(String note) {
        Note = note;
    }

    public double getTotalOrderItemPrice() {
        return TotalOrderItemPrice;
    }

    public void setTotalOrderItemPrice(int totalOrderItemPrice) {
        TotalOrderItemPrice = totalOrderItemPrice;
    }
}
