package com.example.hachikocoffee.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hachikocoffee.R;
import com.example.hachikocoffee.Listener.ToppingListener;

import java.util.ArrayList;

public class ToppingAdapter extends RecyclerView.Adapter<ToppingAdapter.ViewHolder> {
    Context context;
    View view;
    ArrayList<String> arrayList;
    ToppingListener toppingListener;
    ArrayList<String> arrayList_0 = new ArrayList<>();

    public ToppingAdapter(Context context, ArrayList<String> arrayList, ToppingListener toppingListener) {
        this.context = context;
        this.arrayList = arrayList;
        this.toppingListener = toppingListener;
    }
    @NonNull
    @Override
    public ToppingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.topping_checkbox_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ToppingAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if (arrayList != null && arrayList.size() > 0) {
            holder.checkBox.setText(arrayList.get(position));
            holder.checkBox.setTextSize(15);
            holder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.checkBox.isChecked()){
                        arrayList_0.add(arrayList.get(position));
                    } else {
                        arrayList_0.remove(arrayList.get(position));
                    }
                    toppingListener.onToppingChange(arrayList_0);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.check_box);
        }
    }
}
