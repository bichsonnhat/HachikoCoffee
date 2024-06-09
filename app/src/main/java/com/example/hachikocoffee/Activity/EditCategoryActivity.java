package com.example.hachikocoffee.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
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
    CategoryDomain category;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityEditCategoryBinding.inflate(getLayoutInflater());
        LayoutInflater inflater = getLayoutInflater();
        setContentView(binding.getRoot());

        addValidation();
        onCategoryAddedListener = ListenerSingleton.getInstance().getCategoryChangedListener();

        category = getIntent().getParcelableExtra("category");
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
                    myRef.child("title").setValue(binding.editName.getText().toString());
                    myRef.child("imageURL").setValue(binding.imageURL.getText().toString());
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
                AlertDialog alertDialog = new AlertDialog.Builder(EditCategoryActivity.this, R.style.AlertDialog_AppCompat_Custom)
                        .setTitle("Xoá danh mục")
                        .setMessage("Xác nhận xoá danh mục này?")
                        .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
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
                        })
                        .setNegativeButton("Không", null)
                        .show();

                Button positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                Button negativeButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                positiveButton.setTextColor(Color.parseColor("#000000"));
                negativeButton.setTextColor(Color.parseColor("#E47905"));
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
                && binding.imageURL.getText().toString().length() != 0
                && (!binding.editName.getText().toString().equals(category.getTitle())) || !binding.imageURL.getText().toString().equals(category.getImageURL())){
            binding.btnConfirm.setBackground(ContextCompat.getDrawable(this, R.drawable.order_button));
            binding.btnConfirm.setEnabled(true);
        }
        else{
            binding.btnConfirm.setBackground(ContextCompat.getDrawable(this, R.drawable.rounded_rectangle_darkgrey));
            binding.btnConfirm.setEnabled(false);
        }
    }
}