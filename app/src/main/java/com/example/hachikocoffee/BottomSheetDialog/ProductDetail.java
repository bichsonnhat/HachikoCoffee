package com.example.hachikocoffee.BottomSheetDialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hachikocoffee.Adapter.FavouriteAdapter;
import com.example.hachikocoffee.Adapter.SizeAdapter;
import com.example.hachikocoffee.Adapter.ToppingAdapter;
import com.example.hachikocoffee.Domain.CartItem;
import com.example.hachikocoffee.Domain.FavouriteItemDomain;
import com.example.hachikocoffee.Domain.ItemsDomain;
import com.example.hachikocoffee.Management.ManagementCart;
import com.example.hachikocoffee.Listener.ItemClickListener;
import com.example.hachikocoffee.Listener.ToppingListener;
import com.example.hachikocoffee.Listener.UpdateUIListener;
import com.example.hachikocoffee.Management.ManagementUser;
import com.example.hachikocoffee.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;

public class ProductDetail extends BottomSheetDialogFragment implements ToppingListener {
    private final ItemsDomain product;
    private final ArrayList<ItemsDomain> items;
    private final FavouriteAdapter adapter1;
    private SizeAdapter adapter;
    private RecyclerView recyclerViewTopping;
    private AppCompatButton totalProductCost;
    private ItemClickListener itemClickListener;
    private int countProduct = 1;
    private TextView numberOfProduct;
    private String sizeProduct = "Nhỏ";
    private String textNote = "";
    private int totalCost = 0;
    private final ArrayList<String> toppingList = new ArrayList<>();
    private UpdateUIListener updateUIListener;
    private EditText notes;
    DecimalFormatSymbols symbols;

    public ProductDetail(ItemsDomain product) {
        this.product = product;
        this.items = null;
        this.adapter1 = null;
    }

    public ProductDetail(ItemsDomain product, ArrayList<ItemsDomain> items, FavouriteAdapter adapter) {
        this.product = product;
        this.items = items;
        this.adapter1 = adapter;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.product_detail, container, false);
        //format money
        symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');

