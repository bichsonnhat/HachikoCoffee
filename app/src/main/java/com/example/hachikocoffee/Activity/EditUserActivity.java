package com.example.hachikocoffee.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;

import com.example.hachikocoffee.Domain.UserDomain;
import com.example.hachikocoffee.databinding.ActivityEditUserBinding;

public class EditUserActivity extends AppCompatActivity {

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

        user = getIntent().getParcelableExtra("userr");
        if (user != null){
            binding.editName.setText(user.getName());
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
    }

    private void createGenderSpinner() {
        ArrayAdapter<CharSequence> genderAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, gender_options);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spGender.setAdapter(genderAdapter);
    }
}