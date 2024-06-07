package com.example.hachikocoffee.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hachikocoffee.Adapter.DiscountAdapter;
import com.example.hachikocoffee.DiscountDetail;
import com.example.hachikocoffee.Domain.DiscountDomain;
import com.example.hachikocoffee.Management.ManagementUser;
import com.example.hachikocoffee.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link YourDeliveryVoucherFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class YourDeliveryVoucherFragment extends Fragment {
    ArrayList<DiscountDomain> deliveryVoucherList1 = new ArrayList<>();
    ArrayList<DiscountDomain> deliveryVoucherList2 = new ArrayList<>();

    private RecyclerView rcv_deliveryList1;
    private RecyclerView rcv_deliveryList2;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public YourDeliveryVoucherFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OrderHistoryProcessingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static YourDeliveryVoucherFragment newInstance(String param1, String param2) {
        YourDeliveryVoucherFragment fragment = new YourDeliveryVoucherFragment();
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
        View view = inflater.inflate(R.layout.fragment_your_delivery_voucher, container, false);

        initYourDeliveryVoucher(view);

        return view;
    }

    public void initYourDeliveryVoucher(View view) {
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        rcv_deliveryList1 = view.findViewById(R.id.rcv_delivery_list1);
        rcv_deliveryList2 = view.findViewById(R.id.rcv_delivery_list2);

        rcv_deliveryList1.setLayoutManager(linearLayoutManager1);
        rcv_deliveryList2.setLayoutManager(linearLayoutManager2);

        DatabaseReference userVoucherRef = FirebaseDatabase.getInstance().getReference("USERVOUCHER");
        userVoucherRef.orderByChild("userID").equalTo(ManagementUser.getInstance().getUserId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> voucherIDs = new ArrayList<>();
                ArrayList<DiscountDomain> discountList1 = new ArrayList<>();
                ArrayList<DiscountDomain> discountList2 = new ArrayList<>();
                if (dataSnapshot.exists()){
                    for (DataSnapshot userVoucherSnapshot : dataSnapshot.getChildren()) {
                        int isUse = userVoucherSnapshot.child("isUse").getValue(Integer.class);
                        if (isUse == 0){
                            String voucherID = String.valueOf(userVoucherSnapshot.child("voucherID").getValue(Long.class));
                            voucherIDs.add(voucherID);
                        }
                    }

                    DatabaseReference voucherRef = FirebaseDatabase.getInstance().getReference("VOUCHER");

                    voucherRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot voucherSnapshot : dataSnapshot.getChildren()) {
                                String currentVoucherID = String.valueOf(voucherSnapshot.child("voucherID").getValue(Long.class));
                                if (voucherIDs.contains(currentVoucherID)) {
                                    DiscountDomain discount = voucherSnapshot.getValue(DiscountDomain.class);
                                    if (discount != null && discount.getType().equals("Delivery")) { // Must add check date function
                                        if (!discount.isAboutToExpire()) {
                                            deliveryVoucherList2.add(discount);
                                        } else {
                                            deliveryVoucherList1.add(discount);
                                        }
                                    }
                                }
                            }
                            displayDiscountData(deliveryVoucherList1, deliveryVoucherList2);
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

//        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("VOUCHER");
//
//        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()) {
//                //    ArrayList<DiscountDomain> deliveryVoucherList1 = new ArrayList<>();
//                //    ArrayList<DiscountDomain> deliveryVoucherList2 = new ArrayList<>();
//
//                    for (DataSnapshot issue : snapshot.getChildren()) {
//                        DiscountDomain discount = issue.getValue(DiscountDomain.class);
//
//                        if (discount != null && discount.getType().equals("Delivery")) { // Must add check date function
//                            if (discount.isAboutToExpire()) {
//                                deliveryVoucherList1.add(discount);
//                            }
//                            deliveryVoucherList2.add(discount);
//                        }
//                    }
//                    displayDiscountData(deliveryVoucherList1, deliveryVoucherList2);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Log.e("FirebaseError", "Failed to read value.", error.toException());
//            }
//        });
    }

    private void displayDiscountData(ArrayList<DiscountDomain> discountList1, ArrayList<DiscountDomain> discountList2) {
        if (!discountList1.isEmpty()) {
            DiscountAdapter discountAdapter1 = new DiscountAdapter(discountList1, this::onClickToDiscountDetailFunc);
            rcv_deliveryList1.setAdapter(discountAdapter1);
        }

        if (!discountList2.isEmpty()) {
            DiscountAdapter discountAdapter2 = new DiscountAdapter(discountList2, this::onClickToDiscountDetailFunc);
            rcv_deliveryList2.setAdapter(discountAdapter2);
        }

        TextView delivery1Size = getView().findViewById(R.id.counter_delivery1);
        TextView delivery2Size = getView().findViewById(R.id.counter_delivery2);

        delivery1Size.setText(String.valueOf(discountList1.size()));
        delivery2Size.setText(String.valueOf(discountList2.size()));
    }

    private void onClickToDiscountDetailFunc(DiscountDomain discount) {
        DiscountDetail discountDetail = DiscountDetail.newInstance(discount);
        discountDetail.show(getParentFragmentManager(), discountDetail.getTag());
    }

    public int getRecyclerViewSize() {
        return deliveryVoucherList2.size(); // or deliveryVoucherList2.size() for YourDeliveryVoucherFragment
    }
}