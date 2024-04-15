package com.example.hachikocoffee;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ProductDetail extends AppCompatActivity {
    RecyclerView recyclerView;
    ItemClickListener itemClickListener;
    MainAdapter adapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_detail);
        recyclerView = findViewById(R.id.productRecyclerSize);
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Lớn");
        arrayList.add("Vừa");
        arrayList.add("Nhỏ");
        itemClickListener = new ItemClickListener() {
            @Override
            public void onClick(String s) {
                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
                Toast.makeText(getApplicationContext(), "Selected " + s, Toast.LENGTH_SHORT).show();
            }
        };

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter =  new MainAdapter(arrayList, itemClickListener);
        recyclerView.setAdapter(adapter);
    }
}
