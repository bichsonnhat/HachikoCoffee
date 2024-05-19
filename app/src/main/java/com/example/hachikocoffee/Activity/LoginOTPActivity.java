package com.example.hachikocoffee.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hachikocoffee.Domain.ShopDomain;
import com.example.hachikocoffee.Domain.UserDomain;
import com.example.hachikocoffee.InfoAccountLoginActivity;
import com.example.hachikocoffee.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginOTPActivity extends AppCompatActivity {
    private TextView textMobile;
    private String verificationId;
    private EditText inputCode1, inputCode2, inputCode3, inputCode4, inputCode5, inputCode6;

    private int selectedETPosition = 0;
    SharedPreferences.Editor editor;
    SharedPreferences perf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_otp);
        textMobile = findViewById(R.id.textMobile);
        textMobile.setText(String.format(
                "%s", getIntent().getStringExtra("mobile")
        ));
        inputCode1 = findViewById(R.id.otpET1);
        inputCode2 = findViewById(R.id.otpET2);
        inputCode3 = findViewById(R.id.otpET3);
        inputCode4 = findViewById(R.id.otpET4);
        inputCode5 = findViewById(R.id.otpET5);
        inputCode6 = findViewById(R.id.otpET6);
        perf = getSharedPreferences("User", MODE_PRIVATE);
        editor = perf.edit();
        setupOTPInputs();

        final Button buttonVerify = findViewById(R.id.verifyButton);
        verificationId = getIntent().getStringExtra("verificationId");

        buttonVerify.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (inputCode1.getText().toString().trim().isEmpty()
                || inputCode2.getText().toString().trim().isEmpty()
                || inputCode3.getText().toString().trim().isEmpty()
                || inputCode4.getText().toString().trim().isEmpty()
                || inputCode5.getText().toString().trim().isEmpty()
                || inputCode6.getText().toString().trim().isEmpty()){
                    Toast.makeText(LoginOTPActivity.this, "Please enter valid code", Toast.LENGTH_SHORT).show();
                    return;
                }
                String code = inputCode1.getText().toString() +
                        inputCode2.getText().toString() +
                        inputCode3.getText().toString() +
                        inputCode4.getText().toString() +
                        inputCode5.getText().toString() +
                        inputCode6.getText().toString();
                if (verificationId != null) {
                    PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(
                            verificationId,
                            code
                    );
                    FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        editor.putInt("UserID", Integer.valueOf(getIntent().getStringExtra("mobile")));
                                        editor.putBoolean("LoggedIn", true);
                                        editor.apply();
                                        // Check account is existed
                                        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("USER");
                                        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if (snapshot.exists()){
                                                    boolean isExist = false;
                                                    for (DataSnapshot issue : snapshot.getChildren()) {
                                                        UserDomain user = issue.getValue(UserDomain.class);
                                                        if (user.getPhoneNumber().equals(getIntent().getStringExtra("mobile"))) {
                                                            isExist = true;
                                                            break;
                                                        }
                                                    }
                                                    if (isExist) {
                                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                                        intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                        startActivity(intent);
                                                    } else {
                                                        // Create new record for USER here ...
                                                        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("USER");
                                                        String phoneNumber = getIntent().getStringExtra("mobile");
                                                        UserDomain userDomain = new UserDomain(Integer.valueOf(phoneNumber), phoneNumber, "", "", "");
                                                        userRef.push().setValue(userDomain);
                                                        Intent intent = new Intent(getApplicationContext(), InfoAccountLoginActivity.class);
                                                        intent.putExtra("phoneNumber", phoneNumber);
                                                        intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                        startActivity(intent);
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    } else {
                                        Toast.makeText(LoginOTPActivity.this, "The verification code entered was invalid", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }

    private void setupOTPInputs() {
        inputCode1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    inputCode2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        inputCode2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    inputCode3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        inputCode3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    inputCode4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        inputCode4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    inputCode5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        inputCode5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    inputCode6.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}
