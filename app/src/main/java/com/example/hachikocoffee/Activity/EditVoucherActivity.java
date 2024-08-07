package com.example.hachikocoffee.Activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.example.hachikocoffee.Adapter.MultiSelectSpinnerAdapterEdit;
import com.example.hachikocoffee.Domain.DiscountDomain;
import com.example.hachikocoffee.Domain.UserDomain;
import com.example.hachikocoffee.Domain.UserVoucherDomain;
import com.example.hachikocoffee.Listener.Callback;
import com.example.hachikocoffee.R;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditVoucherActivity extends AppCompatActivity {
    private static Callback callback;
    private EditText addVoucherName;
    private EditText addImageVoucher;
    private EditText etDescription;
    private EditText etMinOderCapacity;
    private EditText etMinOrderPrice;
    private EditText etValueDouble;
    private EditText etValueInteger;
    private Spinner spVoucherType;
    private Spinner spFreeShipping;
    private Button btnCalendarAddVoucher;
    private AppCompatButton btnConfirmAddVoucher;
    private TextView txtview_canlendar;
    private ArrayAdapter<CharSequence> typeAdapter;
    private ArrayAdapter<Integer> freeShippingAdapter;
    private ImageView btnBackAddVoucher;
    private ConstraintLayout btnDeleteVoucher;
    private Spinner spVoucherUser;
    private List<UserDomain> selectedItem = new ArrayList<>();
    private ArrayList<UserDomain> spinnerListItem = new ArrayList<>();
    private static final String[] type_options = {"Pick up", "Delivery"};
    private static final Integer[] free_shipping_options = {0, 1};
    private int VoucherID;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_voucher);
        btnDeleteVoucher = findViewById(R.id.btnDeleteVoucher);
        addVoucherName = findViewById(R.id.addVoucherName);
        addVoucherNameTextChanged();

        addImageVoucher = findViewById(R.id.addImageVoucher);
        addImageVoucherTextChanged();

        etDescription = findViewById(R.id.etDescription);
        etDescriptionTextChanged();

        etMinOderCapacity = findViewById(R.id.etMinOrderCapacity);
        etMinOderCapacityTextChanged();

        etMinOrderPrice = findViewById(R.id.etMinOrderPrice);
        etMinOrderPriceTextChanged();

        etValueDouble = findViewById(R.id.etValueDouble);
        etValueDoubleTextChanged();

        etValueInteger = findViewById(R.id.etValueInteger);
        etValueIntegerTextChanged();

        spVoucherType = findViewById(R.id.spVoucherType);
        spVoucherTypeTextChanged();
        createVoucherTypeSpinner();

        spFreeShipping = findViewById(R.id.spFreeShipping);
        spFreeShippingTextChanged();
        createFreeShippingSpinner();

        btnCalendarAddVoucher = findViewById(R.id.btnCalendarAddVoucher);
        btnCalendarAddVoucherListener();

        btnConfirmAddVoucher = findViewById(R.id.btnConfirmAddVoucher);
        btnConfirmAddVoucher.setBackground(ContextCompat.getDrawable(this, R.drawable.rounded_rectangle_darkgrey));
        btnConfirmAddVoucher.setEnabled(false);

        txtview_canlendar = findViewById(R.id.txtview_canlendar);
        addTxtViewCalendar();
        btnBackAddVoucher = findViewById(R.id.btnBackAddVoucher);

        Intent intent = getIntent();
        VoucherID = intent.getIntExtra("VoucherID", 0);
        initDefaultVoucher();


        spVoucherUser = findViewById(R.id.spVoucherUser);
        initSpinnerVoucherUser();

        btnBackAddVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnConfirmAddVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateVoucher(VoucherID);
                Toast.makeText(EditVoucherActivity.this, "Cập nhật voucher thành công!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        btnDeleteVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteVoucher();
            }
        });
    }

    private void initSpinnerVoucherUser() {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("USER");
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot issue : snapshot.getChildren()){
                        UserDomain user = issue.getValue(UserDomain.class);
                        if (user.getIsAdmin() != 1){
                            String fullname = user.getName();
                            String[] name = fullname.split(",");
                            user.setName(name[1].trim() + " " + name[0]);
                            spinnerListItem.add(user);
                        }
                    }

                    DatabaseReference userVoucherRef = FirebaseDatabase.getInstance().getReference("USERVOUCHER");
                    userVoucherRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()){
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                    UserVoucherDomain userVoucherDomain = dataSnapshot.getValue(UserVoucherDomain.class);
//                                    Toast.makeText(EditVoucherActivity.this, "userVoucherDomain UserID: " + userVoucherDomain.getUserID(), Toast.LENGTH_SHORT).show();
//                                    if (userVoucherDomain.getVoucherID() == VoucherID) {
                                        DatabaseReference userRef1 = FirebaseDatabase.getInstance().getReference("USER");
                                        userRef1.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if (snapshot.exists()) {
                                                    for (DataSnapshot issue : snapshot.getChildren()) {
                                                        UserDomain userDomain = issue.getValue(UserDomain.class);
//                                                        Log.d("userVoucherDomain", userDomain.getUserID() + " " + userVoucherDomain.getUserID() + " " + userVoucherDomain.getVoucherID() + " " + VoucherID);
                                                        if (userDomain.getUserID() == userVoucherDomain.getUserID() && userVoucherDomain.getVoucherID() == VoucherID){
                                                            String fullname = userDomain.getName();
                                                            String[] name = fullname.split(",");
                                                            userDomain.setName(name[1].trim() + " " + name[0]);
                                                            selectedItem.add(userDomain);
                                                        }
                                                    }
//                                                    if (userVoucherDomain.getVoucherID() == VoucherID){
                                                        MultiSelectSpinnerAdapterEdit adapter = new MultiSelectSpinnerAdapterEdit(
                                                                getApplicationContext(),
                                                                spinnerListItem,
                                                                selectedItem,
                                                                VoucherID
                                                        );

                                                        spVoucherUser.setAdapter(adapter);

                                                        adapter.setOnItemSelectedListener(new MultiSelectSpinnerAdapterEdit.OnItemSelectedListener() {
                                                            @Override
                                                            public void onItemSelected(List<UserDomain> selectedItems, int pos) {
                                                                Toast.makeText(EditVoucherActivity.this, "Đã thay đổi đối tượng áp dụng!", Toast.LENGTH_SHORT).show();
                                                                if (selectedItems == null || selectedItems.isEmpty()) {
                                                                    removeVoucherUser();
                                                                } else {
                                                                    StringBuilder selectedNames = new StringBuilder();
                                                                    for (UserDomain item : selectedItems) {
                                                                        selectedNames.append(item.getName()).append(", ");
                                                                    }
                                                                    // Remove the trailing comma and space
                                                                    selectedNames.delete(selectedNames.length() - 2, selectedNames.length());
//                                name.setText(selectedNames.toString());
                                                                }
                                                            }
                                                        });
//                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
//                                        break;
//                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void deleteVoucher() {
        AlertDialog alertDialog = new AlertDialog.Builder(EditVoucherActivity.this, R.style.AlertDialog_AppCompat_Custom)
                .setTitle("Xóa voucher")
                .setMessage("Xác nhận xóa voucher này?")
                .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DatabaseReference voucherRef = FirebaseDatabase.getInstance().getReference().child("VOUCHER");
                        voucherRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()){
                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                        DiscountDomain voucher = dataSnapshot.getValue(DiscountDomain.class);
                                        if (voucher.getVoucherID() == VoucherID){
                                            dataSnapshot.getRef().removeValue();
                                            callback.onCallback();
                                            Toast.makeText(EditVoucherActivity.this, "Xóa voucher thành công!", Toast.LENGTH_SHORT).show();
                                            finish();
                                            break;
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                })
                .setNegativeButton("Không", null)
                .show();

        Button positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        Button negativeButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        positiveButton.setTextColor(Color.parseColor("#000000"));
        negativeButton.setTextColor(Color.parseColor("#E47905"));
    }

    private void initDefaultVoucher() {
        DatabaseReference voucherRef = FirebaseDatabase.getInstance().getReference().child("VOUCHER");
        voucherRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot issue : snapshot.getChildren()){
                        DiscountDomain voucher = issue.getValue(DiscountDomain.class);
                        if (voucher.getVoucherID() == VoucherID){
                            addVoucherName.setText(""+voucher.getTitle());
                            addImageVoucher.setText(""+voucher.getImageURL());
                            etDescription.setText(""+voucher.getDescription());
                            etMinOderCapacity.setText(""+voucher.getMinOrderCapacity());
                            etMinOrderPrice.setText(""+voucher.getMinOrderPrice());
                            etValueDouble.setText(""+voucher.getValueDouble());
                            etValueInteger.setText(""+voucher.getValueInteger());
                            txtview_canlendar.setText(""+voucher.getExpiryDate());
                            typeAdapter = new ArrayAdapter<>(EditVoucherActivity.this, android.R.layout.simple_spinner_item, type_options);
                            typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spVoucherType.setAdapter(typeAdapter);
                            spVoucherType.setSelection(typeAdapter.getPosition(voucher.getType()));
                            freeShippingAdapter = new ArrayAdapter<>(EditVoucherActivity.this, android.R.layout.simple_spinner_item, free_shipping_options);
                            freeShippingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spFreeShipping.setAdapter(freeShippingAdapter);
                            spFreeShipping.setSelection(freeShippingAdapter.getPosition(Integer.valueOf(voucher.getFreeShipping())));
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void addTxtViewCalendar() {
        txtview_canlendar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (txtview_canlendar.getText().toString().length() == 0){
                    txtview_canlendar.setError("Vui lòng chọn ngày hết hạn");
                }
                checkAllRequireFields();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void createFreeShippingSpinner() {
        freeShippingAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, free_shipping_options);
        freeShippingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spFreeShipping.setAdapter(freeShippingAdapter);
    }

    private void createVoucherTypeSpinner() {
        typeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, type_options);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spVoucherType.setAdapter(typeAdapter);
    }

    private void updateVoucher(int i) {
        DatabaseReference voucherRef = FirebaseDatabase.getInstance().getReference().child("VOUCHER");
        voucherRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        DiscountDomain voucher = dataSnapshot.getValue(DiscountDomain.class);
                        if (voucher.getVoucherID() == i){
                            Map<String, Object> updates = new HashMap<>();
                            updates.put("title", addVoucherName.getText().toString());
                            updates.put("type", spVoucherType.getSelectedItem().toString());
                            updates.put("freeShipping", Integer.parseInt(spFreeShipping.getSelectedItem().toString()));
                            updates.put("imageURL", addImageVoucher.getText().toString());
                            updates.put("description", etDescription.getText().toString());
                            updates.put("minOrderCapacity", Integer.parseInt(etMinOderCapacity.getText().toString()));
                            updates.put("minOrderPrice", Integer.parseInt(etMinOrderPrice.getText().toString()));
                            updates.put("valueDouble", Double.parseDouble(etValueDouble.getText().toString()));
                            updates.put("valueInteger", Integer.parseInt(etValueInteger.getText().toString()));
                            updates.put("expiryDate", txtview_canlendar.getText().toString());
                            dataSnapshot.getRef().updateChildren(updates);
                            callback.onCallback();
                            break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        voucherRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot voucherSnapshot : dataSnapshot.getChildren()) {
                    // Get the voucher ID
                    String voucherId = voucherSnapshot.getKey();

                    // Remove the aboutToExpire field
                    voucherRef.child(voucherId).child("aboutToExpire").removeValue()
                            .addOnSuccessListener(aVoid -> {
                                // Field successfully removed
                                Log.d("Firebase", "aboutToExpire field removed from voucher: " + voucherId);
                            })
                            .addOnFailureListener(e -> {
                                // An error occurred
                                Log.e("Firebase", "Error removing aboutToExpire field: ", e);
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle potential errors
                Log.e("Firebase", "Error querying vouchers: ", databaseError.toException());
            }
        });
    }

    private void btnCalendarAddVoucherListener() {
        btnCalendarAddVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });
    }

    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String x, y;
                        if (dayOfMonth < 10){
                            x = "0" + dayOfMonth;
                        } else {
                            x = String.valueOf(dayOfMonth);
                        }
                        if (monthOfYear + 1 < 10){
                            y = "0" + (monthOfYear + 1);
                        } else {
                            y = String.valueOf(monthOfYear + 1);
                        }
                        txtview_canlendar.setText(x + "-" + y + "-" + year);
                    }
                }, year, month, dayOfMonth);
        datePickerDialog.show();
    }

    private void spFreeShippingTextChanged() {
        spFreeShipping.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                checkAllRequireFields();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void spVoucherTypeTextChanged() {
        spVoucherType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                checkAllRequireFields();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void etValueIntegerTextChanged() {
        etValueInteger.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etValueInteger.getText().toString().length() == 0){
                    etValueInteger.setError("Vui lòng nhập giá trị voucher");
                }
                checkAllRequireFields();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void etValueDoubleTextChanged() {
        etValueDouble.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etValueDouble.getText().toString().length() == 0){
                    etValueDouble.setError("Vui lòng nhập giá trị voucher");
                }
                checkAllRequireFields();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void etMinOrderPriceTextChanged() {
        etMinOrderPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etMinOrderPrice.getText().toString().length() == 0){
                    etMinOrderPrice.setError("Vui lòng nhập giá trị đơn hàng tối thiểu");
                }
                checkAllRequireFields();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void etMinOderCapacityTextChanged() {
        etMinOderCapacity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etMinOderCapacity.getText().toString().length() == 0){
                    etMinOderCapacity.setError("Vui lòng nhập số lượng đơn hàng tối thiểu");
                }
                checkAllRequireFields();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void etDescriptionTextChanged() {
        etDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etDescription.getText().toString().length() == 0){
                    etDescription.setError("Vui lòng nhập mô tả");
                }
                checkAllRequireFields();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void addImageVoucherTextChanged() {
        addImageVoucher.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (addImageVoucher.getText().toString().length() == 0){
                    addImageVoucher.setError("Vui lòng nhập URL ảnh");
                }
                checkAllRequireFields();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void addVoucherNameTextChanged() {
        addVoucherName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (addVoucherName.getText().toString().length() == 0){
                    addVoucherName.setError("Vui lòng nhập tên voucher");
                }
                checkAllRequireFields();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void checkAllRequireFields() {
        if (addVoucherName.getText().toString().length() != 0
                && spVoucherType.getSelectedItem().toString().length() != 0
                && spFreeShipping.getSelectedItem().toString().length() != 0
                && addImageVoucher.getText().toString().length() != 0
                && etDescription.getText().toString().length() != 0
                && etMinOderCapacity.getText().toString().length() != 0
                && etMinOrderPrice.getText().toString().length() != 0
                && etValueDouble.getText().toString().length() != 0
                && etValueInteger.getText().toString().length() != 0
                && txtview_canlendar.getText().toString().length() != 0){
            DatabaseReference voucherRef = FirebaseDatabase.getInstance().getReference().child("VOUCHER");
            voucherRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        for (DataSnapshot issue : snapshot.getChildren()){
                            DiscountDomain voucher = issue.getValue(DiscountDomain.class);
                            if (voucher.getVoucherID() == VoucherID){
                                if (addVoucherName.getText().toString().equals(voucher.getTitle())
                                        && spVoucherType.getSelectedItem().toString().equals(voucher.getType())
                                        && Integer.parseInt(spFreeShipping.getSelectedItem().toString()) == voucher.getFreeShipping()
                                        && addImageVoucher.getText().toString().equals(voucher.getImageURL())
                                        && etDescription.getText().toString().equals(voucher.getDescription())
                                        && etMinOderCapacity.getText().toString().equals(String.valueOf(voucher.getMinOrderCapacity()))
                                        && etMinOrderPrice.getText().toString().equals(String.valueOf(voucher.getMinOrderPrice()))
                                        && etValueDouble.getText().toString().equals(String.valueOf(voucher.getValueDouble()))
                                        && etValueInteger.getText().toString().equals(String.valueOf(voucher.getValueInteger()))
                                        && txtview_canlendar.getText().toString().equals(voucher.getExpiryDate()))
                                {
                                    if (btnConfirmAddVoucher.isEnabled()){;
                                        btnConfirmAddVoucher.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.rounded_rectangle_darkgrey));
                                        btnConfirmAddVoucher.setEnabled(false);
                                    }
                                } else {
                                    btnConfirmAddVoucher.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.background_color));
                                    btnConfirmAddVoucher.setEnabled(true);
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
            btnConfirmAddVoucher.setBackground(ContextCompat.getDrawable(this, R.drawable.rounded_rectangle_darkgrey));
            btnConfirmAddVoucher.setEnabled(false);
        }
    }

    private void checkAllFieldRequireSpiner(){
        if (addVoucherName.getText().toString().length() != 0
                && spVoucherType.getSelectedItem().toString().length() != 0
                && spFreeShipping.getSelectedItem().toString().length() != 0
                && addImageVoucher.getText().toString().length() != 0
                && etDescription.getText().toString().length() != 0
                && etMinOderCapacity.getText().toString().length() != 0
                && etMinOrderPrice.getText().toString().length() != 0
                && etValueDouble.getText().toString().length() != 0
                && etValueInteger.getText().toString().length() != 0
                && txtview_canlendar.getText().toString().length() != 0){
           btnConfirmAddVoucher.setBackground(ContextCompat.getDrawable(this, R.drawable.background_color));
           btnConfirmAddVoucher.setEnabled(true);
        } else {
            btnConfirmAddVoucher.setBackground(ContextCompat.getDrawable(this, R.drawable.rounded_rectangle_darkgrey));
            btnConfirmAddVoucher.setEnabled(false);
        }
    }

    private void removeVoucherUser() {
        DatabaseReference userVoucherRef = FirebaseDatabase.getInstance().getReference("USERVOUCHER");
        userVoucherRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot issue : snapshot.getChildren()){
                        UserVoucherDomain userVoucherDomain = issue.getValue(UserVoucherDomain.class);
                        if (userVoucherDomain.getVoucherID() == VoucherID) {
                            issue.getRef().removeValue();
                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public static void setEditInterfaceInstance(Context context) {
        callback = (Callback) context;
    }
}
