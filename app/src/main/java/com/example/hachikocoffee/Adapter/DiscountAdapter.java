package com.example.hachikocoffee.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hachikocoffee.Listener.DiscountClickListener;
import com.example.hachikocoffee.Domain.DiscountDomain;
import com.example.hachikocoffee.R;

import java.util.List;

public class DiscountAdapter extends RecyclerView.Adapter<DiscountAdapter.DiscountViewHolder>{
    private final List<DiscountDomain> mListDiscount;
    private final DiscountClickListener discountClickListener;

    public DiscountAdapter(List<DiscountDomain> mListDiscount, DiscountClickListener discountClickListener) {
        this.mListDiscount = mListDiscount;
        this.discountClickListener = discountClickListener;
    }

    @NonNull
    @Override
    public DiscountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_voucherticket, parent, false);
        return new DiscountViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DiscountViewHolder holder, int position) {
        DiscountDomain discount = mListDiscount.get(position);
        if (discount == null) {
            return;
        }

        Glide.with(holder.discountImage.getContext()).load(discount.getImageURL()).into(holder.discountImage);
        holder.discountTitle.setText(discount.getTitle());
        holder.discountExpired.setText("Hết hạn " + discount.getExpiryDate());

        holder.discountItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                discountClickListener.onClickDiscountItem(discount);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mListDiscount != null) {
            return mListDiscount.size();
        }
        return 0;
    }

    public static class DiscountViewHolder extends RecyclerView.ViewHolder {

        private final ConstraintLayout discountItem;
        private final ImageView discountImage;
        private final TextView discountTitle;
        private final TextView  discountExpired;
        public DiscountViewHolder(@NonNull View itemView) {
            super(itemView);

            discountItem = itemView.findViewById(R.id.item_discount);
            discountImage = itemView.findViewById(R.id.coupon_img);
            discountTitle = itemView.findViewById(R.id.coupon_detail);
            discountExpired = itemView.findViewById(R.id.coupon_expired);
        }
    }
}
