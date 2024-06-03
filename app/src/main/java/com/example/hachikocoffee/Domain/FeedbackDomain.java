package com.example.hachikocoffee.Domain;


import java.io.Serializable;

public class FeedbackDomain implements Serializable {
    private int UserID;
    private int FeedbackID;
    private String Description;

    public FeedbackDomain() {
    }

    public FeedbackDomain(int userID, int feedbackID, String description) {
        UserID = userID;
        FeedbackID = feedbackID;
        Description = description;
    }

    public int getUserID() {
        return UserID;
    }

    public void setUserID(int userID) {
        UserID = userID;
    }

    public int getFeedbackID() {
        return FeedbackID;
    }

    public void setFeedbackID(int feedbackID) {
        FeedbackID = feedbackID;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }
}
