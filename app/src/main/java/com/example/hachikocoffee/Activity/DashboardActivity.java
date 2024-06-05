package com.example.hachikocoffee.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.hachikocoffee.Login;
import com.example.hachikocoffee.R;

public class DashboardActivity extends AppCompatActivity {
    private Button btn_category, btn_shop, btn_product, btn_voucher, btn_notification, btn_user;
    private ImageView imageFeedback;
    private ImageView imageExit;
    private ConstraintLayout revenue, confirmedOrders, pendingOrders, cancelledOrders;
    SharedPreferences perf;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        btn_category = findViewById(R.id.btn_category);
        btn_shop = findViewById(R.id.btn_shop);
        btn_product = findViewById(R.id.btn_product);
        btn_voucher = findViewById(R.id.btn_voucher);
        btn_notification = findViewById(R.id.btn_notification);
        btn_user = findViewById(R.id.btn_user);

        imageFeedback = findViewById(R.id.imageFeedback);
        imageExit = findViewById(R.id.imageExit);

        revenue = findViewById(R.id.revenue);
        confirmedOrders = findViewById(R.id.confirmed_orders);
        pendingOrders = findViewById(R.id.pending_orders);
        cancelledOrders = findViewById(R.id.canceled_orders);

        imageFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, FeedbackManagementActivity.class);
                startActivity(intent);
            }
        });

        imageExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(DashboardActivity.this, R.style.AlertDialog_AppCompat_Custom)
                        .setTitle("Đăng xuất")
                        .setMessage("Bạn có muốn đăng xuất không?")
                        .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                perf = getSharedPreferences("User", Context.MODE_PRIVATE);
                                editor = perf.edit();
                                editor.putBoolean("LoggedIn", false);
                                editor.apply();
                                Intent intent = new Intent(getApplicationContext(), Login.class);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("Không", null)
                        .show();

                Button positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                Button negativeButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                positiveButton.setTextColor(getResources().getColor(R.color.black));
                negativeButton.setTextColor(Color.parseColor("#E47905"));
            }
        });

        btn_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, StoreManagementActivity.class);
                startActivity(intent);
            }
        });

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

        btn_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, NotificationManagementActivity.class);
                startActivity(intent);
            }
        });

        revenue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, RevenueOrdersActivity.class);
                startActivity(intent);
            }
        });

        // Click on cancelledOrders button to move to CancelOrdersActivity
        cancelledOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, CancelOrdersActivity.class);
                startActivity(intent);
            }
        });


    }
}