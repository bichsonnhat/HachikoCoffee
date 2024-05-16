package com.example.hachikocoffee.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.hachikocoffee.Adapter.ShopAdapter;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShopFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShopFragment extends Fragment {

    private RecyclerView recyclerView_listShop1;
    private RecyclerView recyclerView_listShop2; // not used yet
    private RecyclerView recyclerView_listShop3; // not used yet

    private ArrayList<ShopDomain> shopList;

    private ShopAdapter shopAdapter;

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

        YourPickupVoucherFragment pickupFragment = (YourPickupVoucherFragment) getFragmentManager().findFragmentByTag("YourPickupVoucherFragment");
        YourDeliveryVoucherFragment deliveryFragment = (YourDeliveryVoucherFragment) getFragmentManager().findFragmentByTag("YourDeliveryVoucherFragment");

        int pickupSize = pickupFragment != null ? pickupFragment.getRecyclerViewSize_pickup2() : 0;
        int deliverySize = deliveryFragment != null ? deliveryFragment.getRecyclerViewSize_delivery2() : 0;

        int totalSize = pickupSize + deliverySize;

        TextView voucherCount = view.findViewById(R.id.voucherBtn_count);
        voucherCount.setText(String.valueOf(totalSize));
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
}