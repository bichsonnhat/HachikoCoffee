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
                if (items.get(position).getTitle().toString().equals("Món Mới Phải Thử")){
                    nestedScrollView.post(() -> nestedScrollView.smoothScrollTo(0, 780));
                }
                if (items.get(position).getTitle().toString().equals("Trà Trái Cây")){
                    nestedScrollView.post(() -> nestedScrollView.smoothScrollTo(0, 8903));
                }
                if (items.get(position).getTitle().equals("CloudFee")){
                    nestedScrollView.post(() -> nestedScrollView.smoothScrollTo(0, recyclerView.getChildAt(50).getBottom() + 340));
                }
                if (items.get(position).getTitle().equals("Đá Xay Frosty")){
                    nestedScrollView.post(() -> nestedScrollView.smoothScrollTo(0, recyclerView.getChildAt(60).getTop() + 300));
                }
                if (items.get(position).getTitle().equals("Bánh Ngọt")){
                    nestedScrollView.post(() -> nestedScrollView.smoothScrollTo(0, recyclerView.getChildAt(76).getTop() + 300));
                }
                if (items.get(position).getTitle().equals("Cafe Tại Nhà")){
                    nestedScrollView.post(() -> nestedScrollView.smoothScrollTo(0, recyclerView.getChildAt(98).getTop() + 300));
                }
                if (items.get(position).getTitle().equals("Các Loại Đồ Ăn Khác")){
                    nestedScrollView.post(() -> nestedScrollView.smoothScrollTo(0, recyclerView.getChildAt(114).getTop() + 300));
                }
                if (items.get(position).getTitle().equals("Cafe")){
                    nestedScrollView.post(() -> nestedScrollView.smoothScrollTo(0, 1620));
                }
                if (items.get(position).getTitle().equals("Trà Sữa Macchiato")){
                    nestedScrollView.post(() -> nestedScrollView.smoothScrollTo(0, recyclerView.getChildAt(40).getTop() + 300));
                }
                if (items.get(position).getTitle().equals("Trà Xanh Tây Bắc")){
                    nestedScrollView.post(() -> nestedScrollView.smoothScrollTo(0, recyclerView.getChildAt(55).getTop() + 300));
                }
                if (items.get(position).getTitle().equals("Bánh Mặn")){
                    nestedScrollView.post(() -> nestedScrollView.smoothScrollTo(0, recyclerView.getChildAt(68).getTop() + 300));
                }
                if (items.get(position).getTitle().equals("Topping")){
                    nestedScrollView.post(() -> nestedScrollView.smoothScrollTo(0, recyclerView.getChildAt(90).getTop() + 300));
                }
                if (items.get(position).getTitle().equals("Chai Fresh Không Đá")){
                    nestedScrollView.post(() -> nestedScrollView.smoothScrollTo(0, recyclerView.getChildAt(106).getTop() + 300));
                }
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
