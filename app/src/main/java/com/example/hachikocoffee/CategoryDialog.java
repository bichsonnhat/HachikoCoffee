package com.example.hachikocoffee;

import android.app.Dialog;
import android.content.DialogInterface;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hachikocoffee.Adapter.CategoryDialogAdapter;
import com.example.hachikocoffee.Domain.CategoryDomain;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CategoryDialog extends BottomSheetDialogFragment {
    public  OnDismissListener dismissListener;
    RecyclerView recyclerViewCategoryDialog;

    public void setOnDismissListener(OnDismissListener listener) {
        this.dismissListener = listener;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        if (dismissListener != null) {
            dismissListener.onDissmiss();
        }
    }
    public CategoryDialog(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_category, container, false);
        recyclerViewCategoryDialog = view.findViewById(R.id.recyclerView_Category_Dialog);

        loadCategoryData();

        ImageView closeBtn = view.findViewById(R.id.category_dialog_closeBtn);

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Đóng dialog
                dismiss();
            }
        });

        return view;
    }

    private void loadCategoryData() {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("CATEGORY");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    ArrayList<CategoryDomain> items = new ArrayList<>();
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        CategoryDomain category = issue.getValue(CategoryDomain.class);
                        items.add(category);
                    }
                    displayCategoryData(items);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Failed to read value.", error.toException());
                // Notify user about the error
            }
        });

//        ArrayList<CategoryDomain> items = new ArrayList<>();
//
//        items.add(new CategoryDomain(1, "https://firebasestorage.googleapis.com/v0/b/thehachikocoffee-aed51.appspot.com/o/Category%2F1_CafeHighlight.png?alt=media&token=7bbe9ee5-98b4-4dc8-9f47-1765623e4967", "Món mới phải thử"));
//        items.add(new CategoryDomain(2, "https://firebasestorage.googleapis.com/v0/b/thehachikocoffee-aed51.appspot.com/o/Category%2F2_Cafe.png?alt=media&token=294e7a67-5a1f-471f-a9f8-f1a3cbe3309a", "Cafe"));
//        items.add(new CategoryDomain(3, "https://firebasestorage.googleapis.com/v0/b/thehachikocoffee-aed51.appspot.com/o/Category%2F3_TraTraiCay.png?alt=media&token=dbf7b302-f868-42f1-9223-e74b6103f476", "Trà Trái Cây"));
//        items.add(new CategoryDomain(4, "https://firebasestorage.googleapis.com/v0/b/thehachikocoffee-aed51.appspot.com/o/Category%2F4_TraSuaMacchiato.png?alt=media&token=039eed0d-c26f-4990-8d26-1e9986d87884", "Trà Sữa Macchiato"));
//        items.add(new CategoryDomain(5, "https://firebasestorage.googleapis.com/v0/b/thehachikocoffee-aed51.appspot.com/o/Category%2F5_CloudFee.png?alt=media&token=5da8321d-4302-4cf8-9263-2e79bd5e1ab6", "CloudFee"));
//        items.add(new CategoryDomain(6, "https://firebasestorage.googleapis.com/v0/b/thehachikocoffee-aed51.appspot.com/o/Category%2F6_TraXanhTayBac.png?alt=media&token=2243acdf-f736-49f8-ad57-a5d28d53ec03", "Trà Xanh Tây Bắc"));
//        items.add(new CategoryDomain(7, "https://firebasestorage.googleapis.com/v0/b/thehachikocoffee-aed51.appspot.com/o/Category%2F7_DaXayFrosty.png?alt=media&token=3a79e066-817a-49f2-9a87-4aa02034c30b", "Đá Xay Frosty"));
//        items.add(new CategoryDomain(8, "https://firebasestorage.googleapis.com/v0/b/thehachikocoffee-aed51.appspot.com/o/Category%2F8_BanhMan.png?alt=media&token=59883df8-9519-4730-a7f9-0a5571ff2f09", "Bánh Mặn"));
//        items.add(new CategoryDomain(9, "https://firebasestorage.googleapis.com/v0/b/thehachikocoffee-aed51.appspot.com/o/Category%2F9_BanhNgot.png?alt=media&token=3beb83cf-44b6-449c-b467-9297daab52b0", "Bánh Ngot"));
//        items.add(new CategoryDomain(10, "https://firebasestorage.googleapis.com/v0/b/thehachikocoffee-aed51.appspot.com/o/Category%2F10_Topping.png?alt=media&token=3b03a22f-fdb9-4c87-b714-df4e3af2f6cd", "Topping"));
//        items.add(new CategoryDomain(11, "https://firebasestorage.googleapis.com/v0/b/thehachikocoffee-aed51.appspot.com/o/Category%2F11_CafeTaiNha.png?alt=media&token=fda9e4d6-63b0-4ff7-8ddb-fef616878a4c", "Cafe Tại Nhà"));
//        items.add(new CategoryDomain(12, "https://firebasestorage.googleapis.com/v0/b/thehachikocoffee-aed51.appspot.com/o/Category%2F12_ChaiFreshKhongDa.png?alt=media&token=ac4ea710-9f2c-47c7-934c-aa5218d65419", "Chai Fresh Không Đá"));
//        items.add(new CategoryDomain(13, "https://firebasestorage.googleapis.com/v0/b/thehachikocoffee-aed51.appspot.com/o/Category%2F13_CacLoaiDoAnKhac.png?alt=media&token=20d45b18-b464-4034-894a-4a25f44119d6", "Các Loại Đồ Ăn Khác"));
//
//        displayCategoryData(items);
    }

    private void displayCategoryData(ArrayList<CategoryDomain> items) {
        if (!items.isEmpty()) {
            recyclerViewCategoryDialog.setLayoutManager(new GridLayoutManager(getActivity(), 4));
            recyclerViewCategoryDialog.setAdapter(new CategoryDialogAdapter(items));
            recyclerViewCategoryDialog.setHasFixedSize(true);
        }
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
                setHeight(parentLayout);
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);

            }
        });

        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        return dialog;
    }

    private void setHeight(View bottomSheet) {
        ViewGroup.LayoutParams layoutParams = bottomSheet.getLayoutParams();
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        bottomSheet.setLayoutParams(layoutParams);
    }
}

