package com.example.hachikocoffee.BottomSheetDialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hachikocoffee.Adapter.FavouriteAdapter;
import com.example.hachikocoffee.Adapter.SizeAdapter;
import com.example.hachikocoffee.Adapter.ToppingAdapter;
import com.example.hachikocoffee.Domain.FavouriteItemDomain;
import com.example.hachikocoffee.Domain.ItemsDomain;
import com.example.hachikocoffee.Listener.ItemClickListener;
import com.example.hachikocoffee.Listener.ToppingListener;
import com.example.hachikocoffee.Listener.UpdateUIListener;
import com.example.hachikocoffee.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Random;

public class ProductDetail extends BottomSheetDialogFragment implements ToppingListener {
    FavouriteAdapter adapter1;
    ArrayList<ItemsDomain> items;
    ItemsDomain object;
    RecyclerView recyclerView;
    RecyclerView recyclerViewTopping;
    ItemClickListener itemClickListener;
    SizeAdapter adapter;
    ToppingAdapter toppingAdapter;
    int countProduct = 1;
    String SizeProduct = "Nhỏ";
    ArrayList<String> toppingList = new ArrayList<>();
    int sizeToping = 0;
    private UpdateUIListener updateUIListener;

    public ProductDetail(ItemsDomain object){ this.object = object;};
    public ProductDetail(ItemsDomain object, ArrayList<ItemsDomain> items, FavouriteAdapter adapter){
        this.object = object;
        this.items = items;
        this.adapter1 = adapter;
    }
    AppCompatButton totalProductCost;
    int totalOrder = 0;
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
        totalProductCost = view.findViewById(R.id.totalProductCost);
        TextView productDescription = view.findViewById(R.id.productDescription);
        TextView productMinimumSize = view.findViewById(R.id.productMinimumSize);
        TextView productMediumSize = view.findViewById(R.id.productMediumSize);
        TextView productLargeSize = view.findViewById(R.id.productLargeSize);
        totalProductCost.setText("Chọn • " + (int) (object.getPrice() + 10 * sizeToping) + "đ");
        CheckBox favouriteProduct = view.findViewById(R.id.favouriteProduct);

        String ProductID = object.getProductID();
        int UserID = 1;
        Random random = new Random();
        int FavouriteProductID = random.nextInt(100) + 1;

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference favoriteProductsRef = database.getReference("FAVORITEPRODUCT");

        favouriteProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (favouriteProduct.isChecked()) {
                    FavouriteItemDomain favouriteItem = new FavouriteItemDomain(ProductID, FavouriteProductID, UserID);
                    DatabaseReference newFavoriteProductRef = favoriteProductsRef.push();
                    newFavoriteProductRef.setValue(favouriteItem)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getContext(), "Thêm thành công vào danh sách yêu thích", Toast.LENGTH_SHORT).show();
                                    Log.d("ProductDetailActivity", "Successfully added/removed favorite product");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e("ProductDetailActivity", "Error adding/removing favorite product: " + e.getMessage());
                                }
                            });
                }
                else{
                    Query query = favoriteProductsRef.orderByChild("userID").equalTo(UserID);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @SuppressLint("NotifyDataSetChanged")
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot product : snapshot.getChildren()) {
                                FavouriteItemDomain favouriteItem = product.getValue(FavouriteItemDomain.class);
                                if (favouriteItem != null && favouriteItem.getProductID().equals(ProductID)) {
                                    product.getRef().removeValue();
                                    if (items != null){
                                        items.remove(object);
                                        if (items.isEmpty()){
                                            updateUIListener.updateUI(0);
                                        }
                                    }
                                    //
                                    Toast.makeText(getContext(), "Xóa thành công khỏi danh sách yêu thích", Toast.LENGTH_SHORT).show();
                                }
                            }

                            if (adapter1 != null){
                                adapter1.notifyDataSetChanged();
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Xử lý lỗi
                        }
                    });
                }
            }
        });

        Query query = favoriteProductsRef.orderByChild("userID").equalTo(UserID);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean isProductFavorite = false;
                for (DataSnapshot product : snapshot.getChildren()) {
                    FavouriteItemDomain favouriteItem = product.getValue(FavouriteItemDomain.class);
                    if (favouriteItem != null && favouriteItem.getProductID().equals(ProductID)) {
                        isProductFavorite = true;
                        break;
                    }
                }

                favouriteProduct.setChecked(isProductFavorite);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi
            }
        });


        totalOrder = (int) object.getPrice();
        minusProduct.setOnClickListener(new View.OnClickListener() {
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
        productDescription.setText(object.getDescription());
        productMinimumSize.setText(itemCost + "đ");
        productMediumSize.setText(10000 + itemCost + "đ");
        productLargeSize.setText(20000 + itemCost + "đ");
        productCost.setText(itemCost + "đ");
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
        sizeToping = arrayList.size();
        totalOrder = (((int) object.getPrice() + 10 * sizeToping) * countProduct);
        totalProductCost.setText("Chọn • " + totalOrder + "đ");
        Toast.makeText(getContext(), arrayList.toString(), Toast.LENGTH_SHORT).show();
    }

    public void setUpdateUIListener(UpdateUIListener listener) {
        this.updateUIListener = listener;
    }
}
