package com.oneness.fdxmerchant.Models.ProfileModels;

public class DeliveryConfigUpdateRequestModel {
    public String restaurant_id = "";
    public String delivery_types = "";

    public DeliveryConfigUpdateRequestModel(String restaurant_id, String delivery_types) {
        this.restaurant_id = restaurant_id;
        this.delivery_types = delivery_types;
    }
}
