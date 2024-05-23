package com.example.hachikocoffee.Domain;

import java.io.Serializable;

public class OrderDomain implements Serializable {
    private String OrderID;
    private int UserID;
    private String Delivered;
    private String OrderAddress;
    private String OrderTime;
    private String OrderMethod;
    private int Cost;
    private int VoucherID;
    private String RecipentName;
    private String RecipentPhone;
    private int StoreID;
    private String OrderStatus;

    private String OrderCreatedTime;

    public String getOrderCreatedTime() {
        return OrderCreatedTime;
    }

    public void setOrderCreatedTime(String orderCreatedTime) {
        OrderCreatedTime = orderCreatedTime;
    }

    public OrderDomain() {
    }

    public OrderDomain(String orderID, int userID, String delivered, String orderAddress, String orderTime, String orderMethod, int cost, int voucherID, String recipentName, String recipentPhone, int storeID, String orderStatus, String orderCreatedTime) {
        OrderID = orderID;
        UserID = userID;
        Delivered = delivered;
        OrderAddress = orderAddress;
        OrderTime = orderTime;
        OrderMethod = orderMethod;
        Cost = cost;
        VoucherID = voucherID;
        RecipentName = recipentName;
        RecipentPhone = recipentPhone;
        StoreID = storeID;
        OrderStatus = orderStatus;
        OrderCreatedTime = orderCreatedTime;
    }

    public String getOrderID() {
        return OrderID;
    }

    public void setOrderID(String orderID) {
        OrderID = orderID;
    }

    public int getUserID() {
        return UserID;
    }

    public void setUserID(int userID) {
        UserID = userID;
    }

    public String getDelivered() {
        return Delivered;
    }

    public void setDelivered(String delivered) {
        Delivered = delivered;
    }

    public String getOrderAddress() {
        return OrderAddress;
    }

    public void setOrderAddress(String orderAddress) {
        OrderAddress = orderAddress;
    }

    public String getOrderTime() {
        return OrderTime;
    }

    public void setOrderTime(String orderTime) {
        OrderTime = orderTime;
    }

    public String getOrderMethod() {
        return OrderMethod;
    }

    public void setOrderMethod(String orderMethod) {
        OrderMethod = orderMethod;
    }

    public int getCost() {
        return Cost;
    }

    public void setCost(int cost) {
        Cost = cost;
    }

    public int getVoucherID() {
        return VoucherID;
    }

    public void setVoucherID(int voucherID) {
        VoucherID = voucherID;
    }

    public String getRecipentName() {
        return RecipentName;
    }

    public void setRecipentName(String recipentName) {
        RecipentName = recipentName;
    }

    public String getRecipentPhone() {
        return RecipentPhone;
    }

    public void setRecipentPhone(String recipentPhone) {
        RecipentPhone = recipentPhone;
    }

    public int getStoreID() {
        return StoreID;
    }

    public void setStoreID(int storeID) {
        StoreID = storeID;
    }

    public String getOrderStatus() {
        return OrderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        OrderStatus = orderStatus;
    }
}
