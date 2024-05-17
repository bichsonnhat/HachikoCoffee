package com.example.hachikocoffee.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hachikocoffee.Adapter.ListHeaderItemAdapter;
import com.example.hachikocoffee.Domain.CategoryDomain;
import com.example.hachikocoffee.Domain.ItemsDomain;
import com.example.hachikocoffee.Fragment.OrderFragment;
import com.example.hachikocoffee.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchItemActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private SearchView searchView;
    public GridLayoutManager gridLayoutManager;

    private TextView cancelSearch;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_item);

        searchView = findViewById(R.id.SearchViewItem);
        cancelSearch = findViewById(R.id.cancelSearch);
        cancelSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return false;
            }
        });
        recyclerView = findViewById(R.id.recyclerItemSearch);
    }

    private void filterList(String newText) {
        ArrayList<Object> filteredList = new ArrayList<>();
        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference("PRODUCTS");
        productRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        ItemsDomain product = issue.getValue(ItemsDomain.class);
                        if (product.getTitle().toLowerCase().contains(newText.toLowerCase())){
                            filteredList.add(product);
                        }
                    }
                    if (newText.isEmpty()){
                        filteredList.clear();
                    }
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    int spanCount = 1;
                    gridLayoutManager = new GridLayoutManager(getApplicationContext(),spanCount);
                    GridLayoutManager.SpanSizeLookup sizeLookup = new GridLayoutManager.SpanSizeLookup() {
                        @Override
                        public int getSpanSize(int position) {
                            return filteredList.get(position) instanceof String ? spanCount : 1;
                        }
                    };
                    sizeLookup.setSpanGroupIndexCacheEnabled(true);
                    sizeLookup.setSpanIndexCacheEnabled(true);
                    gridLayoutManager.setSpanSizeLookup(sizeLookup);
                    recyclerView.setLayoutManager(gridLayoutManager);

                    recyclerView.setAdapter(new ListHeaderItemAdapter(filteredList));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}