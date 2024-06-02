package com.example.hachikocoffee.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hachikocoffee.Domain.UserDomain;
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
        holder.binding.itemTitle.setText(items.get(position).getName());
        holder.binding.itemID.setText("Id: " + items.get(position).getUserID());
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
