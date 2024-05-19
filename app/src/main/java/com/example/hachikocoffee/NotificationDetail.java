package com.example.hachikocoffee;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.hachikocoffee.Adapter.NotificationAdapter;
import com.example.hachikocoffee.Domain.NotificationDomain;

import java.util.ArrayList;
import java.util.List;

public class NotificationDetail extends AppCompatActivity {
    private Button btnback;
    private RecyclerView rcvNoti;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_detail);

        btnback = findViewById(R.id.btnback);
        rcvNoti = findViewById(R.id.rcv_noti);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rcvNoti.setLayoutManager(layoutManager);
        NotificationAdapter adapter = new NotificationAdapter(getListNotification());
        rcvNoti.setAdapter(adapter);
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private List<NotificationDomain> getListNotification() {
        List<NotificationDomain> list = new ArrayList<>();
        list.add(new NotificationDomain("Chào bạn mới", "Lần đầu đến với Hachiko, Hachiko mong bạn có thật nhiều niềm vui nhé!", R.drawable.coffee_store, "19/05"));
        return list;
    }
}
