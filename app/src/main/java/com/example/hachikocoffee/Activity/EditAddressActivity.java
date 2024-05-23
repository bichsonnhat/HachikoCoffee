package com.example.hachikocoffee.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.hachikocoffee.Domain.AddressDomain;
import com.example.hachikocoffee.InfoAccountLoginActivity;
import com.example.hachikocoffee.Listener.OnAddressChangedListener;
import com.example.hachikocoffee.Login;
import com.example.hachikocoffee.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class EditAddressActivity extends AppCompatActivity {
    private EditText editAddressName;
    private EditText editAddress;
    private EditText editAddressBuilding;
    private EditText editAddressGate;
    private EditText editAddressNote;
    private EditText editAddressReceiverName;
    private EditText editAddressReceiverPhone;
    private RelativeLayout btnDeleteAddress;
    private Button btnEditAddress;

    private Button btnHomeAddressBack;
    private static OnAddressChangedListener onAddressChangedListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_address);
        editAddressName = findViewById(R.id.editAddressName);
        editAddress = findViewById(R.id.editAddress);
        editAddressBuilding = findViewById(R.id.editAddressBuilding);
        editAddressGate = findViewById(R.id.editAddressGate);
        editAddressNote = findViewById(R.id.editAddressNote);
        editAddressReceiverName = findViewById(R.id.editAddressReceiverName);
        editAddressReceiverPhone = findViewById(R.id.editAddressReceiverPhone);
        btnDeleteAddress = findViewById(R.id.btnDeleteAddress);
        btnEditAddress = findViewById(R.id.btnEditAddress);
        btnHomeAddressBack = findViewById(R.id.btnHomeAddressBack);
        String addressID = getIntent().getStringExtra("AddressID");
        Toast.makeText(this, ""+addressID, Toast.LENGTH_SHORT).show();
        initEditedAddress(addressID);
        btnEditAddress.setBackground(ContextCompat.getDrawable(this, R.drawable.rounded_rectangle_darkgrey));
        btnEditAddress.setEnabled(false);

        btnHomeAddressBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        editAddressName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (editAddressName.getText().toString().isEmpty()){
                    editAddressName.setError("Vui lòng nhập tên địa chỉ");
                } else {
                    checkAllFieldsRequire();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (editAddress.getText().toString().isEmpty()){
                    editAddress.setError("Vui lòng nhập địa chỉ");
                } else {
                    checkAllFieldsRequire();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editAddressReceiverName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (editAddressReceiverName.getText().toString().isEmpty()){
                    editAddressReceiverName.setError("Vui lòng nhập tên người nhận");
                } else {
                    checkAllFieldsRequire();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editAddressReceiverPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (editAddressReceiverPhone.getText().toString().isEmpty()){
                    editAddressReceiverPhone.setError("Vui lòng nhập số điện thoại người nhận");
                } else {
                    checkAllFieldsRequire();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btnDeleteAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference addressRef = FirebaseDatabase.getInstance().getReference().child("ADDRESS");                              addressRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            for (DataSnapshot issue : snapshot.getChildren()){
                                AddressDomain address = issue.getValue(AddressDomain.class);
                                if (address.getAddressID().equals(addressID)){
                                    issue.getRef().removeValue();
                                    onAddressChangedListener.onAddressChanged();
                                    break;
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
                finish();
            }
        });

        btnEditAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // update database
                DatabaseReference addressRef = FirebaseDatabase.getInstance().getReference().child("ADDRESS");
                addressRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            for (DataSnapshot issue : snapshot.getChildren()){
                                AddressDomain address = issue.getValue(AddressDomain.class);
                                if (address.getAddressID().equals(addressID)){
                                    Map <String, Object> updates = new HashMap<>();
                                    updates.put("title", editAddressName.getText().toString());
                                    updates.put("description", editAddress.getText().toString());
                                    updates.put("detail", editAddressBuilding.getText().toString());
                                    updates.put("gate", editAddressGate.getText().toString());
                                    updates.put("note", editAddressNote.getText().toString());
                                    updates.put("recipentName", editAddressReceiverName.getText().toString());
                                    updates.put("recipentPhone", editAddressReceiverPhone.getText().toString());
                                    issue.getRef().updateChildren(updates);
                                    onAddressChangedListener.onAddressChanged();
                                    break;
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                showSuccessDialog();
            }
        });


    }

    private void checkAllFieldsRequire() {
        if (!editAddressName.getText().toString().isEmpty() && !editAddress.getText().toString().isEmpty() && !editAddressReceiverName.getText().toString().isEmpty() && !editAddressReceiverPhone.getText().toString().isEmpty()){
            DatabaseReference addressRef = FirebaseDatabase.getInstance().getReference().child("ADDRESS");
            addressRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        for (DataSnapshot issue : snapshot.getChildren()){
                            AddressDomain address = issue.getValue(AddressDomain.class);
                            if (address.getAddressID().equals(getIntent().getStringExtra("AddressID"))){
                                if (editAddressName.getText().toString().equals(address.getTitle()) && editAddress.getText().toString().equals(address.getDescription()) && editAddressBuilding.getText().toString().equals(address.getDetail()) && editAddressGate.getText().toString().equals(address.getGate()) && editAddressNote.getText().toString().equals(address.getNote()) && editAddressReceiverName.getText().toString().equals(address.getRecipentName()) && editAddressReceiverPhone.getText().toString().equals(address.getRecipentPhone())){
                                    btnEditAddress.setBackground(ContextCompat.getDrawable(EditAddressActivity.this, R.drawable.rounded_rectangle_darkgrey));
                                    btnEditAddress.setEnabled(false);
                                } else {
                                    btnEditAddress.setBackground(ContextCompat.getDrawable(EditAddressActivity.this, R.drawable.background_color));
                                    btnEditAddress.setEnabled(true);
                                }
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else {
            btnEditAddress.setBackground(ContextCompat.getDrawable(this, R.drawable.rounded_rectangle_darkgrey));
            btnEditAddress.setEnabled(false);
        }
    }

    private void initEditedAddress(String addressID) {
        DatabaseReference addressRef = FirebaseDatabase.getInstance().getReference().child("ADDRESS");
        addressRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot issue : snapshot.getChildren()){
                        AddressDomain address = issue.getValue(AddressDomain.class);
                        if (address.getAddressID().equals(addressID)) {
                            editAddressName.setText(address.getTitle());
                            editAddress.setText(address.getDescription());
                            editAddressBuilding.setText(address.getDetail());
                            editAddressGate.setText(address.getGate());
                            editAddressNote.setText(address.getNote());
                            editAddressReceiverName.setText(address.getRecipentName());
                            editAddressReceiverPhone.setText(address.getRecipentPhone());
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void showSuccessDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thông báo");
        builder.setMessage("Đã lưu địa chỉ thành công!");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        AlertDialog alertDialog = builder.show();
    }

    public static void setEditInterfaceInstance(Context context){
        onAddressChangedListener = (OnAddressChangedListener) context;
    }
}