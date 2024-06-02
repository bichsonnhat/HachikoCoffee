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
import com.example.hachikocoffee.Activity.EditProductActivity;
import com.example.hachikocoffee.Domain.ItemsDomain;
import com.example.hachikocoffee.R;

import java.util.ArrayList;
import static com.example.hachikocoffee.Activity.EditProductActivity.setEditInterfaceInstance;

public class EditProductAdapter extends RecyclerView.Adapter<EditProductAdapter.EditProductViewHolder>{
    private final ArrayList<ItemsDomain> itemsDomainArrayList;

    public EditProductAdapter(ArrayList<ItemsDomain> itemsDomainArrayList) {
        this.itemsDomainArrayList = itemsDomainArrayList;
    }
    @NonNull
    @Override
    public EditProductAdapter.EditProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_edit_product, parent, false);
        return new EditProductAdapter.EditProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EditProductAdapter.EditProductViewHolder holder, int position) {
        ItemsDomain itemsDomain = itemsDomainArrayList.get(position);
        if (itemsDomain == null) {
            return;
        }

        Glide.with(holder.imageEditProduct.getContext()).load(itemsDomain.getImageURL()).into(holder.imageEditProduct);
        holder.tvEditProductName.setText(""+itemsDomain.getTitle());
        holder.tvEditProductID.setText(""+itemsDomain.getProductID());
        holder.btnEditProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEditInterfaceInstance(v.getContext());
                Intent intent = new Intent(v.getContext(), EditProductActivity.class);
                intent.putExtra("productID", itemsDomain.getProductID());
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (itemsDomainArrayList != null) {
            return itemsDomainArrayList.size();
        }
        return 0;
    }

    public class EditProductViewHolder extends RecyclerView.ViewHolder{
        private final ImageView imageEditProduct;
        private final TextView tvEditProductName;
        private final TextView tvEditProductID;
        private final ConstraintLayout btnEditProduct;
        public EditProductViewHolder(@NonNull View parent) {
            super(parent);
            imageEditProduct = parent.findViewById(R.id.imageEditProduct);
            tvEditProductName = parent.findViewById(R.id.tvEditProductName);
            tvEditProductID = parent.findViewById(R.id.tvEditProductID);
            btnEditProduct = parent.findViewById(R.id.btnEditProduct);
        }
    }
}
