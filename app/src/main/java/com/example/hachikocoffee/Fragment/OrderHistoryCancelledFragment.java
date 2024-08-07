package com.example.hachikocoffee.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hachikocoffee.Adapter.OrderAdapter;
import com.example.hachikocoffee.Domain.OrderDomain;
import com.example.hachikocoffee.Listener.CanceledClickListener;
import com.example.hachikocoffee.Management.ManagementUser;
import com.example.hachikocoffee.Activity.OrderDetail;
import com.example.hachikocoffee.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OrderHistoryCancelledFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderHistoryCancelledFragment extends Fragment implements CanceledClickListener {
    ArrayList<OrderDomain> cancelledOrderList = new ArrayList<>();
    private RecyclerView rcv_cancelledOrderList;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    View globalView;
    private int UserID;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OrderHistoryCancelledFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OrderHistoryCancelledFragment newInstance(String param1, String param2) {
        OrderHistoryCancelledFragment fragment = new OrderHistoryCancelledFragment();
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
//        setInterfaceInstanceCanceled(this);
        UserID = ManagementUser.getInstance().getUserId();
        View view = inflater.inflate(R.layout.fragment_order_history_cancelled, container, false);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        rcv_cancelledOrderList = view.findViewById(R.id.rcv_orderList_cancelled);
        rcv_cancelledOrderList.setLayoutManager(linearLayoutManager);

        initCancelledOrderList(view);

        globalView = view;
        return view;
    }

    public void initCancelledOrderList(View view) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        rcv_cancelledOrderList = view.findViewById(R.id.rcv_orderList_cancelled);
        rcv_cancelledOrderList.setLayoutManager(linearLayoutManager);

        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("ORDER");

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        OrderDomain order = issue.getValue(OrderDomain.class);

                        if (order != null && "Canceled".equals(order.getOrderStatus()) && order.getUserID() == UserID){
                            cancelledOrderList.add(order);
                        }
                    }
                    displayCancelledOrderList(cancelledOrderList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Failed to read value.", error.toException());
            }
        });
    }

    private void displayCancelledOrderList(ArrayList<OrderDomain> cancelledOrderList) {
        if (!cancelledOrderList.isEmpty()) {
            OrderAdapter orderAdapter = new OrderAdapter(cancelledOrderList, this::onClickToOrderDetailFunc);
            rcv_cancelledOrderList.setAdapter(orderAdapter);
        }
    }

    private void onClickToOrderDetailFunc(OrderDomain order) {
        Intent intent = new Intent(getActivity(), OrderDetail.class);
        startActivity(intent);
    }

    public void addOrder(OrderDomain order) {
        cancelledOrderList.add(order);
        displayCancelledOrderList(cancelledOrderList); // Refresh the RecyclerView
    }

    @Override
    public void onCanceledClick() {
        Toast.makeText(getContext(), "Canceled", Toast.LENGTH_SHORT).show();
//        rcv_cancelledOrderList.clearFocus();
//        initCancelledOrderList(globalView);
    }
}