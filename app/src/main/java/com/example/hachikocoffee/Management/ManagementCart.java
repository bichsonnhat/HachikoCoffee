package com.example.hachikocoffee.Management;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.hachikocoffee.Domain.CartItem;
import com.example.hachikocoffee.Listener.OnCartChangedListener;
import com.example.hachikocoffee.Listener.OnCartLoadedListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ManagementCart {
    private static ManagementCart instance;
    private ArrayList<CartItem> cartItems;
    private FirebaseDatabase database;
    private long itemsCount;
    private String recipentName;
    private String recipentPhone;
    private DatabaseReference cartRef;
    private OnCartLoadedListener onCartLoadedListener;
    public void setOnCartLoadedListener(OnCartLoadedListener listener) {
        this.onCartLoadedListener = listener;
    }

    public long getItemsCount() {
        return itemsCount;
    }

    public String getRecipentName() {
        return recipentName;
    }

    public String getRecipentPhone() {
        return recipentPhone;
    }

    public void setRecipentName(String recipentName) {
        this.recipentName = recipentName;
    }

    public void setRecipentPhone(String recipentPhone) {
        this.recipentPhone = recipentPhone;
    }

    private ManagementCart() {
        cartItems = new ArrayList<>();
        itemsCount = 0;
        recipentName = "";
        recipentPhone = "";
        database = FirebaseDatabase.getInstance();
        cartRef = database.getReference("CARTS");
    }

    public static ManagementCart getInstance() {
        if (instance == null) {
            instance = new ManagementCart();
        }
        return instance;
    }

    public void addToCart(CartItem newItem) {
        boolean itemExists = false;

        for (CartItem item : cartItems) {
            if (item.getProductId().equals(newItem.getProductId()) && item.getSize().equals(newItem.getSize())) {
                if ((item.getToppings() != null)){
                    Log.d("CartAdapter", "Toppings list1: " + "true");
                }
                if ((newItem.getToppings() != null)){
                    Log.d("CartAdapter", "Toppings list2: " + "true");
                }
                if ((item.getToppings() == null && newItem.getToppings() == null) ||
                        (item.getToppings() != null && newItem.getToppings() != null && item.getToppings().equals(newItem.getToppings()))) {

                    item.setQuantity(item.getQuantity() + newItem.getQuantity());
                    item.setTotalCost(item.getTotalCost() + newItem.getTotalCost());
                    itemExists = true;
                    break;
                }
            }
        }

        if (!itemExists) {
            cartItems.add(newItem);
        }

        itemsCount += newItem.getQuantity();
        updateItemCountToFirebase("1");
        saveCartToFirebase("1");

        notifyCartChanged();
    }

    public void removeFromCart(int position) {
        CartItem removedItem = cartItems.get(position);
        cartItems.remove(position);
        DatabaseReference userCartRef = cartRef.child("1");
        userCartRef.child("itemCount").setValue(itemsCount);
        userCartRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    CartItem item = dataSnapshot.getValue(CartItem.class);
                    if (item != null && item.equals(removedItem)) {
                        dataSnapshot.getRef().removeValue();
                        Log.e("ManagementCart", "Failed to remove item from Firebase: " + "true");
                        break;
                    }
                    if (item == null){
                        Log.e("ManagementCart", "remove " + "true");
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("ManagementCart", "Failed to remove item from Firebase: " + error.getMessage());
            }
        });

        if (cartItems.isEmpty()){
            userCartRef.child("itemCount").removeValue();
        }

        itemsCount -= removedItem.getQuantity();
        updateItemCountToFirebase("1");
        notifyCartChanged();
    }

    public void updateCart(int position, CartItem item) {
        cartItems.set(position, item);
        notifyCartChanged();
    }

    public ArrayList<CartItem> getCartItems() {
        return cartItems;
    }

    public void clearCart() {
        cartItems.clear();
        itemsCount = 0;
        DatabaseReference userCartRef = cartRef.child("1");
        userCartRef.removeValue();
        notifyCartChanged();
    }
    public int getTotalCost() {
        int totalCost = 0;
        for (CartItem item : cartItems) {
            totalCost += item.getTotalCost();
        }
        return totalCost;
    }

    public void saveCartToFirebase(String userId) {
        DatabaseReference userCartRef = cartRef.child(userId);
        for (int i = 0; i < cartItems.size(); i++) {
            CartItem item = cartItems.get(i);
            userCartRef.child(String.valueOf(i)).setValue(item);
        }
    }

    public void updateItemCountToFirebase(String userId){
        DatabaseReference userCartRef = cartRef.child(userId);
        userCartRef.child("itemCount").setValue(itemsCount);
    }
    public void updateNameAndPhoneToFireBase(String userId){
        DatabaseReference userCartRef = cartRef.child(userId);
        userCartRef.child("recipentName").setValue(recipentName);
        userCartRef.child("recipentPhone").setValue(recipentPhone);
    }

    public void loadNameAndPhone(){
        recipentName = ManagementUser.getInstance().getUser().getName();
        recipentPhone = ManagementUser.getInstance().getUser().getPhoneNumber();
    }

    public void loadCartFromFirebase(String userId) {
        DatabaseReference userCartRef = cartRef.child(userId);
        userCartRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cartItems.clear();
                if (snapshot.hasChild("itemCount")) {
                    itemsCount = snapshot.child("itemCount").getValue(Long.class);
                }

                // Lấy danh sách CartItem, bỏ qua itemCount
                for (DataSnapshot cartItemSnapshot : snapshot.getChildren()) {
                    if (!"itemCount".equals(cartItemSnapshot.getKey())) {
                        CartItem item = cartItemSnapshot.getValue(CartItem.class);
                        if (item != null) {
                            cartItems.add(item);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private ArrayList<OnCartChangedListener> cartChangedListeners = new ArrayList<>();

    public void addOnCartChangedListener(OnCartChangedListener listener) {
        cartChangedListeners.add(listener);
    }

    public void removeOnCartChangedListener(OnCartChangedListener listener) {
        cartChangedListeners.remove(listener);
    }

    private void notifyCartChanged() {
        for (OnCartChangedListener listener : cartChangedListeners) {
            listener.onCartChanged();
        }
    }
}