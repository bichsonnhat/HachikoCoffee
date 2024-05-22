package com.example.hachikocoffee.Domain;

import java.io.Serializable;

public class AddressDomain implements Serializable {
    private String AddressID;
    private String Description;
    private String Detail;
    private String Gate;
    private String Note;
    private String RecipentName;
    private String RecipentPhone;
    private String Title;
    private int UserID;

    public String getAddressID() {
        return AddressID;
    }

    public void setAddressID(String addressID) {
        AddressID = addressID;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getDetail() {
        return Detail;
    }

    public void setDetail(String detail) {
        Detail = detail;
    }

    public String getGate() {
        return Gate;
    }

    public void setGate(String gate) {
        Gate = gate;
    }

    public String getNote() {
        return Note;
    }

    public void setNote(String note) {
        Note = note;
    }

    public String getRecipentName() {
        return RecipentName;
    }

    public void setRecipentName(String recipentName) {
        RecipentName = recipentName;
    }

    public String getRecipentPhone() {
        return RecipentPhone;
    }

    public void setRecipentPhone(String recipentPhone) {
        RecipentPhone = recipentPhone;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public int getUserID() {
        return UserID;
    }

    public void setUserID(int userID) {
        UserID = userID;
    }

    public AddressDomain() {}

    public AddressDomain(String addressID, String description, String detail, String gate, String note, String recipentName, String recipentPhone, String title, int userID) {
        AddressID = addressID;
        Description = description;
        Detail = detail;
        Gate = gate;
        Note = note;
        RecipentName = recipentName;
        RecipentPhone = recipentPhone;
        Title = title;
        UserID = userID;
    }
}
