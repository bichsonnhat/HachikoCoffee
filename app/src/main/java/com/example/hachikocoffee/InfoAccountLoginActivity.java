package com.example.hachikocoffee;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class InfoAccountLoginActivity extends AppCompatActivity {

    private TextView textView;
    private Button btnCalendar, btnCalendar2;
    private Spinner spinnerGender;
    private ArrayAdapter<CharSequence> genderAdapter;

    private static final String[] gender_options = {"Nam", "Nữ"};
    private boolean genderSelected = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_account_login);

        textView = findViewById(R.id.txtview_canlendar);
        btnCalendar = findViewById(R.id.btn_calendar);
        btnCalendar2 = findViewById(R.id.btn_calendar2);

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