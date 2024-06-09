//EditStoreActivity.java
package com.example.hachikocoffee.Activity;

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
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.example.hachikocoffee.Domain.ShopDomain;
import com.example.hachikocoffee.Listener.Callback;
import com.example.hachikocoffee.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class EditStoreActivity extends AppCompatActivity {
    private static Callback callback;
    private ImageView storeMgmt_back;
    private EditText storeMgmt_editName, storeMgmt_editImageURL, storeMgmt_editLongtitude, storeMgmt_editLatitude, storeMgmt_editAddress;
    private ConstraintLayout storeMgmt_editDelete;
    private AppCompatButton storeMgmt_editConfirm;
    private int StoreID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_store);

        storeMgmt_back = findViewById(R.id.storeMgmt_back);
        storeMgmt_editName = findViewById(R.id.storeMgmt_editName);
        storeMgmt_editImageURL = findViewById(R.id.storeMgmt_editImageURL);
        storeMgmt_editLongtitude = findViewById(R.id.storeMgmt_editLongtitude);
        storeMgmt_editLatitude = findViewById(R.id.storeMgmt_editLatitude);
        storeMgmt_editAddress = findViewById(R.id.storeMgmt_editAddress);
        storeMgmt_editDelete = findViewById(R.id.storeMgmt_editDelete);
        storeMgmt_editConfirm = findViewById(R.id.storeMgmt_editConfirm);

        addAttributeTextChanged();

        Intent intent = getIntent();
        StoreID = intent.getIntExtra("StoreID", 0);
        initDefaultStore();

        storeMgmt_back.setOnClickListener(v -> finish());

        storeMgmt_editConfirm.setBackground(ContextCompat.getDrawable(this, R.drawable.rounded_rectangle_darkgrey));
        storeMgmt_editConfirm.setEnabled(false);
        storeMgmt_editConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateStore(StoreID);
                Toast.makeText(EditStoreActivity.this, "Store updated", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        storeMgmt_editDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(EditStoreActivity.this, R.style.AlertDialog_AppCompat_Custom)
                        .setTitle("Xoá cửa hàng")
                        .setMessage("Xác nhận xoá cửa hàng này?")
                        .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                deleteStore(StoreID);
                            }
                        })
                        .setNegativeButton("Không", null)
                        .show();

                Button positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                Button negativeButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                positiveButton.setTextColor(Color.parseColor("#000000"));
                negativeButton.setTextColor(Color.parseColor("#E47905"));
                deleteStore(StoreID);
            }
        });
    }

    private void initDefaultStore() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("STORE");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        ShopDomain shopDomain = issue.getValue(ShopDomain.class);
                        if (shopDomain.getStoreID() == StoreID) {
                            storeMgmt_editName.setText(shopDomain.getName());
                            storeMgmt_editImageURL.setText(shopDomain.getImageURL());
                            storeMgmt_editLongtitude.setText(""+shopDomain.getLongitude());
                            storeMgmt_editLatitude.setText(""+shopDomain.getLatitude());
                            storeMgmt_editAddress.setText(shopDomain.getAddress());
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //
            }
        });
    }

    private void updateStore(int index) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("STORE");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        ShopDomain shopDomain = issue.getValue(ShopDomain.class);
                        if (shopDomain.getStoreID() == index) {
                            Map<String, Object> updates = new HashMap<>();
                            updates.put("Address", storeMgmt_editAddress.getText().toString());
                            updates.put("Name", storeMgmt_editName.getText().toString());
                            updates.put("ImageURL", storeMgmt_editImageURL.getText().toString());
                            updates.put("Coordinate", "POINT(" + storeMgmt_editLongtitude.getText().toString() + " " + storeMgmt_editLatitude.getText().toString() + ")");
                            //ref.child(String.valueOf(index)).updateChildren(updates);
                            issue.getRef().updateChildren(updates);
                            callback.onCallback();
                            break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //
            }
        });
    }

    private void deleteStore(int index) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("STORE");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        ShopDomain shopDomain = issue.getValue(ShopDomain.class);
                        if (shopDomain.getStoreID() == index) {
                            issue.getRef().removeValue();
                            callback.onCallback();
                            Toast.makeText(EditStoreActivity.this, "Deleted store", Toast.LENGTH_SHORT).show();
                            finish();
                            break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //
            }
        });
    }

    private void addAttributeTextChanged() {
        storeMgmt_editName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkAllFieldsFilled();
            }

            @Override
            public void afterTextChanged(Editable s) {
                //
            }
        });

        storeMgmt_editImageURL.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkAllFieldsFilled();
            }

            @Override
            public void afterTextChanged(Editable s) {
                //
            }
        });

        storeMgmt_editLongtitude.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkAllFieldsFilled();
            }

            @Override
            public void afterTextChanged(Editable s) {
                //
            }
        });

        storeMgmt_editLatitude.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkAllFieldsFilled();
            }

            @Override
            public void afterTextChanged(Editable s) {
                //
            }
        });

        storeMgmt_editAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkAllFieldsFilled();
            }

            @Override
            public void afterTextChanged(Editable s) {
                //
            }
        });
    }

    private void checkAllFieldsFilled() {
        if (!storeMgmt_editName.getText().toString().isEmpty() &&
                !storeMgmt_editImageURL.getText().toString().isEmpty() &&
                !storeMgmt_editLongtitude.getText().toString().isEmpty() &&
                !storeMgmt_editLatitude.getText().toString().isEmpty() &&
                !storeMgmt_editAddress.getText().toString().isEmpty()) {
            storeMgmt_editConfirm.setBackground(ContextCompat.getDrawable(this, R.drawable.background_color));
            storeMgmt_editConfirm.setEnabled(true);
        } else {
            storeMgmt_editConfirm.setBackground(ContextCompat.getDrawable(this, R.drawable.rounded_rectangle_darkgrey));
            storeMgmt_editConfirm.setEnabled(false);
        }
    }

    public static void setEditInterfaceInstance(Context context) {
        callback = (Callback) context;
    }
}