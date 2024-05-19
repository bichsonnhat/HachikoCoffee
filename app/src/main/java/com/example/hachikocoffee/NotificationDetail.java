package com.example.hachikocoffee;

import androidx.annotation.NonNull;
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
import android.widget.Toast;

import com.example.hachikocoffee.Adapter.NotificationAdapter;
import com.example.hachikocoffee.Domain.NotificationDomain;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

        getListNotification();
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void getListNotification() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rcvNoti.setLayoutManager(layoutManager);
        List<NotificationDomain> list = new ArrayList<>();
        DatabaseReference notificationRef = FirebaseDatabase.getInstance().getReference("NOTIFICATION");
        notificationRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Toast.makeText(NotificationDetail.this, "Notification ??", Toast.LENGTH_SHORT).show();
                    for (DataSnapshot issue : snapshot.getChildren()){
                        NotificationDomain notification = issue.getValue(NotificationDomain.class);
                        list.add(notification);
//                        list.add(new NotificationDomain("Chào bạn mới", "Lần đầu đến với Hachiko, Hachiko mong bạn có thật nhiều niềm vui nhé!", R.drawable.coffee_store, "19/05"));
                    }
                    NotificationAdapter adapter = new NotificationAdapter(list);
                    rcvNoti.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//        list.add(new NotificationDomain("Chào bạn mới", "Lần đầu đến với Hachiko, Hachiko mong bạn có thật nhiều niềm vui nhé!", R.drawable.coffee_store, "19/05"));
    }
}
