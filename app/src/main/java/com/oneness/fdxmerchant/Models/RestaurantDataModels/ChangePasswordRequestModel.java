package com.oneness.fdxmerchant.Models.RestaurantDataModels;

public class ChangePasswordRequestModel {

    public String restaurant_id = "";
    public String password = "";

    public ChangePasswordRequestModel(String restaurant_id, String password) {
        this.restaurant_id = restaurant_id;
        this.password = password;
    }
}
