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

import com.example.hachikocoffee.Adapter.OrderAdapter;
import com.example.hachikocoffee.Domain.OrderDomain;
import com.example.hachikocoffee.OrderDetail;
import com.example.hachikocoffee.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OrderHistoryProcessingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderHistoryProcessingFragment extends Fragment {
    ArrayList<OrderDomain> processingOrderList = new ArrayList<>();
    private RecyclerView rcv_processingOrderList;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public OrderHistoryProcessingFragment() {
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
    public static OrderHistoryProcessingFragment newInstance(String param1, String param2) {
        OrderHistoryProcessingFragment fragment = new OrderHistoryProcessingFragment();
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
        View view = inflater.inflate(R.layout.fragment_order_history_processing, container, false);

        initProcessingOrderList(view);

        return view;
    }

    public void initProcessingOrderList(View view) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        rcv_processingOrderList = view.findViewById(R.id.rcv_orderList_processing);
        rcv_processingOrderList.setLayoutManager(linearLayoutManager);

        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("ORDER");

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        OrderDomain order = issue.getValue(OrderDomain.class);

                        if (order != null && "Pending".equals(order.getOrderStatus())) {
                            processingOrderList.add(order);
                        }
                    }
                    displayProcessingOrderList(processingOrderList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Failed to read value.", error.toException());
            }
        });
    }

    private void displayProcessingOrderList(ArrayList<OrderDomain> processingOrderList) {
        if (!processingOrderList.isEmpty()) {
            OrderAdapter orderAdapter = new OrderAdapter(processingOrderList, this::onClickToOrderDetailFunc);
            rcv_processingOrderList.setAdapter(orderAdapter);
        }
    }

    private void onClickToOrderDetailFunc(OrderDomain order) {
        Intent intent = new Intent(getActivity(), OrderDetail.class);
        startActivity(intent);
    }
}