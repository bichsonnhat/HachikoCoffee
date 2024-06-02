package com.example.hachikocoffee.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;

import com.example.hachikocoffee.Adapter.ListCategoryAdapter;
import com.example.hachikocoffee.Adapter.ListUserAdapter;
import com.example.hachikocoffee.Domain.CategoryDomain;
import com.example.hachikocoffee.Domain.UserDomain;
import com.example.hachikocoffee.Listener.OnCategoryChangedListener;
import com.example.hachikocoffee.Listener.OnUserChangedListener;
import com.example.hachikocoffee.R;
import com.example.hachikocoffee.databinding.ActivityCategoryManagementBinding;
import com.example.hachikocoffee.databinding.ActivityUserManagementBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserManagementActivity extends AppCompatActivity implements OnUserChangedListener {

    ActivityUserManagementBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityUserManagementBinding.inflate(getLayoutInflater());
        LayoutInflater inflater = getLayoutInflater();
        setContentView(binding.getRoot());
        loadUserData();
    }

    private void loadUserData() {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("USER");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    ArrayList<UserDomain> items = new ArrayList<>();
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        UserDomain category = issue.getValue(UserDomain.class);
                        items.add(category);
                        Log.d("User: ", "true");
                    }
                    displayUserData(items);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Failed to read value.", error.toException());
                // Notify user about the error
            }
        });
    }

    private void displayUserData(ArrayList<UserDomain> items) {
        if (!items.isEmpty()) {
            binding.recyclerViewUser.setLayoutManager(new LinearLayoutManager(this));
            binding.recyclerViewUser.setAdapter(new ListUserAdapter(items));
        }
    }


    @Override
    public void onUserChanged() {
        loadUserData();
    }
}