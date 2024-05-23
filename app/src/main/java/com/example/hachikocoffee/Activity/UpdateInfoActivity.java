package com.example.hachikocoffee.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
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
import android.widget.Toast;

import com.example.hachikocoffee.Domain.UserDomain;
import com.example.hachikocoffee.InfoAccountLoginActivity;
import com.example.hachikocoffee.Management.ManagementUser;
import com.example.hachikocoffee.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    private int UserID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_info);
        UserID = ManagementUser.getInstance().getUserId();
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

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("USER");
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot issue : snapshot.getChildren()){
                        UserDomain user = issue.getValue(UserDomain.class);
                        if (user.getUserID() == UserID){
                            String fullname = user.getName();
                            String[] names = fullname.split(",");
                            String firstName = names[0].trim();
                            String lastName = names[1].trim();
                            etFirstName.setText(firstName);
                            etLastName.setText(lastName);
                            etEmail.setText(user.getEmail());
                            tvCanlendar.setText(user.getBirthday());
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnUpdateAccount = findViewById(R.id.btnUpdateAccount);
        btnUpdateAccount.setEnabled(false);

        btnUpdateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        etFirstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etFirstName.length() == 0){
                    etFirstName.setError("Vui lòng nhập tên!");
                }
                checkAllFieldsFilled();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etLastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etLastName.length() == 0){
                    etLastName.setError("Vui lòng nhập họ!");
                }
                checkAllFieldsFilled();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etEmail.length() == 0){
                    etEmail.setError("Vui lòng nhập email!");
                } else
                if (!isValidEmail(etEmail.getText().toString())){
                    etEmail.setError("Email không hợp lệ!");
                } else {
                    checkAllFieldsFilled();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        spGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                checkAllFieldsFilled();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static boolean isValidEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.matches();
    }

    private void checkAllFieldsFilled() {
        if (!TextUtils.isEmpty(etFirstName.getText().toString()) && !TextUtils.isEmpty(etLastName.getText().toString()) && !TextUtils.isEmpty(tvCanlendar.getText().toString())) {
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("USER");
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        for (DataSnapshot issue : snapshot.getChildren()){
                            UserDomain user = issue.getValue(UserDomain.class);
                            if (user.getUserID() == UserID) {
                                String fullname = user.getName();
                                String[] names = fullname.split(",");
                                String firstName = names[0].trim();
                                String lastName = names[1].trim();
                                if (!firstName.equals(etFirstName.getText().toString())
                                        || !lastName.equals(etLastName.getText().toString())
                                        || !user.getEmail().equals(etEmail.getText().toString())
                                        || !user.getBirthday().equals(tvCanlendar.getText().toString())
                                        || !user.getGender().equals(spGender.getSelectedItem().toString())) {
                                    btnUpdateAccount.setBackground(ContextCompat.getDrawable(UpdateInfoActivity.this, R.drawable.background_color));
                                    btnUpdateAccount.setEnabled(true);
                                } else {
                                    btnUpdateAccount.setBackground(ContextCompat.getDrawable(UpdateInfoActivity.this, R.drawable.rounded_rectangle_darkgrey));
                                    btnUpdateAccount.setEnabled(false);
                                }
                                break;
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else {
//            btnUpdateAccount.setBackground(ContextCompat.getDrawable(UpdateInfoActivity.this, R.drawable.rounded_rectangle_darkgrey));
//            btnUpdateAccount.setEnabled(false);
        }
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
}