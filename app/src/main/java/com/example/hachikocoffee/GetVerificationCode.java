package com.example.hachikocoffee;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.hachikocoffee.Management.ManagementCart;
import com.example.hachikocoffee.Management.ManagementUser;

public class GetVerificationCode extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        SharedPreferences perf = getSharedPreferences("User", Context.MODE_PRIVATE);
        int UserID = perf.getInt("UserID", -1);

        if (UserID != -1) {
            ManagementUser.getInstance().loadFromFirebase(UserID);
            ManagementCart.getInstance().loadCartFromFirebase(String.valueOf(UserID));
        }
    }
    public static String getVerificationCode() {
        return verificationCode;
    }

    public static void setVerificationCode(String verificationCode) {
        GetVerificationCode.verificationCode = verificationCode;
    }

    public static String verificationCode;
}
