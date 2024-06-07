package com.example.hachikocoffee;

import static java.security.AccessController.getContext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
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
import com.example.hachikocoffee.Activity.SplashActivity;
import com.example.hachikocoffee.Domain.LocationDomain;
import com.example.hachikocoffee.Domain.UserDomain;
import com.example.hachikocoffee.Domain.UserVoucherDomain;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InfoAccountLoginActivity extends AppCompatActivity {

    private TextView textView;
    private Button btnCalendar, btnCalendar2;
    private Spinner spinnerGender;
    private ArrayAdapter<CharSequence> genderAdapter;

    private TextView btnRegisterAccount;

    private EditText firstName, lastName, email;
    private String phoneNumber;
    private static final String[] gender_options = {"Nam", "Nữ"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        phoneNumber = getIntent().getStringExtra("phoneNumber");
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

        lastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (lastName.length() == 0){
                    lastName.setError("Vui lòng nhập họ!");
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

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (email.length() == 0){
                    email.setError("Vui lòng nhập email!");
                } else if (!isValidEmail(email.getText().toString())){
                    email.setError("Email không hợp lệ!");
                } else {
                    checkAllFieldsFilled();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btnRegisterAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addVoucherForNewUser();
                // Save this information to Firebase
                DatabaseReference locationRef = FirebaseDatabase.getInstance().getReference().child("USER");
                locationRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                            UserDomain userDomain = childSnapshot.getValue(UserDomain.class);
                            if (userDomain.getPhoneNumber().equals(phoneNumber)) {
                                Map<String, Object> updates = new HashMap<>();
                                updates.put("name", firstName.getText().toString() + ", " + lastName.getText().toString());
                                updates.put("email", email.getText().toString());
                                updates.put("birthday", textView.getText().toString());
                                updates.put("gender", spinnerGender.getSelectedItem().toString());
                                childSnapshot.getRef().updateChildren(updates);
                                break;
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.w("TAG", "Error fetching locations: " + error.getMessage());
                    }
                });
//                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(intent);
                showSuccessDialog();
            }
        });
    }

    private void addVoucherForNewUser() {
        DatabaseReference userVoucherRef = FirebaseDatabase.getInstance().getReference("USERVOUCHER");
        userVoucherRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    int max_id = 0;
                    for (DataSnapshot issue : snapshot.getChildren()){
                        UserVoucherDomain userVoucherDomain = issue.getValue(UserVoucherDomain.class);
                        max_id = Math.max(max_id, userVoucherDomain.getUserVoucherID());
                    }
                    UserVoucherDomain voucherFree1 = new UserVoucherDomain(6, Integer.parseInt(phoneNumber), max_id + 1, 0);
                    userVoucherRef.child(String.valueOf(max_id + 1)).setValue(voucherFree1);
                    UserVoucherDomain voucherFree2 = new UserVoucherDomain(7, Integer.parseInt(phoneNumber), max_id + 2, 0);
                    userVoucherRef.child(String.valueOf(max_id + 2)).setValue(voucherFree2);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static boolean isValidEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.matches();
    }


    private void showSuccessDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(InfoAccountLoginActivity.this);
        builder.setTitle("Thông báo");
        builder.setMessage("Bạn đã tạo tài khoản thành công!");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(InfoAccountLoginActivity.this, SplashActivity.class);
                startActivity(intent);
                InfoAccountLoginActivity.this.finish();
            }
        });
        AlertDialog alertDialog = builder.show();
    }

    private void checkAllFieldsFilled() {
        if (!TextUtils.isEmpty(firstName.getText().toString()) && !TextUtils.isEmpty(lastName.getText().toString()) && !TextUtils.isEmpty(email.getText().toString()) && !TextUtils.isEmpty(textView.getText().toString())
                && getAge(Integer.parseInt(textView.getText().toString().split("/")[2]), Integer.parseInt(textView.getText().toString().split("/")[1]), Integer.parseInt(textView.getText().toString().split("/")[0])) >= 18 && isValidEmail(email.getText().toString())){
            btnRegisterAccount.setBackground(ContextCompat.getDrawable(this, R.drawable.background_color));
            btnRegisterAccount.setEnabled(true);
        } else {
            btnRegisterAccount.setBackground(ContextCompat.getDrawable(this, R.drawable.rounded_rectangle_darkgrey));
            btnRegisterAccount.setEnabled(false);
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
                        checkAge(year, monthOfYear, dayOfMonth);
                    }
                }, year, month, dayOfMonth);
        datePickerDialog.show();
    }

    private void checkAge(int year, int monthOfYear, int dayOfMonth) {
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, monthOfYear, dayOfMonth);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }
        if (age < 18) {
            textView.setError("Vui lòng nhập ngày sinh trên 18 tuổi!");
            Toast.makeText(this, "Vui lòng nhập ngày sinh trên 18 tuổi!", Toast.LENGTH_SHORT).show();
        } else {
            textView.setError(null);
            checkAllFieldsFilled();
        }
    }

    private int getAge(int year, int monthOfYear, int dayOfMonth) {
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, monthOfYear, dayOfMonth);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }
        return age;
    }
}