package com.example.hachikocoffee.Management;

import com.example.hachikocoffee.Listener.OnCategoryChangedListener;

public class ListenerSingleton {
    private static ListenerSingleton instance;
    private OnCategoryChangedListener categoryChangedListener;

    private ListenerSingleton() {}

    public static ListenerSingleton getInstance() {
        if (instance == null) {
            instance = new ListenerSingleton();
        }
        return instance;
    }

    public OnCategoryChangedListener getCategoryChangedListener() {
        return categoryChangedListener;
    }

    public void setCategoryChangedListener(OnCategoryChangedListener categoryAddedListener) {
        this.categoryChangedListener = categoryAddedListener;
    }
}
