package com.example.hachikocoffee.Domain;

import com.google.gson.annotations.SerializedName;

public class FavouriteItemDomain {
    private String ProductID;

    private int FavoriteProductID;

    private int UserID;
    public FavouriteItemDomain(){}

    public FavouriteItemDomain(String ProductID, int FavoriteProductID, int UserID) {
        this.ProductID = ProductID;
        this.FavoriteProductID = FavoriteProductID;
        this.UserID = UserID;
    }

    public String getProductID() {
        return ProductID;
    }

    public void setProductID(String ProductID) {
        this.ProductID = ProductID;
    }

    public int getFavoriteProductID() {
        return FavoriteProductID;
    }

    public void setFavoriteProductID(int FavoriteProductID) {
        this.FavoriteProductID = FavoriteProductID;
    }

    public int getUserID() {
        return UserID;
    }

    public void setUserID(int UserID) {
        this.UserID = UserID;
    }
}
