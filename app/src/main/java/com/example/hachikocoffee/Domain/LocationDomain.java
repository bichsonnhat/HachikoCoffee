package com.example.hachikocoffee.Domain;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class LocationDomain implements Serializable {
    private int UserID;
    private double LocationX;
    private double LocationY;
    private String Address;

    public LocationDomain() {}
    public LocationDomain(int userID, double locationX, double locationY, String address) {
        UserID = userID;
        LocationX = locationX;
        LocationY = locationY;
        Address = address;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public int getUserID() {
        return UserID;
    }

    public void setUserID(int userID) {
        UserID = userID;
    }

    public double getLocationX() {
        return LocationX;
    }

    public void setLocationX(double locationX) {
        LocationX = locationX;
    }

    public double getLocationY() {
        return LocationY;
    }

    public void setLocationY(double locationY) {
        LocationY = locationY;
    }
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("UserID", UserID);
        result.put("LocationX", LocationX);
        result.put("LocationY", LocationY);
        return result;
    }
}
