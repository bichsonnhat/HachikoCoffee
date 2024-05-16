package com.example.hachikocoffee;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.example.hachikocoffee.Adapter.YourVoucherViewPagerAdapter;
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

        vp2.setAdapter(new YourVoucherViewPagerAdapter(this));

        TabLayoutMediator tlm = new TabLayoutMediator(tl, vp2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position){
                    case 0:{
                        tab.setText("Giao hàng      ");
                        BadgeDrawable bd = tab.getOrCreateBadge();
                        bd.setBackgroundColor(getResources().getColor(R.color.orange));
                        bd.setVisible(true);
                        bd.setNumber(9);
                        break;
                    }
                    case 1:{
                        tab.setText("Mang đi      ");
                        BadgeDrawable bd = tab.getOrCreateBadge();
                        bd.setBackgroundColor(getResources().getColor(R.color.orange));
                        bd.setVisible(true);
                        bd.setNumber(2);
                        break;
                    }
                }
            }
        });tlm.attach();
    }
}

