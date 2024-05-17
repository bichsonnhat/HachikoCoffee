package com.example.hachikocoffee.Domain;

import java.io.Serializable;

public class LocationDomain implements Serializable {
    private int UserID;
    private double LocationX;
    private double LocationY;

    public LocationDomain() {}
    public LocationDomain(int userID, double locationX, double locationY) {
        UserID = userID;
        LocationX = locationX;
        LocationY = locationY;
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
}
