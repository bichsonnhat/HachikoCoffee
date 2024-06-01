package com.example.hachikocoffee.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.example.hachikocoffee.Domain.CategoryDomain;
import com.example.hachikocoffee.R;
import com.example.hachikocoffee.databinding.ActivityCategoryManagementBinding;
import com.example.hachikocoffee.databinding.ActivityEditCategoryBinding;

public class EditCategoryActivity extends AppCompatActivity {

    ActivityEditCategoryBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityEditCategoryBinding.inflate(getLayoutInflater());
        LayoutInflater inflater = getLayoutInflater();
        setContentView(binding.getRoot());

        CategoryDomain category = getIntent().getParcelableExtra("category");
        if (category!= null){
            binding.editName.setText(category.getTitle());
            binding.imageURL.setText(category.getImageURL());
        }

        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}