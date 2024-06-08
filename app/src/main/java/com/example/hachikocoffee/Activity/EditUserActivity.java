package com.example.hachikocoffee.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.hachikocoffee.Domain.UserDomain;
import com.example.hachikocoffee.Listener.OnUserChangedListener;
import com.example.hachikocoffee.Management.ListenerSingleton;
import com.example.hachikocoffee.R;
import com.example.hachikocoffee.databinding.ActivityEditUserBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditUserActivity extends AppCompatActivity {

    private OnUserChangedListener onUserChangedListener;
    ActivityEditUserBinding binding;
    UserDomain user;
    private static final String[] gender_options = {"Nam", "Nữ"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityEditUserBinding.inflate(getLayoutInflater());
        LayoutInflater inflater = getLayoutInflater();
        setContentView(binding.getRoot());
        createGenderSpinner();

        addValidation();
        onUserChangedListener = ListenerSingleton.getInstance().getUserChangedListener();

        user = getIntent().getParcelableExtra("userr");
        if (user != null){
            String fullname = user.getName();
            String[] names = fullname.split(",");
            String firstName = names[0].trim();
            String lastName = names[1].trim();
            binding.editFirstName.setText(firstName);
            binding.editLastName.setText(lastName);
            binding.editBirthDay.setText(user.getBirthday());
            binding.editEmail.setText(user.getEmail());
            if (user.getGender().equals("Nam")){
                binding.spGender.setSelection(0);
            }
            else{
                binding.spGender.setSelection(1);
            }
        }

        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.btnCalendar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        binding.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user != null){
                    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("USER").child(String.valueOf(user.getUserID()));
                    myRef.child("name").setValue(binding.editFirstName.getText().toString() + ", " + binding.editLastName.getText().toString());
                    myRef.child("email").setValue(binding.editEmail.getText().toString());
                    myRef.child("gender").setValue(binding.spGender.getSelectedItem().toString());
                    myRef.child("birthday").setValue(binding.editBirthDay.getText().toString());
                    if (onUserChangedListener != null){
                        onUserChangedListener.onUserChanged();
                    }
                    finish();
                }
                else{
                    Toast.makeText(EditUserActivity.this,"Lỗi", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.constraintLayoutDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user != null){
                    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("USER").child(String.valueOf(user.getUserID()));
                    myRef.removeValue();
                    if (onUserChangedListener != null) {
                        onUserChangedListener.onUserChanged();
                    }
                    finish();
                }
                else{
                    Toast.makeText(EditUserActivity.this,"Lỗi", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void addValidation() {
        binding.editFirstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() == 0){
                    binding.editFirstName.setError("Vui lòng nhập tên");
                }
                checkAllRequireFields();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.editLastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() == 0){
                    binding.editLastName.setError("Vui lòng nhập họ");
                }
                checkAllRequireFields();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.editEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() == 0){
                    binding.editEmail.setError("Vui lòng nhập email");
                }
                else if (!isValidEmail(s.toString())){
                    binding.editEmail.setError("Email không hợp lệ!");
                }
                checkAllRequireFields();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.spGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                checkAllRequireFields();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(EditUserActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar selectedDate = Calendar.getInstance();
                        selectedDate.set(year, monthOfYear, dayOfMonth);

                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                        String formattedDate = dateFormat.format(selectedDate.getTime());

                        binding.editBirthDay.setText(formattedDate);
                        checkAge(year, monthOfYear, dayOfMonth);
                    }
                }, year, month, dayOfMonth);
        datePickerDialog.show();
    }

    private void createGenderSpinner() {
        ArrayAdapter<CharSequence> genderAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, gender_options);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spGender.setAdapter(genderAdapter);
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
            binding.editBirthDay.setError("Vui lòng nhập ngày sinh trên 18 tuổi!");
            Toast.makeText(this, "Vui lòng nhập ngày sinh trên 18 tuổi!", Toast.LENGTH_SHORT).show();
            binding.btnConfirm.setBackground(ContextCompat.getDrawable(this, R.drawable.rounded_rectangle_darkgrey));
            binding.btnConfirm.setEnabled(false);
        } else {
            binding.editBirthDay.setError(null);
            checkAllRequireFields();
        }
    }

    private void checkAllRequireFields() {
        String fullName = binding.editFirstName.getText().toString() + ", " + binding.editLastName.getText().toString();
        if (binding.editFirstName.getText().toString().length() != 0
                && binding.editEmail.getText().toString().length() != 0
                && (!fullName.equals(user.getName()))
                    || !binding.editEmail.getText().toString().equals(user.getEmail())
                    || !binding.editBirthDay.getText().toString().equals(user.getBirthday())
                    || !binding.spGender.getSelectedItem().toString().equals(user.getGender())){
            binding.btnConfirm.setBackground(ContextCompat.getDrawable(this, R.drawable.order_button));
            binding.btnConfirm.setEnabled(true);
        }
        else{
            binding.btnConfirm.setBackground(ContextCompat.getDrawable(this, R.drawable.rounded_rectangle_darkgrey));
            binding.btnConfirm.setEnabled(false);
        }
    }

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static boolean isValidEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.matches();
    }
}