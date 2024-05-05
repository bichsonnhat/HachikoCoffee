package com.example.hachikocoffee.Adapter;

import android.content.Context;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hachikocoffee.CategoryDialog;
import com.example.hachikocoffee.Domain.CategoryDomain;
import com.example.hachikocoffee.databinding.ViewholderCategoryDialogBinding;

import java.util.ArrayList;

public class CategoryDialogAdapter extends RecyclerView.Adapter<CategoryDialogAdapter.Viewholder> {
    private ArrayList<CategoryDomain> items;
    private Context context;

    private RecyclerView recyclerView;
    private NestedScrollView nestedScrollView;

    private CategoryDialog categoryDialog;

    public  CategoryDialogAdapter(ArrayList<CategoryDomain> items, RecyclerView recyclerView, NestedScrollView nestedScrollView, CategoryDialog categoryDialog){
        this.items = items;
        this.recyclerView = recyclerView;
        this.nestedScrollView = nestedScrollView;
        this.categoryDialog = categoryDialog;
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

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryDialog.dismiss();
                nestedScrollView.post(() -> nestedScrollView.smoothScrollTo(0, recyclerView.getChildAt(10).getTop()));
            }
        });
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
