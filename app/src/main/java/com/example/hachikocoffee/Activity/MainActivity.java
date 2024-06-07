package com.example.hachikocoffee.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.example.hachikocoffee.BottomSheetDialog.CartBottomSheetDialogFragment;
import com.example.hachikocoffee.Domain.LocationDomain;
import com.example.hachikocoffee.Domain.ShopDomain;
import com.example.hachikocoffee.Management.ManagementCart;
import com.example.hachikocoffee.Fragment.DiscountFragment;
import com.example.hachikocoffee.Fragment.HomeFragment;
import com.example.hachikocoffee.Fragment.OrderFragment;
import com.example.hachikocoffee.Fragment.OtherFragment;
import com.example.hachikocoffee.Fragment.ShopFragment;
import com.example.hachikocoffee.Listener.OnCartChangedListener;
import com.example.hachikocoffee.Management.ManagementMinDistance;
import com.example.hachikocoffee.Management.ManagementUser;
import com.example.hachikocoffee.R;
import com.example.hachikocoffee.databinding.ActivityMainBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.ShapeAppearanceModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements LocationListener {

    ActivityMainBinding binding;
    int UserID;
    private boolean shouldShowAppBar = true;
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private double locationX = 0, locationY = 0;
    int curId = R.id.home;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        LayoutInflater inflater = getLayoutInflater();
        setContentView(binding.getRoot());

        UserID = ManagementUser.getInstance().getUserId();

        replaceFragment(new HomeFragment());
        setBackground();
        updateOrderButtonVisibility();
        binding.itemsCount.setText(""+ ManagementCart.getInstance().getItemsCount());

        //format money
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        String a = new DecimalFormat("#,###", symbols).format(ManagementCart.getInstance().getTotalCost());
        binding.totalCost.setText(a + "đ");


        ManagementCart.getInstance().addOnCartChangedListener(new OnCartChangedListener() {
            @Override
            public void onCartChanged() {
                updateOrderButtonVisibility();
            }
        });

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {

            if (curId == item.getItemId())
                return false;
            curId = item.getItemId();
            if (curId == R.id.home) {
                replaceFragment(new HomeFragment());
            } else if (curId == R.id.order) {
                replaceFragment(new OrderFragment());
            } else if (curId == R.id.shop) {
                replaceFragment(new ShopFragment());
            } else if (curId == R.id.voucher) {
                replaceFragment(new DiscountFragment());
            } else if (curId == R.id.menu) {
                replaceFragment(new OtherFragment());
            }

            return true;
        });

        binding.orderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CartBottomSheetDialogFragment cartBottomSheet = new CartBottomSheetDialogFragment();
                cartBottomSheet.show(getSupportFragmentManager(), "CartBottomSheet");
            }
        });

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        binding.appBarLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            REQUEST_LOCATION_PERMISSION);
                } else {
                    getLastLocation();
                }
            }
        });

        binding.appBarLayout.performClick();

        DatabaseReference locationRef = FirebaseDatabase.getInstance().getReference("LOCATION");
        DatabaseReference locationUserRef = locationRef.child(String.valueOf(UserID));
