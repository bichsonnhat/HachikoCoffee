package com.example.hachikocoffee.Domain;

public class ShopDomain {
    private int resourceId; // link to shop img
    private String address; // address of shop
    private String distance; // distance between user and shop

    public ShopDomain(int resourceId, String address, String distance) {
        this.resourceId = resourceId;
        this.address = address;
        this.distance = distance;
    }

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }
}
