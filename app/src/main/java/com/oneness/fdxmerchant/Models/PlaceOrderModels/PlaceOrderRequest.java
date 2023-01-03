package com.oneness.fdxmerchant.Models.PlaceOrderModels;

public class PlaceOrderRequest {
    public String restaurant_id = "";
    public String user_id = "";
    public String name = "";
    public String mobile = "";
    public String email = "";
    public String delivery_address = "";
    public String delivery_landmark = "";
    public String delivery_country = "";
    public String delivery_city = "";
    public String delivery_pin = "";
    public String delivery_lat = "";
    public String delivery_lng = "";
    public String amount = "";
    public String coupon_code = "";
    public String discounted_amount = "";
    public String transaction_id = "";


    public PlaceOrderRequest(String restaurant_id, String user_id, String name, String mobile,
                             String email, String delivery_address, String delivery_landmark,
                             String delivery_country, String delivery_city, String delivery_pin,
                             String delivery_lat, String delivery_lng, String amount,
                             String coupon_code, String discounted_amount, String transaction_id) {
        this.restaurant_id = restaurant_id;
        this.user_id = user_id;
        this.name = name;
        this.mobile = mobile;
        this.email = email;
        this.delivery_address = delivery_address;
        this.delivery_landmark = delivery_landmark;
        this.delivery_country = delivery_country;
        this.delivery_city = delivery_city;
        this.delivery_pin = delivery_pin;
        this.delivery_lat = delivery_lat;
        this.delivery_lng = delivery_lng;
        this.amount = amount;
        this.coupon_code = coupon_code;
        this.discounted_amount = discounted_amount;
        this.transaction_id = transaction_id;
    }
}
