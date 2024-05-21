package com.example.hachikocoffee.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hachikocoffee.Domain.ItemsDomain;
import com.example.hachikocoffee.Listener.OnItemClickListener;
import com.example.hachikocoffee.databinding.ViewholderItemsBinding;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.Viewholder>  {

    private final ArrayList<ItemsDomain> items;
    Context context;
    private OnItemClickListener onItemClickListener;
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public FavouriteAdapter(ArrayList<ItemsDomain> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public FavouriteAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        ViewholderItemsBinding binding = ViewholderItemsBinding.inflate(LayoutInflater.from(context), parent, false);
        return new Viewholder(binding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull FavouriteAdapter.Viewholder holder, @SuppressLint("RecyclerView") int position) {
        holder.binding.itemName.setText(items.get(position).getTitle());

        //format money
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        String a = new DecimalFormat("#,###", symbols).format(items.get(position).getPrice());
        holder.binding.itemCost.setText(a +"đ");

        Glide.with(context)
                .load(items.get(position).getImageURL())
                .into(holder.binding.itemImage);
        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(items.get(position));
            }
        });

        //((FavouriteActivity) holder.itemView.getContext()).updateUI(getItemCount());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class Viewholder extends RecyclerView.ViewHolder {
        ViewholderItemsBinding binding;

        public Viewholder(ViewholderItemsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }
}
