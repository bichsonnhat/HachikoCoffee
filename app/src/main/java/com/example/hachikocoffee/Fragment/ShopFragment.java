package com.example.hachikocoffee.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.hachikocoffee.Activity.SearchShopActivity;
import com.example.hachikocoffee.Adapter.ShopAdapter;
import com.example.hachikocoffee.Domain.LocationDomain;
import com.example.hachikocoffee.Domain.ShopDomain;
import com.example.hachikocoffee.NotificationDetail;
import com.example.hachikocoffee.R;
import com.example.hachikocoffee.Listener.ShopClickListener;
import com.example.hachikocoffee.BottomSheetDialog.ShopDetail;
import com.example.hachikocoffee.YourVoucher;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShopFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShopFragment extends Fragment implements LocationListener {

    private RecyclerView rcv_listShop;

    private ShopAdapter shopAdapter;
    LocationManager locationManager;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private double locationX = 0, locationY = 0;
    private View viewFragment;
    private int UserID = 1;

    public ShopFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ShopFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ShopFragment newInstance(String param1, String param2) {
        ShopFragment fragment = new ShopFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_shop, container, false);
        TextView location = view.findViewById(R.id.map);
        viewFragment = view;
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions((Activity) getContext(), new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION
                    }, 100);
                    getLocation();
                }
            }
        });

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            getLocation();
        }

        EditText editText = view.findViewById(R.id.search);
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SearchShopActivity.class);
                startActivity(intent);
            }
        });

        Button btnToVouchers = view.findViewById(R.id.btn_to_voucher);
        Button btnToNotification = view.findViewById(R.id.btn_to_notification);

        // Set on click listener for the button to move from ShopFragment to YourVoucher Activity
        btnToVouchers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Move to YourVoucher Activity
                Intent intent = new Intent(getActivity(), YourVoucher.class);
                startActivity(intent);
            }
        });

        // Set on click listener for the button to move from ShopFragment to NotificationDetail Activity
        btnToNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Move to NotificationDetail Activity
                Intent intent = new Intent(getActivity(), NotificationDetail.class);
                startActivity(intent);
            }
        });

        initShop(view);
        return view;
    }

    @SuppressLint("MissingPermission")
    private void getLocation() {
        try {
            locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, ShopFragment.this);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void initShop(View view) {
        rcv_listShop = view.findViewById(R.id.rcv_list_shop);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rcv_listShop.setLayoutManager(linearLayoutManager);
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("STORE");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
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
                                    filteredList.add(shop);
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
            rcv_listShop.setAdapter(shopAdapter);
        }
    }

    private void onClickToShopDetailFunc(ShopDomain shop) {
        ShopDetail shopDetail = ShopDetail.newInstance(shop);
        shopDetail.show(getFragmentManager(), shopDetail.getTag());
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
        try {
            Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
            List<Address> address = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            String myAddress = address.get(0).getAddressLine(0);
        } catch (Exception e){
            e.printStackTrace();
        }
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