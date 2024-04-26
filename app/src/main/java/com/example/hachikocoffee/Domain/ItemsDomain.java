package com.example.hachikocoffee.Domain;

import android.content.ClipData;

import java.io.Serializable;
import java.util.ArrayList;

public class ItemsDomain implements Serializable{
    private String title;
    private double price;
    private String picUrl;

    public ItemsDomain(){};
    public ItemsDomain(String title, double price, String picUrl) {
        this.title = title;
        this.price = price;
        this.picUrl = picUrl;
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

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }
}
