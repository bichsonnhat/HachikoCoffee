package com.example.hachikocoffee.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.hachikocoffee.Listener.FinishedClickListener;
import com.example.hachikocoffee.OrderDetail;
import com.example.hachikocoffee.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import static com.example.hachikocoffee.Adapter.OrderAdapter.setInterfaceInstanceFinished;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OrderHistoryFinishedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderHistoryFinishedFragment extends Fragment implements FinishedClickListener {
    ArrayList<OrderDomain> finishedOrderList = new ArrayList<>();
    private RecyclerView rcv_finishedOrderList;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    View glocalView;

    private int UserID;

    public OrderHistoryFinishedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OrderHistoryFinishedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OrderHistoryFinishedFragment newInstance(String param1, String param2) {
        OrderHistoryFinishedFragment fragment = new OrderHistoryFinishedFragment();
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
        setInterfaceInstanceFinished(this);
        SharedPreferences perf = requireActivity().getSharedPreferences("User", Context.MODE_PRIVATE);
        UserID = perf.getInt("UserID", 1);
        View view = inflater.inflate(R.layout.fragment_order_history_finished, container, false);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        rcv_finishedOrderList = view.findViewById(R.id.rcv_orderList_finished);
        rcv_finishedOrderList.setLayoutManager(linearLayoutManager);

        initFinishedOrderList(view);

        glocalView = view;
        return view;
    }

    public void initFinishedOrderList(View view) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        rcv_finishedOrderList = view.findViewById(R.id.rcv_orderList_finished);
        rcv_finishedOrderList.setLayoutManager(linearLayoutManager);

        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("ORDER");

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        OrderDomain order = issue.getValue(OrderDomain.class);

                        if (order != null && "Finished".equals(order.getOrderStatus()) && order.getUserID() == UserID) {
                            finishedOrderList.add(order);
                        }
                    }
                    displayFinishedOrderList(finishedOrderList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Failed to read value.", error.toException());
            }
        });
    }

    private void displayFinishedOrderList(ArrayList<OrderDomain> finishedOrderList) {
        if (!finishedOrderList.isEmpty()) {
            OrderAdapter orderAdapter = new OrderAdapter(finishedOrderList, this::onClickToOrderDetailFunc);
            rcv_finishedOrderList.setAdapter(orderAdapter);
        }
    }

    private void onClickToOrderDetailFunc(OrderDomain order) {
        Intent intent = new Intent(getActivity(), OrderDetail.class);
        startActivity(intent);
    }

    public void addOrder(OrderDomain order) {
        finishedOrderList.add(order);
        displayFinishedOrderList(finishedOrderList);
    }

    @Override
    public void onFinishedClick() {
        rcv_finishedOrderList.clearFocus();
        initFinishedOrderList(glocalView);
//        Toast.makeText(getContext(), "Finished", Toast.LENGTH_SHORT).show();
    }
}