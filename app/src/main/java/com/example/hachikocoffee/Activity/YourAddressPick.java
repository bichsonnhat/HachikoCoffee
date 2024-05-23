package com.example.hachikocoffee.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hachikocoffee.Adapter.AddressAdapter;
import com.example.hachikocoffee.Adapter.AddressAdapter1;
import com.example.hachikocoffee.Domain.AddressDomain;
import com.example.hachikocoffee.Listener.OnPickListener;
import com.example.hachikocoffee.Management.ManagementUser;
import com.example.hachikocoffee.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import static com.example.hachikocoffee.Adapter.AddressAdapter1.setInterfaceCallback;
public class YourAddressPick extends AppCompatActivity implements OnPickListener {
    RecyclerView recyclerView;
    private int UserID;
    Button btnBack, btnAddressConfirm;
    private int selectedItem = RecyclerView.NO_POSITION;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_address_pick);
        UserID = ManagementUser.getInstance().getUserId();
        btnBack = findViewById(R.id.btnback);
        recyclerView = findViewById(R.id.rcv_address_pick);
        btnAddressConfirm = findViewById(R.id.btnAddressConfirm);
        setInterfaceCallback(this);
        initAddress();
        btnAddressConfirm.setEnabled(false);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnAddressConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initAddress() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        DatabaseReference addressRef = FirebaseDatabase.getInstance().getReference().child("ADDRESS");
        addressRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    ArrayList<AddressDomain> addressList = new ArrayList<>();
                    for (DataSnapshot issue : snapshot.getChildren()){
                        AddressDomain address = issue.getValue(AddressDomain.class);
                        if (address.getUserID() == UserID){
                            addressList.add(address);
                        }
                    }
                    AddressAdapter1 addressAdapter = new AddressAdapter1(addressList);
                    recyclerView.setAdapter(addressAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onPick() {
        btnAddressConfirm.setBackground(ContextCompat.getDrawable(this, R.drawable.background_color));
        btnAddressConfirm.setEnabled(true);
    }
}
