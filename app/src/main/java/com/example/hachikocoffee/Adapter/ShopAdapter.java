package com.example.hachikocoffee.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hachikocoffee.Domain.ShopDomain;
import com.example.hachikocoffee.R;

import java.util.List;

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.ShopViewHolder>{

    private Context mContext;
    private List<ShopDomain> mListShop;

//    public ShopAdapter(Context mContext) {
//        this.mContext = mContext;
//    }


    public ShopAdapter(List<ShopDomain> mListShop) {
        this.mListShop = mListShop;
    }

//    public void setData(List<ShopDomain> list) {
//        this.mListShop = list;
//        notifyDataSetChanged();
//    }

    @NonNull
    @Override
    public ShopViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_nearest_store, parent, false);
        return new ShopViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShopViewHolder holder, int position) {
        ShopDomain shop = mListShop.get(position);
        if (shop == null) {
            return;
        }

        holder.shopImage.setImageResource(shop.getResourceId());
        holder.shopAddress.setText(shop.getAddress());
        holder.shopDistance.setText("Cách đây " + shop.getDistance() + " km");
    }

    @Override
    public int getItemCount() {
        if (mListShop != null) {
            return mListShop.size();
        }
        return 0;
    }

    public class ShopViewHolder extends RecyclerView.ViewHolder {

        private ImageView shopImage;
        private TextView shopAddress;
        private TextView shopDistance;

        public ShopViewHolder(@NonNull View itemView) {
            super(itemView);

            shopImage = itemView.findViewById(R.id.anh_cua_hang);
            shopAddress = itemView.findViewById(R.id.dia_chi);
            shopDistance = itemView.findViewById(R.id.khoang_cach);
        }
    }
}
