package com.example.hachikocoffee.BottomSheetDialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hachikocoffee.Adapter.CartAdapter;
import com.example.hachikocoffee.Domain.CartItem;
import com.example.hachikocoffee.Management.ManagementCart;
import com.example.hachikocoffee.Listener.OnCartChangedListener;
import com.example.hachikocoffee.Management.ManagementUser;
import com.example.hachikocoffee.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.ShapeAppearanceModel;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;

public class CartBottomSheetDialogFragment extends BottomSheetDialogFragment implements OnCartChangedListener {

    TextView itemCount;
    TextView totalItemCost;
    TextView totalAfterFee;
    RecyclerView recyclerViewCart;
    private CartAdapter cartAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_cart, container, false);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageView backButton = view.findViewById(R.id.backBtn);
        ConstraintLayout addBtn = view.findViewById(R.id.addBtn);
        TextView deleteCartBtn = view.findViewById(R.id.deleteCart);
        totalItemCost = view.findViewById(R.id.itemsCost);
        AppCompatButton confirmBtn = view.findViewById(R.id.confirmBtn);
        recyclerViewCart = view.findViewById(R.id.recyclerView_CartItems);
        itemCount = view.findViewById(R.id.itemCount);
        totalAfterFee = view.findViewById(R.id.totalAfterFee);
        TextView recipentName = view.findViewById(R.id.name);
        TextView recipentPhone = view.findViewById(R.id.phone);

        recyclerViewCart.setLayoutManager(new LinearLayoutManager(requireContext()));

        ShapeAppearanceModel shapeAppearanceModel = new ShapeAppearanceModel()
                .toBuilder()
                .setAllCornerSizes(50)
                .build();
        MaterialShapeDrawable materialShapeDrawable = new MaterialShapeDrawable(shapeAppearanceModel);
        materialShapeDrawable.setFillColor(ColorStateList.valueOf(Color.WHITE));
        confirmBtn.setBackground(materialShapeDrawable);

        MaterialShapeDrawable materialShapeDrawable1 = new MaterialShapeDrawable(shapeAppearanceModel);
        materialShapeDrawable1.setFillColor(ColorStateList.valueOf(Color.parseColor("#fef7e5")));
        addBtn.setBackground(materialShapeDrawable1);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        deleteCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ManagementCart.getInstance().clearCart();
                dismiss();
            }
        });
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


        ArrayList<CartItem> cartItems = ManagementCart.getInstance().getCartItems();
        if (cartItems.size() > 0)
        {
            cartAdapter = new CartAdapter(cartItems, getChildFragmentManager(), this);
            recyclerViewCart.setAdapter(cartAdapter);
        }

        recipentName.setText(ManagementCart.getInstance().getRecipentName());
        recipentPhone.setText(ManagementCart.getInstance().getRecipentPhone());
        itemCount.setText("Giao hàng • " + ManagementCart.getInstance().getItemsCount() + " sản phẩm");

        //format money
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        String a = new DecimalFormat("#,###", symbols).format(ManagementCart.getInstance().getTotalCost());
        totalItemCost.setText(a +"đ");
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

    @SuppressLint({"SetTextI18n", "NotifyDataSetChanged"})
    @Override
    public void onCartChanged() {
        if (ManagementCart.getInstance().getCartItems().isEmpty())
        {
            dismiss();
            return;
        }
        cartAdapter.setCartItems(ManagementCart.getInstance().getCartItems());
        cartAdapter.notifyDataSetChanged();

        recyclerViewCart.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                Log.d("Change", "true");

                DecimalFormatSymbols symbols = new DecimalFormatSymbols();
                symbols.setGroupingSeparator('.');
                String a = new DecimalFormat("#,###", symbols).format(ManagementCart.getInstance().getTotalCost());
                totalItemCost.setText(a +"đ");


                itemCount.setText("Giao hàng • " + ManagementCart.getInstance().getItemsCount() + " sản phẩm");

                recyclerViewCart.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

}