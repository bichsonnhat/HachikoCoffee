package com.example.hachikocoffee.Domain;

import java.io.Serializable;

public class NotificationDomain implements Serializable {
    private String Title;
    private int NotificationID;
    private String ImageURL;
    private String Description;
    public  NotificationDomain() {}

    public NotificationDomain(String title, int notificationID, String imageURL, String description) {
        Title = title;
        NotificationID = notificationID;
        ImageURL = imageURL;
        Description = description;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public int getNotificationID() {
        return NotificationID;
    }

    public void setNotificationID(int notificationID) {
        NotificationID = notificationID;
    }

    public String getImageURL() {
        return ImageURL;
    }

    public void setImageURL(String imageURL) {
        ImageURL = imageURL;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }
}
