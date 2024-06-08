package com.example.hachikocoffee.Activity;

import androidx.annotation.NonNull;
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
import android.widget.TextView;

import com.example.hachikocoffee.Domain.OrderDomain;
import com.example.hachikocoffee.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DashboardActivity extends AppCompatActivity {
    private Button btn_category, btn_shop, btn_product, btn_voucher, btn_notification, btn_user;
    private ImageView imageFeedback, imageExit;
    private ConstraintLayout revenue, confirmOrders, pendingorders, cancelledOrders;
    private TextView confirmOrdersCount, pendingOrdersCount, cancelledOrdersCount, revenueCount;
    private String startDate, endDate;
    DecimalFormatSymbols symbols;
  
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
        confirmOrders = findViewById(R.id.confirmed_orders);
        pendingorders = findViewById(R.id.pending_orders);
        cancelledOrders = findViewById(R.id.canceled_orders);

        confirmOrdersCount = findViewById(R.id.confirmOrdersCount);
        pendingOrdersCount = findViewById(R.id.pendingOrdersCount);
        cancelledOrdersCount = findViewById(R.id.cancelledOrdersCount);
        revenueCount = findViewById(R.id.revenueCount);

        symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        startDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        endDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        initDashboard();


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
        btn_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, CategoryManagementActivity.class);
                startActivity(intent);
            }
        });

        btn_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, UserManagementActivity.class);
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

        cancelledOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, CancelOrdersActivity.class);
                startActivity(intent);
            }
        });

        pendingorders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, PendingOrdersActivity.class);
                startActivity(intent);
            }
        });

        confirmOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, FinishedOrdersActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initDashboard() {
        DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference().child("ORDER");
        orderRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    int totalRevenue = 0;
                    int confirmOrders = 0;
                    int pendingOrders = 0;
                    int cancelledOrders = 0;
                    for (DataSnapshot issue : snapshot.getChildren()){
                        OrderDomain order = issue.getValue(OrderDomain.class);
                            String date = order.getOrderCreatedTime().substring(0, 10);
                            String status = order.getOrderStatus();
                            // check string date is between start date and end date with format dd/MM/yyyy
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                        LocalDate LocalDate = java.time.LocalDate.parse(date, formatter);
                        LocalDate LocalStartDate = LocalDate.parse(startDate, formatter);
                        LocalDate LocalEndDate = LocalDate.parse(endDate, formatter);
                        if (LocalDate.isAfter(LocalStartDate) && LocalDate.isBefore(LocalEndDate)){
                            totalRevenue += (int) order.getCost();
                            if (status.equals("Finished")){
                                confirmOrders += 1;
                            } else if (status.equals("Pending")){
                                pendingOrders += 1;
                            } else if (status.equals("Canceled")){
                                cancelledOrders += 1;
                            }
                        }
                    }
                    String a = new DecimalFormat("#,###", symbols).format(totalRevenue);
                    confirmOrdersCount.setText(""+String.valueOf(confirmOrders));
                    pendingOrdersCount.setText(""+String.valueOf(pendingOrders));
                    cancelledOrdersCount.setText(""+String.valueOf(cancelledOrders));
                    revenueCount.setText(""+String.valueOf(totalRevenue)+"đ");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}