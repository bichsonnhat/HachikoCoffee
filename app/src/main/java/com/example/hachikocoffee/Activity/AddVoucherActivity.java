package com.example.hachikocoffee.Activity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
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
import androidx.core.content.ContextCompat;

import com.example.hachikocoffee.Adapter.MultiSelectSpinnerAdapter;
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
import java.util.List;

public class AddVoucherActivity extends AppCompatActivity {
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
    private Spinner spVoucherUser;
    private List<UserDomain> selectedItem = new ArrayList<>();
    private ArrayList<UserDomain> spinnerListItem = new ArrayList<>();
    private static final String[] type_options = {"Pick up", "Delivery"};
    private static final Integer[] free_shipping_options = {0, 1};
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_voucher);
        btnBackAddVoucher = findViewById(R.id.btnBackAddVoucher);
        txtview_canlendar = findViewById(R.id.txtview_canlendar);
        addTxtViewCalendar();
        addVoucherName = findViewById(R.id.addVoucherName);
        addVoucherNameTextChanged();
        addImageVoucher = findViewById(R.id.addImageVoucher);
        addImageVoucherTextChanged();
        etDescription = findViewById(R.id.etDescription);
        etDescriptionTextChanged();
        etMinOderCapacity = findViewById(R.id.etMinOderCapacity);
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

        spVoucherUser = findViewById(R.id.spVoucherUser);
        initSpinnerVoucherUser();

        btnCalendarAddVoucher = findViewById(R.id.btnCalendarAddVoucher);
        btnCalendarAddVoucherListener();
        btnConfirmAddVoucher = findViewById(R.id.btnConfirmAddVoucher);
        btnConfirmAddVoucher.setBackground(ContextCompat.getDrawable(this, R.drawable.rounded_rectangle_darkgrey));
        btnConfirmAddVoucher.setEnabled(false);
        btnConfirmAddVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference voucherRef = FirebaseDatabase.getInstance().getReference().child("VOUCHER");
                voucherRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            int index = 0;
                            for (DataSnapshot issue : snapshot.getChildren()){
                                DiscountDomain discountDomain = issue.getValue(DiscountDomain.class);
                                index = Math.max(index, discountDomain.getVoucherID());
                            }
                            addVoucher(index + 1);
                            Toast.makeText(AddVoucherActivity.this, "Thêm voucher thành công!", Toast.LENGTH_SHORT).show();
                            callback.onCallback();
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        btnBackAddVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference voucherRef = FirebaseDatabase.getInstance().getReference("VOUCHER");
                voucherRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            int voucherID = 0;
                            for (DataSnapshot issue : snapshot.getChildren()){
                                DiscountDomain discountDomain = issue.getValue(DiscountDomain.class);
                                voucherID = Math.max(voucherID, discountDomain.getVoucherID());
                            }
                            removeVoucherUser(voucherID + 1);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                finish();
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
                            spinnerListItem.add(user);
                        }
                    }
                    selectedItem.clear();
                    MultiSelectSpinnerAdapter adapter = new MultiSelectSpinnerAdapter(
                            getApplicationContext(),
                            spinnerListItem,
                            selectedItem
                    );

                    spVoucherUser.setAdapter(adapter);

                    adapter.setOnItemSelectedListener(new MultiSelectSpinnerAdapter.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(List<UserDomain> selectedItems, int pos) {
                            if (selectedItems == null || selectedItems.isEmpty()) {
                                DatabaseReference voucherRef = FirebaseDatabase.getInstance().getReference("VOUCHER");
                                voucherRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()){
                                            int voucherID = 0;
                                            for (DataSnapshot issue : snapshot.getChildren()){
                                                DiscountDomain discountDomain = issue.getValue(DiscountDomain.class);
                                                voucherID = Math.max(voucherID, discountDomain.getVoucherID());
                                            }
                                            removeVoucherUser(voucherID + 1);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
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
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void removeVoucherUser(int voucherID) {
        DatabaseReference userVoucherRef = FirebaseDatabase.getInstance().getReference("USERVOUCHER");
        userVoucherRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot issue : snapshot.getChildren()){
                        UserVoucherDomain userVoucherDomain = issue.getValue(UserVoucherDomain.class);
                        if (userVoucherDomain.getVoucherID() == voucherID) {
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

    private void addTxtViewCalendar() {
        txtview_canlendar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (txtview_canlendar.getText().toString().length() == 0){
                    txtview_canlendar.setError("Vui lòng chọn ngày hết hạn");
                } else {
                    checkAllRequireFields();
                }
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

    private void addVoucher(int i) {
        DatabaseReference voucherRef = FirebaseDatabase.getInstance().getReference().child("VOUCHER");
//        VoucherID = voucherID;
//        Description = description;
//        ExpiryDate = expiryDate;
//        ImageURL = imageURL;
//        Title = title;
//        ValueInteger = valueInteger;
//        ValueDouble = valueDouble;
//        FreeShipping = freeShipping;
//        MinOrderCapacity = minOrderCapacity;
//        MinOrderPrice = minOrderPrice;
//        Type = type;
        DiscountDomain discountDomain = new DiscountDomain(
                i,
                etDescription.getText().toString(),
                txtview_canlendar.getText().toString(),
                addImageVoucher.getText().toString(),
                addVoucherName.getText().toString(),
                Integer.parseInt(etValueInteger.getText().toString()),
                Double.parseDouble(etValueDouble.getText().toString()),
                Integer.parseInt(spFreeShipping.getSelectedItem().toString()),
                Integer.parseInt(etMinOderCapacity.getText().toString()),
                Integer.parseInt(etMinOrderPrice.getText().toString()),
                spVoucherType.getSelectedItem().toString()
        );
        Log.d("Index: ", String.valueOf(i));
        Log.d("Description: ", etDescription.getText().toString());
        Log.d("ExpiryDate: ", txtview_canlendar.getText().toString());
        Log.d("ImageURL: ", addImageVoucher.getText().toString());
        Log.d("Title: ", addVoucherName.getText().toString());
        Log.d("ValueInteger: ", etValueInteger.getText().toString());
        Log.d("ValueDouble: ", etValueDouble.getText().toString());
        Log.d("FreeShipping: ", String.valueOf(Integer.parseInt(spFreeShipping.getSelectedItem().toString())));
        Log.d("MinOrderCapacity: ", etMinOderCapacity.getText().toString());
        Log.d("MinOrderPrice: ", etMinOrderPrice.getText().toString());
        Log.d("Type: ", spVoucherType.getSelectedItem().toString());
        Log.d("Discount Domain before push:", discountDomain.toString());
        voucherRef.child(String.valueOf(i)).setValue(discountDomain);
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
    }

    private void spVoucherTypeTextChanged() {
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
            btnConfirmAddVoucher.setBackground(ContextCompat.getDrawable(this, R.drawable.background_color));
            btnConfirmAddVoucher.setEnabled(true);
        } else {
            btnConfirmAddVoucher.setBackground(ContextCompat.getDrawable(this, R.drawable.rounded_rectangle_darkgrey));
            btnConfirmAddVoucher.setEnabled(false);
        }
    }

    public static void setEditInterfaceInstance(VoucherManagementActivity context){
        callback = (Callback) context;
    }
}
