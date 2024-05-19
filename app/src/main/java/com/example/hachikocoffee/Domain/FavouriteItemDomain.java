package com.example.hachikocoffee.Domain;

import com.google.gson.annotations.SerializedName;

public class FavouriteItemDomain {
    private String ProductID;
    private int UserID;
    public FavouriteItemDomain(){}

    public FavouriteItemDomain(String ProductID, int UserID) {
        this.ProductID = ProductID;
        this.UserID = UserID;
    }

    public String getProductID() {
        return ProductID;
    }

    public void setProductID(String ProductID) {
        this.ProductID = ProductID;
    }


    public int getUserID() {
        return UserID;
    }

    public void setUserID(int UserID) {
        this.UserID = UserID;
    }
}
