package com.oneness.fdxmerchant.Models.CouponModels;

public class UpdateCouponRequestModel {
    public String id = "";
    public String restaurant_id = "";
    public String title = "";
    public String description = "";
    public String code = "";
    public String type = "";
    public String rate = "";
    public String maximum_offer_rate = "";
    public String start_date = "";
    public String end_date = "";
    public String maximum_time_of_use = "";
    public String maximum_time_user_can_use = "";

    public UpdateCouponRequestModel(String id, String restaurant_id, String title,
                                    String description, String code, String type, String rate,
                                    String maximum_offer_rate, String start_date, String end_date,
                                    String maximum_time_of_use, String maximum_time_user_can_use) {
        this.id = id;
        this.restaurant_id = restaurant_id;
        this.title = title;
        this.description = description;
        this.code = code;
        this.type = type;
        this.rate = rate;
        this.maximum_offer_rate = maximum_offer_rate;
        this.start_date = start_date;
        this.end_date = end_date;
        this.maximum_time_of_use = maximum_time_of_use;
        this.maximum_time_user_can_use = maximum_time_user_can_use;
    }
}
