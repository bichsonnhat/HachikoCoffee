package com.example.hachikocoffee;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hachikocoffee.Adapter.OrderItemAdapter;
import com.example.hachikocoffee.Domain.OrderItemDomain;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OrderDetail extends AppCompatActivity {
    Button btnback;
    private RecyclerView recyclerView;
    private String OrderID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_detail);
        recyclerView = findViewById(R.id.rcv_list);
        OrderID = getIntent().getStringExtra("OrderID");
        btnback = findViewById(R.id.btnback);
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        initOrderItem();
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
