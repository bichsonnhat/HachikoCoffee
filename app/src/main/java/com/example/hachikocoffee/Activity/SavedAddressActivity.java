package com.example.hachikocoffee.Activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.core.view.ViewCompat;

import android.app.Activity;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hachikocoffee.Adapter.AddressAdapter;
import com.example.hachikocoffee.Domain.AddressDomain;
import com.example.hachikocoffee.Fragment.HomeAddressFragment;
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
    private ActivityResultLauncher<Intent> startForResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Intent data = result.getData();
                        String homeAddress = data.getStringExtra("Address");
                        String userNameAndTelephone = data.getStringExtra("NameAndTelephone");

                        // Tạo bundle và đặt dữ liệu vào
                        Bundle bundle = new Bundle();
                        bundle.putString("Address", homeAddress);
                        bundle.putString("NameAndTelephone", userNameAndTelephone);

                        // Tạo Fragment và setArguments(bundle)
                        HomeAddressFragment fragment = new HomeAddressFragment();
                        fragment.setArguments(bundle);

                        // Thực hiện thay thế fragment trong container
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.rl_home_address, fragment);
                        fragmentTransaction.commit();
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_address);

//        RelativeLayout rlHomeAddress = findViewById(R.id.rl_home_address);
        boolean replaceHomeAddress = getIntent().getBooleanExtra("show_home_address", false);
        if (savedInstanceState == null) {
            if (replaceHomeAddress) {
                // Ẩn RelativeLayout
//                rlHomeAddress.setVisibility(View.GONE);

//                Thêm HomeAddressFragment vào FragmentTransaction
                Fragment HomeAddressFragment = new HomeAddressFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.rl_home_address, HomeAddressFragment).commit();
                TextView vhAddressNameAddress = findViewById(R.id.vh_address_nameAddress);
                TextView vhAddressDetailAddress = findViewById(R.id.vh_address_detailAddress);
                TextView vhAddressUserNameAndTelephone = findViewById(R.id.vh_address_userNameAndTelephone);
            }
        }

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
                startForResult.launch(intent);
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