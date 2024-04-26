package com.example.hachikocoffee.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hachikocoffee.Adapter.ShopAdapter;
import com.example.hachikocoffee.Domain.ShopDomain;
import com.example.hachikocoffee.R;

import java.lang.reflect.Array;
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
    //private RecyclerView.Adapter adapter;

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

        initShop(view);

        return view;
    }

    public void initShop(View view) {
        recyclerView_listShop1 = view.findViewById(R.id.rcv_list_shop1);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView_listShop1.setLayoutManager(linearLayoutManager);

        shopList = new ArrayList<>();
        shopList.add(new ShopDomain(R.drawable.coffee_store, "HCM Cao Thắng", "0,01"));
        shopList.add(new ShopDomain(R.drawable.coffee_store, "HCM Cao Thắng", "0,21"));
        shopList.add(new ShopDomain(R.drawable.coffee_store, "HCM Cao Thắng", "3,21"));

        shopAdapter = new ShopAdapter(shopList);
        recyclerView_listShop1.setAdapter(shopAdapter);
    }
}