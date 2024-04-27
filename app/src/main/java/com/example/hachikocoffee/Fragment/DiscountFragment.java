package com.example.hachikocoffee.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hachikocoffee.Adapter.DiscountAdapter;
import com.example.hachikocoffee.Domain.DiscountDomain;
import com.example.hachikocoffee.R;
import com.example.hachikocoffee.VerticalSpaceItemDecoration;

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

        // change space between items
        //recyclerView_listDiscount.addItemDecoration(new VerticalSpaceItemDecoration(VERTICAL_ITEM_SPACE));

        discountList = new ArrayList<>();
        discountList.add(new DiscountDomain(R.drawable.voucher_img, "Bộ Ghiền 39K + Freeship", "4"));
        discountList.add(new DiscountDomain(R.drawable.voucher_img, "Bộ Ghiền 40K + Freeship", "5"));
        discountList.add(new DiscountDomain(R.drawable.voucher_img, "Bộ Ghiền 41K + Freeship", "6"));

        discountAdapter = new DiscountAdapter(discountList);
        recyclerView_listDiscount.setAdapter(discountAdapter);
    }
}