package com.example.hachikocoffee.Domain;

import java.io.Serializable;

public class UserVoucherDomain implements Serializable {
    private int voucherID;
    private int userID;
    private int userVoucherID;
    private int isUse;
    public UserVoucherDomain() {
    }

    public UserVoucherDomain(int voucherID, int userID, int userVoucherID, int isUse) {
        this.voucherID = voucherID;
        this.userID = userID;
        this.userVoucherID = userVoucherID;
        this.isUse = isUse;
    }

    public int getVoucherID() {
        return voucherID;
    }

    public void setVoucherID(int voucherID) {
        this.voucherID = voucherID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getUserVoucherID() {
        return userVoucherID;
    }

    public void setUserVoucherID(int userVoucherID) {
        this.userVoucherID = userVoucherID;
    }

    public int getIsUse() {
        return isUse;
    }

    public void setIsUse(int isUse) {
        this.isUse = isUse;
    }
}
