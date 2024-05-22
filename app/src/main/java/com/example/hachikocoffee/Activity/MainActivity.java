package com.example.hachikocoffee.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.example.hachikocoffee.BottomSheetDialog.CartBottomSheetDialogFragment;
import com.example.hachikocoffee.Management.ManagementCart;
import com.example.hachikocoffee.Fragment.DiscountFragment;
import com.example.hachikocoffee.Fragment.HomeFragment;
import com.example.hachikocoffee.Fragment.OrderFragment;
import com.example.hachikocoffee.Fragment.OtherFragment;
import com.example.hachikocoffee.Fragment.ShopFragment;
import com.example.hachikocoffee.Listener.OnCartChangedListener;
import com.example.hachikocoffee.R;
import com.example.hachikocoffee.databinding.ActivityMainBinding;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.ShapeAppearanceModel;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    private boolean shouldShowAppBar = true;
    int curId = R.id.home;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        LayoutInflater inflater = getLayoutInflater();
        setContentView(binding.getRoot());
        replaceFragment(new HomeFragment());
        setBackground();
        updateOrderButtonVisibility();
        binding.itemsCount.setText(""+ ManagementCart.getInstance().getItemsCount());

        //format money
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        String a = new DecimalFormat("#,###", symbols).format(ManagementCart.getInstance().getTotalCost());
        binding.totalCost.setText(a + "đ");


        ManagementCart.getInstance().addOnCartChangedListener(new OnCartChangedListener() {
            @Override
            public void onCartChanged() {
                updateOrderButtonVisibility();
            }
        });

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {

            if (curId == item.getItemId())
                return false;
            curId = item.getItemId();
            if (curId == R.id.home) {
                replaceFragment(new HomeFragment());
            } else if (curId == R.id.order) {
                replaceFragment(new OrderFragment());
            } else if (curId == R.id.shop) {
                replaceFragment(new ShopFragment());
            } else if (curId == R.id.voucher) {
                replaceFragment(new DiscountFragment());
            } else if (curId == R.id.menu) {
                replaceFragment(new OtherFragment());
            }

            return true;
        });

        binding.orderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CartBottomSheetDialogFragment cartBottomSheet = new CartBottomSheetDialogFragment();
                cartBottomSheet.show(getSupportFragmentManager(), "CartBottomSheet");
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void updateOrderButtonVisibility() {
        if (ManagementCart.getInstance().getCartItems().size() > 0) {
            binding.orderBtn.setVisibility(View.VISIBLE);
            Log.d("MainActivity", "orderBtn: " + ManagementCart.getInstance().getCartItems().size());
            Log.d("MainActivity", "orderBtn: " + "true");
        } else {
            Log.d("MainActivity", "orderBtn: " + ManagementCart.getInstance().getCartItems().size());
            Log.d("MainActivity", "orderBtn: " + "false");
            binding.orderBtn.setVisibility(View.GONE);
        }

        binding.itemsCount.setText(""+ ManagementCart.getInstance().getItemsCount());

        //format money
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        String a = new DecimalFormat("#,###", symbols).format(ManagementCart.getInstance().getTotalCost());
        binding.totalCost.setText(a + "đ");
    }

    private void setBackground(){
        ShapeAppearanceModel shapeAppearanceModel = new ShapeAppearanceModel()
                .toBuilder()
                .setAllCornerSizes(50f)
                .build();
        MaterialShapeDrawable materialShapeDrawable = new MaterialShapeDrawable(shapeAppearanceModel);
        materialShapeDrawable.setFillColor(ColorStateList.valueOf(Color.WHITE));
        materialShapeDrawable.setStroke(0.5F, Color.parseColor("#CCCCCC"));
        materialShapeDrawable.setElevation(10);
        binding.linearLayout.setBackground(materialShapeDrawable);

        MaterialShapeDrawable materialShapeDrawable1 = new MaterialShapeDrawable(shapeAppearanceModel);
        materialShapeDrawable1.setFillColor(ColorStateList.valueOf(Color.parseColor("#E47905")));
        binding.orderBtn.setBackground(materialShapeDrawable1);
    }
    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();

        if (fragment instanceof HomeFragment || fragment instanceof OrderFragment) {
            shouldShowAppBar = true;
            binding.appBarLayout.setVisibility(View.VISIBLE);
        } else {
            shouldShowAppBar = false;
            binding.appBarLayout.setVisibility(View.GONE);
        }
    }

    public void navigateToOrderFragment() {
        //replaceFragment(new OrderFragment());

        binding.bottomNavigationView.setSelectedItemId(R.id.order);
    }
}