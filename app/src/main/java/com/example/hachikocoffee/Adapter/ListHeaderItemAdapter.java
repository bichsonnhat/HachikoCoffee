package com.example.hachikocoffee.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hachikocoffee.Domain.ItemsDomain;
import com.example.hachikocoffee.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ListHeaderItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final ArrayList<Object> itemsDomains;
    public ListHeaderItemAdapter(ArrayList<Object> itemsDomains){
        this.itemsDomains = itemsDomains;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 1){
            return new HeaderHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.single_row_for_header, parent, false));
        } else {
            return new ItemHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.viewholder_items, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Object dataItem = itemsDomains.get(position);
        if (holder instanceof HeaderHolder && dataItem instanceof String){
            HeaderHolder headerHolder = (HeaderHolder) holder;
            headerHolder.header.setText((String)itemsDomains.get(position));
        }else if (holder instanceof ItemHolder && dataItem instanceof ItemsDomain){
            ItemHolder itemHolder = (ItemHolder) holder;
            ItemsDomain data = (ItemsDomain) dataItem;;
            itemHolder.itemName.setText(data.getTitle());
            Picasso.get().load(data.getImageURL().toString()).into(itemHolder.itemImage);
            itemHolder.itemCost.setText(""+data.getPrice());
        }
    }

    @Override
    public int getItemCount() {
        return itemsDomains.size();
    }

    @Override
    public int getItemViewType(int position) {
        // 1 for header and 2 for items
        return itemsDomains.get(position) instanceof String ? 1 : 2;
    }

    public static class HeaderHolder extends RecyclerView.ViewHolder{
        private final TextView header;
        public HeaderHolder(@NonNull View itemView) {
            super(itemView);
            header = itemView.findViewById(R.id.header);
        }
    }
    public static class ItemHolder extends RecyclerView.ViewHolder{
        private final ImageView itemImage;
        private final TextView itemName;
        private final TextView itemCost;
        private final ImageView addItemButton;
        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.itemImage);
            itemName = itemView.findViewById(R.id.itemName);
            itemCost = itemView.findViewById(R.id.itemCost);
            addItemButton = itemView.findViewById(R.id.addItemButton);
        }
    }
}
