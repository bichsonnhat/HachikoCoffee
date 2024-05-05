package com.example.hachikocoffee.Domain;

import java.io.Serializable;

public class ShopDomain implements Serializable {
    private String Address;
    private String Coordinate;
    private String ImageURL;
    private String Name;
    private int StoreID;

    //private String distance;
    //private String detailaddress; // cái địa chỉ cụ thể hơn hiển thị trong bottomsheet khi ấn vào shop

    public ShopDomain() {}

    public ShopDomain(String address, String coordinate, String imageURL, String name, int storeID) {
        Address = address;
        Coordinate = coordinate;
        ImageURL = imageURL;
        Name = name;
        StoreID = storeID;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getCoordinate() {
        return Coordinate;
    }

    public void setCoordinate(String coordinate) {
        Coordinate = coordinate;
    }

    public String getImageURL() {
        return ImageURL;
    }

    public void setImageURL(String imageURL) {
        ImageURL = imageURL;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getStoreID() {
        return StoreID;
    }

    public void setStoreID(int storeID) {
        StoreID = storeID;
    }
}