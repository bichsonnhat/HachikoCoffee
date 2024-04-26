package com.example.hachikocoffee.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hachikocoffee.Domain.DiscountDomain;
import com.example.hachikocoffee.R;

import java.time.LocalDate;
import java.util.List;

public class DiscountAdapter extends RecyclerView.Adapter<DiscountAdapter.DiscountViewHolder>{
    private Context mContext;
    private List<DiscountDomain> mListDiscount;

    public DiscountAdapter(List<DiscountDomain> mListDiscount) {
        this.mListDiscount = mListDiscount;
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

        holder.discountImage.setImageResource(discount.getResourceId());
        holder.discountDetail.setText(discount.getContent());
        holder.discountExpired.setText("Hết hạn trong " + discount.getExpired() + " ngày");

        //if (discount.getExpired().equals(LocalDate.now()))
        //{
        //    holder.discountExpired.setText("Hết hạn trong hôm nay");
        //} else {
        //    holder.discountExpired.setText("Hết hạn trong " + discount.getExpired() + " ngày");
        //}
    }

    @Override
    public int getItemCount() {
        if (mListDiscount != null) {
            return mListDiscount.size();
        }
        return 0;
    }

    public class DiscountViewHolder extends RecyclerView.ViewHolder {

        private ImageView discountImage;
        private TextView  discountDetail;
        private TextView  discountExpired;
        public DiscountViewHolder(@NonNull View itemView) {
            super(itemView);

            discountImage = itemView.findViewById(R.id.coupon_img);
            discountDetail = itemView.findViewById(R.id.coupon_detail);
            discountExpired = itemView.findViewById(R.id.coupon_expired);
        }
    }
}
