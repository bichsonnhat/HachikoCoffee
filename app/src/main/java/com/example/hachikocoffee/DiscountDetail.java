package com.example.hachikocoffee;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.hachikocoffee.Activity.MainActivity;
import com.example.hachikocoffee.Domain.DiscountDomain;
import com.example.hachikocoffee.Fragment.DiscountFragment;
import com.example.hachikocoffee.Listener.OnVoucherClick;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class DiscountDetail extends BottomSheetDialogFragment {
    private static OnVoucherClick onVoucherClick;
    private static final String KEY_DISCOUNT2DETAIL = "discount2detail";
    private DiscountDomain discount;

    TextView discountdetailTitle;
    TextView discountdetailDescription;
    TextView discountdetailExpirydate;
    ImageView discountdetailImage;
    ImageView discountdetailClose;
    Button discountdetailBtn;


    public static DiscountDetail newInstance(DiscountDomain shop) {
        DiscountDetail discountDetailBottomSheet = new DiscountDetail();
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_DISCOUNT2DETAIL, shop);
        discountDetailBottomSheet.setArguments(bundle);
        return discountDetailBottomSheet;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            discount = (DiscountDomain) bundle.get(KEY_DISCOUNT2DETAIL);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

        View viewDialog = LayoutInflater.from(getContext()).inflate(R.layout.view_voucher, null);
        bottomSheetDialog.setContentView(viewDialog);

        initView(viewDialog);

        return bottomSheetDialog;
    }

    private void initView(View view) {
        discountdetailTitle = view.findViewById(R.id.txt_voucher_name);
        discountdetailDescription = view.findViewById(R.id.txt_description);
        discountdetailExpirydate = view.findViewById(R.id.txt_expired_date);
        discountdetailImage = view.findViewById(R.id.voucherdetail_img);
        discountdetailClose = view.findViewById(R.id.voucher_closeBtn);
        discountdetailBtn = view.findViewById(R.id.order_bt);

        if (discount == null) {
            return;
        }

        Glide.with(discountdetailImage.getContext()).load(discount.getImageURL()).into(discountdetailImage);
        discountdetailTitle.setText(discount.getTitle());
        discountdetailDescription.setText(discount.getDescription());
        discountdetailExpirydate.setText(discount.getExpiryDate());

        discountdetailClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        discountdetailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (requireActivity() instanceof MainActivity) {
                    ((MainActivity) requireActivity()).navigateToOrderFragment();
                } else if (requireActivity() instanceof YourVoucher) {
                    ((YourVoucher) requireActivity()).finish();
                    onVoucherClick.onVoucherClick();
                }
            }
        });
    }

    public static void setInterfaceInstance(DiscountFragment context){
        onVoucherClick = (OnVoucherClick) context;
    }
}
