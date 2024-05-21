package com.example.hachikocoffee.Adapter;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hachikocoffee.BottomSheetDialog.DetailCart;
import com.example.hachikocoffee.Domain.ItemsDomain;
import com.example.hachikocoffee.BottomSheetDialog.ProductDetail;
import com.example.hachikocoffee.databinding.ViewholderNewListBinding;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;

public class NewListAdapter extends RecyclerView.Adapter<NewListAdapter.Viewholder> {
    private final ArrayList<ItemsDomain> items;
    Context context;

    public NewListAdapter(ArrayList<ItemsDomain> items){
        this.items = items;
    }
    @NonNull
    @Override
    public NewListAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        ViewholderNewListBinding binding = ViewholderNewListBinding.inflate(LayoutInflater.from(context), parent, false);
        return new Viewholder(binding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull NewListAdapter.Viewholder holder, @SuppressLint("RecyclerView") int position) {
        holder.binding.itemTitle.setText(items.get(position).getTitle());

        //format money
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        String a = new DecimalFormat("#,###", symbols).format(items.get(position).getPrice());
        holder.binding.priceTxt.setText(a +"đ");

        Glide.with(context)
                .load(items.get(position).getImageURL())
                .into(holder.binding.pic);

        holder.binding.addItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ItemsDomain item = items.get(position);
                DetailCart detailBottomSheetDialog = new DetailCart(item);

                detailBottomSheetDialog.show(((AppCompatActivity) context).getSupportFragmentManager(), "DetailBottomSheetDialog");
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ItemsDomain item = items.get(position);
                ProductDetail detailBottomSheetDialog = new ProductDetail(item);

                detailBottomSheetDialog.show(((AppCompatActivity) context).getSupportFragmentManager(), "DetailBottomSheetDialog");
            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class Viewholder extends RecyclerView.ViewHolder{
        ViewholderNewListBinding binding;
        public Viewholder(ViewholderNewListBinding binding){
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
