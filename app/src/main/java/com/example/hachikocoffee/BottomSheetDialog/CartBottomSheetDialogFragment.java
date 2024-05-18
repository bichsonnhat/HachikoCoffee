package com.example.hachikocoffee.BottomSheetDialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hachikocoffee.Adapter.CartAdapter;
import com.example.hachikocoffee.Domain.CartItem;
import com.example.hachikocoffee.Domain.ManagementCart;
import com.example.hachikocoffee.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.ShapeAppearanceModel;

import java.util.ArrayList;

public class CartBottomSheetDialogFragment extends BottomSheetDialogFragment {

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
        TextView totalItemCost = view.findViewById(R.id.itemsCost);
        AppCompatButton confirmBtn = view.findViewById(R.id.confirmBtn);
        RecyclerView recyclerViewCart = view.findViewById(R.id.recyclerView_CartItems);
        recyclerViewCart.setLayoutManager(new LinearLayoutManager(requireContext()));

        ShapeAppearanceModel shapeAppearanceModel = new ShapeAppearanceModel()
                .toBuilder()
                .setAllCornerSizes(50)
                .build();
        MaterialShapeDrawable materialShapeDrawable = new MaterialShapeDrawable(shapeAppearanceModel);
        materialShapeDrawable.setFillColor(ColorStateList.valueOf(Color.WHITE));
        confirmBtn.setBackground(materialShapeDrawable);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        ArrayList<CartItem> cartItems = ManagementCart.getInstance().getCartItems();
        if (cartItems.size() > 0){
            CartAdapter cartAdapter = new CartAdapter(cartItems, getChildFragmentManager());
            recyclerViewCart.setAdapter(cartAdapter);
        }
        totalItemCost.setText(ManagementCart.getInstance().getTotalCost() +"đ");
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