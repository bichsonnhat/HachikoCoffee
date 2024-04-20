package com.example.hachikocoffee.Activity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.Window;
import android.view.Gravity;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.hachikocoffee.Domain.ItemsDomain;
import com.example.hachikocoffee.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class DetailActivity extends BottomSheetDialogFragment {
    private ItemsDomain object;

    public DetailActivity(ItemsDomain object) {
        this.object = object;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_detail, container, false);

        TextView titleDetail = view.findViewById(R.id.titleDetail);
        TextView priceDetail = view.findViewById(R.id.priceDetail);
        ImageView imageDetail = view.findViewById(R.id.imageDetail);

        titleDetail.setText(object.getTitle());
        priceDetail.setText(object.getPrice() + "đ");

        String picUrl = object.getPicUrl();
        int drawableResourceId = getResources().getIdentifier(picUrl, "drawable", requireContext().getPackageName());

        Glide.with(requireContext())
                .load(drawableResourceId)
                .into(imageDetail);

        ImageView backBtn = view.findViewById(R.id.backBtn);

        backBtn.setOnClickListener(v -> dismiss());

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        BottomSheetDialog dialog = (BottomSheetDialog) getDialog();
        if (dialog != null) {
            Window window = dialog.getWindow();
            if (window != null) {
                window.setGravity(Gravity.BOTTOM);
                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                window.getAttributes().windowAnimations = R.style.DialogAnimation;
            }
        }
    }
}