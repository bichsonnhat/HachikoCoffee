package com.example.hachikocoffee;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.example.hachikocoffee.Adapter.YourVoucherViewPagerAdapter;
import com.example.hachikocoffee.Fragment.OrderFragment;
import com.example.hachikocoffee.Fragment.YourDeliveryVoucherFragment;
import com.example.hachikocoffee.Fragment.YourPickupVoucherFragment;
import com.example.hachikocoffee.databinding.YourVoucherCustomtabBinding;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class YourVoucher extends AppCompatActivity {
    Button btnback;
    TabLayout tl;
    ViewPager2 vp2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.your_voucher);

        btnback = findViewById(R.id.btnback);
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tl = findViewById(R.id.tab_layout);
        vp2 = findViewById(R.id.view_pager);

        YourVoucherViewPagerAdapter adapter = new YourVoucherViewPagerAdapter(this);
        vp2.setAdapter(adapter);

        TabLayoutMediator tlm = new TabLayoutMediator(tl, vp2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position){
                    case 0:{
                        //YourDeliveryVoucherFragment fragment1 = (YourDeliveryVoucherFragment) adapter.getRegisteredFragment(position);
                        //if (fragment1 != null) {
                        //    int recyclerViewSize1 = fragment1.getRecyclerViewSize_delivery2();
                        tab.setText("Giao hàng");
                        BadgeDrawable bd = tab.getOrCreateBadge();
                        bd.setBackgroundColor(getResources().getColor(R.color.orange));
                        bd.setVisible(false);
                        //    bd.setNumber(recyclerViewSize1);
                        //}
                        break;
                    }
                    case 1:{
                        //YourPickupVoucherFragment fragment2 = (YourPickupVoucherFragment) adapter.getRegisteredFragment(position);
                        //if (fragment2 != null) {
                        //    int recyclerViewSize2 = fragment2.getRecyclerViewSize_pickup2();
                        tab.setText("Mang đi");
                        BadgeDrawable bd = tab.getOrCreateBadge();
                        bd.setBackgroundColor(getResources().getColor(R.color.orange));
                        bd.setVisible(false);
                        //    bd.setNumber(recyclerViewSize2);
                        //}
                        break;
                    }
                }
            }
        });tlm.attach();
    }

    public void updateTab(int position, String text, int number) {
        TabLayout.Tab tab = tl.getTabAt(position);
        if (tab != null) {
            //YourVoucherCustomtabBinding binding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.your_voucher_customtab, tl, false);
            //binding.setBadgeNumber(number);
            //TextView tabText = binding.getRoot().findViewById(R.id.tabTitle);
            //tabText.setText(text);
            //tab.setCustomView(binding.getRoot());

            tab.setText(text);
            BadgeDrawable bd = tab.getOrCreateBadge();
            bd.setBackgroundColor(getResources().getColor(R.color.orange));
            bd.setVisible(true);
            bd.setNumber(number);
            //tab.setCustomView(R.layout.your_voucher_customtab);
        }
    }

    public void navigateToOrderFragment() {
        OrderFragment orderFragment = new OrderFragment();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, orderFragment);
        fragmentTransaction.commit();
    }

    /* Crash reason
    *  The error message indicates that the OrderFragment is trying to replace a view with the id frame_layout, but this view cannot be found in the current layout.
    *  This is causing the app to crash.
    *  The navigateToOrderFragment() method in YourVoucher.java is trying to replace the frame_layout view with an instance of OrderFragment.
    *  However, the frame_layout view is not present in the layout file your_voucher.xml which is associated with YourVoucher activity.
    *  To fix this issue, you need to ensure that the frame_layout view is present in the layout file associated with the activity
       where you are trying to replace it with the OrderFragment.
    */
}

