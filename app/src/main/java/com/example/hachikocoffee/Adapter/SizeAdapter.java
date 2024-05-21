package com.example.hachikocoffee.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hachikocoffee.Listener.ItemClickListener;
import com.example.hachikocoffee.R;

import java.util.ArrayList;

public class SizeAdapter extends RecyclerView.Adapter<SizeAdapter.ViewHolder> {

    ArrayList<String> arrayList;
    ItemClickListener itemClickListener;
    int selectPosition;
    String selectedSize;

    public SizeAdapter(ArrayList<String> arrayList, ItemClickListener itemClickListener, String selectedSize) {
        this.arrayList = arrayList;
        this.itemClickListener = itemClickListener;
        this.selectedSize = selectedSize;
        switch (selectedSize) {
            case "Lớn":
                selectPosition = 0;
                break;
            case "Vừa":
                selectPosition = 1;
                break;
            case "Nhỏ":
                selectPosition = 2;
                break;
        }
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_size, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.radioButton.setText(arrayList.get(position));
        holder.radioButton.setChecked(position == selectPosition);
        holder.radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    selectPosition = holder.getAdapterPosition();
                    itemClickListener.onClick(holder.radioButton.getText().toString());
                }
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RadioButton radioButton;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            radioButton = itemView.findViewById(R.id.radio_button);
        }
    }
}
