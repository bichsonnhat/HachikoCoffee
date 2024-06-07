package com.example.hachikocoffee.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.example.hachikocoffee.Adapter.DiscountAdapter1;
import com.example.hachikocoffee.Domain.DiscountDomain;
import com.example.hachikocoffee.Management.ManagementCart;
import com.example.hachikocoffee.Management.ManagementUser;
import com.example.hachikocoffee.R;
import com.example.hachikocoffee.databinding.ActivityMainBinding;
import com.example.hachikocoffee.databinding.ActivityYourVoucherPickBinding;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class YourVoucherPick extends AppCompatActivity implements DiscountAdapter1.OnDiscountSelectedListener {
    
    ActivityYourVoucherPickBinding binding;
    DiscountAdapter1 adapter;
    ArrayList<DiscountDomain> deliveryVoucherList1 = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        binding = ActivityYourVoucherPickBinding.inflate(getLayoutInflater());
        LayoutInflater inflater = getLayoutInflater();
        setContentView(binding.getRoot());

        binding.btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DiscountDomain selectedDiscount = adapter.getSelectedDiscount();
                if (isVoucherSelectedTrue()) {
                    Intent intent = new Intent();
                    intent.putExtra("selectedDiscount", selectedDiscount);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }{
                    showSnackbar("Vui lòng chọn voucher hợp lệ");
                }
            }
        });
        
        initVoucher();
    }

    private void initVoucher() {
        binding.rcvDelivery.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        DatabaseReference userVoucherRef = FirebaseDatabase.getInstance().getReference("USERVOUCHER");
        userVoucherRef.orderByChild("userID").equalTo(ManagementUser.getInstance().getUserId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> voucherIDs = new ArrayList<>();
                if (dataSnapshot.exists()){
                    for (DataSnapshot userVoucherSnapshot : dataSnapshot.getChildren()) {
                        int isUse = userVoucherSnapshot.child("isUse").getValue(Integer.class);
                        if (isUse == 0){
                            String voucherID = String.valueOf(userVoucherSnapshot.child("voucherID").getValue(Long.class));
                            voucherIDs.add(voucherID);
                        }
                    }

                    DatabaseReference voucherRef = FirebaseDatabase.getInstance().getReference("VOUCHER");

                    voucherRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot voucherSnapshot : dataSnapshot.getChildren()) {
                                String currentVoucherID = String.valueOf(voucherSnapshot.child("voucherID").getValue(Long.class));
                                if (voucherIDs.contains(currentVoucherID)) {
                                    DiscountDomain discount = voucherSnapshot.getValue(DiscountDomain.class);
                                    if (discount != null && discount.getType().equals("Delivery")) { // Must add check date function
                                        if (!discount.isAboutToExpire()) {
                                            deliveryVoucherList1.add(discount);
                                        }
                                    }
                                }
                            }

                            displayDiscountData(deliveryVoucherList1);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            // Xử lý lỗi nếu có
                            System.err.println("Error retrieving vouchers: " + databaseError.getMessage());
                        }
                    });
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xử lý lỗi nếu có
                System.err.println("Error retrieving user vouchers: " + databaseError.getMessage());
            }
        });

    }

    private void displayDiscountData(ArrayList<DiscountDomain> deliveryVoucherList1) {
        adapter = new DiscountAdapter1(deliveryVoucherList1);
        adapter.setOnDiscountSelectedListener(this);

        updateConfirmButtonState();
        binding.rcvDelivery.setAdapter(adapter);
    }

    private boolean isVoucherSelectedTrue() {
        DiscountDomain selectedDiscount = adapter.getSelectedDiscount();
        Log.d("count", ""+ ManagementCart.getInstance().getItemsCount());
        if (selectedDiscount == null){
            return false;
        }
        if (ManagementCart.getInstance().getItemsCount() < selectedDiscount.getMinOrderCapacity()){
            return false;
        }
        if (ManagementCart.getInstance().getTotalCost() < selectedDiscount.getMinOrderPrice()){
            return false;
        }
        return true;
    }
    @Override
    public void onDiscountSelected(DiscountDomain discount) {
        updateConfirmButtonState();
    }
    private void updateConfirmButtonState() {
        if (isVoucherSelectedTrue()){
            binding.btnConfirm.setEnabled(true);
            Log.d("Voucher", "true");
            binding.btnConfirm.setBackground(ContextCompat.getDrawable(YourVoucherPick.this, R.drawable.background_color));
        }
        else{
            Log.d("Voucher", "false");
            binding.btnConfirm.setEnabled(false);
            binding.btnConfirm.setBackground(ContextCompat.getDrawable(YourVoucherPick.this, R.drawable.rounded_rectangle_darkgrey));
        }
    }

    private void showSnackbar(String message) {
        Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_SHORT).show();
    }
}