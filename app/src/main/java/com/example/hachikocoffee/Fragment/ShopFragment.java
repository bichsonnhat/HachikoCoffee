package com.example.hachikocoffee.Fragment;

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
import android.widget.TextView;
import android.widget.Toast;

import com.example.hachikocoffee.Adapter.ShopAdapter;
import com.example.hachikocoffee.Domain.ShopDomain;
import com.example.hachikocoffee.R;
import com.example.hachikocoffee.Listener.ShopClickListener;
import com.example.hachikocoffee.BottomSheetDialog.ShopDetail;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShopFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShopFragment extends Fragment implements LocationListener {

    private RecyclerView recyclerView_listShop1;
    private RecyclerView recyclerView_listShop2; // not used yet
    private RecyclerView recyclerView_listShop3; // not used yet

    private ArrayList<ShopDomain> shopList;

    private ShopAdapter shopAdapter;
    LocationManager locationManager;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

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
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions((Activity) getContext(), new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION
                    }, 100);
                }
                getLocation();
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
        recyclerView_listShop1 = view.findViewById(R.id.rcv_list_shop1);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView_listShop1.setLayoutManager(linearLayoutManager);

        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("STORE");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    ArrayList<ShopDomain> shopList = new ArrayList<>();
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        ShopDomain shop = issue.getValue(ShopDomain.class);
                        shopList.add(shop);
                    }
                    displayShopData(shopList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Failed to read value.", error.toException());
                // Notify user about the error
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
            recyclerView_listShop1.setAdapter(shopAdapter);
        }
    }

    private void onClickToShopDetailFunc(ShopDomain shop) {
        ShopDetail shopDetail = ShopDetail.newInstance(shop);
        shopDetail.show(getFragmentManager(), shopDetail.getTag());
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        Toast.makeText(getContext(), ""+location.getLatitude()+","+location.getLongitude(), Toast.LENGTH_SHORT).show();
        try {
            Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
            List<Address> address = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            String myAddress = address.get(0).getAddressLine(0);
            Toast.makeText(getContext(), "My address is: " + myAddress, Toast.LENGTH_SHORT).show();
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