package com.example.hachikocoffee.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
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
import java.util.List;
import java.util.UUID;

public class AddProductActivity extends AppCompatActivity {
    private static Callback callback;
    private EditText editName;
    private EditText editPrice;
    private EditText imageURL;
    private EditText etDescription;
    private Spinner spCategory;
    private ImageView btnBack;
    private AppCompatButton btnConfirm;
    private ArrayAdapter<String> categoryAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        editName = findViewById(R.id.editName);
        editNameTextChanged();
        editPrice = findViewById(R.id.editPrice);
        editPriceTextChanged();
        imageURL = findViewById(R.id.imageURL);
        imageURLTextChanged();
        etDescription = findViewById(R.id.etDescription);
        etDescriptionTextChanged();
        spCategory = findViewById(R.id.spCategory);
        initSpinnerCategory();
        spCategorySelected();
        btnBack = findViewById(R.id.back_button);
        btnConfirm = findViewById(R.id.btnConfirm);
        btnConfirm.setEnabled(false);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProduct();
//                Toast.makeText(AddProductActivity.this, "Thêm sản phẩm thành công!", Toast.LENGTH_SHORT).show();
                finish();
                // Add product to database
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void addProduct() {
        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference().child("PRODUCTS");
        productRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    UUID uuid = UUID.randomUUID();
//                    this.title = title;
//                    this.price = price;
//                    this.picUrl = picUrl;
//                    this.Description = description;
//                    this.CategoryID = categoryID;
//                    this.itemID = itemID;
                    DatabaseReference categoryRef = FirebaseDatabase.getInstance().getReference().child("CATEGORY");
                    categoryRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()){
                                for (DataSnapshot issue : snapshot.getChildren()){
                                    if (!"idCount".equals(issue.getKey())){
                                        CategoryDomain categoryDomain = issue.getValue(CategoryDomain.class);
                                        if (categoryDomain.getTitle().equals(spCategory.getSelectedItem().toString())){
                                            ItemsDomain itemsDomain = new ItemsDomain(editName.getText().toString(), Double.parseDouble(editPrice.getText().toString()), imageURL.getText().toString(), etDescription.getText().toString(), categoryDomain.getCategoryID(), uuid.toString());
                                            productRef.push().setValue(itemsDomain);
                                            callback.onCallback();
                                        }
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
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

    private void spCategorySelected() {

    }

    private void checkAllFieldsRequire() {
        if (editName.getText().toString().length() > 0 && editPrice.getText().toString().length() > 0 && imageURL.getText().toString().length() > 0 && etDescription.getText().toString().length() > 0){
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