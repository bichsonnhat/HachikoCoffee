package com.example.hachikocoffee.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hachikocoffee.Activity.EditUserActivity;
import com.example.hachikocoffee.Domain.UserDomain;
import com.example.hachikocoffee.Listener.OnCategoryChangedListener;
import com.example.hachikocoffee.Listener.OnUserChangedListener;
import com.example.hachikocoffee.Management.ListenerSingleton;
import com.example.hachikocoffee.databinding.ViewholderUserItemBinding;

import java.util.ArrayList;

public class ListUserAdapter extends RecyclerView.Adapter<ListUserAdapter.Viewholder>{
    private final ArrayList<UserDomain> items;
    private Context context;

    public ListUserAdapter(ArrayList<UserDomain> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ListUserAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        ViewholderUserItemBinding binding = ViewholderUserItemBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ListUserAdapter.Viewholder(binding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ListUserAdapter.Viewholder holder, int position) {
        String fullname = items.get(position).getName();
        String[] name = fullname.split(",");
        holder.binding.itemTitle.setText(name[1].trim() + " " + name[0]);
        holder.binding.itemID.setText("Id: " + items.get(position).getUserID());

        holder.binding.imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditUserActivity.class);
                intent.putExtra("userr", items.get(position));
                ListenerSingleton.getInstance().setUserChangedListener((OnUserChangedListener) context);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        ViewholderUserItemBinding binding;
        public Viewholder(ViewholderUserItemBinding binding){
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
