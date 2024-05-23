package com.example.hachikocoffee;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hachikocoffee.Adapter.OrderItemAdapter;
import com.example.hachikocoffee.Domain.OrderDomain;
import com.example.hachikocoffee.Domain.OrderItemDomain;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;

public class OrderDetail extends AppCompatActivity {
    Button btnback;
    private RecyclerView recyclerView;
    private String OrderID;
    private TextView TotalOrderCost, ShippingCost, DiscountCost;

    DecimalFormatSymbols symbols;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_detail);
        recyclerView = findViewById(R.id.rcv_list);
        OrderID = getIntent().getStringExtra("OrderID");
        btnback = findViewById(R.id.btnback);
        TotalOrderCost = findViewById(R.id.TotalOrderCost);
        ShippingCost = findViewById(R.id.ShippingCost);
        DiscountCost = findViewById(R.id.DiscountCost);
        symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');


        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        initOrderItem();
        initTotalOrderCost();
    }

    @SuppressLint("SetTextI18n")
    private void initTotalOrderCost() {
        DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference().child("ORDER");
        orderRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot issue : snapshot.getChildren()){
                        OrderDomain order = issue.getValue(OrderDomain.class);
                        if (order.getOrderID().equals(OrderID)) {
                            String a = new DecimalFormat("#,###", symbols).format((long) order.getCost());
                            TotalOrderCost.setText(a + "đ");
                            break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void initOrderItem() {
        ArrayList<OrderItemDomain> list = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        DatabaseReference orderItemRef = FirebaseDatabase.getInstance().getReference().child("ORDERITEM");
        orderItemRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot issue : snapshot.getChildren()){
                        OrderItemDomain orderItem = issue.getValue(OrderItemDomain.class);
                        if (orderItem.getOrderID().equals(OrderID)) {
                            list.add(orderItem);
                        }
                    }
                    OrderItemAdapter orderItemAdapter = new OrderItemAdapter(list);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    recyclerView.setAdapter(orderItemAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
