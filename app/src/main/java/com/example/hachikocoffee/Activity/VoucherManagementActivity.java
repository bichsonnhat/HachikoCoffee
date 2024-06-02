package com.example.hachikocoffee.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hachikocoffee.Adapter.EditVoucherAdapter;
import com.example.hachikocoffee.Domain.DiscountDomain;
import com.example.hachikocoffee.Listener.Callback;
import com.example.hachikocoffee.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import static com.example.hachikocoffee.Activity.AddVoucherActivity.setEditInterfaceInstance;

public class VoucherManagementActivity extends AppCompatActivity implements Callback {
    private ImageView btnBack;
    private ConstraintLayout btnAddVoucher;
    private RecyclerView recyclerViewVoucher;
    private ArrayList<DiscountDomain> voucherList;
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
                 setEditInterfaceInstance(VoucherManagementActivity.this);
                 Intent intent = new Intent(VoucherManagementActivity.this, AddVoucherActivity.class);
                 startActivity(intent);
            }
        });

        initVoucher();
    }

    private void initVoucher() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerViewVoucher.setLayoutManager(linearLayoutManager);
        voucherList = new ArrayList<>();
        DatabaseReference voucherRef = FirebaseDatabase.getInstance().getReference().child("VOUCHER");
        voucherRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        DiscountDomain voucher = dataSnapshot.getValue(DiscountDomain.class);
                        voucherList.add(voucher);
                    }
                    EditVoucherAdapter editVoucherAdapter = new EditVoucherAdapter(voucherList);
                    recyclerViewVoucher.setAdapter(editVoucherAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onCallback() {
        voucherList.clear();
        initVoucher();
    }
}
