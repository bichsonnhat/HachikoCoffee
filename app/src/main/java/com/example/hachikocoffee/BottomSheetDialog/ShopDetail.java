package com.example.hachikocoffee.BottomSheetDialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.example.hachikocoffee.Activity.MainActivity;
import com.example.hachikocoffee.Domain.ShopDomain;
import com.example.hachikocoffee.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class ShopDetail extends BottomSheetDialogFragment {

    private static final String KEY_SHOP2DETAIL = "shop2detail";
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
        BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

        View viewDialog = LayoutInflater.from(getContext()).inflate(R.layout.shop_detail, null);
        bottomSheetDialog.setContentView(viewDialog);

        initView(viewDialog);

        return bottomSheetDialog;
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
                ((MainActivity) requireActivity()).navigateToOrderFragment();
            }
        });
    }
}