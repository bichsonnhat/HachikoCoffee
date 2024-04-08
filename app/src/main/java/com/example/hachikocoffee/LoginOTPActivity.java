package com.example.hachikocoffee;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class LoginOTPActivity extends AppCompatActivity {
    private String phoneNumber;
    public String verificationCode;
    private EditText otpEt1, otpEt2, otpEt3, otpEt4, otpEt5, otpEt6;

    private int selectedETPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_otp);
        verificationCode = ((GetVerificationCode) this.getApplication()).getVerificationCode();        verificationCode = ((GetVerificationCode) this.getApplication()).getVerificationCode();

        otpEt1 = findViewById(R.id.otpET1);
        otpEt2 = findViewById(R.id.otpET2);
        otpEt3 = findViewById(R.id.otpET3);
        otpEt4 = findViewById(R.id.otpET4);
        otpEt5 = findViewById(R.id.otpET5);
        otpEt6 = findViewById(R.id.otpET6);

        otpEt1.addTextChangedListener(textWatcher);
        otpEt2.addTextChangedListener(textWatcher);
        otpEt3.addTextChangedListener(textWatcher);
        otpEt4.addTextChangedListener(textWatcher);
        otpEt5.addTextChangedListener(textWatcher);
        otpEt6.addTextChangedListener(textWatcher);

        showKeyBoard(otpEt1);

        final Button verifyButton = findViewById(R.id.verifyButton);
        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String generateOTP = otpEt1.getText().toString() + otpEt2.getText().toString()
                                         + otpEt3.getText().toString() + otpEt4.getText().toString()
                                         + otpEt5.getText().toString() + otpEt6.getText().toString();
                if (generateOTP.length() == 6) {
                    if (generateOTP.equals(verificationCode)){
                        startActivity(new Intent(LoginOTPActivity.this, MainActivity.class));
                        finish();
                    }
                    // handle your otp verification here
                }
            }
        });
    }

    private void showKeyBoard(EditText otpET) {
        otpET.requestFocus();

        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(otpET, InputMethodManager.SHOW_IMPLICIT);
    }
    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() > 0) {
                if (selectedETPosition == 0){
                    selectedETPosition = 1;
                    showKeyBoard(otpEt2);
                }
                else if (selectedETPosition == 1){
                    selectedETPosition = 2;
                    showKeyBoard(otpEt3);
                }
                else if (selectedETPosition == 2){
                    selectedETPosition = 3;
                    showKeyBoard(otpEt4);
                }
                else if (selectedETPosition == 3){
                    selectedETPosition = 4;
                    showKeyBoard(otpEt5);
                }
                else if (selectedETPosition == 4){
                    selectedETPosition = 5;
                    showKeyBoard(otpEt6);
                }
            }
        }
    };

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DEL){
            if (selectedETPosition == 5){
                selectedETPosition = 4;
                showKeyBoard(otpEt5);
            }
            else if (selectedETPosition == 4){
                selectedETPosition = 3;
                showKeyBoard(otpEt4);
            }
            else if (selectedETPosition == 3){
                selectedETPosition = 2;
                showKeyBoard(otpEt3);
            }
            else if (selectedETPosition == 2){
                selectedETPosition = 1;
                showKeyBoard(otpEt2);
            }
            else if (selectedETPosition == 1){
                selectedETPosition = 0;
                showKeyBoard(otpEt1);
            }
            return true;
        } else {
            return super.onKeyUp(keyCode, event);
        }
    }
}
