package com.example.hachikocoffee.Activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hachikocoffee.R;
import com.example.hachikocoffee.databinding.ActivityCartBinding;

public class PlaceOrderActivity extends AppCompatActivity {
    private ActivityCartBinding binding;
    private RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}
