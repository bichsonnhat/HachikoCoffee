package com.example.hachikocoffee.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.hachikocoffee.Domain.FeedbackDomain;
import com.example.hachikocoffee.InfoAccountLoginActivity;
import com.example.hachikocoffee.Management.ManagementUser;
import com.example.hachikocoffee.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FeedbackActivity extends AppCompatActivity {

    private EditText etFeedback;
    private Button submitFeedback;
    private int UserID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        Button btnFeedbackBack = findViewById(R.id.btnFeedbackBack);
        etFeedback = findViewById(R.id.edtFeedbackContent);
        submitFeedback = findViewById(R.id.sendFeedback);
        submitFeedback.setEnabled(false);
        UserID = ManagementUser.getInstance().getUserId();
        btnFeedbackBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        etFeedback.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etFeedback.getText().toString().trim().length() > 0) {
                    submitFeedback.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.background_color));
                    submitFeedback.setEnabled(true);
                } else {
                    submitFeedback.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.rounded_rectangle_darkgrey));
                    submitFeedback.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        submitFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference feedbackRef = FirebaseDatabase.getInstance().getReference().child("FEEDBACK");
                feedbackRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            int max_id = 0;
                            for (DataSnapshot issue : snapshot.getChildren()){
                                FeedbackDomain feedback = issue.getValue(FeedbackDomain.class);
                                max_id = Math.max(max_id, feedback.getFeedbackID());
                            }
                            max_id += 1;
                            FeedbackDomain feedback = new FeedbackDomain(UserID, max_id, etFeedback.getText().toString());
                            feedbackRef.child(String.valueOf(max_id)).setValue(feedback);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                showSuccessDialog();
            }
        });
    }

    private void showSuccessDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thông báo");
        builder.setMessage("Cảm ơn bạn đã gửi góp ý!");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        AlertDialog alertDialog = builder.show();
    }
}