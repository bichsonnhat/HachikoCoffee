package com.example.hachikocoffee.Domain;

import java.io.Serializable;

public class AdvertisementDomain implements Serializable {
    private int AdID;
    private String Description;

    public AdvertisementDomain() {
    }
    public AdvertisementDomain(int adID, String description) {
        AdID = adID;
        Description = description;
    }

    public int getAdID() {
        return AdID;
    }

    public void setAdID(int adID) {
        AdID = adID;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }
}