//        locationUserRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()){
//                    if (snapshot.hasChild("address")){
//                        String curAddress = snapshot.child("address").getValue(String.class);
//                        binding.location.setText(curAddress);
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }

    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(MainActivity.this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            double latitude = location.getLatitude();
                            double longitude = location.getLongitude();
                            String myAddress = null;
                            Log.d("LocationXY", "Latitude: " + latitude + ", Longitude: " + longitude);
                            try {
                                Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                                List<Address> address = geocoder.getFromLocation(latitude, longitude, 1);
                                myAddress = address.get(0).getAddressLine(0);
                                binding.location.setText(myAddress);
                                Log.d("Address", myAddress);
                            } catch (Exception e){
                                e.printStackTrace();
                                Log.e("Loi", e.toString());
                            }

                            String finalMyAddress = myAddress;

                            ManagementCart.getInstance().setLocation(finalMyAddress);
                            ManagementCart.getInstance().setSubLocation("");
                            binding.location.setText(finalMyAddress);
                            DatabaseReference locationRef = FirebaseDatabase.getInstance().getReference().child("LOCATION");
                            LocationDomain locationDomain = new LocationDomain(UserID, latitude, longitude, finalMyAddress);
                            locationRef.child(String.valueOf(UserID)).setValue(locationDomain);
                        }
                    }
                });
        getDistance();
    }

    private void getDistance(){
        DatabaseReference locationRef = FirebaseDatabase.getInstance().getReference("LOCATION");
        DatabaseReference locationUserRef = locationRef.child(String.valueOf(UserID));
        locationUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    LocationDomain locationDomain = snapshot.getValue(LocationDomain.class);
                    assert locationDomain != null;
                    locationX = locationDomain.getLocationX();
                    locationY = locationDomain.getLocationY();
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
                                    Double coorY = Double.parseDouble(values[0]);
                                    Double coorX = Double.parseDouble(values[1]);

                                    Double deltaLat = locationX - coorX;
                                    Double deltaLon = locationY - coorY;

                                    Double deltaLatKm = deltaLat * 111.0;

                                    Double latAvg = (locationX + coorX) / 2.0;
                                    Double cosLatAvg = Math.cos(Math.toRadians(latAvg));
                                    Double deltaLonKm = deltaLon * 111.0 * cosLatAvg;

                                    Double distance = Math.sqrt(Math.pow(deltaLatKm, 2) + Math.pow(deltaLonKm, 2));

                                    if (distance < ManagementMinDistance.getInstance().getMinDistance()){
                                        ManagementMinDistance.getInstance().setMinDistance(distance);
                                        ManagementCart.getInstance().setStoreId(shop.getStoreID());
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

    @SuppressLint("SetTextI18n")
    private void updateOrderButtonVisibility() {
        if (ManagementCart.getInstance().getCartItems().size() > 0) {
            binding.orderBtn.setVisibility(View.VISIBLE);
            Log.d("MainActivity", "orderBtn: " + ManagementCart.getInstance().getCartItems().size());
            Log.d("MainActivity", "orderBtn: " + "true");
        } else {
            Log.d("MainActivity", "orderBtn: " + ManagementCart.getInstance().getCartItems().size());
            Log.d("MainActivity", "orderBtn: " + "false");
            binding.orderBtn.setVisibility(View.GONE);
        }

        binding.itemsCount.setText(""+ ManagementCart.getInstance().getItemsCount());

        //format money
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        String a = new DecimalFormat("#,###", symbols).format(ManagementCart.getInstance().getTotalCost());
        binding.totalCost.setText(a + "đ");
    }

    private void setBackground(){
        ShapeAppearanceModel shapeAppearanceModel = new ShapeAppearanceModel()
                .toBuilder()
                .setAllCornerSizes(50f)
                .build();
        MaterialShapeDrawable materialShapeDrawable = new MaterialShapeDrawable(shapeAppearanceModel);
        materialShapeDrawable.setFillColor(ColorStateList.valueOf(Color.WHITE));
        materialShapeDrawable.setStroke(0.5F, Color.parseColor("#CCCCCC"));
        materialShapeDrawable.setElevation(10);
        binding.linearLayout.setBackground(materialShapeDrawable);

        MaterialShapeDrawable materialShapeDrawable1 = new MaterialShapeDrawable(shapeAppearanceModel);
        materialShapeDrawable1.setFillColor(ColorStateList.valueOf(Color.parseColor("#E47905")));
        binding.orderBtn.setBackground(materialShapeDrawable1);
    }
    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();

        if (fragment instanceof HomeFragment || fragment instanceof OrderFragment) {
            shouldShowAppBar = true;
            binding.appBarLayout.setVisibility(View.VISIBLE);
        } else {
            shouldShowAppBar = false;
            binding.appBarLayout.setVisibility(View.GONE);
        }
    }

    public void navigateToOrderFragment() {
        //replaceFragment(new OrderFragment());

        binding.bottomNavigationView.setSelectedItemId(R.id.order);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        DatabaseReference locationRef = FirebaseDatabase.getInstance().getReference().child("LOCATION");
        locationRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    LocationDomain locationDomain = childSnapshot.getValue(LocationDomain.class);
                    if (locationDomain.getUserID() == UserID) {
                        Map<String, Object> updates = new HashMap<>();
                        updates.put("LocationX", location.getLatitude());
                        updates.put("LocationY", location.getLongitude());
                        childSnapshot.getRef().updateChildren(updates);
                        break;
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("TAG", "Error fetching locations: " + error.getMessage());
            }
        });
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        LocationListener.super.onStatusChanged(provider, status, extras);
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        LocationListener.super.onProviderEnabled(provider);
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        LocationListener.super.onProviderDisabled(provider);
    }
}