        setupViews(view);
        setupRecyclerViews(view);
        setupClickListeners(view);
        setupFavoriteProduct(view);
        return view;
    }

    @SuppressLint("SetTextI18n")
    private void setupViews(View view) {
        TextView productName = view.findViewById(R.id.productName);
        TextView productCost = view.findViewById(R.id.productCost);
        ImageView productImage = view.findViewById(R.id.product_image_scr);
        recyclerViewTopping = view.findViewById(R.id.productRecyclerTopping);
        totalProductCost = view.findViewById(R.id.totalProductCost);
        TextView productDescription = view.findViewById(R.id.productDescription);
        numberOfProduct = view.findViewById(R.id.numberOfProduct);
        TextView productMinimumSize = view.findViewById(R.id.productMinimumSize);
        TextView productMediumSize = view.findViewById(R.id.productMediumSize);
        TextView productLargeSize = view.findViewById(R.id.productLargeSize);
        totalCost = (int) product.getPrice();
        //totalProductCost.setText("Chọn • " + (totalCost) + "đ");

        int itemCost = totalCost;

        productName.setText(product.getTitle());
        productDescription.setText(product.getDescription());

        String a = new DecimalFormat("#,###", symbols).format(itemCost);
        String b = new DecimalFormat("#,###", symbols).format(itemCost + 10000);
        String c = new DecimalFormat("#,###", symbols).format(itemCost + 20000);

        productMinimumSize.setText(a + "đ");
        productMediumSize.setText(b + "đ");
        productLargeSize.setText(c + "đ");
        productCost.setText(a + "đ");

        Glide.with(requireContext())
                .load(product.getImageURL())
                .into(productImage);

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
//                Toast.makeText(getContext(), "Selected " + s, Toast.LENGTH_SHORT).show();
                switch (sizeProduct) {
                    case "Lớn":
                        totalCost = (int) (product.getPrice() + 20000);
                        break;
                    case "Vừa":
                        totalCost = (int) (product.getPrice() + 10000);
                        break;
                    default:
                        totalCost = (int) product.getPrice();
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
            if (toppingList.size() != 0){
                cartItem = new CartItem(product.getProductID(), product.getTitle(), countProduct, sizeProduct, toppingList, totalCost * countProduct + toppingList.size() * 10000 * countProduct, product.getPrice(), textNote);
            }
            else {
                cartItem = new CartItem(product.getProductID(), product.getTitle(), countProduct, sizeProduct, null, totalCost * countProduct + toppingList.size() * 10000 * countProduct, product.getPrice(), textNote);
            }
            ManagementCart.getInstance().addToCart(cartItem);;

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

        notes = view.findViewById(R.id.notes);
        notes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditDialog();
            }
        });
    }

    private void showEditDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_edit_note, null);
        builder.setView(dialogView);

        final EditText editTextDialog = dialogView.findViewById(R.id.editTextDialog);
        editTextDialog.setText(notes.getText());
        TextView btnConfirm = dialogView.findViewById(R.id.btnConfirm);

        final AlertDialog dialog = builder.create();

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputText = editTextDialog.getText().toString();
                if (inputText.trim().isEmpty()){
                    dialog.dismiss();
                }
                else{
                    notes.setText(inputText);
                    textNote = inputText;
                    dialog.dismiss();
                }
            }
        });

        dialog.show();
    }

    private void setupFavoriteProduct(View view) {
        CheckBox favoriteProduct = view.findViewById(R.id.favouriteProduct);
        int userId = ManagementUser.getInstance().getUserId();
        String productId = product.getProductID();
        DatabaseReference favoriteProductsRef = FirebaseDatabase.getInstance().getReference("FAVORITEPRODUCT");

        favoriteProduct.setOnClickListener(v -> {
            if (favoriteProduct.isChecked()) {
                addToFavorites(productId, userId, favoriteProductsRef);
            } else {
                removeFromFavorites(productId, userId, favoriteProductsRef);
            }
        });

        checkIfProductIsFavorite(productId, userId, favoriteProductsRef, favoriteProduct);
    }

    private void addToFavorites(String productId, int userId, DatabaseReference favoriteProductsRef) {
        FavouriteItemDomain favoriteItem = new FavouriteItemDomain(productId, userId);
        DatabaseReference newFavoriteProductRef = favoriteProductsRef.push();
        newFavoriteProductRef.setValue(favoriteItem)
                .addOnSuccessListener(aVoid ->
                        Toast.makeText(requireContext(), "Thêm thành công vào danh sách yêu thích", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e ->
                        Toast.makeText(requireContext(), "Lỗi khi thêm vào danh sách yêu thích", Toast.LENGTH_SHORT).show());
    }

    private void removeFromFavorites(String productId, int userId, DatabaseReference favoriteProductsRef) {
        Query query = favoriteProductsRef.orderByChild("userID").equalTo(userId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot pro : snapshot.getChildren()) {
                    FavouriteItemDomain favoriteItem = pro.getValue(FavouriteItemDomain.class);
                    if (favoriteItem != null && favoriteItem.getProductID().equals(productId)) {
                        pro.getRef().removeValue();
                        if (items != null){
                            items.remove(product);
                            if (items.isEmpty()){
                                updateUIListener.updateUI(0);
                            }
                        }
                        //
                        Toast.makeText(getContext(), "Xóa thành công khỏi danh sách yêu thích", Toast.LENGTH_SHORT).show();
                    }
                }
                if (adapter1 != null) {
                    adapter1.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(requireContext(), "Lỗi: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkIfProductIsFavorite(String productId, int userId, DatabaseReference favoriteProductsRef, CheckBox favoriteProduct) {
        Query query = favoriteProductsRef.orderByChild("userID").equalTo(userId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean isProductFavorite = false;
                for (DataSnapshot product : snapshot.getChildren()) {
                    FavouriteItemDomain favoriteItem = product.getValue(FavouriteItemDomain.class);
                    if (favoriteItem != null && favoriteItem.getProductID().equals(productId)) {
                        isProductFavorite = true;
                        break;
                    }
                }
                favoriteProduct.setChecked(isProductFavorite);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(requireContext(), "Lỗi: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
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
//        Toast.makeText(requireContext(), toppings.toString(), Toast.LENGTH_SHORT).show();
    }

    public void setUpdateUIListener(UpdateUIListener listener) {
        updateUIListener = listener;
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
}