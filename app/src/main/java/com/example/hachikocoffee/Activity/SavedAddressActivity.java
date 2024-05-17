package com.example.hachikocoffee.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.hachikocoffee.Login;
import com.example.hachikocoffee.NotificationDetail;
import com.example.hachikocoffee.R;
import com.example.hachikocoffee.YourVoucher;

public class SavedAddressActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_address);

        Button btnSavedAddressBack = findViewById(R.id.btnSavedAddressBack);
        btnSavedAddressBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        Button btnHomeAddress = findViewById(R.id.btnHomeAddresss);
        btnHomeAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SavedAddressActivity.this, HomeAddressActivity.class);
                startActivity(intent);
            }
        });

        Button btnCompanyAddress = findViewById(R.id.btnCompanyAddress);
        btnCompanyAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SavedAddressActivity.this, CompanyAddressActivity.class);
                startActivity(intent);
            }
        });

        Button btnNewAddress = findViewById(R.id.btnNewAddress);
        btnNewAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SavedAddressActivity.this, NewAddressActivity.class);
                startActivity(intent);
            }
        });
    }
}