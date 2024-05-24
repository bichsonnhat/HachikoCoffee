package com.example.hachikocoffee.BottomSheetDialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.example.hachikocoffee.Activity.MainActivity;
import com.example.hachikocoffee.Activity.SearchShopActivity;
import com.example.hachikocoffee.Domain.ShopDomain;
import com.example.hachikocoffee.Fragment.ShopFragment;
import com.example.hachikocoffee.Listener.OnStoreClick;
import com.example.hachikocoffee.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class ShopDetail extends BottomSheetDialogFragment {

    private static final String KEY_SHOP2DETAIL = "shop2detail";
    private static OnStoreClick onStoreClick;
    private ShopDomain shop;

    ImageView shopdetailImage;
    ImageView shopdetailClose;
    TextView shopdetailName;
    TextView shopdetailAddress;
    TextView shopdetailCoordinate;
    ConstraintLayout shopdetailBtn;

    public static ShopDetail newInstance(ShopDomain shop) {
        ShopDetail shopDetailBottomSheet = new ShopDetail();
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_SHOP2DETAIL, shop);
        shopDetailBottomSheet.setArguments(bundle);
        return shopDetailBottomSheet;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            shop = (ShopDomain) bundle.get(KEY_SHOP2DETAIL);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

        View viewDialog = LayoutInflater.from(getContext()).inflate(R.layout.shop_detail, null);
        dialog.setContentView(viewDialog);
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

        initView(viewDialog);

        return dialog;
    }

    private void initView(View view) {
        shopdetailImage = view.findViewById(R.id.shopdetail_image);
        shopdetailName = view.findViewById(R.id.shopdetail_name);
        shopdetailAddress = view.findViewById(R.id.shopdetail_address);
        shopdetailCoordinate = view.findViewById(R.id.shopdetail_coordinate);
        shopdetailClose = view.findViewById(R.id.shop_closeBtn);
        shopdetailBtn = view.findViewById(R.id.shopdetail_orderbtn);

        if (shop == null) {
            return;
        }

        Glide.with(shopdetailImage.getContext()).load(shop.getImageURL()).into(shopdetailImage);
        shopdetailName.setText(shop.getName());
        shopdetailAddress.setText(shop.getAddress());
        shopdetailCoordinate.setText(shop.getCoordinate());

        shopdetailClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        shopdetailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                if (requireActivity() instanceof MainActivity) {
                    ((MainActivity) requireActivity()).navigateToOrderFragment();
                } else {
                    ((SearchShopActivity) requireActivity()).finish();
                    onStoreClick.onStoreClick();
                }
            }
        });
    }

    public static void setInterfaceInstance(ShopFragment context){
        onStoreClick = (OnStoreClick) context;
    }


    private void setupFullHeight(View bottomSheet) {
        ViewGroup.LayoutParams layoutParams = bottomSheet.getLayoutParams();
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        bottomSheet.setLayoutParams(layoutParams);
    }
}
