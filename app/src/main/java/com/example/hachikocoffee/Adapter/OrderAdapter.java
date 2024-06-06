package com.example.hachikocoffee.Adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hachikocoffee.Activity.PendingOrdersActivity;
import com.example.hachikocoffee.Domain.OrderDomain;
import com.example.hachikocoffee.Fragment.OrderHistoryCancelledFragment;
import com.example.hachikocoffee.Fragment.OrderHistoryFinishedFragment;
import com.example.hachikocoffee.Listener.CanceledClickListener;
import com.example.hachikocoffee.Listener.FinishedClickListener;
import com.example.hachikocoffee.Listener.OrderClickListener;
import com.example.hachikocoffee.OrderDetail;
import com.example.hachikocoffee.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder>{
    private final List<OrderDomain> mListOrder;
    private final OrderClickListener orderClickListener;
    private final PendingOrdersActivity activity;
    private static CanceledClickListener canceledClickListener;
    private static FinishedClickListener finishedClickListener;

    public OrderAdapter(List<OrderDomain> mListOrder, OrderClickListener orderClickListener) {
        this.mListOrder = mListOrder;
        this.orderClickListener = orderClickListener;
        this.activity = null;
    }

    public OrderAdapter(List<OrderDomain> mListOrder, OrderClickListener orderClickListener, PendingOrdersActivity activity) {
        this.mListOrder = mListOrder;
        this.orderClickListener = orderClickListener;
        this.activity = activity;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_order_history, parent, false);
        return new OrderViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if (this.activity != null){
            holder.orderHistory_accept.setVisibility(View.GONE);
            holder.orderHistory_cancel.setVisibility(View.GONE);
        }
        else{
            holder.orderHistory_accept.setVisibility(View.VISIBLE);
            holder.orderHistory_cancel.setVisibility(View.VISIBLE);
        }


        OrderDomain order = mListOrder.get(position);
        if (order == null) {
            return;
        }
        DecimalFormatSymbols symbols;
        symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        String a = new DecimalFormat("#,###", symbols).format((long) order.getCost());
        holder.orderHistory_id.setText(order.getOrderID());
        holder.orderHistory_cost.setText(a + "đ");
        holder.orderHistory_date.setText(order.getOrderCreatedTime().substring(0, 10));
        holder.orderHistory_time.setText(order.getOrderCreatedTime().substring(11));

        holder.orderItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), OrderDetail.class);
                intent.putExtra("OrderID", order.getOrderID());
                v.getContext().startActivity(intent);
//                orderClickListener.onClickOrderItem(order);
            }
        });

        if (order.getOrderStatus().equals("Finished")){
            order.setOrderStatus("Finished");
            holder.orderHistory_state.setText("Đã hoàn tất");
            holder.orderHistory_state.setTextColor(holder.itemView.getResources().getColor(R.color.green));
            holder.orderHistory_accept.setVisibility(View.INVISIBLE);
            holder.orderHistory_cancel.setVisibility(View.INVISIBLE);
            return;
        }

        if (order.getOrderStatus().equals("Canceled")){
            order.setOrderStatus("Canceled");
            holder.orderHistory_state.setText("Đã hủy");
            holder.orderHistory_state.setTextColor(holder.itemView.getResources().getColor(R.color.red));
            holder.orderHistory_accept.setVisibility(View.INVISIBLE);
            holder.orderHistory_cancel.setVisibility(View.INVISIBLE);
            return;
        }

        holder.orderHistory_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference historyOrderRef = FirebaseDatabase.getInstance().getReference().child("ORDER");
                historyOrderRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                OrderDomain orderDomain = childSnapshot.getValue(OrderDomain.class);
                                if (order.getOrderID().equals(orderDomain.getOrderID())) {
                                    Map<String, Object> updates = new HashMap<>();
                                    updates.put("orderStatus", "Finished");
                                    childSnapshot.getRef().updateChildren(updates);
                                    finishedClickListener.onFinishedClick();
                                    break;
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
//                order.setOrderStatus("Finished");
//                holder.orderHistory_state.setText("Đã hoàn tất");
//                holder.orderHistory_state.setTextColor(v.getResources().getColor(R.color.green));
//                holder.orderHistory_accept.setVisibility(View.INVISIBLE);
//                holder.orderHistory_cancel.setVisibility(View.INVISIBLE);
                mListOrder.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, mListOrder.size());
                //OrderHistoryProcessingFragment orderHistoryProcessingFragment = new OrderHistoryProcessingFragment();
                //orderHistoryProcessingFragment.removeOrder(order);

                //OrderHistoryFinishedFragment orderHistoryFinishedFragment = new OrderHistoryFinishedFragment();
                //orderHistoryFinishedFragment.addOrder(order);
            }
        });

        holder.orderHistory_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference historyOrderRef = FirebaseDatabase.getInstance().getReference().child("ORDER");
                historyOrderRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                OrderDomain orderDomain = childSnapshot.getValue(OrderDomain.class);
                                if (order.getOrderID().equals(orderDomain.getOrderID())) {
                                    Map<String, Object> updates = new HashMap<>();
                                    updates.put("orderStatus", "Canceled");
                                    childSnapshot.getRef().updateChildren(updates);
//                                    if (canceledClickListener != null){
//                                        canceledClickListener.onCanceledClick();
//                                    }
                                    break;
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
//                order.setOrderStatus("Finished");
//                holder.orderHistory_state.setText("Đã hoàn tất");
//                holder.orderHistory_state.setTextColor(v.getResources().getColor(R.color.green));
//                holder.orderHistory_accept.setVisibility(View.INVISIBLE);
//                holder.orderHistory_cancel.setVisibility(View.INVISIBLE);
                mListOrder.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, mListOrder.size());
                //OrderHistoryProcessingFragment orderHistoryProcessingFragment = new OrderHistoryProcessingFragment();
                //orderHistoryProcessingFragment.removeOrder(order);

                //OrderHistoryFinishedFragment orderHistoryFinishedFragment = new OrderHistoryFinishedFragment();
                //orderHistoryFinishedFragment.addOrder(order);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (mListOrder != null) {
            return mListOrder.size();
        }
        return 0;
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {
        private final TextView orderHistory_cost;
        private final TextView orderHistory_date;
        private final TextView orderHistory_time;
        private final ConstraintLayout orderItem;
        private final CardView orderHistory_accept;
        private final CardView orderHistory_cancel;
        private final TextView orderHistory_state;
        private final TextView orderHistory_id;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);

            orderHistory_cost = itemView.findViewById(R.id.orderHis_cost);
            orderHistory_date = itemView.findViewById(R.id.orderHis_date);
            orderHistory_time = itemView.findViewById(R.id.orderHis_time);
            orderItem = itemView.findViewById(R.id.order_history_item);
            orderHistory_accept = itemView.findViewById(R.id.orderHis_accept);
            orderHistory_cancel = itemView.findViewById(R.id.orderHis_cancel);
            orderHistory_state = itemView.findViewById(R.id.orderHis_state);
            orderHistory_id = itemView.findViewById(R.id.orderHis_id);
        }
    }

    public static void setInterfaceInstanceFinished(OrderHistoryFinishedFragment context){
        finishedClickListener = (FinishedClickListener) context;
    }

    public static void setInterfaceInstanceCanceled(OrderHistoryCancelledFragment context){
        canceledClickListener = (CanceledClickListener) context;
    }
}