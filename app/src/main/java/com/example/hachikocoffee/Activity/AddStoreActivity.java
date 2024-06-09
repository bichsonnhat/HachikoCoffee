package com.example.hachikocoffee.Activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import com.example.hachikocoffee.Domain.ShopDomain;
import com.example.hachikocoffee.Listener.Callback;
import com.example.hachikocoffee.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddStoreActivity extends AppCompatActivity {
    private static Callback callback;
    private ImageView storeMgmt_back;
    private EditText storeMgmt_addName, storeMgmt_addImageURL, storeMgmt_addLongtitude, storeMgmt_addLatitude, storeMgmt_addAddress;
    private AppCompatButton storeMgmt_addConfirm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_store);

        storeMgmt_back = findViewById(R.id.storeMgmt_back);
        storeMgmt_addName = findViewById(R.id.storeMgmt_addName);
        storeMgmt_addImageURL = findViewById(R.id.storeMgmt_addImageURL);
        storeMgmt_addLongtitude = findViewById(R.id.storeMgmt_addLongtitude);
        storeMgmt_addLatitude = findViewById(R.id.storeMgmt_addLatitude);
        storeMgmt_addAddress = findViewById(R.id.storeMgmt_addAddress);
        storeMgmt_addConfirm = findViewById(R.id.storeMgmt_addConfirm);

        addAttributeTextChanged();

        storeMgmt_back.setOnClickListener(v -> finish());

        storeMgmt_addConfirm.setBackground(ContextCompat.getDrawable(this, R.drawable.rounded_rectangle_darkgrey));
        storeMgmt_addConfirm.setEnabled(false);
        storeMgmt_addConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("STORE");

                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            int index = 0;
                            for (DataSnapshot issue : snapshot.getChildren()) {
                                ShopDomain shopDomain = issue.getValue(ShopDomain.class);
                                index = Math.max(index, shopDomain.getStoreID());
                            }
                            addStore(index + 1);
                            Toast.makeText(AddStoreActivity.this, "Thêm cửa hàng thành công!", Toast.LENGTH_SHORT).show();
                            callback.onCallback();
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        //
                    }
                });
            }
        });
    }

    private void addStore(int index) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("STORE");
        ShopDomain shop = new ShopDomain(
                index,
                storeMgmt_addAddress.getText().toString(),
                storeMgmt_addName.getText().toString(),
                storeMgmt_addImageURL.getText().toString(),
                "POINT(" + storeMgmt_addLongtitude.getText().toString() + " " + storeMgmt_addLatitude.getText().toString() + ")"
        );
        ref.child(String.valueOf(index)).setValue(shop);
    }

    private void addAttributeTextChanged() {
        storeMgmt_addName.addTextChangedListener(new TextWatcher() {
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

        storeMgmt_addImageURL.addTextChangedListener(new TextWatcher() {
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

        storeMgmt_addLongtitude.addTextChangedListener(new TextWatcher() {
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

        storeMgmt_addLatitude.addTextChangedListener(new TextWatcher() {
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

        storeMgmt_addAddress.addTextChangedListener(new TextWatcher() {
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
        if (!storeMgmt_addName.getText().toString().isEmpty() &&
                !storeMgmt_addImageURL.getText().toString().isEmpty() &&
                !storeMgmt_addLongtitude.getText().toString().isEmpty() &&
                !storeMgmt_addLatitude.getText().toString().isEmpty() &&
                !storeMgmt_addAddress.getText().toString().isEmpty()) {
            storeMgmt_addConfirm.setBackground(ContextCompat.getDrawable(this, R.drawable.background_color));
            storeMgmt_addConfirm.setEnabled(true);
        } else {
            storeMgmt_addConfirm.setBackground(ContextCompat.getDrawable(this, R.drawable.rounded_rectangle_darkgrey));
            storeMgmt_addConfirm.setEnabled(false);
        }
    }

    public static void setEditInterfaceInstance(StoreManagementActivity context) {
        callback = (Callback) context;
    }
}