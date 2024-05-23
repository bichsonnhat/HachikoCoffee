package com.example.hachikocoffee.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hachikocoffee.Domain.CartItem;
import com.example.hachikocoffee.Domain.DiscountDomain;
import com.example.hachikocoffee.R;
import com.example.hachikocoffee.databinding.ViewholderItemCartBinding;
import com.example.hachikocoffee.databinding.ViewholderVoucherticketBinding;
import com.google.android.material.transition.Hold;

import java.util.ArrayList;

public class DiscountAdapter1 extends RecyclerView.Adapter<DiscountAdapter1.ViewHolder>{
    private OnDiscountSelectedListener listener;
    public void setOnDiscountSelectedListener(OnDiscountSelectedListener listener) {
        this.listener = listener;
    }

    public interface OnDiscountSelectedListener {
        void onDiscountSelected(DiscountDomain discount);
    }

    private int selectedItem = RecyclerView.NO_POSITION;
    private ArrayList<DiscountDomain> discountArrayList;
    Context context;
    private DiscountDomain selectedDiscount;


    public DiscountAdapter1(ArrayList<DiscountDomain> discountArrayList) {
        this.discountArrayList = discountArrayList;
    }

    @NonNull
    @Override
    public DiscountAdapter1.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        ViewholderVoucherticketBinding binding = ViewholderVoucherticketBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull DiscountAdapter1.ViewHolder holder, int position) {
        DiscountDomain discount = discountArrayList.get(position);
        if (discount == null) {
            return;
        }

        Glide.with(holder.binding.couponImg.getContext()).load(discount.getImageURL()).into(holder.binding.couponImg);
        holder.binding.couponDetail.setText(discount.getTitle());
        holder.binding.couponExpired.setText("Hết hạn " + discount.getExpiryDate());
        if (position == selectedItem) {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.selectedItemColor));
        } else {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.defaultItemColor));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int previousSelectedItem = selectedItem;
                selectedItem = position;
                notifyItemChanged(previousSelectedItem);
                notifyItemChanged(selectedItem);
                selectedDiscount = discount;

                if (listener != null) {
                    listener.onDiscountSelected(discount);
                }
            }
        });
    }

    public DiscountDomain getSelectedDiscount() {
        return selectedDiscount;
    }

    @Override
    public int getItemCount() {
        return discountArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ViewholderVoucherticketBinding binding;
        public ViewHolder(ViewholderVoucherticketBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
