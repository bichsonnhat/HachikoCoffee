package com.example.hachikocoffee.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.hachikocoffee.Fragment.YourDeliveryVoucherFragment;
import com.example.hachikocoffee.Fragment.YourPickupVoucherFragment;

public class YourVoucherViewPagerAdapter extends FragmentStateAdapter {
    public YourVoucherViewPagerAdapter(@NonNull FragmentActivity fa) {
        super(fa);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new YourDeliveryVoucherFragment();
            case 1:
                return new YourPickupVoucherFragment();
            default:
                return new YourDeliveryVoucherFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}

