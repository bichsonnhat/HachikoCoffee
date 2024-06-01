package com.example.hachikocoffee.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.example.hachikocoffee.Adapter.CategoryDialogAdapter;
import com.example.hachikocoffee.Adapter.ListCategoryAdapter;
import com.example.hachikocoffee.Domain.CategoryDomain;
import com.example.hachikocoffee.R;
import com.example.hachikocoffee.databinding.ActivityCategoryManagementBinding;
import com.example.hachikocoffee.databinding.ActivityMainBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CategoryManagementActivity extends AppCompatActivity {

    ActivityCategoryManagementBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCategoryManagementBinding.inflate(getLayoutInflater());
        LayoutInflater inflater = getLayoutInflater();
        setContentView(binding.getRoot());
        loadCategoryData();

        binding.constraintLayoutStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoryManagementActivity.this, AddCategoryAcivity.class);
                startActivity(intent);
            }
        });
    }

    private void loadCategoryData() {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("CATEGORY");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    ArrayList<CategoryDomain> items = new ArrayList<>();
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        CategoryDomain category = issue.getValue(CategoryDomain.class);
                        items.add(category);
                    }
                    displayCategoryData(items);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Failed to read value.", error.toException());
                // Notify user about the error
            }
        });
    }

    private void displayCategoryData(ArrayList<CategoryDomain> items) {
        if (!items.isEmpty()) {
            binding.recyclerViewCategory.setLayoutManager(new LinearLayoutManager(this));
            binding.recyclerViewCategory.setAdapter(new ListCategoryAdapter(items));
        }
    }
}