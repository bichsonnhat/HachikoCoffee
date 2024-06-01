package com.example.hachikocoffee.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hachikocoffee.R;

public class VoucherManagementActivity extends AppCompatActivity {
    private ImageView btnBack;
    private ConstraintLayout btnAddVoucher;
    private RecyclerView recyclerViewVoucher;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voucher_management);
        btnBack = findViewById(R.id.btnBackVoucherManagement);
        btnAddVoucher = findViewById(R.id.btnAddVoucher);
        recyclerViewVoucher = findViewById(R.id.recyclerViewVoucher);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnAddVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 Intent intent = new Intent(VoucherManagementActivity.this, AddVoucherActivity.class);
                 startActivity(intent);
            }
        });
    }
}
