package com.example.hachikocoffee.Adapter;

import android.content.Context;
import android.view.LayoutInflater;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hachikocoffee.Domain.CategoryDomain;
import com.example.hachikocoffee.databinding.ViewholderCategoryDialogBinding;

import java.util.ArrayList;

public class CategoryDialogAdapter extends RecyclerView.Adapter<CategoryDialogAdapter.Viewholder> {
    private ArrayList<CategoryDomain> items;
    private Context context;

    public  CategoryDialogAdapter(ArrayList<CategoryDomain> items){
        this.items = items;
    }
    @NonNull
    @Override
    public CategoryDialogAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        ViewholderCategoryDialogBinding binding = ViewholderCategoryDialogBinding.inflate(LayoutInflater.from(context), parent, false);
        return new Viewholder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryDialogAdapter.Viewholder holder, int position) {
        holder.binding.categoryName.setText(items.get(position).getTitle());

        Glide.with(context)
                .load(items.get(position).getImageURL())
                .into(holder.binding.categryImage);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder{
        ViewholderCategoryDialogBinding binding;

        public Viewholder(ViewholderCategoryDialogBinding binding){
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
