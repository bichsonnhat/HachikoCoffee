package com.example.hachikocoffee.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hachikocoffee.Adapter.DiscountAdapter;
import com.example.hachikocoffee.DiscountClickListener;
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
 * Use the {@link DiscountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DiscountFragment extends Fragment {

    private RecyclerView recyclerView_listDiscount;

    private ArrayList<DiscountDomain> discountList;

    private DiscountAdapter discountAdapter;

    //private static final int VERTICAL_ITEM_SPACE = 1;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DiscountFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DiscountFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DiscountFragment newInstance(String param1, String param2) {
        DiscountFragment fragment = new DiscountFragment();
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
        View view = inflater.inflate(R.layout.fragment_discount, container, false);

        initDiscount(view);

        return view;
    }

    public void initDiscount(View view) {
        recyclerView_listDiscount = view.findViewById(R.id.rcv_list_coupon);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView_listDiscount.setLayoutManager(linearLayoutManager);

        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("VOUCHER");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    ArrayList<DiscountDomain> discountList = new ArrayList<>();
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        DiscountDomain discount = issue.getValue(DiscountDomain.class);
                        discountList.add(discount);
                    }
                    displayDiscountData(discountList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Failed to read value.", error.toException());
                // Notify user about the error
            }
        });
    }

    private void displayDiscountData(ArrayList<DiscountDomain> discountList) {
        if (!discountList.isEmpty()) {
            discountAdapter = new DiscountAdapter(discountList, new DiscountClickListener() {
                @Override
                public void onClickDiscountItem(DiscountDomain discount) {
                    onClickToDiscountDetailFunc(discount);
                }
            });
            recyclerView_listDiscount.setAdapter(discountAdapter);
        }
    }

    private void onClickToDiscountDetailFunc(DiscountDomain discount) {
        DiscountDetail discountDetail = DiscountDetail.newInstance(discount);
        discountDetail.show(getParentFragmentManager(), discountDetail.getTag());
    }
}