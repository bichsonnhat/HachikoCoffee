package com.example.hachikocoffee.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.hachikocoffee.Domain.DiscountDomain;
import com.example.hachikocoffee.Domain.UserDomain;
import com.example.hachikocoffee.Domain.UserVoucherDomain;
import com.example.hachikocoffee.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MultiSelectSpinnerAdapter extends ArrayAdapter<UserDomain> {

    private final List<UserDomain> items;
    private final List<UserDomain> selectedItems;
    private final boolean[] checkedItems;
    private OnItemSelectedListener onItemSelectedListener;

    public interface OnItemSelectedListener {
        void onItemSelected(List<UserDomain> selectedItems, int pos);
    }

    public MultiSelectSpinnerAdapter(Context context, List<UserDomain> items, List<UserDomain> selectedItems) {
        super(context, 0, items);
        this.items = items;
        this.selectedItems = selectedItems;
        this.checkedItems = new boolean[items.size()];

        for (int i = 0; i < items.size(); i++) {
            checkedItems[i] = selectedItems.contains(items.get(i));
        }
    }

    public void setOnItemSelectedListener(OnItemSelectedListener listener) {
        this.onItemSelectedListener = listener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return createView(position, convertView, parent, false);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return createView(position, convertView, parent, true);
    }

    private View createView(int position, View convertView, ViewGroup parent, boolean isDropDown) {
        View view = convertView != null
                ? convertView
                : LayoutInflater.from(getContext()).inflate(
                isDropDown ? R.layout.custom_spinner_dropdown_item : R.layout.custom_non_select_dropdown,
                parent,
                false
        );

        TextView textView = view.findViewById(R.id.spin_txt);

        if (isDropDown) {
            CheckBox checkBox = view.findViewById(R.id.spinnerCheckbox);
            TextView itemName = view.findViewById(R.id.itemName);

            itemName.setText(items.get(position).getName());
            checkBox.setOnCheckedChangeListener((compoundButton, isChecked) -> {
                checkedItems[position] = isChecked;
                if (isChecked) {
                    if (!selectedItems.contains(items.get(position))) {
                        selectedItems.add(items.get(position));
                    }
                } else {
                    selectedItems.remove(items.get(position));
                }
                notifyDataSetChanged();
                if (onItemSelectedListener != null) {
                    onItemSelectedListener.onItemSelected(selectedItems, position);
                }
            });
        } else {
            if (selectedItems.isEmpty()) {
                textView.setText("Chọn người dùng");
            } else {
                DatabaseReference voucherRef = FirebaseDatabase.getInstance().getReference("VOUCHER");
                voucherRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            int voucherID = 0;
                            for (DataSnapshot issue : snapshot.getChildren()){
                                DiscountDomain discountDomain = issue.getValue(DiscountDomain.class);
                                voucherID = Math.max(voucherID, discountDomain.getVoucherID());
                            }
                            voucherID += 1;
                            ArrayList<String> names = new ArrayList<>();
                            ArrayList<Integer> userIDs = new ArrayList<>();
                            for (UserDomain selectedItem : getSelectedItems()) {
                                names.add(selectedItem.getName());
                                userIDs.add(selectedItem.getUserID());
                            }
                            textView.setText(names.toString().replace("[", "").replace("]", ""));
                            removeVoucherUser(voucherID);
                            addVoucherUser(voucherID, userIDs);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        }

        return view;
    }

    private void addVoucherUser(int voucherID, ArrayList<Integer> userIDs) {
        DatabaseReference userVoucherRef = FirebaseDatabase.getInstance().getReference("USERVOUCHER");
        userVoucherRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    int max_id = 0;
                    for (DataSnapshot issue : snapshot.getChildren()){
                        UserVoucherDomain userVoucherDomain = issue.getValue(UserVoucherDomain.class);
                        max_id = Math.max(max_id, userVoucherDomain.getUserVoucherID());
                    }
                    for (int userID : userIDs) {
                        max_id += 1;
                        UserVoucherDomain userVoucherDomain = new UserVoucherDomain(voucherID, userID, max_id, 0);
                        userVoucherRef.child(String.valueOf(max_id)).setValue(userVoucherDomain);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void removeVoucherUser(int voucherID) {
        DatabaseReference userVoucherRef = FirebaseDatabase.getInstance().getReference("USERVOUCHER");
        userVoucherRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot issue : snapshot.getChildren()){
                        UserVoucherDomain userVoucherDomain = issue.getValue(UserVoucherDomain.class);
                        if (userVoucherDomain.getVoucherID() == voucherID) {
                            issue.getRef().removeValue();
                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private List<UserDomain> getSelectedItems() {
        return selectedItems;
    }
}
