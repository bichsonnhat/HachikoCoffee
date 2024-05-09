package com.example.hachikocoffee.Adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hachikocoffee.Domain.ShopDomain;
import com.example.hachikocoffee.R;
import com.example.hachikocoffee.Listener.ShopClickListener;

import java.util.List;

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.ShopViewHolder>{
    private final List<ShopDomain> mListShop;
    private final ShopClickListener shopClickListener;

    public ShopAdapter(List<ShopDomain> mListShop, ShopClickListener shopClickListener) {
        this.mListShop = mListShop;
        this.shopClickListener = shopClickListener;
    }

    @NonNull
    @Override
    public ShopViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_nearest_store, parent, false);
        return new ShopViewHolder(view);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onBindViewHolder(@NonNull ShopViewHolder holder, int position) {
        ShopDomain shop = mListShop.get(position);
        if (shop == null) {
            return;
        }

        Glide.with(holder.shopImage.getContext()).load(shop.getImageURL()).into(holder.shopImage);
        holder.shopAddress.setText(shop.getAddress());
        holder.shopCoordinate.setText(shop.getCoordinate());

        holder.shopItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shopClickListener.onClickShopItem(shop);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mListShop != null) {
            return mListShop.size();
        }
        return 0;
    }

    public static class ShopViewHolder extends RecyclerView.ViewHolder {

        private final ConstraintLayout shopItem;
        private final ImageView shopImage;
        private final TextView shopAddress;
        private final TextView shopCoordinate;

        public ShopViewHolder(@NonNull View itemView) {
            super(itemView);

            shopItem = itemView.findViewById(R.id.shopitem);
            shopImage = itemView.findViewById(R.id.shopimage);
            shopAddress = itemView.findViewById(R.id.shopaddress);
            shopCoordinate = itemView.findViewById(R.id.shopcoordinate);
        }
    }
}
