package com.example.hachikocoffee.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hachikocoffee.Domain.ShortcutDomain;
import com.example.hachikocoffee.R;

import java.util.ArrayList;

public class ShortcutAdapter extends RecyclerView.Adapter<ShortcutAdapter.MyViewHolder> {

    private final ArrayList<ShortcutDomain> itemList;

    public ShortcutAdapter(ArrayList<ShortcutDomain> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_shortcut, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.shortcutName.setText(itemList.get(position).getTitle());
        String picUrl ="";
        switch (position){
            case 0: {
                picUrl = "shipping";
                break;
            }
            case 1: {
                picUrl = "takeaway";
                break;
            }
            case 2: {
                picUrl = "shipping";
                break;
            }
            case 3: {
                picUrl = "shipping";
                break;
            }
            case 4: {
                picUrl = "shipping";
                break;
            }
        }

        int drawableResourceId = holder.itemView.getContext().getResources()
                .getIdentifier(picUrl, "drawable",
                        holder.itemView.getContext().getPackageName());
        Glide.with(holder.itemView.getContext())
                .load(drawableResourceId)
                .into(holder.shortcutPic);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ShortcutDomain item = itemList.get(position);
                // Toast.makeText(holder.itemView.getContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
            }
        });

        holder.itemView.setBackgroundResource(R.drawable.background_shortcut);
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