package com.example.androidfeature.bean;

import java.util.List;

public class Order {
    public List<Integer> foodIds;
    public String order_owner;

    public Order(List<Integer> foodIds, String order_owner) {
        this.foodIds = foodIds;
        this.order_owner = order_owner;
    }
}
