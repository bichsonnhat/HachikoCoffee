package com.example.hachikocoffee.Listener;

import com.example.hachikocoffee.Domain.OrderDomain;

public interface OrderClickListener {
    void onClickOrderItem(OrderDomain order);
}