package com.example.hachikocoffee.BottomSheetDialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hachikocoffee.Adapter.CartAdapter;
import com.example.hachikocoffee.Adapter.SizeAdapter;
import com.example.hachikocoffee.Adapter.ToppingAdapter;
import com.example.hachikocoffee.Domain.CartItem;
import com.example.hachikocoffee.Domain.ItemsDomain;
import com.example.hachikocoffee.Listener.ItemClickListener;
import com.example.hachikocoffee.Listener.OnCartChangedListener;
import com.example.hachikocoffee.Listener.ToppingListener;
import com.example.hachikocoffee.Management.ManagementCart;
import com.example.hachikocoffee.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;

public class DetailCart extends BottomSheetDialogFragment implements ToppingListener {
    private final ItemsDomain product;;
    private final CartItem cartItem;
    private final double productCost;
    private final String productName;
    private SizeAdapter adapter;
    private RecyclerView recyclerViewTopping;
    private AppCompatButton totalProductCost;
    private int countProduct = 1;
    private int position;
    private CartAdapter cartAdapter;
    private boolean check = false;
    private TextView numberOfProduct;
    private String sizeProduct = "Nhỏ";
    private int totalCost = 0;
    private final ArrayList<String> toppingList = new ArrayList<>();
    DecimalFormatSymbols symbols;
    public DetailCart(ItemsDomain product){
        this.product = product;
        this.cartItem = null;
        this.productCost = this.product.getPrice();
        this.productName = this.product.getTitle();
    }

    public DetailCart(CartItem cartItem, int position, CartAdapter cartAdapter){
        this.product = null;
        this.cartItem = cartItem;
        this.productCost = this.cartItem.getCost();
        this.productName = this.cartItem.getProductName();
        this.sizeProduct = cartItem.getSize();
        this.position = position;
        this.cartAdapter = cartAdapter;
        this.countProduct = cartItem.getQuantity();
        if (cartItem.getToppings() != null){
            this.toppingList.addAll(cartItem.getToppings());
        }
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.detail_cart, container, false);

        //format money
        symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');

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
        numberOfProduct.setText(String.valueOf(countProduct));

        switch (sizeProduct) {
            case "Lớn":
                totalCost = (int) (productCost + 20000);
                break;
            case "Vừa":
                totalCost = (int) (productCost + 10000);
                break;
            default:
                totalCost = (int) productCost;
                break;
        }

        productName.setText(this.productName);

        String a = new DecimalFormat("#,###", symbols).format(productCost);
        String b = new DecimalFormat("#,###", symbols).format(productCost + 10000);
        String c = new DecimalFormat("#,###", symbols).format(productCost + 20000);

        productMinimumSize.setText(a + "đ");
        productMediumSize.setText(b + "đ");
        productLargeSize.setText(c + "đ");

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
                        totalCost = (int) (productCost + 20000);
                        break;
                    case "Vừa":
                        totalCost = (int) (productCost + 10000);
                        break;
                    default:
                        totalCost = (int) productCost;
                        break;
                }
                updateTotalCost();
            }
        };

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new SizeAdapter(sizeList, itemClickListener, sizeProduct);
        recyclerView.setAdapter(adapter);

        setRecyclerViewTopping();
    }

    private void setRecyclerViewTopping() {
        recyclerViewTopping.setHasFixedSize(true);
        recyclerViewTopping.setLayoutManager(new LinearLayoutManager(getContext()));
        ToppingAdapter toppingAdapter = new ToppingAdapter(getContext(), getToppingList(), toppingList, this);
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

            CartItem cartItem;
            if (this.product != null)
            {
                if (toppingList.size() != 0){
                    cartItem = new CartItem(product.getProductID(), product.getTitle(), countProduct, sizeProduct, toppingList, totalCost * countProduct + toppingList.size() * 10000 * countProduct, productCost);
                }
                else {
                    cartItem = new CartItem(product.getProductID(), product.getTitle(), countProduct, sizeProduct, null, totalCost * countProduct + toppingList.size() * 10000 * countProduct, productCost);
                }
                ManagementCart.getInstance().addToCart(cartItem);
            }
            else
            {
                if (toppingList.size() != 0){
                    cartItem = new CartItem(this.cartItem.getProductId(), this.cartItem.getProductName(), countProduct, sizeProduct, toppingList, totalCost * countProduct + toppingList.size() * 10000 * countProduct, productCost);
                }
                else {
                    cartItem = new CartItem(this.cartItem.getProductId(), this.cartItem.getProductName(), countProduct, sizeProduct, null, totalCost * countProduct + toppingList.size() * 10000 * countProduct, productCost);
                }
                ManagementCart.getInstance().updateCart(this.position, cartItem);
                check = true;
                cartAdapter.notifyItemChanged(this.position);
            }

            dismiss();
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
        String a = new DecimalFormat("#,###", symbols).format((long) totalCost * countProduct + toppingList.size() * 10000L * countProduct);

        totalProductCost.setText("Chọn • " + a + "đ");
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

//    @Override
//    public void onDismiss(@NonNull DialogInterface dialog) {
//        super.onDismiss(dialog);
//        if (check){
//            cartChangedListener.onCartChanged();
//        }
//    }
}
