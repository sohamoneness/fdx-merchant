package com.oneness.fdxmerchant.Models.OrderModels.OrderItemsModels;

public class OrderItemRequestModel {
    public String restaurant_id = "";
    public String user_id = "";

    public OrderItemRequestModel(String restaurant_id, String user_id) {
        this.restaurant_id = restaurant_id;
        this.user_id = user_id;
    }
}
