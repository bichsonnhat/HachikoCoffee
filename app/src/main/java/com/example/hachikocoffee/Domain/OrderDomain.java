package com.example.hachikocoffee.Domain;

import java.io.Serializable;

public class OrderDomain implements Serializable {
    private String OrderID;
    private int UserID;
    private int Delivered;
    private String OrderAddress;
    private String OrderTime;
    private String OrderMethod;
    private double Cost;
    private int VoucherID;
    private String RecipentName;
    private String RecipentPhone;
    private int StoreID;
    private String OrderStatus;

    private String OrderCreatedTime;
    private double discountMoney;
    private double shippingFee;

    public String getOrderCreatedTime() {
        return OrderCreatedTime;
    }

    public void setOrderCreatedTime(String orderCreatedTime) {
        OrderCreatedTime = orderCreatedTime;
    }

    public OrderDomain() {
    }

    public OrderDomain(String orderID, int userID, int delivered, String orderAddress,
                       String orderTime, String orderMethod, double cost, int voucherID,
                       String recipentName, String recipentPhone, int storeID, String orderStatus,
                       String orderCreatedTime, double discountMoney, double shippingFee) {
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
        this.discountMoney = discountMoney;
        this.shippingFee = shippingFee;
    }

    public double getDiscountMoney() {
        return discountMoney;
    }

    public void setDiscountMoney(double discountMoney) {
        this.discountMoney = discountMoney;
    }

    public double getShippingFee() {
        return shippingFee;
    }

    public void setShippingFee(double shippingFee) {
        this.shippingFee = shippingFee;
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

    public int getDelivered() {
        return Delivered;
    }

    public void setDelivered(int delivered) {
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

    public double getCost() {
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
