package com.example.hachikocoffee.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.hachikocoffee.Activity.EditStoreActivity;
import com.example.hachikocoffee.Domain.ShopDomain;
import com.example.hachikocoffee.R;

import java.util.ArrayList;
import static com.example.hachikocoffee.Activity.EditStoreActivity.setEditInterfaceInstance;
public class StoreManagementAdapter extends RecyclerView.Adapter<StoreManagementAdapter.StoreManagementViewHolder> {
    private final ArrayList<ShopDomain> mListStoreManagement;

    public StoreManagementAdapter(ArrayList<ShopDomain> mListStoreManagement) {
        this.mListStoreManagement = mListStoreManagement;
    }

    @NonNull
    @Override
    public StoreManagementAdapter.StoreManagementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_store_item, parent, false);
        return new StoreManagementAdapter.StoreManagementViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StoreManagementAdapter.StoreManagementViewHolder holder, int position) {
        ShopDomain shop = mListStoreManagement.get(position);
        if (shop == null) {
            return;
        }

        holder.item_shopTitle.setText(shop.getName());
        holder.item_shopID.setText(String.valueOf(shop.getStoreID()));
        holder.item_shopEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEditInterfaceInstance(v.getContext());
                Intent intent = new Intent(v.getContext(), EditStoreActivity.class);
                intent.putExtra("StoreID", shop.getStoreID());
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mListStoreManagement != null) {
            return mListStoreManagement.size();
        }
        return 0;
    }

    public static class StoreManagementViewHolder extends RecyclerView.ViewHolder {
        private final TextView item_shopTitle;
        private final TextView item_shopID;
        private final ImageView item_shopEdit;

        public StoreManagementViewHolder(@NonNull View itemView) {
            super(itemView);

            item_shopTitle = itemView.findViewById(R.id.item_shopTitle);
            item_shopID = itemView.findViewById(R.id.item_shopID);
            item_shopEdit = itemView.findViewById(R.id.item_shopEdit);
        }
    }
}