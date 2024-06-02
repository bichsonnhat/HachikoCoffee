package com.example.hachikocoffee.Domain;

import java.io.Serializable;

public class ShopDomain implements Serializable {
    private int StoreID;
    private String Address;
    private String Name;
    private String ImageURL;
    private String Coordinate; // POINT(106.641594 10.800494) (longitude, latitude)

    public ShopDomain() {}

    public ShopDomain(int storeID, String address, String name, String imageURL, String coordinate) {
        StoreID = storeID;
        Address = address;
        Name = name;
        ImageURL = imageURL;
        Coordinate = coordinate;
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

    // Get longitude from Coordinate POINT(106.641594 10.800494)
    public double getLongitude() {
        String[] parts = Coordinate.split(" ");
        return Double.parseDouble(parts[0].substring(6)); // 106.641594
    }

    // Get latitude from Coordinate
    public double getLatitude() {
        String[] parts = Coordinate.split(" ");
        return Double.parseDouble(parts[1].substring(0, parts[1].length() - 1)); // 10.800494
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