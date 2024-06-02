package com.example.hachikocoffee.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hachikocoffee.Activity.AddCategoryAcivity;
import com.example.hachikocoffee.Activity.CategoryManagementActivity;
import com.example.hachikocoffee.Activity.EditCategoryActivity;
import com.example.hachikocoffee.Domain.CategoryDomain;
import com.example.hachikocoffee.Listener.OnCategoryChangedListener;
import com.example.hachikocoffee.Management.ListenerSingleton;
import com.example.hachikocoffee.databinding.ViewholderCategoryBinding;
import com.example.hachikocoffee.databinding.ViewholderCategoryItemBinding;

import java.util.ArrayList;

public class ListCategoryAdapter extends RecyclerView.Adapter<ListCategoryAdapter.Viewholder>{
    private final ArrayList<CategoryDomain> items;
    private Context context;

    public ListCategoryAdapter(ArrayList<CategoryDomain> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ListCategoryAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        ViewholderCategoryItemBinding binding = ViewholderCategoryItemBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ListCategoryAdapter.Viewholder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ListCategoryAdapter.Viewholder holder, int position) {
        holder.binding.itemTitle.setText(items.get(position).getTitle());
        holder.binding.itemID.setText(String.valueOf(items.get(position).getCategoryID()));

        Glide.with(context)
                .load(items.get(position).getImageURL())
                .into(holder.binding.icon);

        holder.binding.imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditCategoryActivity.class);
                intent.putExtra("category", items.get(position));
                ListenerSingleton.getInstance().setCategoryChangedListener((OnCategoryChangedListener) context);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        ViewholderCategoryItemBinding binding;
        public Viewholder(ViewholderCategoryItemBinding binding){
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
