package com.example.hachikocoffee;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hachikocoffee.Activity.LoginOTPActivity;
import com.example.hachikocoffee.Activity.MainActivity;
import com.example.hachikocoffee.Activity.SplashActivity;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class Login extends AppCompatActivity {

    FirebaseAuth mAuth;
    EditText phone;
    Button btnGenOTP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences perf = getSharedPreferences("User", MODE_PRIVATE);
        if (perf.getBoolean("LoggedIn", false)){
            Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
            startActivity(intent);
            return;
        }
        setContentView(R.layout.login);
        phone = findViewById(R.id.phone);
        btnGenOTP = findViewById(R.id.btngenerateOTP);
        mAuth = FirebaseAuth.getInstance();
        btnGenOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (phone.getText().toString().trim().isEmpty()) {
                    Toast.makeText(Login.this, "Please enter phone number again.", Toast.LENGTH_SHORT).show();
                    return;
                }
                String number = phone.getText().toString();
                Intent intent = new Intent(getApplicationContext(), LoginOTPActivity.class);
                intent.putExtra("mobile", phone.getText().toString());
                startActivity(intent);
//                sendVerificationCode(number);
            }
        });
    }

    private void sendVerificationCode(String phoneNumber) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+84" + phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // (optional) Activity for callback binding
                        // If no activity is passed, reCAPTCHA verification can not be used.
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {

        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            String errorMessage = e.getMessage();
            if (errorMessage != null) {
                Toast.makeText(Login.this, errorMessage, Toast.LENGTH_LONG).show();
                Log.d("error", "" + errorMessage);
            } else {
                Toast.makeText(Login.this, e.toString(), Toast.LENGTH_SHORT).show();
                Log.d("error", "" + e.toString());
            }
        }

        @Override
        public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            Intent intent = new Intent(getApplicationContext(), LoginOTPActivity.class);
            intent.putExtra("mobile", phone.getText().toString());
            intent.putExtra("verificationId", verificationId);
            startActivity(intent);
        }
    };
}