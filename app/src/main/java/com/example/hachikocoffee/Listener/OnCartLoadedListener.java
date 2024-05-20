package com.example.hachikocoffee.Listener;

import com.example.hachikocoffee.Domain.CartItem;

import java.util.ArrayList;

public interface OnCartLoadedListener {
    void onCartLoaded(ArrayList<CartItem> cartItems);
}
