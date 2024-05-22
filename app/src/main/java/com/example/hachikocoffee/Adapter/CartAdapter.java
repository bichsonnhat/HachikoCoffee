package com.example.hachikocoffee.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hachikocoffee.BottomSheetDialog.DetailCart;
import com.example.hachikocoffee.Domain.CartItem;
import com.example.hachikocoffee.Management.ManagementCart;
import com.example.hachikocoffee.Management.ManagementUser;
import com.example.hachikocoffee.databinding.ViewholderItemCartBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder>{
    private ArrayList<CartItem> cartItems;
    private final FragmentManager fragmentManager;
    Context context;
    public class CartFirebase {
        private List<Object> carts;

        public List<Object> getCarts() {
            return carts;
        }

        public void setCarts(List<Object> carts) {
            this.carts = carts;
        }
    }

    class CartItemFirebase {
        private Map<String, Object> item;
        private int itemCount;
        private int noId;

        public Map<String, Object> getItem() {
            return item;
        }

        public void setItem(Map<String, Object> item) {
            this.item = item;
        }

        public int getItemCount() {
            return itemCount;
        }

        public void setItemCount(int itemCount) {
            this.itemCount = itemCount;
        }

        public int getNoId() {
            return noId;
        }

        public void setNoId(int noId) {
            this.noId = noId;
        }
    }

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
            FirebaseDatabase databaseReference = FirebaseDatabase.getInstance();
            DatabaseReference ref = databaseReference.getReference("CARTS");
            DatabaseReference userRef = ref.child(String.valueOf(ManagementUser.getInstance().getUserId()));
            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot cartItemSnapshot : snapshot.getChildren()) {
                        if (!"itemCount".equals(cartItemSnapshot.getKey()) && !"noId".equals(cartItemSnapshot.getKey())
                                && !"recipentName".equals(cartItemSnapshot.getKey()) && !"recipentPhone".equals(cartItemSnapshot.getKey())
                                && !"orderTime".equals(cartItemSnapshot.getKey())) {
                            CartItem item = cartItemSnapshot.getValue(CartItem.class);
                            assert item != null;
                            if (item.getCartItemId().equals(cartItem.getCartItemId())){
                                updateItem(item);
                                break;
                            }
//                            Toast.makeText(context, "Name"+item.getQuantity(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                private void updateItem(CartItem item) {
                    holder.binding.itemTitle.setText("x" + item.getQuantity() + " " + item.getProductName());
                    holder.binding.itemSize.setText(item.getSize());

                    //format money
                    DecimalFormatSymbols symbols = new DecimalFormatSymbols();
                    symbols.setGroupingSeparator('.');
                    String a = new DecimalFormat("#,###", symbols).format(item.getTotalCost());
                    holder.binding.totalDetailCost.setText(a + "đ");



                    if (item.getToppings() != null && item.getToppings().size() != 0) {
                        String tpList = String.join(", ", item.getToppings());
                        holder.binding.topping1.setText(tpList);
                        holder.binding.topping1.setVisibility(View.VISIBLE);
                        Log.d("CartAdapter", "Toppings list: " + tpList);
                    } else {
                        holder.binding.topping1.setVisibility(View.GONE);
                    }

                    if (!item.getNote().isEmpty()){
                        holder.binding.notes.setText(item.getNote());
                        holder.binding.notes.setVisibility(View.VISIBLE);
                    }
                    else{
                        holder.binding.notes.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CartItem item = cartItems.get(position);
                    Log.d("CartAdapter", "position: " + position);
                    Log.d("CartAdapter", "size: " + ManagementCart.getInstance().getCartItems().size());
                    DetailCart detailBottomSheetDialog = new DetailCart(item, position, CartAdapter.this);
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

                    }
                }
            });
//            Toast.makeText(context, cartItem.getSize().toString() + " " + cartItem.getQuantity(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public void setCartItems(ArrayList<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ViewholderItemCartBinding binding;

        public ViewHolder(ViewholderItemCartBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }
}
