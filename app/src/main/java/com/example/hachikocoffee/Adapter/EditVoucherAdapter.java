package com.example.hachikocoffee.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hachikocoffee.Activity.EditVoucherActivity;
import com.example.hachikocoffee.Domain.DiscountDomain;
import com.example.hachikocoffee.R;

import java.util.ArrayList;
import static com.example.hachikocoffee.Activity.EditVoucherActivity.setEditInterfaceInstance;

public class EditVoucherAdapter extends RecyclerView.Adapter<EditVoucherAdapter.EditVoucherViewHolder>{
    private final ArrayList<DiscountDomain> discountArrayList;

    public EditVoucherAdapter(ArrayList<DiscountDomain> discountArrayList) {
        this.discountArrayList = discountArrayList;
    }

    @NonNull
    @Override
    public EditVoucherAdapter.EditVoucherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_edit_voucher, parent, false);
        return new EditVoucherAdapter.EditVoucherViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EditVoucherAdapter.EditVoucherViewHolder holder, int position) {
        DiscountDomain discount = discountArrayList.get(position);
        if (discount == null) {
            return;
        }

        Glide.with(holder.imageEditVoucher.getContext()).load(discount.getImageURL()).into(holder.imageEditVoucher);
        holder.tvEditVoucherName.setText(""+discount.getTitle());
        holder.tvEditVoucherID.setText(""+discount.getVoucherID());
        holder.btnEditVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEditInterfaceInstance(v.getContext());
                Intent intent = new Intent(v.getContext(), EditVoucherActivity.class);
                intent.putExtra("VoucherID", discount.getVoucherID());
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (discountArrayList != null) {
            return discountArrayList.size();
        }
        return 0;
    }

    public static class EditVoucherViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageEditVoucher;
        private final TextView tvEditVoucherName;
        private final TextView tvEditVoucherID;
        private final ConstraintLayout btnEditVoucher;
        public EditVoucherViewHolder(@NonNull View itemView) {
            super(itemView);
            imageEditVoucher = itemView.findViewById(R.id.imageEditVoucher);
            tvEditVoucherName = itemView.findViewById(R.id.tvEditVoucherName);
            tvEditVoucherID = itemView.findViewById(R.id.tvEditVoucherID);
            btnEditVoucher = itemView.findViewById(R.id.btnEditVoucher);
        }
    }
}
