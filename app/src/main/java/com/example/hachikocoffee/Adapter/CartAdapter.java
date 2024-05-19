package com.example.hachikocoffee.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hachikocoffee.BottomSheetDialog.DetailCart;
import com.example.hachikocoffee.BottomSheetDialog.ProductDetail;
import com.example.hachikocoffee.Domain.CartItem;
import com.example.hachikocoffee.Domain.ItemsDomain;
import com.example.hachikocoffee.Listener.OnItemClickListener;
import com.example.hachikocoffee.databinding.ViewholderItemCartBinding;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder>{
    private final ArrayList<CartItem> cartItems;
    private final FragmentManager fragmentManager;
    Context context;

    public CartAdapter(ArrayList<CartItem> cartItems, FragmentManager fragmentManager) {
        this.cartItems = cartItems;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        ViewholderItemCartBinding binding = ViewholderItemCartBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CartAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        CartItem cartItem = cartItems.get(position);
        if (cartItem != null){
            holder.binding.itemTitle.setText("x" + cartItem.getQuantity() + " " + cartItem.getProductName());
            holder.binding.itemSize.setText(cartItem.getSize());
            holder.binding.totalDetailCost.setText(cartItem.getTotalCost() + "đ");
            holder.binding.topping1.setText(cartItem.getToppings().toString());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CartItem item = cartItems.get(position);
                    DetailCart detailBottomSheetDialog = new DetailCart(item.getProductId(), item.getProductName(), item.getCost());
                    detailBottomSheetDialog.show(fragmentManager, "DetailCart");
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ViewholderItemCartBinding binding;

        public ViewHolder(ViewholderItemCartBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }
}
