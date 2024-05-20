package com.example.hachikocoffee.Domain;

import java.io.Serializable;

public class NotificationDomain implements Serializable {
    private String Title;
    private String Description;
    private String ImageURL;
    private int NotificationID;
    private String Date;

    public NotificationDomain() {}

    public NotificationDomain(String title, String desciption, String imageURL, String date) {
        Title = title;
        Description = desciption;
        ImageURL = imageURL;
        Date = date;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getImageURL() {
        return ImageURL;
    }

    public void setImageURL(String imageURL) {
        ImageURL = imageURL;
    }

    public int getNotificationID() {
        return NotificationID;
    }

    public void setNotificationID(int notificationID) {
        NotificationID = notificationID;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }
}
