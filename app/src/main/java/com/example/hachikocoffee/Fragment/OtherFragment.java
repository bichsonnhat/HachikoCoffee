package com.example.hachikocoffee.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.hachikocoffee.Activity.ContactFeedbackActivity;
import com.example.hachikocoffee.Activity.SavedAddressActivity;
import com.example.hachikocoffee.Activity.UpdateInfoActivity;
import com.example.hachikocoffee.Domain.DiscountDomain;
import com.example.hachikocoffee.Login;
import com.example.hachikocoffee.Management.ManagementUser;
import com.example.hachikocoffee.NotificationDetail;
import com.example.hachikocoffee.OrderHistory;
import com.example.hachikocoffee.R;
import com.example.hachikocoffee.YourVoucher;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OtherFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OtherFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView voucherCount;
    private TextView notificationCount;
    SharedPreferences perf;
    SharedPreferences.Editor editor;

    public OtherFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OtherFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OtherFragment newInstance(String param1, String param2) {
        OtherFragment fragment = new OtherFragment();
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
        View view = inflater.inflate(R.layout.fragment_other, container, false);
        Button btnToVouchers2 = view.findViewById(R.id.btn_to_voucher2);
        Button btnToNotification2 = view.findViewById(R.id.btn_to_notification2);
        voucherCount = view.findViewById(R.id.voucherBtn_count2);
        notificationCount = view.findViewById(R.id.notificationCountOther);
        voucherCount.setText("0");
        initVoucherCount();
        initNotificationCount();

        // Set on click listener for the button to move from ShopFragment to YourVoucher Activity
        btnToVouchers2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Move to YourVoucher Activity
                Intent intent = new Intent(getActivity(), YourVoucher.class);
                startActivity(intent);
            }
        });

        // Set on click listener for the button to move from ShopFragment to NotificationDetail Activity
        btnToNotification2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Move to NotificationDetail Activity
                Intent intent = new Intent(getActivity(), NotificationDetail.class);
                startActivity(intent);
            }
        });


	Button updateInfoBtn = view.findViewById(R.id.update_info_btn);
        updateInfoBtn.setOnClickListener(new View.OnClickListener() {		
	    @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), UpdateInfoActivity.class);
                startActivity(intent);
	    }
	});

	Button contactBtn = view.findViewById(R.id.contact_btn);
        contactBtn.setOnClickListener(new View.OnClickListener() {	
	    @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ContactFeedbackActivity.class);
                startActivity(intent);
	    }
	});

        Button savedAddressBtn = view.findViewById(R.id.saved_address_btn);
        savedAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SavedAddressActivity.class);
                startActivity(intent);
            }
        });

        Button logoutBtn = view.findViewById(R.id.logout_btn);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog = new AlertDialog.Builder(getContext(), R.style.AlertDialog_AppCompat_Custom)
                        .setTitle("Đăng xuất")
                        .setMessage("Bạn có muốn đăng xuất không?")
                        .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                perf = getActivity().getSharedPreferences("User", Context.MODE_PRIVATE);
                                editor = perf.edit();
                                editor.putBoolean("LoggedIn", false);
                                editor.apply();
                                Intent intent = new Intent(getActivity(), Login.class);
                                startActivity(intent);
                                getActivity().finish();
                            }
                        })
                        .setNegativeButton("Không", null)
                        .show();

                Button positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                Button negativeButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                positiveButton.setTextColor(getResources().getColor(R.color.black));
                negativeButton.setTextColor(Color.parseColor("#E47905"));
            }
        });

        Button btnHistoryOrder = view.findViewById(R.id.btnHistoryOrder);
        btnHistoryOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), OrderHistory.class);
                startActivity(intent);
            }
        });

        return view;
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
        DatabaseReference userVoucherRef = FirebaseDatabase.getInstance().getReference("USERVOUCHER");
        userVoucherRef.orderByChild("UserID").equalTo(ManagementUser.getInstance().getUserId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> voucherIDs = new ArrayList<>();
                ArrayList<DiscountDomain> discountList1 = new ArrayList<>();
                ArrayList<DiscountDomain> discountList2 = new ArrayList<>();
                if (dataSnapshot.exists()){
                    for (DataSnapshot userVoucherSnapshot : dataSnapshot.getChildren()) {
                        int isUse = userVoucherSnapshot.child("IsUse").getValue(Integer.class);
                        if (isUse == 0){
                            String voucherID = String.valueOf(userVoucherSnapshot.child("VoucherID").getValue(Long.class));
                            voucherIDs.add(voucherID);
                        }
                    }

                    DatabaseReference voucherRef = FirebaseDatabase.getInstance().getReference("VOUCHER");

                    voucherRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            int voucherAvailable = 0;
                            for (DataSnapshot voucherSnapshot : dataSnapshot.getChildren()) {
                                String currentVoucherID = String.valueOf(voucherSnapshot.child("VoucherID").getValue(Long.class));
                                if (voucherIDs.contains(currentVoucherID)) {
                                    DiscountDomain discount = voucherSnapshot.getValue(DiscountDomain.class);
                                    if (discount != null) { // Must add check date function
                                        voucherAvailable += 1;
                                    }
                                }
                            }
                            voucherCount.setText("" + voucherAvailable);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            // Xử lý lỗi nếu có
                            System.err.println("Error retrieving vouchers: " + databaseError.getMessage());
                        }
                    });
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xử lý lỗi nếu có
                System.err.println("Error retrieving user vouchers: " + databaseError.getMessage());
            }
        });
    }
}