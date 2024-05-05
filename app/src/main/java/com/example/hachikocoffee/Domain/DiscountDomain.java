package com.example.hachikocoffee.Domain;

public class DiscountDomain {
    //private int resourceId;
    //private String content;
    //private String expired;

    private int VoucherID;
    private String Description;
    private String ExpiryDate;
    private String ImageURL;
    private String Title;
    private int ValueInteger;
    private double ValueDouble;
    private String Type;

    public DiscountDomain() {}

    public DiscountDomain(int voucherID, String description, String expiryDate, String imageURL, String title, int valueInteger, double valueDouble, String type) {
        VoucherID = voucherID;
        Description = description;
        ExpiryDate = expiryDate;
        ImageURL = imageURL;
        Title = title;
        ValueInteger = valueInteger;
        ValueDouble = valueDouble;
        Type = type;
    }

    public int getVoucherID() {
        return VoucherID;
    }

    public void setVoucherID(int voucherID) {
        VoucherID = voucherID;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getExpiryDate() {
        return ExpiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        ExpiryDate = expiryDate;
    }

    public String getImageURL() {
        return ImageURL;
    }

    public void setImageURL(String imageURL) {
        ImageURL = imageURL;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public int getValueInteger() {
        return ValueInteger;
    }

    public void setValueInteger(int valueInteger) {
        ValueInteger = valueInteger;
    }

    public double getValueDouble() {
        return ValueDouble;
    }

    public void setValueDouble(double valueDouble) {
        ValueDouble = valueDouble;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }
}
