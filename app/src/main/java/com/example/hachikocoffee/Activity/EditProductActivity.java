package com.example.hachikocoffee.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.hachikocoffee.Domain.CategoryDomain;
import com.example.hachikocoffee.Domain.ItemsDomain;
import com.example.hachikocoffee.Listener.Callback;
import com.example.hachikocoffee.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditProductActivity extends AppCompatActivity {
    private static Callback callback;
    private EditText editName;
    private EditText editPrice;
    private EditText imageURL;
    private EditText etDescription;
    private ArrayAdapter<String> categoryAdapter;
    private Spinner spCategory;
    private ImageView btnBack;
    private AppCompatButton btnConfirm;
    private String productID;
    private ConstraintLayout constraintLayoutDelete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);
        editName = findViewById(R.id.editName);
        editNameTextChanged();
        editPrice = findViewById(R.id.editPrice);
        editPriceTextChanged();
        imageURL = findViewById(R.id.imageURL);
        imageURLTextChanged();
        etDescription = findViewById(R.id.etDescription);
        etDescriptionTextChanged();
        spCategory = findViewById(R.id.spCategory);
        spCategorySelected();
        initSpinnerCategory();
        btnBack = findViewById(R.id.back_button);
        btnConfirm = findViewById(R.id.btnConfirm);
        btnConfirm.setEnabled(false);
        constraintLayoutDelete = findViewById(R.id.constraintLayoutDelete);
        constraintLayoutDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(EditProductActivity.this, R.style.AlertDialog_AppCompat_Custom)
                        .setTitle("Xoá sản phẩm")
                        .setMessage("Xác nhận xoá sản phẩm này?")
                        .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                DatabaseReference productRef = FirebaseDatabase.getInstance().getReference().child("PRODUCTS");
                                productRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()){
                                            for (DataSnapshot issue : snapshot.getChildren()){
                                                ItemsDomain itemsDomain = issue.getValue(ItemsDomain.class);
                                                if (itemsDomain.getProductID().equals(productID)) {
                                                    issue.getRef().removeValue();
                                                    callback.onCallback();
                                                    Toast.makeText(EditProductActivity.this, "Xoá sản phẩm thành công!", Toast.LENGTH_SHORT).show();
                                                    finish();
                                                    break;
                                                }
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
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
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProduct();
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = getIntent();
        productID = intent.getStringExtra("productID");
        initEditProduct();
    }

    private void spCategorySelected() {
        spCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                checkAllFieldsRequire();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initEditProduct() {
        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference().child("PRODUCTS");
        productRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot issue : snapshot.getChildren()){
                        ItemsDomain itemsDomain = issue.getValue(ItemsDomain.class);
                        if (itemsDomain.getProductID().equals(productID)) {
                            editName.setText(""+itemsDomain.getTitle());
                            editPrice.setText(""+itemsDomain.getPrice());
                            imageURL.setText(""+itemsDomain.getImageURL());
                            etDescription.setText(""+itemsDomain.getDescription());
                            DatabaseReference categoryRef = FirebaseDatabase.getInstance().getReference().child("CATEGORY");
                            categoryRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()){
                                        List<String> categoryList = new ArrayList<>();
                                        int index = 0;
                                        for (DataSnapshot issue : snapshot.getChildren()){
                                            if (!"idCount".equals(issue.getKey())){
                                                CategoryDomain categoryDomain = issue.getValue(CategoryDomain.class);
                                                categoryList.add(categoryDomain.getTitle());
                                            }
                                        }
                                        categoryAdapter = new ArrayAdapter<>(getApplication(), android.R.layout.simple_spinner_item, categoryList);
                                        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        spCategory.setAdapter(categoryAdapter);
                                        for (DataSnapshot issue : snapshot.getChildren()){
                                            if (!"idCount".equals(issue.getKey())) {
                                                CategoryDomain categoryDomain = issue.getValue(CategoryDomain.class);
                                                if (categoryDomain.getCategoryID() == (itemsDomain.getCategoryID())) {
                                                    spCategory.setSelection(categoryAdapter.getPosition(categoryDomain.getTitle()));
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                            break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void updateProduct() {
        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference().child("PRODUCTS");
        productRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot item : snapshot.getChildren()){
                        ItemsDomain itemsDomain = item.getValue(ItemsDomain.class);
                        if (itemsDomain.getProductID().equals(productID)) {
                            DatabaseReference categoryRef = FirebaseDatabase.getInstance().getReference().child("CATEGORY");
                            categoryRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()){
                                        for (DataSnapshot issue : snapshot.getChildren()){
                                            if (!"idCount".equals(issue.getKey())){
                                                CategoryDomain categoryDomain = issue.getValue(CategoryDomain.class);
                                                if (categoryDomain.getTitle().equals(spCategory.getSelectedItem().toString())){
                                                    Map<String, Object> map = new HashMap<>();
                                                    map.put("title", editName.getText().toString());
                                                    map.put("price", Double.parseDouble(editPrice.getText().toString()));
                                                    map.put("imageURL", imageURL.getText().toString());
                                                    map.put("description", etDescription.getText().toString());
                                                    map.put("categoryID", categoryDomain.getCategoryID());
                                                    item.getRef().updateChildren(map);
                                                    Toast.makeText(EditProductActivity.this, "Cập nhật sản phẩm thành công!", Toast.LENGTH_SHORT).show();
                                                    callback.onCallback();
                                                    finish();
                                                }
                                            }
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                            break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initSpinnerCategory() {
        DatabaseReference categoryRef = FirebaseDatabase.getInstance().getReference().child("CATEGORY");
        categoryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    List<String> categoryList = new ArrayList<>();
                    int index = 0;
                    for (DataSnapshot issue : snapshot.getChildren()){
                        if (!"idCount".equals(issue.getKey())){
                            CategoryDomain categoryDomain = issue.getValue(CategoryDomain.class);
                            categoryList.add(categoryDomain.getTitle());
                        }
                    }
                    categoryAdapter = new ArrayAdapter<>(getApplication(), android.R.layout.simple_spinner_item, categoryList);
                    categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spCategory.setAdapter(categoryAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void editNameTextChanged() {
        editName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (editName.getText().toString().length() == 0){
                    editName.setError("Vui lòng nhập tên sản phẩm");
                }
                checkAllFieldsRequire();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void editPriceTextChanged() {
        editPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (editPrice.getText().toString().length() == 0){
                    editPrice.setError("Vui lòng nhập giá sản phẩm");
                }
                checkAllFieldsRequire();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void imageURLTextChanged() {
        imageURL.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (imageURL.getText().toString().length() == 0){
                    imageURL.setError("Vui lòng nhập URL hình ảnh");
                }
                checkAllFieldsRequire();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void etDescriptionTextChanged() {
        etDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etDescription.getText().toString().length() == 0){
                    etDescription.setError("Vui lòng nhập mô tả sản phẩm");
                }
                checkAllFieldsRequire();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void checkAllFieldsRequire() {
        if (editName.getText().toString().length() > 0 && editPrice.getText().toString().length() > 0 && imageURL.getText().toString().length() > 0 && etDescription.getText().toString().length() > 0){
            DatabaseReference productRef = FirebaseDatabase.getInstance().getReference().child("PRODUCTS");
            productRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        for (DataSnapshot issue : snapshot.getChildren()){
                            ItemsDomain itemsDomain = issue.getValue(ItemsDomain.class);
                            if (itemsDomain.getProductID().equals(productID)) {
                                DatabaseReference categoryRef = FirebaseDatabase.getInstance().getReference().child("CATEGORY");
                                categoryRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()) {
                                            for (DataSnapshot issue : snapshot.getChildren()) {
                                                if (!"idCount".equals(issue.getKey())) {
                                                    CategoryDomain categoryDomain = issue.getValue(CategoryDomain.class);
                                                    if (categoryDomain.getTitle().equals(spCategory.getSelectedItem().toString())) {
                                                        if (editName.getText().toString().equals(itemsDomain.getTitle())
                                                                && Double.parseDouble(editPrice.getText().toString()) == itemsDomain.getPrice()
                                                                && imageURL.getText().toString().equals(itemsDomain.getImageURL())
                                                                && etDescription.getText().toString().equals(itemsDomain.getDescription())
                                                                && categoryDomain.getCategoryID() == itemsDomain.getCategoryID()) {
                                                            if (btnConfirm.isEnabled()) {
                                                                btnConfirm.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.rounded_rectangle_darkgrey));
                                                                btnConfirm.setEnabled(false);
                                                            }
                                                        } else {
                                                            btnConfirm.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.background_color));
                                                            btnConfirm.setEnabled(true);
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                                break;
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            btnConfirm.setBackground(ContextCompat.getDrawable(this, R.drawable.background_color));
            btnConfirm.setEnabled(true);
        } else {
            if (btnConfirm.isEnabled()){
                btnConfirm.setBackground(ContextCompat.getDrawable(this, R.drawable.rounded_rectangle_darkgrey));
            }
            btnConfirm.setEnabled(false);
        }
    }

    public static void setEditInterfaceInstance(Context context) {
        callback = (Callback) context;
    }
}