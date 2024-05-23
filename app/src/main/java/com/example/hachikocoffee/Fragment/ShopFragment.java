package com.example.hachikocoffee.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.hachikocoffee.Activity.MainActivity;
import com.example.hachikocoffee.Activity.SearchShopActivity;
import com.example.hachikocoffee.Adapter.ShopAdapter;
import com.example.hachikocoffee.Domain.LocationDomain;
import com.example.hachikocoffee.Domain.ShopDomain;
import com.example.hachikocoffee.Listener.OnStoreClick;
import com.example.hachikocoffee.Management.ManagementUser;
import com.example.hachikocoffee.NotificationDetail;
import com.example.hachikocoffee.R;
import com.example.hachikocoffee.Listener.ShopClickListener;
import com.example.hachikocoffee.BottomSheetDialog.ShopDetail;
import com.example.hachikocoffee.YourVoucher;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
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

import static com.example.hachikocoffee.BottomSheetDialog.ShopDetail.setInterfaceInstance;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShopFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShopFragment extends Fragment implements OnStoreClick, LocationListener {

    private RecyclerView rcv_listShop;

    LocationManager locationManager;
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private FusedLocationProviderClient fusedLocationProviderClient;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private double locationX = 0, locationY = 0;
    private int UserID = 1;
    private TextView voucherCount;
    private TextView notificationCount;

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
        View view = inflater.inflate(R.layout.fragment_shop, container, false);
        voucherCount = view.findViewById(R.id.voucherBtn_count1);
        notificationCount = view.findViewById(R.id.notBtn_cntFinal);
        initVoucherCount();
        initNotificationCount();
        TextView location = view.findViewById(R.id.map);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext());
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(requireActivity(),
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            REQUEST_LOCATION_PERMISSION);
                } else {
                    getLastLocation();
                }
            }
        });

        EditText editText = view.findViewById(R.id.search);
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setInterfaceInstance(ShopFragment.this);
                Intent intent = new Intent(getContext(), SearchShopActivity.class);
                startActivity(intent);
            }
        });

        Button btnToVouchers = view.findViewById(R.id.btn_to_voucher);
        Button btnToNotification = view.findViewById(R.id.btn_to_notification);

        btnToVouchers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), YourVoucher.class);
                startActivity(intent);
            }
        });
        btnToNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NotificationDetail.class);
                startActivity(intent);
            }
        });

        initShop(view);

        //YourPickupVoucherFragment pickupFragment = (YourPickupVoucherFragment) getFragmentManager().findFragmentByTag("YourPickupVoucherFragment");
        //YourDeliveryVoucherFragment deliveryFragment = (YourDeliveryVoucherFragment) getFragmentManager().findFragmentByTag("YourDeliveryVoucherFragment");

        //if (pickupFragment != null && deliveryFragment != null) {
        //    int totalSize = pickupFragment.getRecyclerViewSize() + deliveryFragment.getRecyclerViewSize();
        //    voucherCount.setText(String.valueOf(totalSize));
        //}

        return view;
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
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Log.d("ShopFragment", ""+UserID);
        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(requireActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            double latitude = location.getLatitude();
                            double longitude = location.getLongitude();
                            Log.d("LocationXY", "Latitude: " + latitude + ", Longitude: " + longitude);

                            DatabaseReference locationRef = FirebaseDatabase.getInstance().getReference().child("LOCATION");
                                locationRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        boolean locationExists = false;

                                        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                            LocationDomain locationDomain = childSnapshot.getValue(LocationDomain.class);
                                            if (locationDomain != null && locationDomain.getUserID() == UserID) {
                                                locationExists = true;
                                                Map<String, Object> updates = new HashMap<>();
                                                updates.put("LocationX", latitude);
                                                updates.put("LocationY", longitude);
                                                childSnapshot.getRef().updateChildren(updates);
                                                break;
                                            }
                                        }

                                        if (!locationExists) {
                                            String newLocationKey = locationRef.push().getKey();
                                            if (newLocationKey != null) {
                                                LocationDomain newLocation = new LocationDomain(UserID, latitude, longitude);
                                                Map<String, Object> locationValues = newLocation.toMap();

                                                Map<String, Object> childUpdates = new HashMap<>();
                                                childUpdates.put(newLocationKey, locationValues);

                                                locationRef.updateChildren(childUpdates);
                                            }
                                        }

                                        View view = getView();
                                        if (view != null) {
                                            initShop(view);
                                        }
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Log.w("TAG", "Error fetching locations: " + error.getMessage());
                                    }
                                });

                        }
                    }
                });
    }
    private void initNotificationCount() {
        DatabaseReference notificationRef = FirebaseDatabase.getInstance().getReference("NOTIFICATION");
        notificationRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    int cnt_notification = 0;
                    for (DataSnapshot issue : snapshot.getChildren()){
                        cnt_notification += 1;
                    }
                    notificationCount.setText(""+cnt_notification);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initVoucherCount() {
        DatabaseReference voucherRef = FirebaseDatabase.getInstance().getReference("VOUCHER");
        voucherRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    int cnt_voucher = 0;
                    for (DataSnapshot issue : snapshot.getChildren()){
                        cnt_voucher += 1;
                    }
                    voucherCount.setText(""+cnt_voucher);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void initShop(View view) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        rcv_listShop = view.findViewById(R.id.rcv_list_shop);
        rcv_listShop.setLayoutManager(linearLayoutManager);

        UserID = ManagementUser.getInstance().getUserId();

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
                                    Double coorY = Double.parseDouble(values[0]);
                                    Double coorX = Double.parseDouble(values[1]);

                                    Double deltaLat = locationX - coorX;
                                    Double deltaLon = locationY - coorY;

                                    Double deltaLatKm = deltaLat * 111.0;

                                    Double latAvg = (locationX + coorX) / 2.0;
                                    Double cosLatAvg = Math.cos(Math.toRadians(latAvg));
                                    Double deltaLonKm = deltaLon * 111.0 * cosLatAvg;

                                    Double distance = Math.sqrt(Math.pow(deltaLatKm, 2) + Math.pow(deltaLonKm, 2));

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
            ShopAdapter shopAdapter = new ShopAdapter(shopList, new ShopClickListener() {
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

    @Override
    public void onStoreClick() {
        FragmentActivity fragmentActivity = getActivity();
        fragmentActivity.onStateNotSaved();
        ((MainActivity) requireActivity()).navigateToOrderFragment();
    }
}