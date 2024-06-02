package com.example.hachikocoffee.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;

import com.example.hachikocoffee.Adapter.OrderAdapter;
import com.example.hachikocoffee.Domain.OrderDomain;
import com.example.hachikocoffee.OrderDetail;
import com.example.hachikocoffee.R;
import com.example.hachikocoffee.databinding.ActivityEditCategoryBinding;
import com.example.hachikocoffee.databinding.ActivityPendingOrdersBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PendingOrdersActivity extends AppCompatActivity {

    ActivityPendingOrdersBinding binding;
    ArrayList<OrderDomain> processingOrderList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPendingOrdersBinding.inflate(getLayoutInflater());
        LayoutInflater inflater = getLayoutInflater();
        setContentView(binding.getRoot());

        initPendingOrdersList();
    }

    private void initPendingOrdersList() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        binding.recyclerViewPendingOrders.setLayoutManager(linearLayoutManager);

        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("ORDER");

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        OrderDomain order = issue.getValue(OrderDomain.class);

                        if (order != null && "Pending".equals(order.getOrderStatus())){
                            processingOrderList.add(order);
                        }
                    }
                    displayProcessingOrderList(processingOrderList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Failed to read value.", error.toException());
            }
        });
    }

    private void displayProcessingOrderList(ArrayList<OrderDomain> processingOrderList) {
        if (!processingOrderList.isEmpty()) {
            OrderAdapter orderAdapter = new OrderAdapter(processingOrderList, this::onClickToOrderDetailFunc, PendingOrdersActivity.this);
            binding.recyclerViewPendingOrders.setAdapter(orderAdapter);
        }
    }

    private void onClickToOrderDetailFunc(OrderDomain order) {
        Intent intent = new Intent(PendingOrdersActivity.this, OrderDetail.class);
        startActivity(intent);
    }
}