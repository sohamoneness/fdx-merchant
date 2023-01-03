package com.oneness.fdxmerchant.Models.OrderModels;

public class AcceptOrderRequestModel {
    public String id = "";
    public String preparation_time = "";

    public AcceptOrderRequestModel(String id, String preparation_time) {
        this.id = id;
        this.preparation_time = preparation_time;
    }
}
