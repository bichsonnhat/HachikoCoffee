package com.example.hachikocoffee.Management;

import com.example.hachikocoffee.Listener.OnCategoryChangedListener;
import com.example.hachikocoffee.Listener.OnUserChangedListener;

public class ListenerSingleton {
    private static ListenerSingleton instance;
    private OnCategoryChangedListener categoryChangedListener;
    private OnUserChangedListener userChangedListener;

    private ListenerSingleton() {}

    public static ListenerSingleton getInstance() {
        if (instance == null) {
            instance = new ListenerSingleton();
        }
        return instance;
    }

    public OnUserChangedListener getUserChangedListener() {
        return userChangedListener;
    }

    public void setUserChangedListener(OnUserChangedListener userChangedListener) {
        this.userChangedListener = userChangedListener;
    }

    public OnCategoryChangedListener getCategoryChangedListener() {
        return categoryChangedListener;
    }

    public void setCategoryChangedListener(OnCategoryChangedListener categoryAddedListener) {
        this.categoryChangedListener = categoryAddedListener;
    }
}
