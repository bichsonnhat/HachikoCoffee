package com.example.hachikocoffee.Adapter;

import android.util.SparseArray;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.hachikocoffee.Fragment.YourDeliveryVoucherFragment;
import com.example.hachikocoffee.Fragment.YourPickupVoucherFragment;

public class YourVoucherViewPagerAdapter extends FragmentStateAdapter {
    private final SparseArray<Fragment> registeredFragments = new SparseArray<>();
    public YourVoucherViewPagerAdapter(@NonNull FragmentActivity fa) {
        super(fa);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment;
        switch (position) {
            case 0:
                fragment = YourDeliveryVoucherFragment.newInstance("param1", "param2");
                break;
            case 1:
                fragment = YourPickupVoucherFragment.newInstance("param1", "param2");
                break;
            default:
                fragment = YourDeliveryVoucherFragment.newInstance("param1", "param2");
                break;
        }
        registeredFragments.put(position, fragment);
        return fragment;
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    @Nullable
    public Fragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }


}

