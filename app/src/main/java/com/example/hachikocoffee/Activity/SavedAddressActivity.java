package com.example.hachikocoffee.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hachikocoffee.Adapter.AddressAdapter;
import com.example.hachikocoffee.Domain.AddressDomain;
import com.example.hachikocoffee.Listener.OnAddressChangedListener;
import com.example.hachikocoffee.Management.ManagementUser;
import com.example.hachikocoffee.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import static com.example.hachikocoffee.Activity.CompanyAddressActivity.setCompanyInterfaceInstance;
import static com.example.hachikocoffee.Activity.HomeAddressActivity.setHomeInterfaceInstance;
import static com.example.hachikocoffee.Activity.NewAddressActivity.setInterfaceInstance;;
import static com.example.hachikocoffee.Activity.EditAddressActivity.setEditInterfaceInstance;

public class SavedAddressActivity extends AppCompatActivity implements OnAddressChangedListener {
    private RecyclerView recyclerView;
    private int UserID = 1;

    private TextView tvAddHomeAddress;
    private TextView tvHome;
    private TextView tvDetailHomeAddress;
    private TextView tvUserNameAndTelephoneHome;

    private TextView tvAddCompanyAddress;
    private TextView tvCompany;
    private TextView tvDetailCompanyAddress;
    private TextView tvUserNameAndTelephoneCompany;
    private ImageView deleteButtonHome, deleteButtonCompany;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_address);

        tvAddHomeAddress = findViewById(R.id.addHomeAddress);
        tvHome = findViewById(R.id.tvNameHomeAddress);
        tvDetailHomeAddress = findViewById(R.id.tvDetailHomeAddress);
        tvUserNameAndTelephoneHome = findViewById(R.id.tvUserNameAndTelephoneHome);
        deleteButtonHome = findViewById(R.id.deleteButtonHome);

        tvAddCompanyAddress = findViewById(R.id.addCompanyAddress);
        tvCompany = findViewById(R.id.tvNameCompanyAddress);
        tvDetailCompanyAddress = findViewById(R.id.tvDetailCompanyAddress);
        tvUserNameAndTelephoneCompany = findViewById(R.id.tvUserNameAndTelephoneCompany);
        deleteButtonCompany = findViewById(R.id.deleteButtonCompany);

        tvDetailHomeAddress.setVisibility(View.GONE);
        tvHome.setVisibility(View.GONE);
        tvUserNameAndTelephoneHome.setVisibility(View.GONE);
        deleteButtonHome.setVisibility(View.GONE);

        tvDetailCompanyAddress.setVisibility(View.GONE);
        tvCompany.setVisibility(View.GONE);
        tvUserNameAndTelephoneCompany.setVisibility(View.GONE);
        deleteButtonCompany.setVisibility(View.GONE);

        recyclerView = findViewById(R.id.recycler_view_saved_addresses);
        UserID = ManagementUser.getInstance().getUserId();
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
                DatabaseReference addressRef = FirebaseDatabase.getInstance().getReference().child("ADDRESS");
                addressRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            for (DataSnapshot issue : snapshot.getChildren()){
                                AddressDomain address = issue.getValue(AddressDomain.class);
                                if (address.getUserID() == UserID && address.getTitle().equals("Nhà")){
                                    setEditInterfaceInstance(SavedAddressActivity.this);
                                    Intent intent = new Intent(SavedAddressActivity.this, EditAddressActivity.class);
                                    intent.putExtra("AddressID", address.getAddressID());
                                    startActivity(intent);
                                    return;
                                }
                            }
                            setHomeInterfaceInstance(SavedAddressActivity.this);
                            Intent intent = new Intent(SavedAddressActivity.this, HomeAddressActivity.class);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
//                startHomeForResult.launch(intent);
            }
        });

        Button btnCompanyAddress = findViewById(R.id.btnCompanyAddress);
        btnCompanyAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference addressRef = FirebaseDatabase.getInstance().getReference().child("ADDRESS");
                addressRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            for (DataSnapshot issue : snapshot.getChildren()){
                                AddressDomain address = issue.getValue(AddressDomain.class);
                                if (address.getUserID() == UserID && address.getTitle().equals("Công ty")){
                                    setEditInterfaceInstance(SavedAddressActivity.this);
                                    Intent intent = new Intent(SavedAddressActivity.this, EditAddressActivity.class);
                                    intent.putExtra("AddressID", address.getAddressID());
                                    startActivity(intent);
                                    return;
                                }
                            }
                            setCompanyInterfaceInstance(SavedAddressActivity.this);
                            Intent intent = new Intent(SavedAddressActivity.this, CompanyAddressActivity.class);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
//                startCompanyForResult.launch(intent);;
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
        initHomeAddress();
        initCompanyAddress();
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
                        if (address.getUserID() == UserID && !address.getTitle().equals("Nhà") && !address.getTitle().equals("Công ty")){
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
        initHomeAddress();
        initCompanyAddress();
    }

    private void initHomeAddress() {
        DatabaseReference addressRef = FirebaseDatabase.getInstance().getReference().child("ADDRESS");
        addressRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    boolean isHomeAddressExist = false;
                    for (DataSnapshot issue : snapshot.getChildren()){
                        AddressDomain address = issue.getValue(AddressDomain.class);
                        if (address.getUserID() == UserID && address.getTitle().equals("Nhà")){
                            tvDetailHomeAddress.setText(address.getDescription());
                            tvUserNameAndTelephoneHome.setText(address.getRecipentName() + " " + address.getRecipentPhone());
                            tvDetailHomeAddress.setVisibility(View.VISIBLE);
                            tvHome.setVisibility(View.VISIBLE);
                            tvUserNameAndTelephoneHome.setVisibility(View.VISIBLE);
                            tvAddHomeAddress.setVisibility(View.GONE);
                            deleteButtonHome.setVisibility(View.VISIBLE);
                            isHomeAddressExist = true;
                            break;
                        }
                    }
                    if (!isHomeAddressExist){
                        tvAddHomeAddress.setVisibility(View.VISIBLE);
                        deleteButtonHome.setVisibility(View.GONE);
                        tvDetailHomeAddress.setVisibility(View.GONE);
                        tvHome.setVisibility(View.GONE);
                        tvUserNameAndTelephoneHome.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initCompanyAddress() {
        DatabaseReference addressRef = FirebaseDatabase.getInstance().getReference().child("ADDRESS");
        addressRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    boolean isCompanyAddressExist = false;
                    for (DataSnapshot issue : snapshot.getChildren()){
                        AddressDomain address = issue.getValue(AddressDomain.class);
                        if (address.getUserID() == UserID && address.getTitle().equals("Công ty")){
                            tvDetailCompanyAddress.setText(address.getDescription());
                            tvUserNameAndTelephoneCompany.setText(address.getRecipentName() + " " + address.getRecipentPhone());
                            tvDetailCompanyAddress.setVisibility(View.VISIBLE);
                            tvCompany.setVisibility(View.VISIBLE);
                            tvUserNameAndTelephoneCompany.setVisibility(View.VISIBLE);
                            tvAddCompanyAddress.setVisibility(View.GONE);
                            deleteButtonCompany.setVisibility(View.VISIBLE);
                            isCompanyAddressExist = true;
                            break;
                        }
                    }
                    if (!isCompanyAddressExist) {
                        tvAddCompanyAddress.setVisibility(View.VISIBLE);
                        tvDetailCompanyAddress.setVisibility(View.GONE);
                        tvCompany.setVisibility(View.GONE);
                        tvUserNameAndTelephoneCompany.setVisibility(View.GONE);
                        deleteButtonCompany.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}