package com.oneness.fdxmerchant.Models.ItemManagementModels;

public class UpdateItemRequestModel {

    public String item_id = "";
    public String category_id = "";
    public String name = "";
    public String description = "";
    public String price = "";
    public String offer_price = "";
    public String is_veg = "";
    public String is_cutlery_required = "";
    public String min_item_for_cutlery = "";
    public String in_stock = "";
    public String image = "";
    public String is_special = "";
    public String is_add_on = "";
    public String add_on_item_id = "";

    public UpdateItemRequestModel(String item_id, String category_id, String name,
                               String description, String price, String offer_price,
                               String is_veg, String is_cutlery_required,
                               String min_item_for_cutlery, String in_stock, String image,
                                  String is_special, String is_add_on, String add_on_item_id) {
        this.item_id = item_id;
        this.category_id = category_id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.offer_price = offer_price;
        this.is_veg = is_veg;
        this.is_cutlery_required = is_cutlery_required;
        this.min_item_for_cutlery = min_item_for_cutlery;
        this.in_stock = in_stock;
        this.image = image;
        this.is_special = is_special;
        this.is_add_on = is_add_on;
        this.add_on_item_id = add_on_item_id;
    }

}
