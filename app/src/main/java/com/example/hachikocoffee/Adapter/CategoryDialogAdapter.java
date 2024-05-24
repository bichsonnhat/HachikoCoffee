package com.example.hachikocoffee.Adapter;

import android.content.Context;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hachikocoffee.BottomSheetDialog.CategoryDialog;
import com.example.hachikocoffee.Domain.CategoryDomain;
import com.example.hachikocoffee.databinding.ViewholderCategoryDialogBinding;

import java.util.ArrayList;

public class CategoryDialogAdapter extends RecyclerView.Adapter<CategoryDialogAdapter.Viewholder> {
    private ArrayList<CategoryDomain> items;
    private Context context;

    private RecyclerView recyclerView;
    private NestedScrollView nestedScrollView;

    private CategoryDialog categoryDialog;
    private int y = 0;

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
                y = recyclerView.getChildAt(1).getTop();
                if (items.get(position).getTitle().toString().equals("Món mới phải thử")){
                    y = y + (y * 10) / 17;
                    nestedScrollView.post(() -> nestedScrollView.smoothScrollTo(0, recyclerView.getChildAt(2).getTop() + y + y / 3 + y / 5));
                }
                if (items.get(position).getTitle().toString().equals("Trà Trái Cây")){
                    y = y + (y * 10) / 17;
                    nestedScrollView.post(() -> nestedScrollView.smoothScrollTo(0, recyclerView.getChildAt(26).getTop() + y + y / 3 + y / 5));
                }
                if (items.get(position).getTitle().equals("CloudFee")){
                    y = y + (y * 10) / 17;
                    nestedScrollView.post(() -> nestedScrollView.smoothScrollTo(0, recyclerView.getChildAt(50).getBottom() + y + y / 3 + y / 5));
                }
                if (items.get(position).getTitle().equals("Đá Xay Frosty")){
                    y = y + (y * 10) / 17;
                    nestedScrollView.post(() -> nestedScrollView.smoothScrollTo(0, recyclerView.getChildAt(60).getTop() + y + y / 3 + y / 5));
                }
                if (items.get(position).getTitle().equals("Bánh Ngọt")){
                    y = y + (y * 10) / 17;
                    nestedScrollView.post(() -> nestedScrollView.smoothScrollTo(0, recyclerView.getChildAt(76).getTop() + y + y / 3 + y / 5));
                }
                if (items.get(position).getTitle().equals("Cafe Tại Nhà")){
                    y = y + (y * 10) / 17;
                    nestedScrollView.post(() -> nestedScrollView.smoothScrollTo(0, recyclerView.getChildAt(98).getTop() + y + y / 3 + y / 5));
                }
                if (items.get(position).getTitle().equals("Các Loại Đồ Ăn Khác")){
                    y = y + (y * 10) / 17;
                    nestedScrollView.post(() -> nestedScrollView.smoothScrollTo(0, recyclerView.getChildAt(114).getTop() + y + y / 3 + y / 5));
                }
                if (items.get(position).getTitle().equals("Cafe")){
                    y = y + 3 * y;
                    nestedScrollView.post(() -> nestedScrollView.smoothScrollTo(0, recyclerView.getChildAt(4).getTop() + y + y / 3));
                }
                if (items.get(position).getTitle().equals("Trà Sữa Macchiato")){
                    y = y + 3 * y;
                    nestedScrollView.post(() -> nestedScrollView.smoothScrollTo(0, recyclerView.getChildAt(39).getTop() + y + y / 3));
                }
                if (items.get(position).getTitle().equals("Trà Xanh Tây Bắc")){
                    y = y + 3 * y;
                    nestedScrollView.post(() -> nestedScrollView.smoothScrollTo(0, recyclerView.getChildAt(54).getTop() + y + y / 3));
                }
                if (items.get(position).getTitle().equals("Bánh Mặn")){
                    y = y + 3 * y;
                    nestedScrollView.post(() -> nestedScrollView.smoothScrollTo(0, recyclerView.getChildAt(67).getTop() + y + y / 3));
                }
                if (items.get(position).getTitle().equals("Topping")){
                    y = y + 3 * y;
                    nestedScrollView.post(() -> nestedScrollView.smoothScrollTo(0, recyclerView.getChildAt(89).getTop() + y + y / 3));
                }
                if (items.get(position).getTitle().equals("Chai Fresh Không Đá")){
                    y = y + 3 * y;
                    nestedScrollView.post(() -> nestedScrollView.smoothScrollTo(0, recyclerView.getChildAt(105).getTop() + y + y / 3));
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
