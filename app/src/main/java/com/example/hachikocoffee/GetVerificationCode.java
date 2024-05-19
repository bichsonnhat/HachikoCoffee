package com.example.hachikocoffee;

import android.app.Application;

import com.example.hachikocoffee.Management.ManagementCart;
import com.example.hachikocoffee.Management.ManagementUser;

public class GetVerificationCode extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        ManagementCart.getInstance().loadCartFromFirebase("1");
        ManagementUser.getInstance().loadFromFirebase(1);
    }
    public static String getVerificationCode() {
        return verificationCode;
    }

    public static void setVerificationCode(String verificationCode) {
        GetVerificationCode.verificationCode = verificationCode;
    }

    public static String verificationCode;
}
