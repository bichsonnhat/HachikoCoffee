package com.example.hachikocoffee.BottomSheetDialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.lifecycle.viewmodel.CreationExtras;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hachikocoffee.Adapter.SizeAdapter;
import com.example.hachikocoffee.Adapter.ToppingAdapter;
import com.example.hachikocoffee.Domain.ItemsDomain;
import com.example.hachikocoffee.Listener.ItemClickListener;
import com.example.hachikocoffee.Listener.ToppingListener;
import com.example.hachikocoffee.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

public class DetailCart extends BottomSheetDialogFragment implements ToppingListener {
    private final String proID;
    private final String proName;
    private final double proCost;
    private SizeAdapter adapter;
    private RecyclerView recyclerViewTopping;
    private AppCompatButton totalProductCost;
    private ItemClickListener itemClickListener;
    private int countProduct = 1;
    private TextView numberOfProduct;
    private String sizeProduct = "Nhỏ";
    private int totalCost = 0;
    private final ArrayList<String> toppingList = new ArrayList<>();
    public DetailCart(String proID, String proName, double proCost){
        this.proID = proID;
        this.proName = proName;
        this.proCost = proCost;
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.detail_cart, container, false);
        setupViews(view);
        setupRecyclerViews(view);
        setupClickListeners(view);
        return view;
    }

    @SuppressLint("SetTextI18n")
    private void setupViews(View view) {
        recyclerViewTopping = view.findViewById(R.id.productRecyclerTopping);
        TextView productMinimumSize = view.findViewById(R.id.productMinimumSize);
        TextView productMediumSize = view.findViewById(R.id.productMediumSize);
        TextView productLargeSize = view.findViewById(R.id.productLargeSize);
        totalProductCost = view.findViewById(R.id.totalProductCost);
        TextView productName = view.findViewById(R.id.productName);
        numberOfProduct = view.findViewById(R.id.numberOfProduct);
        totalProductCost.setText("Chọn • " + (int) (proCost + 10000 * toppingList.size()) + "đ");

        totalCost = (int) proCost;
        int itemCost = totalCost;

        productName.setText(proName);
        productMinimumSize.setText(itemCost + "đ");
        productMediumSize.setText(10000 + itemCost + "đ");
        productLargeSize.setText(20000 + itemCost + "đ");

        updateTotalCost();
    }

    private void setupRecyclerViews(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.productRecyclerSize);
        ArrayList<String> sizeList = new ArrayList<>();
        sizeList.add("Lớn");
        sizeList.add("Vừa");
        sizeList.add("Nhỏ");

        ItemClickListener itemClickListener = new ItemClickListener() {
            @Override
            public void onClick(String s) {
                recyclerView.post(new Runnable() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
                sizeProduct = s;
                Toast.makeText(getContext(), "Selected " + s, Toast.LENGTH_SHORT).show();
                switch (sizeProduct) {
                    case "Lớn":
                        totalCost = (int) (proCost + 20000);
                        break;
                    case "Vừa":
                        totalCost = (int) (proCost + 10000);
                        break;
                    default:
                        totalCost = (int) proCost;
                        break;
                }
                updateTotalCost();
            }
        };

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new SizeAdapter(sizeList, itemClickListener);
        recyclerView.setAdapter(adapter);

        setRecyclerViewTopping();
    }

    private void setRecyclerViewTopping() {
        recyclerViewTopping.setHasFixedSize(true);
        recyclerViewTopping.setLayoutManager(new LinearLayoutManager(getContext()));
        ToppingAdapter toppingAdapter = new ToppingAdapter(getContext(), getToppingList(), this);
        recyclerViewTopping.setAdapter(toppingAdapter);
    }

    private ArrayList<String> getToppingList() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Trân châu trắng");
        arrayList.add("Hạt Sen");
        arrayList.add("Trái vải");
        arrayList.add("Kem Phô Mai Macchiato");
        arrayList.add("Trái Nhãn");
        arrayList.add("Thạch Cà Phê");
        arrayList.add("Đào Miếng");
        return arrayList;
    }

    @SuppressLint("SetTextI18n")
    private void setupClickListeners(View view) {
        totalProductCost = view.findViewById(R.id.totalProductCost);
        totalProductCost.setOnClickListener(v -> {
            if (sizeProduct == null) {
                Toast.makeText(requireContext(), "Vui lòng chọn size đồ uống", Toast.LENGTH_SHORT).show();
                return;
            }
            if (countProduct == 0) {
                Toast.makeText(requireContext(), "Vui lòng chọn số lượng", Toast.LENGTH_SHORT).show();
                return;
            }
            Toast.makeText(requireContext(), toppingList.toString(), Toast.LENGTH_SHORT).show();
        });

        TextView minusProduct = view.findViewById(R.id.minusProduct);
        minusProduct.setOnClickListener(v -> {
            countProduct--;
            if (countProduct < 1) countProduct = 1;
            numberOfProduct.setText(String.valueOf(countProduct));
            updateTotalCost();
        });

        TextView plusProduct = view.findViewById(R.id.plusProduct);
        plusProduct.setOnClickListener(v -> {
            countProduct++;
            numberOfProduct.setText(String.valueOf(countProduct));
            updateTotalCost();
        });
    }

    @SuppressLint("SetTextI18n")
    private void updateTotalCost() {
        totalProductCost.setText("Chọn • " + (totalCost * countProduct + toppingList.size() * 10000) + "đ");
    }

    @Override
    public void onToppingChange(ArrayList<String> toppings) {
        toppingList.clear();
        toppingList.addAll(toppings);
        updateTotalCost();
        Toast.makeText(requireContext(), toppings.toString(), Toast.LENGTH_SHORT).show();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        dialog.setOnShowListener(dialogInterface -> {
            BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) dialogInterface;
            View parentLayout = bottomSheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
            if (parentLayout != null) {
                BottomSheetBehavior<View> behavior = BottomSheetBehavior.from(parentLayout);
                setupFullHeight(parentLayout);
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });

        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        return dialog;
    }

    private void setupFullHeight(View bottomSheet) {
        ViewGroup.LayoutParams layoutParams = bottomSheet.getLayoutParams();
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        bottomSheet.setLayoutParams(layoutParams);
    }
}
