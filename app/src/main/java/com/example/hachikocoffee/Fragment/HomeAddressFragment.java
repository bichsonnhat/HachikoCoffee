package com.example.hachikocoffee.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.hachikocoffee.Adapter.AddressAdapter;
import com.example.hachikocoffee.Domain.AddressDomain;
import com.example.hachikocoffee.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeAddressFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeAddressFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeAddressFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeAddressFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeAddressFragment newInstance(String param1, String param2) {
        HomeAddressFragment fragment = new HomeAddressFragment();
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
        View view = inflater.inflate(R.layout.fragment_home_address, container, false);

        // Lấy dữ liệu từ Bundle
        Bundle bundle = getArguments();
        if (bundle != null) {
            String address = bundle.getString("Address");
            String nameAndTelephone = bundle.getString("NameAndTelephone");

            // Sử dụng dữ liệu để cập nhật giao diện của Fragment
            TextView addressTextView = view.findViewById(R.id.vh_address_detailAddress);
            addressTextView.setText(address);

            TextView nameAndTelephoneTextView = view.findViewById(R.id.vh_address_userNameAndTelephone);
            nameAndTelephoneTextView.setText(nameAndTelephone);
        }

        return view;
    }
}