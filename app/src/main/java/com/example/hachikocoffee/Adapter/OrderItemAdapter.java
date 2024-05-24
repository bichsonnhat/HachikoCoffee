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
import com.example.hachikocoffee.Domain.DiscountDomain;
import com.example.hachikocoffee.Domain.ItemsDomain;
import com.example.hachikocoffee.Domain.OrderItemDomain;
import com.example.hachikocoffee.Listener.DiscountClickListener;
import com.example.hachikocoffee.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.OrderItemViewHolder>{
    private final ArrayList<OrderItemDomain> mListOrderItem;

    public OrderItemAdapter(ArrayList<OrderItemDomain> mListOrderItem) {
        this.mListOrderItem = mListOrderItem;
    }

    @NonNull
    @Override
    public OrderItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_detail_bill, parent, false);
        return new OrderItemViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull OrderItemViewHolder holder, int position) {
        OrderItemDomain orderItem = mListOrderItem.get(position);
        if (orderItem == null) {
            return;
        }
        String itemID = orderItem.getProductID();
        DecimalFormatSymbols symbols;
        symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference().child("PRODUCTS");
        productRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot issue : snapshot.getChildren()){
                        ItemsDomain item = issue.getValue(ItemsDomain.class);
                        if (item.getProductID().equals(itemID)) {
                            Glide.with(holder.vh_detail_bill_imageDrink.getContext()).load(item.getImageURL()).into(holder.vh_detail_bill_imageDrink);
                            holder.vh_detail_bill_nameDrink.setText(item.getTitle());
                            String a = new DecimalFormat("#,###", symbols).format((long) item.getPrice());
                            holder.vh_detail_bill_PriceDrink.setText(a + "đ");
                            holder.vh_detail_bill_Size.setText(orderItem.getSize());
                            holder.vh_detail_bill_NumberOfDrink.setText(""+orderItem.getQuantity());
                            holder.vh_detail_bill_NumberOfDrink2.setText("x" + orderItem.getQuantity());
                            break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        String a = new DecimalFormat("#,###", symbols).format((long) orderItem.getTotalOrderItemPrice());
        holder.vh_detail_bill_totalPrice.setText(a + "đ");
        String topping = orderItem.getTopping();
        String newString = topping.replaceAll("[\\[\\]]", "");

        holder.vh_detail_bill_ListTopping.setText(newString);
    }

    @Override
    public int getItemCount() {
        if (mListOrderItem != null) {
            return mListOrderItem.size();
        }
        return 0;
    }

    public static class OrderItemViewHolder extends RecyclerView.ViewHolder {
        private final ImageView vh_detail_bill_imageDrink;
        private final TextView vh_detail_bill_nameDrink;
        private final TextView vh_detail_bill_Size;
        private final TextView vh_detail_bill_NumberOfDrink;
        private final TextView vh_detail_bill_PriceDrink;
        private final TextView vh_detail_bill_ListTopping;
        private final TextView vh_detail_bill_NumberOfDrink2;
        private final TextView vh_detail_bill_totalPrice;
        public OrderItemViewHolder(@NonNull View itemView) {
            super(itemView);
            vh_detail_bill_imageDrink = itemView.findViewById(R.id.vh_detail_bill_imageDrink);
            vh_detail_bill_nameDrink = itemView.findViewById(R.id.vh_detail_bill_nameDrink);
            vh_detail_bill_Size = itemView.findViewById(R.id.vh_detail_bill_Size);
            vh_detail_bill_NumberOfDrink = itemView.findViewById(R.id.vh_detail_bill_NumberOfDrink);
            vh_detail_bill_PriceDrink = itemView.findViewById(R.id.vh_detail_bill_PriceDrink);
            vh_detail_bill_ListTopping = itemView.findViewById(R.id.vh_detail_bill_ListTopping);
            vh_detail_bill_NumberOfDrink2 = itemView.findViewById(R.id.vh_detail_bill_NumberOfDrink2);
            vh_detail_bill_totalPrice = itemView.findViewById(R.id.vh_detail_bill_totalPrice);
        }
    }
}
