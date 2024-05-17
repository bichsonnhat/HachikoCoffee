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
    int totalOrder = 0;
    int countProduct = 1;
    String SizeProduct = "Nhỏ";
    ItemsDomain object;
    RecyclerView recyclerViewTopping;
    ArrayList<String> toppingList = new ArrayList<>();
    int sizeToping = 0;
    RecyclerView recyclerViewSize;
    AppCompatButton totalProductCost;
    ItemClickListener itemClickListener;
    SizeAdapter adapter;
    ToppingAdapter toppingAdapter;
    @NonNull
    @Override
    public CreationExtras getDefaultViewModelCreationExtras() {
        return super.getDefaultViewModelCreationExtras();
    }

    public DetailCart(ItemsDomain object){ this.object = object;};

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.detail_cart, container,false);
        recyclerViewTopping = view.findViewById(R.id.productRecyclerTopping);
        recyclerViewSize = view.findViewById(R.id.productRecyclerSize);
        TextView minusProduct = view.findViewById(R.id.minusProduct);
        TextView plusProduct = view.findViewById(R.id.plusProduct);
        TextView numberOfProduct = view.findViewById(R.id.numberOfProduct);
        TextView productMinimumSize = view.findViewById(R.id.productMinimumSize);
        TextView productMediumSize = view.findViewById(R.id.productMediumSize);
        TextView productLargeSize = view.findViewById(R.id.productLargeSize);
        totalProductCost = view.findViewById(R.id.totalProductCost);
        TextView productName = view.findViewById(R.id.productName);
        totalProductCost.setText("Chọn • " + (int) (object.getPrice() + 10 * sizeToping) + "đ");

        totalOrder = (int) object.getPrice();
        minusProduct.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                countProduct--;
                if (countProduct == 0){
                    countProduct = 1;
                } else {
                    numberOfProduct.setText(""+countProduct);
                    totalProductCost.setText("Chọn • " + (totalOrder * countProduct + 10 * sizeToping) + "đ");
                }
            }
        });

        plusProduct.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                countProduct++;
                numberOfProduct.setText(""+countProduct);
                totalProductCost.setText("Chọn • " + (totalOrder * countProduct + 10 * sizeToping) + "đ");
            }
        });

        totalProductCost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SizeProduct == null){
                    Toast.makeText(getContext(), "Vui lòng chọn size đồ uống", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (countProduct == 0){
                    Toast.makeText(getContext(), "Vui lòng chọn số lượng", Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(getContext(), toppingList.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        setRecycleViewTopping();
        int itemCost = (int) object.getPrice();
        productName.setText(object.getTitle());
        productMinimumSize.setText(itemCost + "đ");
        productMediumSize.setText(10000 + itemCost + "đ");
        productLargeSize.setText(20000 + itemCost + "đ");

        recyclerViewSize = view.findViewById(R.id.productRecyclerSize);
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Lớn");
        arrayList.add("Vừa");
        arrayList.add("Nhỏ");
        itemClickListener = new ItemClickListener() {
            @Override
            public void onClick(String s) {
                recyclerViewSize.post(new Runnable() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
                SizeProduct = s;
                Toast.makeText(getContext(), "Selected " + s, Toast.LENGTH_SHORT).show();
                if (SizeProduct.equals("Lớn")){
                    totalOrder = 20000 + itemCost;
                }
                if (SizeProduct.equals("Vừa")){
                    totalOrder = 10000 + itemCost;
                }
                if (SizeProduct.equals("Nhỏ")){
                    totalOrder = itemCost;
                }
                totalProductCost.setText("Chọn • " + (totalOrder * countProduct + 10 * sizeToping) + "đ");
            }
        };

        recyclerViewSize.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new SizeAdapter(arrayList, itemClickListener);
        recyclerViewSize.setAdapter(adapter);
        return view;
    }

    private void setRecycleViewTopping() {
        recyclerViewTopping.setHasFixedSize(true);
        recyclerViewTopping.setLayoutManager(new LinearLayoutManager(getContext()));
        toppingAdapter = new ToppingAdapter(getContext(), getToppingList(), this);
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
    @Override
    public void onToppingChange(ArrayList<String> arrayList) {
        toppingList = arrayList;
        sizeToping = arrayList.size();
        totalOrder = (((int) object.getPrice() + 10 * sizeToping) * countProduct);
        totalProductCost.setText("Chọn • " + totalOrder + "đ");
        Toast.makeText(getContext(), arrayList.toString(), Toast.LENGTH_SHORT).show();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
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
