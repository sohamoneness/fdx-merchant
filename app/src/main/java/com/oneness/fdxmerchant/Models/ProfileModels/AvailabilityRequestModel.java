package com.oneness.fdxmerchant.Models.ProfileModels;

public class AvailabilityRequestModel {
    public String restaurant_id = "";
    public String days = "";
    public String start_times = "";
    public String end_times = "";

    public AvailabilityRequestModel(String restaurant_id, String days, String start_times, String end_times) {
        this.restaurant_id = restaurant_id;
        this.days = days;
        this.start_times = start_times;
        this.end_times = end_times;
    }
}
