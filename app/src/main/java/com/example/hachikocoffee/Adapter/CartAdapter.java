package com.example.hachikocoffee.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hachikocoffee.BottomSheetDialog.DetailCart;
import com.example.hachikocoffee.Domain.CartItem;
import com.example.hachikocoffee.Management.ManagementCart;
import com.example.hachikocoffee.Listener.OnCartChangedListener;
import com.example.hachikocoffee.databinding.ViewholderItemCartBinding;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder>{
    private final ArrayList<CartItem> cartItems;
    private final FragmentManager fragmentManager;
    private final OnCartChangedListener cartChangedListener;
    Context context;

    public CartAdapter(ArrayList<CartItem> cartItems, FragmentManager fragmentManager, OnCartChangedListener cartChangedListener) {
        this.cartItems = cartItems;
        this.fragmentManager = fragmentManager;
        this.cartChangedListener = cartChangedListener;
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

            //format money
            DecimalFormatSymbols symbols = new DecimalFormatSymbols();
            symbols.setGroupingSeparator('.');
            String a = new DecimalFormat("#,###", symbols).format(cartItem.getTotalCost());
            holder.binding.totalDetailCost.setText(a + "đ");

            if (cartItem.getToppings() != null && cartItem.getToppings().size() != 0) {
                String tpList = String.join(", ", cartItem.getToppings());
                holder.binding.topping1.setText(tpList);
                holder.binding.topping1.setVisibility(View.VISIBLE);
                Log.d("CartAdapter", "Toppings list: " + tpList);
            } else {
                holder.binding.topping1.setVisibility(View.GONE);
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CartItem item = cartItems.get(position);
                    DetailCart detailBottomSheetDialog = new DetailCart(item.getProductId(), item.getProductName(), item.getCost());
                    detailBottomSheetDialog.show(fragmentManager, "DetailCart");
                }
            });

            holder.binding.deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (position != RecyclerView.NO_POSITION) {

                        ManagementCart.getInstance().removeFromCart(position);

                        notifyItemRemoved(position);

                        notifyItemRangeChanged(position, cartItems.size());

                        if (cartChangedListener != null){
                            cartChangedListener.onCartChanged();
                        }
                    }
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
