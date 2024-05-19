package com.example.hachikocoffee.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.hachikocoffee.Adapter.DiscountAdapter;
import com.example.hachikocoffee.DiscountDetail;
import com.example.hachikocoffee.Domain.DiscountDomain;
import com.example.hachikocoffee.R;
import com.example.hachikocoffee.YourVoucher;
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
    private RecyclerView rcv_listVoucher1;
    private RecyclerView rcv_listVoucher2;

    private ArrayList<DiscountDomain> discountList;

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

        View view = inflater.inflate(R.layout.fragment_discount, container, false);
        CardView btnToVouchers3 = (CardView) view.findViewById(R.id.btn_to_voucher3);
        Button btnToVouchers4 = view.findViewById(R.id.btn_seeall_voucher1);
        Button btnToVouchers5 = view.findViewById(R.id.btn_seeall_voucher2);

        btnToVouchers3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), YourVoucher.class);
                startActivity(intent);
            }
        });

        btnToVouchers4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), YourVoucher.class);
                startActivity(intent);
            }
        });

        btnToVouchers5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), YourVoucher.class);
                startActivity(intent);
            }
        });

        initDiscount(view);

        return view;
    }

    public void initDiscount(View view) {
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        rcv_listVoucher1 = view.findViewById(R.id.rcv_list_voucher1);
        rcv_listVoucher2 = view.findViewById(R.id.rcv_list_voucher2);

        rcv_listVoucher1.setLayoutManager(linearLayoutManager1);
        rcv_listVoucher2.setLayoutManager(linearLayoutManager2);

        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("VOUCHER");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    ArrayList<DiscountDomain> discountList1 = new ArrayList<>();
                    ArrayList<DiscountDomain> discountList2 = new ArrayList<>();

                    for (DataSnapshot issue : snapshot.getChildren()) {
                        DiscountDomain discount = issue.getValue(DiscountDomain.class);

                        if (discount.isAboutToExpire() && discountList1.size() < 3){
                            discountList1.add(discount);
                            Log.d("Discount", "Add a discount that is about to expire");
                        }

                        if (discountList2.size() < 3) {
                            discountList2.add(discount);
                            Log.d("Discount", "Add a discount that is not about to expire");
                        }
                    }
                    displayDiscountData(discountList1, discountList2);
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
            rcv_listVoucher1.setAdapter(discountAdapter1);
        }
        if (!discountList2.isEmpty()) {
            DiscountAdapter discountAdapter2 = new DiscountAdapter(discountList2, this::onClickToDiscountDetailFunc);
            rcv_listVoucher2.setAdapter(discountAdapter2);
        }
    }

    private void onClickToDiscountDetailFunc(DiscountDomain discount) {
        DiscountDetail discountDetail = DiscountDetail.newInstance(discount);
        discountDetail.show(getParentFragmentManager(), discountDetail.getTag());
    }
}