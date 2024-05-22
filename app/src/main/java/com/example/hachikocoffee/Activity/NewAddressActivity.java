package com.example.hachikocoffee.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.hachikocoffee.Domain.AddressDomain;
import com.example.hachikocoffee.Domain.UserDomain;
import com.example.hachikocoffee.Listener.OnAddressChangedListener;
import com.example.hachikocoffee.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.UUID;

public class NewAddressActivity extends AppCompatActivity {
    private int UserID;
    EditText etNewAddressName, etNewAddress, etNewAddressBuilding, etNewAddressGate, etNewAddressNote, etNewAddressReceiverName, etNewAddressReceiverPhone;
    Button btnNewAddressSave;
    private static OnAddressChangedListener onAddressChangedListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences perf = getApplication().getSharedPreferences("User", Context.MODE_PRIVATE);
        UserID = perf.getInt("UserID", 1);
        setContentView(R.layout.activity_new_address);
        etNewAddressName = findViewById(R.id.newAddressName);
        etNewAddress = findViewById(R.id.newAddress);
        etNewAddressBuilding = findViewById(R.id.newAdressBuilding);
        etNewAddressGate = findViewById(R.id.newAddressGate);
        etNewAddressNote = findViewById(R.id.newAddressNote);
        etNewAddressReceiverName = findViewById(R.id.newAddressReceiverName);
        etNewAddressReceiverPhone = findViewById(R.id.newAddressReceiverPhone);
        btnNewAddressSave = findViewById(R.id.btnNewAddressSave);
        btnNewAddressSave.setEnabled(false);
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("USER");
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot issue : snapshot.getChildren()){
                        UserDomain user = issue.getValue(UserDomain.class);
                        if (user.getUserID() == UserID){
                            String fullname = user.getName();
                            String[] name = fullname.split(",");
                            etNewAddressReceiverName.setText(name[0]);
                            etNewAddressReceiverPhone.setText(user.getPhoneNumber());
                            break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        etNewAddressName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etNewAddressName.getText().toString().isEmpty()){
                    etNewAddressName.setError("Vui lòng nhập tên địa chỉ!");
                }
                checkAllFieldsRequire();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etNewAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etNewAddress.getText().toString().isEmpty()){
                    etNewAddress.setError("Vui lòng nhập địa chỉ!");
                }
                checkAllFieldsRequire();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etNewAddressReceiverName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etNewAddressReceiverName.getText().toString().isEmpty()){
                    etNewAddressReceiverName.setError("Vui lòng nhập tên người nhận!");
                }
                checkAllFieldsRequire();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etNewAddressReceiverPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etNewAddressReceiverPhone.getText().toString().isEmpty()){
                    etNewAddressReceiverPhone.setError("Vui lòng nhập số điện thoại người nhận!");
                }
                checkAllFieldsRequire();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Button btnNewAddressBack = findViewById(R.id.btnNewAddressBack);
        btnNewAddressBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnNewAddressSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference addressRef = FirebaseDatabase.getInstance().getReference().child("ADDRESS");
                UUID uuid = UUID.randomUUID();
                String addressID = uuid.toString();
                AddressDomain addressDomain = new AddressDomain(addressID, etNewAddress.getText().toString(), etNewAddress.getText().toString(), etNewAddressGate.getText().toString(), etNewAddressNote.getText().toString(), etNewAddressReceiverName.getText().toString(), etNewAddressReceiverPhone.getText().toString(), etNewAddressName.getText().toString(), UserID);
                // (String addressID, String description, String detail, String gate, String note, String recipentName, String recipentPhone, String title, int userID)
                addressRef.push().setValue(addressDomain);
                finish();
                onAddressChangedListener.onAddressChanged();
            }
        });
    }

    private void checkAllFieldsRequire() {
        if (!etNewAddressName.getText().toString().isEmpty() && !etNewAddress.getText().toString().isEmpty() && !etNewAddressReceiverName.getText().toString().isEmpty() && !etNewAddressReceiverPhone.getText().toString().isEmpty()){
            btnNewAddressSave.setBackground(ContextCompat.getDrawable(this, R.drawable.background_color));
            btnNewAddressSave.setEnabled(true);
        } else {
            btnNewAddressSave.setBackground(ContextCompat.getDrawable(this, R.drawable.rounded_rectangle_darkgrey));
            btnNewAddressSave.setEnabled(false);
        }
    }

    public static void setInterfaceInstance(Context context){
        onAddressChangedListener = (OnAddressChangedListener) context;
    }
}