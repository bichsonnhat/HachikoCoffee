package com.example.hachikocoffee.Adapter;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hachikocoffee.Activity.DetailActivity;
import com.example.hachikocoffee.Domain.ItemsDomain;
import com.example.hachikocoffee.R;

import java.util.ArrayList;

public class NewListAdapter extends RecyclerView.Adapter<NewListAdapter.Viewholder> {
    private final ArrayList<ItemsDomain> items;
    Context context;

    public NewListAdapter(ArrayList<ItemsDomain> items){
        this.items = items;
    }
    @NonNull
    @Override
    public NewListAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_new_list, parent, false);
        return new NewListAdapter.Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewListAdapter.Viewholder holder, int position) {
        holder.title.setText(items.get(position).getTitle());
        holder.priceTxt.setText(items.get(position).getPrice() +"đ");
        String picUrl ="";
        switch (position){
            case 0: {
                picUrl = "peach_tea";
                break;
            }
            case 1: {
                picUrl = "peach_tea";
                break;
            }
            case 2: {
                picUrl = "peach_tea";
                break;
            }
            case 3: {
                picUrl = "peach_tea";
                break;
            }
            case 4: {
                picUrl = "peach_tea";
                break;
            }
        }

        int drawableResourceId = holder.itemView.getContext().getResources()
                .getIdentifier(picUrl, "drawable",
                        holder.itemView.getContext().getPackageName());
        Glide.with(holder.itemView.getContext())
                .load(drawableResourceId)
                .into(holder.pic);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ItemsDomain item = items.get(position);
                DetailActivity detailBottomSheetDialog = new DetailActivity(item);
                detailBottomSheetDialog.show(((AppCompatActivity) context).getSupportFragmentManager(), "DetailBottomSheetDialog");
            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class Viewholder extends RecyclerView.ViewHolder{

        TextView title;
        ImageView pic;
        TextView priceTxt;
        ConstraintLayout mainLayout;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.itemTitle);
            pic = itemView.findViewById(R.id.pic);
            priceTxt = itemView.findViewById(R.id.priceTxt);
            mainLayout = itemView.findViewById(R.id.newlistLayout);
        }

    }
}
