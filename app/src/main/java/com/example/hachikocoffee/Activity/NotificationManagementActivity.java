package com.example.hachikocoffee.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hachikocoffee.Adapter.NotificationManagementAdapter;
import com.example.hachikocoffee.Domain.NotificationDomain;
import com.example.hachikocoffee.Listener.Callback;
import com.example.hachikocoffee.R;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import static com.example.hachikocoffee.Activity.AddNotificationActivity.setEditInterfaceInstance;

public class NotificationManagementActivity extends AppCompatActivity implements Callback {
    private ImageView notiMgmt_back;
    private ConstraintLayout notiMgmt_add;
    private RecyclerView notiMgmt_rcv;
    private ArrayList<NotificationDomain> notiMgmt_notiList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_management);

        notiMgmt_back = findViewById(R.id.notiMgmt_back);
        notiMgmt_add = findViewById(R.id.notiMgmt_add);
        notiMgmt_rcv = findViewById(R.id.notiMgmt_rcv);

        notiMgmt_back.setOnClickListener(v -> finish());

        notiMgmt_add.setOnClickListener(v -> {
            setEditInterfaceInstance(NotificationManagementActivity.this);
            Intent intent = new Intent(NotificationManagementActivity.this, AddNotificationActivity.class);
            startActivity(intent);
        });

        initNotification();
    }

    private void initNotification() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        notiMgmt_rcv.setLayoutManager(linearLayoutManager);
        notiMgmt_notiList = new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("NOTIFICATION");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        NotificationDomain notificationDomain = dataSnapshot.getValue(NotificationDomain.class);
                        notiMgmt_notiList.add(notificationDomain);
                    }
                }

                NotificationManagementAdapter notificationManagementAdapter = new NotificationManagementAdapter(notiMgmt_notiList);
                notiMgmt_rcv.setAdapter(notificationManagementAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //
            }
        });
    }

    @Override
    public void onCallback() {
        notiMgmt_notiList.clear();
        initNotification();
    }
}