package com.example.hachikocoffee.Domain;

import android.content.ClipData;

import com.google.firebase.database.PropertyName;

import java.io.Serializable;
import java.util.ArrayList;

public class ItemsDomain implements Serializable{
    @PropertyName("Title")
    private String title;
    @PropertyName("Price")
    private double price;
    @PropertyName("ImageURL")
    private String picUrl;
    @PropertyName("Description")
    private String Description;
    @PropertyName("CategoryID")
    private int CategoryID;

    @PropertyName("ProductID")
    private String itemID;

    public ItemsDomain(){

    };

    public ItemsDomain(String title, double price, String picUrl, String description, int categoryID, String itemID) {
        this.title = title;
        this.price = price;
        this.picUrl = picUrl;
        this.Description = description;
        this.CategoryID = categoryID;
        this.itemID = itemID;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public int getCategoryID() {
        return CategoryID;
    }

    public void setCategoryID(int categoryID) {
        CategoryID = categoryID;
    }

    public String getProductID() {
        return itemID;
    }

    public void setProductID(String itemID) {
        this.itemID = itemID;
    }

    public String getTitle() {
        return title;
    }

    public double getPrice() {
        return price;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImageURL() {
        return picUrl;
    }

    public void setImageURL(String picUrl) {
        this.picUrl = picUrl;
    }
}
