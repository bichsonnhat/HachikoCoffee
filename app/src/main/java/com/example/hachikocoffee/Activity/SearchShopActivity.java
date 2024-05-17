package com.example.hachikocoffee.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hachikocoffee.Adapter.ListHeaderItemAdapter;
import com.example.hachikocoffee.Adapter.ShopAdapter;
import com.example.hachikocoffee.BottomSheetDialog.ShopDetail;
import com.example.hachikocoffee.Domain.ItemsDomain;
import com.example.hachikocoffee.Domain.LocationDomain;
import com.example.hachikocoffee.Domain.ShopDomain;
import com.example.hachikocoffee.Fragment.ShopFragment;
import com.example.hachikocoffee.Listener.ShopClickListener;
import com.example.hachikocoffee.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SearchShopActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private SearchView searchView;
    public GridLayoutManager gridLayoutManager;

    private TextView cancelSearch;
    private ShopAdapter shopAdapter;
    LocationManager locationManager;
    private int UserID = 1;
    String findText;
    private double locationX = 0;
    private double locationY = 0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_shop);
        searchView = findViewById(R.id.SearchViewShop);
        cancelSearch = findViewById(R.id.cancelSearchShop);
        cancelSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
    }

    private void filterList(String newText) {
        recyclerView = findViewById(R.id.recyclerShopSearch);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        ArrayList<ShopDomain> filteredList = new ArrayList<>();
        DatabaseReference locationRef = FirebaseDatabase.getInstance().getReference("LOCATION");
        locationRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        LocationDomain location = issue.getValue(LocationDomain.class);
                        if (location.getUserID() == UserID) {
                            locationX = location.getLocationX();
                            locationY = location.getLocationY();
                            break;
                        }
                    }
                    DatabaseReference shopRef = FirebaseDatabase.getInstance().getReference("STORE");
                    shopRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()){
                                for (DataSnapshot issue : snapshot.getChildren()) {
                                    ShopDomain shop = issue.getValue(ShopDomain.class);
                                    String coorString = shop.getCoordinate();
                                    String valuesString = coorString.substring(6, coorString.length() - 1);
                                    String[] values = valuesString.split("\\s+");
                                    Double coorX = Double.parseDouble(values[0]);
                                    Double coorY = Double.parseDouble(values[1]);
                                    Double distance = Math.sqrt((locationX - coorX) * (locationY - coorX) + (locationY - coorY) * (locationY - coorY));
                                    String result = String.format("%.1f", distance);
                                    shop.setCoordinate("Cách đây " + result + " km");
                                    Toast.makeText(SearchShopActivity.this, ""+locationY + " " + locationY, Toast.LENGTH_SHORT).show();
                                    if (shop.getAddress().toLowerCase().contains(newText.toLowerCase())){
                                        filteredList.add(shop);
                                    }
                                }
                                if (newText.isEmpty()){
                                    filteredList.clear();
                                }
                                displayShopData(filteredList);
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
                Toast.makeText(SearchShopActivity.this, "Error: " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayShopData(ArrayList<ShopDomain> shopList) {
        if (!shopList.isEmpty()) {
            shopAdapter = new ShopAdapter(shopList, new ShopClickListener() {
                @Override
                public void onClickShopItem(ShopDomain shop) {
                    onClickToShopDetailFunc(shop);
                }
            });
            recyclerView.setAdapter(shopAdapter);
        }
    }

    private void onClickToShopDetailFunc(ShopDomain shop) {
        ShopDetail shopDetail = ShopDetail.newInstance(shop);
        shopDetail.show(getSupportFragmentManager(), shopDetail.getTag());
    }
}
