package com.example.hachikocoffee;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.hachikocoffee.Activity.MainActivity;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class InfoAccountLoginActivity extends AppCompatActivity {

    private TextView textView;
    private Button btnCalendar, btnCalendar2;
    private Spinner spinnerGender;
    private ArrayAdapter<CharSequence> genderAdapter;

    private TextView btnRegisterAccount;

    private EditText firstName, lastName, email;
    private static final String[] gender_options = {"Nam", "Nữ"};
    private boolean genderSelected = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_account_login);

        textView = findViewById(R.id.txtview_canlendar);
        btnCalendar = findViewById(R.id.btn_calendar);
        btnCalendar2 = findViewById(R.id.btn_calendar2);
        firstName = findViewById(R.id.firstNameRegister);
        lastName = findViewById(R.id.lastNameRegister);
        email = findViewById(R.id.emailRegister);
        btnRegisterAccount = findViewById(R.id.btnRegisterAccount);
        btnRegisterAccount.setEnabled(false);

        btnCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        btnCalendar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        spinnerGender = findViewById(R.id.spinnerGender);
        createGenderSpinner();

        firstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (firstName.length() == 0){
                    firstName.setError("Vui lòng nhập tên!");
                }
                checkAllFieldsFilled();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        textView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (textView.length() == 0){
                    textView.setError("Vui lòng chọn ngày sinh!");
                }
                checkAllFieldsFilled();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btnRegisterAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Sava this information to Firebase
//                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(intent);
            }
        });
    }

    private void checkAllFieldsFilled() {
        if (!TextUtils.isEmpty(firstName.getText().toString()) && !TextUtils.isEmpty(textView.getText().toString())){
            btnRegisterAccount.setBackground(ContextCompat.getDrawable(this, R.drawable.background_color));
            btnRegisterAccount.setEnabled(true);
        } else {
            btnRegisterAccount.setBackground(ContextCompat.getDrawable(this, R.drawable.rounded_rectangle_darkgrey));
        }
    }

    private void createGenderSpinner() {
        genderAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, gender_options);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(genderAdapter);
    }


    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(InfoAccountLoginActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        textView.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                    }
                }, year, month, dayOfMonth);
        datePickerDialog.show();

    }

}