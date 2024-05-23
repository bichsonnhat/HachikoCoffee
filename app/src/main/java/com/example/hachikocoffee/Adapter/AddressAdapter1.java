package com.example.hachikocoffee.Adapter;

import static com.example.hachikocoffee.Activity.EditAddressActivity.setEditInterfaceInstance;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hachikocoffee.Activity.EditAddressActivity;
import com.example.hachikocoffee.Activity.YourAddressPick;
import com.example.hachikocoffee.BottomSheetDialog.CartBottomSheetDialogFragment;
import com.example.hachikocoffee.Domain.AddressDomain;
import com.example.hachikocoffee.Listener.OnAddressChangedListener;
import com.example.hachikocoffee.Listener.OnAddressPickListener;
import com.example.hachikocoffee.Listener.OnPickListener;
import com.example.hachikocoffee.R;

import java.util.ArrayList;

public class AddressAdapter1 extends RecyclerView.Adapter<AddressAdapter1.AddressViewHolder> {

    private final ArrayList<AddressDomain> list;
    private static OnAddressPickListener onAddressPickListener;
    private static OnPickListener onPickListener;

    private int selectedItem = RecyclerView.NO_POSITION;
    public AddressAdapter1(ArrayList<AddressDomain> list) {
        this.list = list;
    }
    Context context;

    @NonNull
    @Override
    public AddressAdapter1.AddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_address, parent, false);
        return new AddressViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressAdapter1.AddressViewHolder holder, int position) {
        AddressDomain address = list.get(position);
        if (address == null) {
            return;
        }
        if (position == selectedItem) {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.selectedItemColor));
        } else {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.defaultItemColor));
        }
        holder.AddressTitle.setText(""+address.getTitle());
        holder.AddressDetail.setText(""+address.getDescription());
        holder.AddressInfo.setText(""+address.getRecipentName()+ " " +address.getRecipentPhone());
        holder.vh_address_deleteButton.setVisibility(View.INVISIBLE);
        if (position == 0){
            holder.vh_address_divider.setVisibility(View.INVISIBLE);
        }
        if (selectedItem != RecyclerView.NO_POSITION){
            onPickListener.onPick();
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int previousSelectedItem = selectedItem;
                selectedItem = position;
                notifyItemChanged(previousSelectedItem);
                notifyItemChanged(selectedItem);
                onAddressPickListener.onAddressPick(address);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    public static class AddressViewHolder extends RecyclerView.ViewHolder {

        private final TextView AddressTitle;
        private final TextView AddressDetail;
        private final TextView AddressInfo;

        private final ImageView vh_address_deleteButton;
        private final View vh_address_divider;

        public AddressViewHolder(@NonNull View itemView) {
            super(itemView);
            AddressTitle = itemView.findViewById(R.id.vh_address_nameAddress);
            AddressDetail = itemView.findViewById(R.id.vh_address_detailAddress);
            AddressInfo = itemView.findViewById(R.id.vh_address_userNameAndTelephone);
            vh_address_deleteButton = itemView.findViewById(R.id.vh_address_deleteButton);
            vh_address_divider = itemView.findViewById(R.id.vh_address_divider);
        }
    }

    public static void setInterfaceInstance(CartBottomSheetDialogFragment context){
        onAddressPickListener = (OnAddressPickListener) context;
    }

    public static void setInterfaceCallback(YourAddressPick context){
        onPickListener = (OnPickListener) context;
    }
}
