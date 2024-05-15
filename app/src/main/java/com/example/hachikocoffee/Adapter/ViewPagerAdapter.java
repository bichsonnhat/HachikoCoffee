package com.example.hachikocoffee.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.hachikocoffee.Fragment.OrderHistoryCancelledFragment;
import com.example.hachikocoffee.Fragment.OrderHistoryFinishedFragment;
import com.example.hachikocoffee.Fragment.OrderHistoryProcessingFragment;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new OrderHistoryProcessingFragment();
            case 1:
                return new OrderHistoryFinishedFragment();
            case 2:
                return new OrderHistoryCancelledFragment();
            default:
                return new OrderHistoryProcessingFragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        switch (position) {
            case 0:
                title = "Đang thực hiện";
                break;
            case 1:
                title = "Đã hoàn tất";
                break;
            case 2:
                title = "Đã hủy";
                break;
        }
        return title;
    }
}
