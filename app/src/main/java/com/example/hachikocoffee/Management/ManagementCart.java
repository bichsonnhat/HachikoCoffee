package com.example.hachikocoffee.Management;

import android.util.Log;
import android.widget.Toast;

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
    private int noId;
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
    public int getNoId(){
        return noId;
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
        noId = 0;
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

        noId += 1;
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
                        //saveCartToFirebase("1");
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
        Log.d("ManagementCart", "size: " + cartItems.size());
        Log.d("ManagementCart", "position: " + position);
        notifyCartChanged();
    }

    public void updateCart(int position, CartItem updatedItem) {
        DatabaseReference userCartRef = cartRef.child("1");
        CartItem existingItem = cartItems.get(position);
        itemsCount = itemsCount - existingItem.getQuantity() + updatedItem.getQuantity();

        boolean isNewItem = !updatedItem.getProductId().equals(existingItem.getProductId()) ||
                !updatedItem.getSize().equals(existingItem.getSize()) ||
                (updatedItem.getToppings() != null && existingItem.getToppings() != null && !updatedItem.getToppings().equals(existingItem.getToppings()));

        if (isNewItem) {
            for (CartItem item : cartItems) {
                if (item != existingItem && item.getProductId().equals(updatedItem.getProductId()) && item.getSize().equals(updatedItem.getSize())) {
                    if ((item.getToppings() == null && updatedItem.getToppings() == null) ||
                            (item.getToppings() != null && updatedItem.getToppings() != null && item.getToppings().equals(updatedItem.getToppings()))) {
                        item.setQuantity(item.getQuantity() + updatedItem.getQuantity());
                        item.setTotalCost(item.getTotalCost() + updatedItem.getTotalCost());
                        Log.d("ManagementCart", "update1: " + "true");
                        userCartRef.child(item.getCartItemId()).setValue(item);
                        itemsCount += existingItem.getQuantity();
                        Log.d("ManagementCart", "position: " + position);
                        removeFromCart(position);
                        return;
                    }
                }
            }
        }

        cartItems.set(position, updatedItem);
        cartItems.get(position).setCartItemId(existingItem.getCartItemId());

        updateItemCountToFirebase("1");
        userCartRef.child(cartItems.get(position).getCartItemId()).setValue(cartItems.get(position));

        notifyCartChanged();
    }

    public ArrayList<CartItem> getCartItems() {
        return cartItems;
    }

    public void clearCart() {
        cartItems.clear();
        itemsCount = 0;
        noId = 0;
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
            userCartRef.child(item.getCartItemId()).setValue(item);
        }
    }

    public void updateItemCountToFirebase(String userId){
        DatabaseReference userCartRef = cartRef.child(userId);
        userCartRef.child("itemCount").setValue(itemsCount);
        userCartRef.child("noId").setValue(noId);
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
                if (snapshot.hasChild("noId")){
                    noId = snapshot.child("noId").getValue(Integer.class);
                }
                for (DataSnapshot cartItemSnapshot : snapshot.getChildren()) {
                    if (!"itemCount".equals(cartItemSnapshot.getKey()) && !"noId".equals(cartItemSnapshot.getKey())) {
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
