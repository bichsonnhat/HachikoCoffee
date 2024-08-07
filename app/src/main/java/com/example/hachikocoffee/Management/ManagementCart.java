package com.example.hachikocoffee.Management;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.hachikocoffee.Domain.CartItem;
import com.example.hachikocoffee.Domain.DiscountDomain;
import com.example.hachikocoffee.Listener.OnCartChangedListener;
import com.example.hachikocoffee.Listener.OnCartLoadedListener;
import com.example.hachikocoffee.Listener.OnDataLoadedCallback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ManagementCart {
    private int UserId;
    private static ManagementCart instance;
    private ArrayList<CartItem> cartItems;
    private FirebaseDatabase database;
    private long itemsCount;
    private int noId;
    private DiscountDomain voucher;
    private String recipentName;
    private String recipentPhone;
    private String orderTime;
    private int storeId;
    private String location;
    private String subLocation;
    private DatabaseReference cartRef;
    private OnCartLoadedListener onCartLoadedListener;
    public void setOnCartLoadedListener(OnCartLoadedListener listener) {
        this.onCartLoadedListener = listener;
    }

    public String getSubLocation() {
        return subLocation;
    }

    public void setSubLocation(String subLocation) {
        this.subLocation = subLocation;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
        DatabaseReference userCartRef = cartRef.child(String.valueOf(UserId));
        userCartRef.child("location").setValue(location);
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public DiscountDomain getVoucher() {
        return voucher;
    }

    public void setVoucher(DiscountDomain voucher) {
        this.voucher = voucher;
        notifyCartChanged();
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
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
        orderTime = "";
        recipentName = "";
        recipentPhone = "";
        location = "";
        voucher = null;
        storeId = -1;
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
        updateItemCountToFirebase(String.valueOf(UserId));
        saveCartToFirebase(String.valueOf(UserId));

        notifyCartChanged();
    }

    public void removeFromCart(int position) {
        CartItem removedItem = cartItems.get(position);
        cartItems.remove(position);
        DatabaseReference userCartRef = cartRef.child(String.valueOf(UserId));
        Log.d("user id", "" + UserId);
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
        updateItemCountToFirebase(String.valueOf(UserId));
        Log.d("ManagementCart", "size: " + cartItems.size());
        Log.d("ManagementCart", "position: " + position);
        notifyCartChanged();
    }

    public void updateCart(int position, CartItem updatedItem) {
        DatabaseReference userCartRef = cartRef.child(String.valueOf(UserId));
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

        updateItemCountToFirebase(String.valueOf(UserId));
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
        voucher = null;
        DatabaseReference userCartRef = cartRef.child(String.valueOf(UserId));
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
    public void updateVoucherToFireBase(String userId){
        DatabaseReference userCartRef = cartRef.child(userId);
        userCartRef.child("voucher").setValue(voucher);
    }

    public void removeVoucherFromFireBase(String userId){
        voucher = null;
        DatabaseReference userCartRef = cartRef.child(userId);
        userCartRef.child("voucher").removeValue();
        notifyCartChanged();
    }

    public void loadNameAndPhone(){
        recipentName = ManagementUser.getInstance().getUser().getName();
        recipentPhone = ManagementUser.getInstance().getUser().getPhoneNumber();
        updateNameAndPhoneToFireBase(String.valueOf(UserId));
    }

    public void updateTimeToFireBase(String userId){
        DatabaseReference userCartRef = cartRef.child(userId);
        userCartRef.child("orderTime").setValue(orderTime);
    }
    public void loadTime(){
        LocalTime currentTime = LocalTime.now().withSecond(0).withNano(0);
        int currentHour = currentTime.getHour();
        int currentMinute = currentTime.getMinute();
        @SuppressLint("DefaultLocale") String formattedTime = String.format("%02d:%02d", currentHour, currentMinute);

        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedDate = currentDate.format(formatter);
        orderTime = formattedDate + " " + formattedTime;

        updateTimeToFireBase(String.valueOf(UserId));
    }

    public void loadCartFromFirebase(String userId) {
        this.UserId = Integer.parseInt(userId);
        DatabaseReference userCartRef = cartRef.child(userId);
        userCartRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cartItems.clear();
                if (snapshot.exists()){
                    Log.d("Cart", "True");
                    if (snapshot.hasChild("itemCount")) {
                        itemsCount = snapshot.child("itemCount").getValue(Long.class);
                    }
                    if (snapshot.hasChild("noId")){
                        noId = snapshot.child("noId").getValue(Integer.class);
                    }
                    if (snapshot.hasChild("recipentName") && snapshot.hasChild("recipentPhone")){
                        recipentName = snapshot.child("recipentName").getValue(String.class);
                        recipentPhone = snapshot.child("recipentPhone").getValue(String.class);
                    }
                    if (snapshot.hasChild("voucher")){
                        voucher = snapshot.child("voucher").getValue(DiscountDomain.class);
                    }
                    else{
                        loadNameAndPhone();
                    }

                    for (DataSnapshot cartItemSnapshot : snapshot.getChildren()) {
                        if (!"itemCount".equals(cartItemSnapshot.getKey()) && !"noId".equals(cartItemSnapshot.getKey())
                            && !"recipentName".equals(cartItemSnapshot.getKey()) && !"recipentPhone".equals(cartItemSnapshot.getKey())
                            && !"orderTime".equals(cartItemSnapshot.getKey()) && !"voucher".equals(cartItemSnapshot.getKey())
                            && !"location".equals(cartItemSnapshot.getKey())) {
                            CartItem item = cartItemSnapshot.getValue(CartItem.class);
                            if (item != null) {
                                cartItems.add(item);
                                Log.d("Cart1", "true");
                            }
                        }
                    }
                }
                Log.d("Cart", "Size" + cartItems.size());
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
