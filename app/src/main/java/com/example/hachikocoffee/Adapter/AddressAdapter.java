package com.example.hachikocoffee.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hachikocoffee.Activity.EditAddressActivity;
import com.example.hachikocoffee.Domain.AddressDomain;
import com.example.hachikocoffee.Domain.DiscountDomain;
import com.example.hachikocoffee.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import static com.example.hachikocoffee.Activity.EditAddressActivity.setEditInterfaceInstance;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.AddressViewHolder> {

    private final ArrayList<AddressDomain> list;

    public AddressAdapter(ArrayList<AddressDomain> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public AddressAdapter.AddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_address, parent, false);
        return new AddressViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressAdapter.AddressViewHolder holder, int position) {
        AddressDomain address = list.get(position);
        if (address == null) {
            return;
        }
        holder.AddressTitle.setText(""+address.getTitle());
        holder.AddressDetail.setText(""+address.getDescription());
        holder.AddressInfo.setText(""+address.getRecipentName()+ " " +address.getRecipentPhone());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEditInterfaceInstance(v.getContext());
                Intent intent = new Intent(v.getContext(), EditAddressActivity.class);
                intent.putExtra("AddressID", address.getAddressID());
                v.getContext().startActivity(intent);
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
        public AddressViewHolder(@NonNull View itemView) {
            super(itemView);
            AddressTitle = itemView.findViewById(R.id.vh_address_nameAddress);
            AddressDetail = itemView.findViewById(R.id.vh_address_detailAddress);
            AddressInfo = itemView.findViewById(R.id.vh_address_userNameAndTelephone);
        }
    }
}
