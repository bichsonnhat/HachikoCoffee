package com.example.hachikocoffee.Domain;

import java.io.Serializable;

public class NotificationDomain implements Serializable {
    private String Title;
    private String Decription;
    private int ImageURL;
    private String Date;

    public NotificationDomain(String title, String desciption, int imageURL, String date) {
        Title = title;
        Decription = desciption;
        ImageURL = imageURL;
        Date = date;
    }

    public String getTitle() { return Title; }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Decription;
    }

    public void setDescription(String desciption) {
        Decription = desciption;
    }

    public int getImageURL() {
        return ImageURL;
    }

    public void setImageURL(int imageURL) {
        ImageURL = imageURL;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

}