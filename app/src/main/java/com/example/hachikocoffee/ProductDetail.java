package com.example.hachikocoffee;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hachikocoffee.Adapter.SizeAdapter;
import com.example.hachikocoffee.Adapter.ToppingAdapter;
import com.example.hachikocoffee.Domain.ItemsDomain;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ProductDetail extends BottomSheetDialogFragment implements ToppingListener {
    ItemsDomain object;
    RecyclerView recyclerView;
    RecyclerView recyclerViewTopping;
    ItemClickListener itemClickListener;
    SizeAdapter adapter;
    ToppingAdapter toppingAdapter;
    int countProduct = 0;
    String SizeProduct = null;
    ArrayList<String> toppingList = new ArrayList<>();

    public ProductDetail(ItemsDomain object){ this.object = object;};
    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.product_detail, container,false);
        TextView productName = view.findViewById(R.id.productName);
        TextView productCost = view.findViewById(R.id.productCost);
        ImageView productImage = view.findViewById(R.id.product_image_scr);
        recyclerViewTopping = view.findViewById(R.id.productRecyclerTopping);
        TextView minusProduct = view.findViewById(R.id.minusProduct);
        TextView plusProduct = view.findViewById(R.id.plusProduct);
        TextView numberOfProduct = view.findViewById(R.id.numberOfProduct);
        AppCompatButton totalProductCost = view.findViewById(R.id.totalProductCost);
        minusProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countProduct--;
                countProduct = Math.max(countProduct, 0);
                numberOfProduct.setText(""+countProduct);
            }
        });

        plusProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countProduct++;
                numberOfProduct.setText(""+countProduct);
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

        productName.setText(object.getTitle());
        productCost.setText(Math.round(object.getPrice()) + "000đ");
        String PicUrl = object.getImageURL();

        Glide.with(requireContext())
                .load(PicUrl)
                .into(productImage);

        recyclerView = view.findViewById(R.id.productRecyclerSize);
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Lớn");
        arrayList.add("Vừa");
        arrayList.add("Nhỏ");
        itemClickListener = new ItemClickListener() {
            @Override
            public void onClick(String s) {
                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
                SizeProduct = s;
                Toast.makeText(getContext(), "Selected " + s, Toast.LENGTH_SHORT).show();
            }
        };

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new SizeAdapter(arrayList, itemClickListener);
        recyclerView.setAdapter(adapter);

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

    @Override
    public void onToppingChange(ArrayList<String> arrayList) {
        // Handle your Topping List
        toppingList = arrayList;
        Toast.makeText(getContext(), arrayList.toString(), Toast.LENGTH_SHORT).show();
    }
}
