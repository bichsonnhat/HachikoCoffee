package com.example.hachikocoffee.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hachikocoffee.Domain.CategoryDomain;
import com.example.hachikocoffee.databinding.ViewholderCategoryBinding;

import java.util.ArrayList;
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.Viewholder> {
    private final ArrayList<CategoryDomain> items;
    private Context context;
    private final NestedScrollView nestedScrollView;
    private final RecyclerView recyclerView;
    private int y = 0;

    public CategoryAdapter(ArrayList<CategoryDomain> items, NestedScrollView nestedScrollView, RecyclerView recyclerView){
        this.items = items;
        this.nestedScrollView = nestedScrollView;
        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public CategoryAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        ViewholderCategoryBinding binding = ViewholderCategoryBinding.inflate(LayoutInflater.from(context), parent, false);
        return new Viewholder(binding);
    }


    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.Viewholder holder, @SuppressLint("RecyclerView") int position) {
        holder.binding.categoryName.setText(items.get(position).getTitle());

        Glide.with(context)
                .load(items.get(position).getImageURL())
                .into(holder.binding.categryImage);

        if (recyclerView == null){
            Toast.makeText(context, "RecyclerView is null", Toast.LENGTH_SHORT).show();
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i <= 12; ++i){
                    if (items.get(i).getTitle().toString().equals("Cafe")){
                        int[] location = new int[2];
                        v.getLocationOnScreen(location);
                        y = location[1];
                        break;
                    }
                }
                if (items.get(position).getTitle().toString().equals("Món mới phải thử")){
                    nestedScrollView.post(() -> nestedScrollView.smoothScrollTo(0, recyclerView.getChildAt(2).getTop() + y + y / 3 + y / 5));
                }
                if (items.get(position).getTitle().toString().equals("Trà Trái Cây")){
                    nestedScrollView.post(() -> nestedScrollView.smoothScrollTo(0, recyclerView.getChildAt(26).getTop() + y + y / 3 + y / 5));
                }
                if (items.get(position).getTitle().equals("CloudFee")){
                    nestedScrollView.post(() -> nestedScrollView.smoothScrollTo(0, recyclerView.getChildAt(50).getBottom() + y + y / 3 + y / 5));
                }
                if (items.get(position).getTitle().equals("Đá Xay Frosty")){
                    nestedScrollView.post(() -> nestedScrollView.smoothScrollTo(0, recyclerView.getChildAt(60).getTop() + y + y / 3 + y / 5));
                }
                if (items.get(position).getTitle().equals("Bánh Ngọt")){
                    nestedScrollView.post(() -> nestedScrollView.smoothScrollTo(0, recyclerView.getChildAt(76).getTop() + y + y / 3 + y / 5));
                }
                if (items.get(position).getTitle().equals("Cafe Tại Nhà")){
                    nestedScrollView.post(() -> nestedScrollView.smoothScrollTo(0, recyclerView.getChildAt(98).getTop() + y + y / 3 + y / 5));
                }
                if (items.get(position).getTitle().equals("Các Loại Đồ Ăn Khác")){
                    nestedScrollView.post(() -> nestedScrollView.smoothScrollTo(0, recyclerView.getChildAt(114).getTop() + y + y / 3 + y / 5));
                }
                if (items.get(position).getTitle().equals("Cafe")){
                    nestedScrollView.post(() -> nestedScrollView.smoothScrollTo(0, recyclerView.getChildAt(4).getTop() + y + y / 3));
                }
                if (items.get(position).getTitle().equals("Trà Sữa Macchiato")){
                    nestedScrollView.post(() -> nestedScrollView.smoothScrollTo(0, recyclerView.getChildAt(39).getTop() + y + y / 3));
                }
                if (items.get(position).getTitle().equals("Trà Xanh Tây Bắc")){
                    nestedScrollView.post(() -> nestedScrollView.smoothScrollTo(0, recyclerView.getChildAt(54).getTop() + y + y / 3));
                }
                if (items.get(position).getTitle().equals("Bánh Mặn")){
                    nestedScrollView.post(() -> nestedScrollView.smoothScrollTo(0, recyclerView.getChildAt(67).getTop() + y + y / 3));
                }
                if (items.get(position).getTitle().equals("Topping")){
                    nestedScrollView.post(() -> nestedScrollView.smoothScrollTo(0, recyclerView.getChildAt(89).getTop() + y + y / 3));
                }
                if (items.get(position).getTitle().equals("Chai Fresh Không Đá")){
                    nestedScrollView.post(() -> nestedScrollView.smoothScrollTo(0, recyclerView.getChildAt(105).getTop() + y + y / 3));
                }
//                Toast.makeText(context, "Nhatwooo", Toast.LENGTH_SHORT).show();
            }
        });

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