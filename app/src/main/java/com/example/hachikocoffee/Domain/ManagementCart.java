package com.example.hachikocoffee.Domain;

import java.util.ArrayList;

public class ManagementCart {
    private static ManagementCart instance;
    private ArrayList<CartItem> cartItems;

    private ManagementCart() {
        cartItems = new ArrayList<>();
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
            if (item.getProductId().equals(newItem.getProductId()) && item.getSize().equals(newItem.getSize()) &&
                    item.getToppings().equals(newItem.getToppings()))
            {
                item.setQuantity(item.getQuantity() + newItem.getQuantity());
                item.setTotalCost(item.getTotalCost() + newItem.getTotalCost());
                itemExists = true;
                break;
            }
        }

        if (!itemExists) {
            cartItems.add(newItem);
        }
    }

    public void removeFromCart(int position) {
        cartItems.remove(position);
    }

    public void updateCart(int position, CartItem item) {
        cartItems.set(position, item);
    }

    public ArrayList<CartItem> getCartItems() {
        return cartItems;
    }

    public void clearCart() {
        cartItems.clear();
    }
    public int getTotalCost() {
        int totalCost = 0;
        for (CartItem item : cartItems) {
            totalCost += item.getTotalCost();
        }
        return totalCost;
    }
}
