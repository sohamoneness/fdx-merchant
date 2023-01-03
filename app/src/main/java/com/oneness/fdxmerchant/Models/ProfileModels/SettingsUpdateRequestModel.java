package com.oneness.fdxmerchant.Models.ProfileModels;

public class SettingsUpdateRequestModel {
    public String restaurant_id = "";
    public String including_tax = "";
    public String tax_rate = "";
    public String minimum_order_amount = "";
    public String order_preparation_time = "";
    public String show_out_of_stock_products_in_app = "";

    public SettingsUpdateRequestModel(String restaurant_id, String including_tax, String tax_rate, String minimum_order_amount, String order_preparation_time, String show_out_of_stock_products_in_app) {
        this.restaurant_id = restaurant_id;
        this.including_tax = including_tax;
        this.tax_rate = tax_rate;
        this.minimum_order_amount = minimum_order_amount;
        this.order_preparation_time = order_preparation_time;
        this.show_out_of_stock_products_in_app = show_out_of_stock_products_in_app;
    }
}
