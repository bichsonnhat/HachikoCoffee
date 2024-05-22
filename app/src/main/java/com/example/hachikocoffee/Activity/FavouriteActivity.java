package com.example.hachikocoffee.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hachikocoffee.Adapter.FavouriteAdapter;
import com.example.hachikocoffee.Domain.FavouriteItemDomain;
import com.example.hachikocoffee.Domain.ItemsDomain;
import com.example.hachikocoffee.BottomSheetDialog.ProductDetail;
import com.example.hachikocoffee.R;
import com.example.hachikocoffee.Listener.UpdateUIListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FavouriteActivity extends AppCompatActivity implements UpdateUIListener {

    private ArrayList<ItemsDomain> listFavourites;
    private TextView textViewNoFavourites;
    private FavouriteAdapter adapter;
    private ImageView starIcon;
    private RecyclerView recyclerViewFavourites;
    private int previousItemCount = 0;
    private int UserID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);
        SharedPreferences perf = getSharedPreferences("User", Context.MODE_PRIVATE);
        UserID = perf.getInt("UserID", 1);
        ImageView backButton = findViewById(R.id.back_button);
        textViewNoFavourites = findViewById(R.id.tv_no_favourites);
        recyclerViewFavourites = findViewById(R.id.recyclerView_Favourite);
        starIcon = findViewById(R.id.starIcon);

        listFavourites = new ArrayList<>();
        recyclerViewFavourites.setLayoutManager(new GridLayoutManager(this, UserID));
        //updateUI(previousItemCount);


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference favoriteProductsRef = database.getReference("FAVORITEPRODUCT");
        DatabaseReference productsRef = database.getReference("PRODUCTS");

        favoriteProductsRef.orderByChild("userID").equalTo(1)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot favoriteSnapshot) {
                        listFavourites.clear();

                        // Get productIDs from FAVORITEPRODUCT
                        List<String> productIDs = new ArrayList<>();
                        for (DataSnapshot favoriteProductSnapshot : favoriteSnapshot.getChildren()) {
                            FavouriteItemDomain favouriteItem = favoriteProductSnapshot.getValue(FavouriteItemDomain.class);
                            if (favouriteItem != null) {
                                productIDs.add(favouriteItem.getProductID());
                            }
                        }

                        // Query products from PRODUCTS based on productID list
                        productsRef.orderByChild("ProductID").startAt("").endAt("\uf8ff")
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @SuppressLint("NotifyDataSetChanged")
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot productSnapshot) {
                                        for (DataSnapshot snapshot : productSnapshot.getChildren()) {
                                            ItemsDomain item = snapshot.getValue(ItemsDomain.class);
                                            if (item != null && productIDs.contains(item.getProductID())) {
                                                listFavourites.add(item);
                                            }
                                        }
                                        int currentItemCount = listFavourites.size();
                                        if (currentItemCount != previousItemCount) {
                                            previousItemCount = currentItemCount;
                                            updateUI(currentItemCount);
                                            updateAdapter();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Log.e("FavoriteActivity", "Failed to get products: " + error.getMessage());
                                    }
                                });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("FavoriteActivity", "Failed to get favorite products: " + error.getMessage());
                    }
                });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    @Override
    public void updateUI(int itemCount) {
        if (itemCount > 0) {
            textViewNoFavourites.setVisibility(View.GONE);
            starIcon.setVisibility(View.GONE);
        } else {
            textViewNoFavourites.setVisibility(View.VISIBLE);
            starIcon.setVisibility(View.VISIBLE);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void updateAdapter() {
        if (adapter == null) {
            adapter = new FavouriteAdapter(listFavourites);
            recyclerViewFavourites.setAdapter(adapter);
        }
        else {
            adapter.notifyDataSetChanged();
        }
        adapter.setOnItemClickListener(item -> {
            ProductDetail bottomSheetDialogFragment = new ProductDetail(item, listFavourites, adapter);
            bottomSheetDialogFragment.setUpdateUIListener(FavouriteActivity.this);
            bottomSheetDialogFragment.show(getSupportFragmentManager(), "BottomSheetDialogFragment");
        });
    }
}