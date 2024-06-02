package com.example.hachikocoffee.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.example.hachikocoffee.Domain.CategoryDomain;
import com.example.hachikocoffee.Listener.OnCategoryChangedListener;
import com.example.hachikocoffee.Management.ListenerSingleton;
import com.example.hachikocoffee.R;
import com.example.hachikocoffee.databinding.ActivityCategoryManagementBinding;
import com.example.hachikocoffee.databinding.ActivityEditCategoryBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditCategoryActivity extends AppCompatActivity {

    private OnCategoryChangedListener onCategoryAddedListener;
    ActivityEditCategoryBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityEditCategoryBinding.inflate(getLayoutInflater());
        LayoutInflater inflater = getLayoutInflater();
        setContentView(binding.getRoot());

        onCategoryAddedListener = ListenerSingleton.getInstance().getCategoryChangedListener();

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

        binding.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (category != null){
                    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("CATEGORY").child(String.valueOf(category.getCategoryID()));
                    myRef.child("Title").setValue(binding.editName.getText().toString());
                    myRef.child("ImageURL").setValue(binding.imageURL.getText().toString());
                    if (onCategoryAddedListener != null) {
                        onCategoryAddedListener.onCategoryChanged();
                    }
                    finish();
                }
                else{
                    Toast.makeText(EditCategoryActivity.this,"Lỗi", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.constraintLayoutDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (category != null){
                    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("CATEGORY").child(String.valueOf(category.getCategoryID()));
                    myRef.removeValue();
                    if (onCategoryAddedListener != null) {
                        onCategoryAddedListener.onCategoryChanged();
                    }
                    finish();
                }
                else{
                    Toast.makeText(EditCategoryActivity.this,"Lỗi", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}