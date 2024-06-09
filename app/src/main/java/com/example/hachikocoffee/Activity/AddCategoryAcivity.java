package com.example.hachikocoffee.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.example.hachikocoffee.Domain.CategoryDomain;
import com.example.hachikocoffee.Listener.OnCategoryChangedListener;
import com.example.hachikocoffee.Management.ListenerSingleton;
import com.example.hachikocoffee.R;
import com.example.hachikocoffee.databinding.ActivityAddCategoryAcivityBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddCategoryAcivity extends AppCompatActivity {
    private OnCategoryChangedListener onCategoryAddedListener;
    ActivityAddCategoryAcivityBinding binding;
    int categoryID = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAddCategoryAcivityBinding.inflate(getLayoutInflater());
        LayoutInflater inflater = getLayoutInflater();
        setContentView(binding.getRoot());

        addValidation();
        onCategoryAddedListener = ListenerSingleton.getInstance().getCategoryChangedListener();

        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("CATEGORY");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    categoryID = Math.toIntExact(snapshot.child("idCount").getValue(Long.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("CATEGORY");
                String categoryName = String.valueOf(binding.editName.getText());
                String categoryImage = String.valueOf(binding.imageURL.getText());


                if (categoryID != -1){
                    CategoryDomain categoryDomain = new CategoryDomain(categoryID, categoryImage, categoryName);
                    myRef.child(String.valueOf(categoryID)).setValue(categoryDomain);
                    myRef.child("idCount").setValue(categoryID + 1);
                    Log.d("abcd", "true" + onCategoryAddedListener);
                    if (onCategoryAddedListener != null) {
                        onCategoryAddedListener.onCategoryChanged();
                    }
                    Toast.makeText(AddCategoryAcivity.this, "Thêm danh mục thành công!", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else{
                    Toast.makeText(AddCategoryAcivity.this,"Lỗi", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    private void addValidation() {
        binding.editName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() == 0){
                    binding.editName.setError("Vui lòng nhập tên danh mục");
                }
                checkAllRequireFields();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.imageURL.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() == 0){
                    binding.imageURL.setError("Vui lòng nhập URL ảnh cho danh mục");
                }
                checkAllRequireFields();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void checkAllRequireFields() {
        if (binding.editName.getText().toString().length() != 0
            && binding.imageURL.getText().toString().length() != 0){
            binding.btnConfirm.setBackground(ContextCompat.getDrawable(this, R.drawable.order_button));
            binding.btnConfirm.setEnabled(true);
        }
        else{
            binding.btnConfirm.setBackground(ContextCompat.getDrawable(this, R.drawable.rounded_rectangle_darkgrey));
            binding.btnConfirm.setEnabled(false);
        }
    }
}