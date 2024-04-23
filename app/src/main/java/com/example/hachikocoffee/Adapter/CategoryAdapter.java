package com.example.hachikocoffee.Adapter;

import android.content.Context;
import android.view.LayoutInflater;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hachikocoffee.Domain.CategoryDomain;
import com.example.hachikocoffee.databinding.ViewholderCategoryBinding;

import java.util.ArrayList;
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.Viewholder> {
    private ArrayList<CategoryDomain> items;
    private Context context;

    public CategoryAdapter(ArrayList<CategoryDomain> items){
        this.items = items;
    }

    @NonNull
    @Override
    public CategoryAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        ViewholderCategoryBinding binding = ViewholderCategoryBinding.inflate(LayoutInflater.from(context), parent, false);
        return new Viewholder(binding);
    }

//    @Override
//    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
//        super.onAttachedToRecyclerView(recyclerView);
//
//        GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
//        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
//            @Override
//            public int getSpanSize(int position) {
//                return 1; // Mỗi item chiếm 1 cột
//            }
//        });
//    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.Viewholder holder, int position) {
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
        ViewholderCategoryBinding binding;

        public Viewholder(ViewholderCategoryBinding binding){
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
