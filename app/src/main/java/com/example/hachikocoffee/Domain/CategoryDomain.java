package com.example.hachikocoffee.Domain;

import com.google.firebase.database.PropertyName;

public class CategoryDomain {
    @PropertyName("Title")
    private String title;
    @PropertyName("CategoryID")
    private int id;
    @PropertyName("ImageURL")
    private String picUrl;

    public CategoryDomain() {}

    public CategoryDomain(int id, String picUrl, String title) {
        this.title = title;
        this.id = id;
        this.picUrl = picUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCategoryID() {
        return id;
    }

    public void setCategoryID(int id) {
        this.id = id;
    }

    public String getImageURL() {
        return picUrl;
    }

    public void setImageURL(String picUrl) {
        this.picUrl = picUrl;
    }
}

