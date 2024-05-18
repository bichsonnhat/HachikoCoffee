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
import com.example.hachikocoffee.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link YourPickupVoucherFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class YourPickupVoucherFragment extends Fragment {
    ArrayList<DiscountDomain> pickupVoucherList1 = new ArrayList<>();
    ArrayList<DiscountDomain> pickupVoucherList2 = new ArrayList<>();
    private RecyclerView rcv_pickupList1;
    private RecyclerView rcv_pickupList2;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public YourPickupVoucherFragment() {
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
    public static YourPickupVoucherFragment newInstance(String param1, String param2) {
        YourPickupVoucherFragment fragment = new YourPickupVoucherFragment();
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
        View view = inflater.inflate(R.layout.fragment_your_pickup_voucher, container, false);

        initYourPickupVoucher(view);

        return view;
    }

    public void initYourPickupVoucher(View view) {
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        rcv_pickupList1 = view.findViewById(R.id.rcv_pickup_list1);
        rcv_pickupList2 = view.findViewById(R.id.rcv_pickup_list2);

        rcv_pickupList1.setLayoutManager(linearLayoutManager1);
        rcv_pickupList2.setLayoutManager(linearLayoutManager2);

        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("VOUCHER");

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                //    ArrayList<DiscountDomain> pickupVoucherList1 = new ArrayList<>();
                //    ArrayList<DiscountDomain> pickupVoucherList2 = new ArrayList<>();

                    for (DataSnapshot issue : snapshot.getChildren()) {
                        DiscountDomain discount = issue.getValue(DiscountDomain.class);

                        if (discount != null && discount.getType().equals("Pick up")) { // Must add check date function
                            if (discount.isAboutToExpire()) {
                                pickupVoucherList1.add(discount);
                            }
                            pickupVoucherList2.add(discount);
                        }
                    }
                    displayDiscountData(pickupVoucherList1, pickupVoucherList2);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Failed to read value.", error.toException());
            }
        });
    }

    private void displayDiscountData(ArrayList<DiscountDomain> discountList1, ArrayList<DiscountDomain> discountList2) {
        if (!discountList1.isEmpty()) {
            DiscountAdapter discountAdapter1 = new DiscountAdapter(discountList1, this::onClickToDiscountDetailFunc);
            rcv_pickupList1.setAdapter(discountAdapter1);
        }

        if (!discountList2.isEmpty()) {
            DiscountAdapter discountAdapter2 = new DiscountAdapter(discountList2, this::onClickToDiscountDetailFunc);
            rcv_pickupList2.setAdapter(discountAdapter2);
        }

        TextView pickup1Size = getView().findViewById(R.id.counter_pickup1);
        TextView pickup2Size = getView().findViewById(R.id.counter_pickup2);

        pickup1Size.setText(String.valueOf(discountList1.size()));
        pickup2Size.setText(String.valueOf(discountList2.size()));
    }

    private void onClickToDiscountDetailFunc(DiscountDomain discount) {
        DiscountDetail discountDetail = DiscountDetail.newInstance(discount);
        discountDetail.show(getParentFragmentManager(), discountDetail.getTag());
    }

    public int getRecyclerViewSize() {
        return pickupVoucherList2.size(); // or deliveryVoucherList2.size() for YourDeliveryVoucherFragment
    }
}