package com.example.hachikocoffee.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.hachikocoffee.Fragment.OtherFragment;
import com.example.hachikocoffee.R;

import java.time.Instant;

public class ContactFeedbackActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_feedback);

        Button btnContactBack = findViewById(R.id.btnContactBack);
        btnContactBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Button btnFeedback = findViewById(R.id.btnFeedback);
        btnFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContactFeedbackActivity.this, FeedbackActivity.class);
                startActivity(intent);
            }
        });
    }

}