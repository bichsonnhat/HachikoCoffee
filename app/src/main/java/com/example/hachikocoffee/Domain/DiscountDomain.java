package com.example.hachikocoffee.Domain;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class DiscountDomain implements Serializable {
    private int VoucherID;
    private String Description;
    private String ExpiryDate;
    private String ImageURL;
    private String Title;
    private int ValueInteger;
    private double ValueDouble;
    private int FreeShipping;
    private int MinOrderCapacity;
    private int MinOrderPrice;
    private String Type;

    public DiscountDomain() {}

    public DiscountDomain(int voucherID, String description, String expiryDate, String imageURL, String title, int valueInteger, double valueDouble, int freeShipping, int minOrderCapacity, int minOrderPrice, String type) {
        VoucherID = voucherID;
        Description = description;
        ExpiryDate = expiryDate;
        ImageURL = imageURL;
        Title = title;
        ValueInteger = valueInteger;
        ValueDouble = valueDouble;
        FreeShipping = freeShipping;
        MinOrderCapacity = minOrderCapacity;
        MinOrderPrice = minOrderPrice;
        Type = type;
    }

    public int getVoucherID() {
        return VoucherID;
    }

    public void setVoucherID(int voucherID) {
        VoucherID = voucherID;
    }

    public String getDescription() {
        if (Description != null) {
            int firstIndex = Description.indexOf("-");
            int lastIndex = Description.lastIndexOf("-");
            if (firstIndex != lastIndex) {
                String firstPart = Description.substring(0, firstIndex + 1);
                String secondPart = Description.substring(firstIndex + 1).replace("-", "\n-");
                return firstPart + secondPart;
            }
        }
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getExpiryDate() {
        /*
        SimpleDateFormat originalFormat = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat targetFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date;
        try {
            date = originalFormat.parse(ExpiryDate);
            String formattedDate = targetFormat.format(date);
            return formattedDate;
        } catch (ParseException e) {
            e.printStackTrace();
        }*/
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

    public int getFreeShipping() {
        return FreeShipping;
    }

    public void setFreeShipping(int freeShipping) {
        FreeShipping = freeShipping;
    }

    public int getMinOrderCapacity() {
        return MinOrderCapacity;
    }

    public void setMinOrderCapacity(int minOrderCapacity) {
        MinOrderCapacity = minOrderCapacity;
    }

    public int getMinOrderPrice() {
        return MinOrderPrice;
    }

    public void setMinOrderPrice(int minOrderPrice) {
        MinOrderPrice = minOrderPrice;
    }


    public boolean isAboutToExpire() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate expiryDate = LocalDate.parse(ExpiryDate, formatter);
        LocalDate currentDate = LocalDate.now();
        long days = ChronoUnit.DAYS.between(currentDate, expiryDate);
        return days < 1 && expiryDate.isAfter(currentDate);
    }
}