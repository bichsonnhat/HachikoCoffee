package com.example.hachikocoffee.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.hachikocoffee.R;

public class DashboardActivity extends AppCompatActivity {
    private Button btn_product;
    private Button btn_voucher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        btn_voucher = findViewById(R.id.btn_voucher);
        btn_product = findViewById(R.id.btn_product);

        btn_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, ProductManagementActivity.class);
                startActivity(intent);
            }
        });

        btn_voucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, VoucherManagementActivity.class);
                startActivity(intent);
            }
        });
    }
}