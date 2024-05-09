package com.example.hachikocoffee;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class OrderHistory extends AppCompatActivity {
    Button btnback;
    TabHost tab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_history);

        btnback = findViewById(R.id.btnback);
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tab = findViewById(R.id.tab);
        tab.setup();
        TabHost.TabSpec spec1, spec2, spec3;

        spec1 = tab.newTabSpec("Đang thực hiện");
        spec1.setContent(R.id.tab1);
        spec1.setIndicator("Đang thực hiện");
        tab.addTab(spec1);

        spec2 = tab.newTabSpec("Đã hoàn tất");
        spec2.setContent(R.id.tab2);
        spec2.setIndicator("Đã hoàn tất");
        tab.addTab(spec2);

        spec3 = tab.newTabSpec("Đã hủy");
        spec3.setContent(R.id.tab3);
        spec3.setIndicator("Đã hủy");
        tab.addTab(spec3);

        tab.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                TabWidget tabWidget = tab.getTabWidget();
                for (int i = 0; i < tabWidget.getChildCount(); i++) {
                    View tabView = tabWidget.getChildAt(i);
                    TextView tv = tabView.findViewById(android.R.id.title);
                    if (tab.getCurrentTab() == i) {
                        tabView.setBackgroundColor(getResources().getColor(R.color.white));
                        tv.setTextColor(getResources().getColor(R.color.orange));
                    } else {
                        tabView.setBackgroundColor(getResources().getColor(R.color.white));
                        tv.setTextColor(getResources().getColor(R.color.black));
                    }
                }
            }
        });
    }
}
