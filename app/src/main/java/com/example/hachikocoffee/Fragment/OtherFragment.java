package com.example.hachikocoffee.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.hachikocoffee.NotificationDetail;
import com.example.hachikocoffee.R;
import com.example.hachikocoffee.YourVoucher;

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

        return view;
    }
}