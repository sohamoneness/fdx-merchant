package com.oneness.fdxmerchant.Models.OrderModels;

public class RejectOrderRequestModel {

    public String id = "";
    public String cancellation_reason = "";

    public RejectOrderRequestModel(String id, String cancellation_reason) {
        this.id = id;
        this.cancellation_reason = cancellation_reason;
    }
}
