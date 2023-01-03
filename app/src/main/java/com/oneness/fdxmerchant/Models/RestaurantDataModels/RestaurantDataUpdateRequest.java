package com.oneness.fdxmerchant.Models.RestaurantDataModels;

public class RestaurantDataUpdateRequest {
    public String restaurant_id = "";
    public String name = "";
    public String email = "";
    public String mobile = "";
    public String description = "";
    public String address = "";
    public String location = "";
    public String lat = "";
    public String lng = "";
    public String start_time = "";
    public String close_time = "";
    public String is_pure_veg = "";
    public String commission_rate = "";
    public String estimated_delivery_time = "";
    public String is_not_taking_orders = "";
    public String image = "";
    public String logo = "";

    public RestaurantDataUpdateRequest(String restaurant_id, String name, String email,
                                       String mobile, String description, String address,
                                       String location, String lat, String lng, String start_time,
                                       String close_time, String is_pure_veg, String commission_rate,
                                       String estimated_delivery_time, String is_not_taking_orders,
                                       String image, String logo) {
        this.restaurant_id = restaurant_id;
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.description = description;
        this.address = address;
        this.location = location;
        this.lat = lat;
        this.lng = lng;
        this.start_time = start_time;
        this.close_time = close_time;
        this.is_pure_veg = is_pure_veg;
        this.commission_rate = commission_rate;
        this.estimated_delivery_time = estimated_delivery_time;
        this.is_not_taking_orders = is_not_taking_orders;
        this.image = image;
        this.logo = logo;
    }
}
