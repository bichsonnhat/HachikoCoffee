package com.example.hachikocoffee;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

import com.example.hachikocoffee.Fragment.DiscountFragment;
import com.example.hachikocoffee.Fragment.HomeFragment;
import com.example.hachikocoffee.Fragment.OrderFragment;
import com.example.hachikocoffee.Fragment.OtherFragment;
import com.example.hachikocoffee.Fragment.ShopFragment;
import com.example.hachikocoffee.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(R.layout.login);
        boolean checkLogin = false;
//        LayoutInflater inflater = getLayoutInflater();
//        View anotherView = inflater.inflate(R.layout.login, null);
//        setContentView(anotherView);
        if (checkLogin == true) {
            setContentView(binding.getRoot());
            replaceFragment(new HomeFragment());
            final int shopId = R.id.shop;
            final int homeId = R.id.home;
            final int orderId = R.id.order;
            final int menuId = R.id.menu;
            final int voucherId = R.id.voucher;

            binding.bottomNavigationView.setOnItemSelectedListener(item -> {

                int curId = item.getItemId();

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
        }
    }

    private void replaceFragment(Fragment fragment){

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}