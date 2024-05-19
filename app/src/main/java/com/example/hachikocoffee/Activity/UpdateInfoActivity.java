package com.example.hachikocoffee.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.hachikocoffee.InfoAccountLoginActivity;
import com.example.hachikocoffee.R;

import java.util.Calendar;

public class UpdateInfoActivity extends AppCompatActivity {

    private TextView tvCanlendar;
    private Button btnCalendar, btnCalendar2;
    private Spinner spGender;
    private ArrayAdapter<CharSequence> genderAdapter;

    private TextView btnUpdateAccount;

    private EditText etFirstName, etLastName, etEmail;
    private static final String[] gender_options = {"Nam", "Nữ"};

    private static final String initialFirstName ="Linh";
    private static final String initialLastName ="Bao";
    private static final String initialEmail ="baolinh02112004@gmai.com";
    private static final String initialCanlendar ="02/11/2004";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_info);

        spGender = findViewById(R.id.spGender);
        createGenderSpinner();

        Button btnUpdateInfoBack = findViewById(R.id.btnUpdateInfoBack);
        btnUpdateInfoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        tvCanlendar = findViewById(R.id.tvCanlendar);
        btnCalendar = findViewById(R.id.btnCalendar);
        btnCalendar2 = findViewById(R.id.btnCalendar2);

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


        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etEmail = findViewById(R.id.etEmail);

        btnUpdateAccount = findViewById(R.id.btnUpdateAccount);
        btnUpdateAccount.setEnabled(false);

        watchFields();

        btnUpdateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

    }

    private void createGenderSpinner() {
        genderAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, gender_options);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spGender.setAdapter(genderAdapter);
    }

    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(UpdateInfoActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        tvCanlendar.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                    }
                }, year, month, dayOfMonth);
        datePickerDialog.show();
    }

    private void watchFields() {
        etFirstName.addTextChangedListener(textWatcher);
        etLastName.addTextChangedListener(textWatcher);
        etEmail.addTextChangedListener(textWatcher);
        tvCanlendar.addTextChangedListener(textWatcher);
    }

    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String firstName = etFirstName.getText().toString();
            String lastName = etLastName.getText().toString();
            String email = etEmail.getText().toString();
            String canlendar = tvCanlendar.getText().toString();
            boolean isFirstNameChanged = !firstName.equals(initialFirstName);
            boolean isLastNameChanged = !lastName.equals(initialLastName);
            boolean isEmailChanged = !email.equals(initialEmail);
            boolean isCanlendarChanged = !canlendar.equals(initialCanlendar);

            if(isFirstNameChanged || isLastNameChanged || isEmailChanged || isCanlendarChanged) {
                btnUpdateAccount.setBackground(ContextCompat.getDrawable(UpdateInfoActivity.this, R.drawable.background_color));
                btnUpdateAccount.setEnabled(true);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
}