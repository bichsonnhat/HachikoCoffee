package com.example.hachikocoffee.Adapter;

import android.app.Activity;
import android.content.Context;
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
import com.example.hachikocoffee.Activity.ContactFeedbackActivity;
import com.example.hachikocoffee.Activity.FavouriteActivity;
import com.example.hachikocoffee.Activity.UpdateInfoActivity;
import com.example.hachikocoffee.Domain.ShortcutDomain;
import com.example.hachikocoffee.Activity.OrderHistory;
import com.example.hachikocoffee.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class ShortcutAdapter extends RecyclerView.Adapter<ShortcutAdapter.MyViewHolder> {

    private final ArrayList<ShortcutDomain> itemList;

    public ShortcutAdapter(ArrayList<ShortcutDomain> itemList) {
        this.itemList = itemList;
    }

    Context context;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_shortcut, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.shortcutName.setText(itemList.get(position).getTitle());
        String picUrl = itemList.get(position).getPic();

        int drawableResourceId = holder.itemView.getContext().getResources()
                .getIdentifier(picUrl, "drawable", holder.itemView.getContext().getPackageName());

        Glide.with(holder.itemView.getContext())
                .load(drawableResourceId)
                .into(holder.shortcutPic);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                switch (position) {
                    case 0:
                        BottomNavigationView bottomNavigationView = ((Activity)context).findViewById(R.id.bottomNavigationView);
                        bottomNavigationView.setSelectedItemId(R.id.order);
                        break;
                    case 1:
                        intent = new Intent(context, OrderHistory.class);
                        ((Activity)context).startActivity(intent);
                        break;
                    case 2:
                        intent = new Intent(context, FavouriteActivity.class);
                        ((Activity)context).startActivity(intent);
                        break;
                    case 3:
                        intent = new Intent(context, ContactFeedbackActivity.class);
                        ((Activity)context).startActivity(intent);
                        break;
                    case 4:
                        intent = new Intent(context, UpdateInfoActivity.class);
                        ((Activity)context).startActivity(intent);
                        break;
                }
            }
        });

        holder.itemView.setBackgroundResource(R.drawable.background_item);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView shortcutName;
        ImageView shortcutPic;
        ConstraintLayout mainLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            shortcutName = itemView.findViewById(R.id.shortcutName);
            shortcutPic = itemView.findViewById(R.id.shortcutPic);
            mainLayout = itemView.findViewById(R.id.shortcutLayout);
        }
    }
}