package com.example.hachikocoffee;

import android.app.Application;

public class GetVerificationCode extends Application {
    public static String getVerificationCode() {
        return verificationCode;
    }

    public static void setVerificationCode(String verificationCode) {
        GetVerificationCode.verificationCode = verificationCode;
    }

    public static String verificationCode;
}
