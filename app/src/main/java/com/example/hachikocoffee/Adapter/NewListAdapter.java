package com.example.hachikocoffee.Adapter;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hachikocoffee.Activity.DetailActivity;
import com.example.hachikocoffee.Domain.ItemsDomain;
import com.example.hachikocoffee.ProductDetail;
import com.example.hachikocoffee.R;
import com.example.hachikocoffee.databinding.ViewholderNewListBinding;

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
        holder.binding.priceTxt.setText(Math.round(items.get(position).getPrice()) +"000đ");

        Glide.with(context)
                .load(items.get(position).getImageURL())
                .into(holder.binding.pic);

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
