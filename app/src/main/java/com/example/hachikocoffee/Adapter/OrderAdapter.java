package com.example.hachikocoffee.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hachikocoffee.Domain.OrderDomain;
import com.example.hachikocoffee.Fragment.OrderHistoryCancelledFragment;
import com.example.hachikocoffee.Fragment.OrderHistoryFinishedFragment;
import com.example.hachikocoffee.Fragment.OrderHistoryProcessingFragment;
import com.example.hachikocoffee.Listener.OrderClickListener;
import com.example.hachikocoffee.R;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder>{
    private final List<OrderDomain> mListOrder;
    private final OrderClickListener orderClickListener;

    public OrderAdapter(List<OrderDomain> mListOrder, OrderClickListener orderClickListener) {
        this.mListOrder = mListOrder;
        this.orderClickListener = orderClickListener;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_order_history, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        OrderDomain order = mListOrder.get(position);
        if (order == null) {
            return;
        }

        holder.orderHistory_cost.setText(order.getCost() + "đ");
        holder.orderHistory_date.setText(order.getOrderTime().substring(0, 10));
        holder.orderHistory_time.setText(order.getOrderTime().substring(11));

        holder.orderItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderClickListener.onClickOrderItem(order);
            }
        });

        holder.orderHistory_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                order.setOrderStatus("Finished");
                holder.orderHistory_state.setText("Đã hoàn tất");
                holder.orderHistory_state.setTextColor(v.getResources().getColor(R.color.green));
                holder.orderHistory_accept.setVisibility(View.INVISIBLE);
                holder.orderHistory_cancel.setVisibility(View.INVISIBLE);

                //OrderHistoryProcessingFragment orderHistoryProcessingFragment = new OrderHistoryProcessingFragment();
                //orderHistoryProcessingFragment.removeOrder(order);

                //OrderHistoryFinishedFragment orderHistoryFinishedFragment = new OrderHistoryFinishedFragment();
                //orderHistoryFinishedFragment.addOrder(order);
            }
        });

        holder.orderHistory_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                order.setOrderStatus("Canceled");
                holder.orderHistory_state.setText("Đã hủy");
                holder.orderHistory_state.setTextColor(v.getResources().getColor(R.color.red));
                holder.orderHistory_accept.setVisibility(View.INVISIBLE);
                holder.orderHistory_cancel.setVisibility(View.INVISIBLE);

                //OrderHistoryProcessingFragment orderHistoryProcessingFragment = new OrderHistoryProcessingFragment();
                //orderHistoryProcessingFragment.removeOrder(order);

                //OrderHistoryCancelledFragment orderHistoryCancelledFragment = new OrderHistoryCancelledFragment();
                //orderHistoryCancelledFragment.addOrder(order);
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

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);

            orderHistory_cost = itemView.findViewById(R.id.orderHis_cost);
            orderHistory_date = itemView.findViewById(R.id.orderHis_date);
            orderHistory_time = itemView.findViewById(R.id.orderHis_time);
            orderItem = itemView.findViewById(R.id.order_history_item);
            orderHistory_accept = itemView.findViewById(R.id.orderHis_accept);
            orderHistory_cancel = itemView.findViewById(R.id.orderHis_cancel);
            orderHistory_state = itemView.findViewById(R.id.orderHis_state);
        }
    }
}
