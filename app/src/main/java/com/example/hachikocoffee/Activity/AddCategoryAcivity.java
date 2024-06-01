package com.example.hachikocoffee.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.example.hachikocoffee.R;
import com.example.hachikocoffee.databinding.ActivityAddCategoryAcivityBinding;
import com.example.hachikocoffee.databinding.ActivityCategoryManagementBinding;

public class AddCategoryAcivity extends AppCompatActivity {
    ActivityAddCategoryAcivityBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAddCategoryAcivityBinding.inflate(getLayoutInflater());
        LayoutInflater inflater = getLayoutInflater();
        setContentView(binding.getRoot());

        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}