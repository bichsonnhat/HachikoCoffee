package com.example.hachikocoffee.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.hachikocoffee.Adapter.AddressAdapter;
import com.example.hachikocoffee.Domain.AddressDomain;
import com.example.hachikocoffee.Listener.OnAddressChangedListener;
import com.example.hachikocoffee.Login;
import com.example.hachikocoffee.NotificationDetail;
import com.example.hachikocoffee.R;
import com.example.hachikocoffee.YourVoucher;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import static com.example.hachikocoffee.Activity.NewAddressActivity.setInterfaceInstance;
import static com.example.hachikocoffee.Activity.EditAddressActivity.setEditInterfaceInstance;

public class SavedAddressActivity extends AppCompatActivity implements OnAddressChangedListener {
    private RecyclerView recyclerView;
    private int UserID = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_address);
        recyclerView = findViewById(R.id.recycler_view_saved_addresses);
        SharedPreferences perf = getSharedPreferences("User", Context.MODE_PRIVATE);
        UserID = perf.getInt("UserID", 1);
        Button btnSavedAddressBack = findViewById(R.id.btnSavedAddressBack);
        btnSavedAddressBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        Button btnHomeAddress = findViewById(R.id.btnHomeAddresss);
        btnHomeAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SavedAddressActivity.this, HomeAddressActivity.class);
                startActivity(intent);
            }
        });

        Button btnCompanyAddress = findViewById(R.id.btnCompanyAddress);
        btnCompanyAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SavedAddressActivity.this, CompanyAddressActivity.class);
                startActivity(intent);
            }
        });

        Button btnNewAddress = findViewById(R.id.btnNewAddress);
        btnNewAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setInterfaceInstance(SavedAddressActivity.this);
                Intent intent = new Intent(SavedAddressActivity.this, NewAddressActivity.class);
                startActivity(intent);
            }
        });
        initAddress();
    }

    private void initAddress() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        ArrayList<AddressDomain> addressList = new ArrayList<>();
        DatabaseReference addressRef = FirebaseDatabase.getInstance().getReference().child("ADDRESS");
        addressRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot issue : snapshot.getChildren()){
                        AddressDomain address = issue.getValue(AddressDomain.class);
                        if (address.getUserID() == UserID){
                            addressList.add(address);
                        }
                    }
                    AddressAdapter addressAdapter = new AddressAdapter(addressList);
                    recyclerView.setAdapter(addressAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    public void onAddressChanged() {
        recyclerView.clearFocus();
        initAddress();
    }

}