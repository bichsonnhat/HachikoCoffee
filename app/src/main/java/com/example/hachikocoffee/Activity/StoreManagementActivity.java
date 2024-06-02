package com.example.hachikocoffee.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hachikocoffee.Adapter.StoreManagementAdapter;
import com.example.hachikocoffee.Domain.ShopDomain;
import com.example.hachikocoffee.Listener.Callback;
import com.example.hachikocoffee.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import static com.example.hachikocoffee.Activity.AddStoreActivity.setEditInterfaceInstance;

public class StoreManagementActivity extends AppCompatActivity implements Callback {
    private ImageView storeMgmt_back;
    private ConstraintLayout storeMgmt_add;
    private RecyclerView storeMgmt_rcv;
    private ArrayList<ShopDomain> storeMgmt_storeList;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_management);

        storeMgmt_back = findViewById(R.id.storeMgmt_back);
        storeMgmt_add = findViewById(R.id.storeMgmt_add);
        storeMgmt_rcv = findViewById(R.id.storeMgmt_rcv);

        storeMgmt_back.setOnClickListener(v -> finish());

        storeMgmt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEditInterfaceInstance(StoreManagementActivity.this);
                Intent intent = new Intent(StoreManagementActivity.this, AddStoreActivity.class);
                startActivity(intent);
            }
        });

        initStore();
    }

    private void initStore() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        storeMgmt_rcv.setLayoutManager(linearLayoutManager);
        storeMgmt_storeList = new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("STORE");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        ShopDomain store = dataSnapshot.getValue(ShopDomain.class);
                        storeMgmt_storeList.add(store);
                    }
                }

                StoreManagementAdapter storeManagementAdapter = new StoreManagementAdapter(storeMgmt_storeList);
                storeMgmt_rcv.setAdapter(storeManagementAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //
            }
        });
    }

    @Override
    public void onCallback() {
        storeMgmt_storeList.clear();
        initStore();
    }
}