package com.example.hachikocoffee.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.example.hachikocoffee.BottomSheetDialog.CartBottomSheetDialogFragment;
import com.example.hachikocoffee.Fragment.DiscountFragment;
import com.example.hachikocoffee.Fragment.HomeFragment;
import com.example.hachikocoffee.Fragment.OrderFragment;
import com.example.hachikocoffee.Fragment.OtherFragment;
import com.example.hachikocoffee.Fragment.ShopFragment;
import com.example.hachikocoffee.R;
import com.example.hachikocoffee.databinding.ActivityMainBinding;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.ShapeAppearanceModel;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    private boolean shouldShowAppBar = true;
    int curId = R.id.home;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        LayoutInflater inflater = getLayoutInflater();
        setContentView(binding.getRoot());
        replaceFragment(new HomeFragment());
        final int shopId = R.id.shop;
        final int homeId = R.id.home;
        final int orderId = R.id.order;
        final int menuId = R.id.menu;
        final int voucherId = R.id.voucher;

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

        LinearLayout linearLayout = findViewById(R.id.linearLayout);

        ShapeAppearanceModel shapeAppearanceModel = new ShapeAppearanceModel()
                .toBuilder()
                .setAllCornerSizes(50)
                .build();

        MaterialShapeDrawable materialShapeDrawable = new MaterialShapeDrawable(shapeAppearanceModel);
        materialShapeDrawable.setFillColor(ColorStateList.valueOf(Color.WHITE));
        materialShapeDrawable.setStroke(0.5F, Color.parseColor("#CCCCCC"));
        materialShapeDrawable.setElevation(10);

        linearLayout.setBackground(materialShapeDrawable);

        AppCompatButton orderBtn = findViewById(R.id.orderBtn);
        MaterialShapeDrawable materialShapeDrawable1 = new MaterialShapeDrawable(shapeAppearanceModel);
        materialShapeDrawable1.setFillColor(ColorStateList.valueOf(Color.parseColor("#E47905")));

        orderBtn.setBackground(materialShapeDrawable1);

        orderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CartBottomSheetDialogFragment cartBottomSheet = new CartBottomSheetDialogFragment();
                cartBottomSheet.show(getSupportFragmentManager(), "CartBottomSheet");
            }
        });
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