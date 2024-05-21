package com.example.hachikocoffee.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
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
    ArrayList<String> toppingList;
    ToppingListener toppingListener;
    ArrayList<String> arrayList_0 = new ArrayList<>();
    ArrayList<String> selectedToppings;

    public ToppingAdapter(Context context, ArrayList<String> toppingList, ArrayList<String> selectedToppings, ToppingListener toppingListener) {
        this.context = context;
        this.toppingList = toppingList;
        this.selectedToppings = selectedToppings;
        this.toppingListener = toppingListener;
        arrayList_0.addAll(selectedToppings);
    }
    @NonNull
    @Override
    public ToppingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.topping_checkbox_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ToppingAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if (toppingList != null && toppingList.size() > 0) {
            holder.checkBox.setText(toppingList.get(position));
            holder.checkBox.setChecked(selectedToppings.contains(toppingList.get(position)));
            holder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.checkBox.isChecked()){
                        arrayList_0.add(toppingList.get(position));
                    } else {
                        arrayList_0.remove(toppingList.get(position));
                    }
                    toppingListener.onToppingChange(arrayList_0);
                }
            });

//            Log.d("ToppingAdapter", "Toppings list1: " + selectedToppings.toString());
//            Log.d("ToppingAdapter", "Toppings list2: " + arrayList_0.toString());
        }
    }

    @Override
    public int getItemCount() {
        return toppingList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.check_box);
        }
    }
}
