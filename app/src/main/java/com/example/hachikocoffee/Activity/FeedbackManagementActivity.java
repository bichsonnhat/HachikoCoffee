package com.example.hachikocoffee.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hachikocoffee.Adapter.EditProductAdapter;
import com.example.hachikocoffee.Adapter.FeedbackAdapter;
import com.example.hachikocoffee.Domain.FeedbackDomain;
import com.example.hachikocoffee.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FeedbackManagementActivity extends AppCompatActivity {
    private RecyclerView recyclerViewFeedback;
    private ArrayList<FeedbackDomain> feedbackDomainArrayList;
    private ImageView back_button;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_management);
        recyclerViewFeedback = findViewById(R.id.recyclerViewFeedback);
        back_button = findViewById(R.id.back_button);

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initFeedback();
    }

    private void initFeedback() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerViewFeedback.setLayoutManager(linearLayoutManager);
        feedbackDomainArrayList = new ArrayList<>();
        DatabaseReference feedbackRef = FirebaseDatabase.getInstance().getReference().child("FEEDBACK");
        feedbackRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        FeedbackDomain feedback = issue.getValue(FeedbackDomain.class);
                        feedbackDomainArrayList.add(feedback);
                    }
                }
                FeedbackAdapter feedbackAdapter = new FeedbackAdapter(feedbackDomainArrayList);
                recyclerViewFeedback.setAdapter(feedbackAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
