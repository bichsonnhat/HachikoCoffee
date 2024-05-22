package com.example.hachikocoffee.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.hachikocoffee.R;

public class CompanyAddressActivity extends AppCompatActivity {

    private EditText companyAddress;
    private EditText companyFloors;
    private EditText companyGate;
    private EditText companyNote;
    private EditText companyRecipentName;
    private EditText companyRecipentPhone;
    private Button companyFinishedButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_address);
        companyAddress = findViewById(R.id.companyAddress);
        companyFloors = findViewById(R.id.companyFloors);
        companyGate = findViewById(R.id.companyGate);
        companyNote = findViewById(R.id.companyNote);
        companyRecipentName = findViewById(R.id.companyRecipentName);
        companyRecipentPhone = findViewById(R.id.companyRecipentPhone);
        companyFinishedButton = findViewById(R.id.companyFinishBtn);
        Button btnCompanyAddressBack = findViewById(R.id.btnCompanyAddressBack);
        btnCompanyAddressBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        companyAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (companyAddress.length() == 0){
                    companyAddress.setError("Vui lòng nhập địa chỉ!");
                }
                checkAllFieldsFilled();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        companyRecipentName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (companyRecipentName.length() == 0){
                    companyRecipentName.setError("Vui lòng nhập tên người nhận!");
                }
                checkAllFieldsFilled();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        companyRecipentPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (companyRecipentPhone.length() == 0){
                    companyRecipentPhone.setError("Vui lòng nhập số điện thoại người nhận!");
                }
                checkAllFieldsFilled();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        companyFinishedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // save to database
                finish();
            }
        });
    }

    private void checkAllFieldsFilled() {
        if (!TextUtils.isEmpty(companyAddress.getText().toString()) && !TextUtils.isEmpty(companyRecipentName.getText().toString()) && !TextUtils.isEmpty(companyRecipentPhone.getText().toString())){
            companyFinishedButton.setBackground(ContextCompat.getDrawable(this, R.drawable.background_color));
            companyFinishedButton.setEnabled(true);
        } else {
            companyFinishedButton.setBackground(ContextCompat.getDrawable(this, R.drawable.rounded_rectangle_darkgrey));
            companyFinishedButton.setEnabled(false);
        }
    }
}