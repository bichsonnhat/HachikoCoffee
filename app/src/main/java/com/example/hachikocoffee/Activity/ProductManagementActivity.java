package com.example.hachikocoffee.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.hachikocoffee.Adapter.EditProductAdapter;
import com.example.hachikocoffee.Adapter.EditVoucherAdapter;
import com.example.hachikocoffee.Domain.ItemsDomain;
import com.example.hachikocoffee.Listener.Callback;
import com.example.hachikocoffee.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import static com.example.hachikocoffee.Activity.AddProductActivity.setEditInterfaceInstance;

public class ProductManagementActivity extends AppCompatActivity implements Callback {
    private ConstraintLayout constraintLayoutStart;
    private RecyclerView recyclerViewProduct;
    private ImageView btnBack;
    private ArrayList<ItemsDomain> itemsDomainArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_management);
        constraintLayoutStart = findViewById(R.id.constraintLayoutStart);
        recyclerViewProduct = findViewById(R.id.recyclerViewProduct);
        btnBack = findViewById(R.id.back_button);
        constraintLayoutStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEditInterfaceInstance(ProductManagementActivity.this);
                Intent intent = new Intent(ProductManagementActivity.this, AddProductActivity.class);
                startActivity(intent);
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        initProduct();
    }

    private void initProduct() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerViewProduct.setLayoutManager(linearLayoutManager);
        itemsDomainArrayList = new ArrayList<>();
        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference().child("PRODUCTS");
        productRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot issue : snapshot.getChildren()){
                        ItemsDomain itemsDomain = issue.getValue(ItemsDomain.class);
                        itemsDomainArrayList.add(itemsDomain);
                    }
                }
                EditProductAdapter editProductAdapter = new EditProductAdapter(itemsDomainArrayList);
                recyclerViewProduct.setAdapter(editProductAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onCallback() {
        itemsDomainArrayList.clear();
        initProduct();
    }
}