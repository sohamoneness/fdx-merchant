package com.oneness.fdxmerchant.Models.CategoryManagementModels;

public class CategoryAddRequestModel {

    public String restaurant_id = "";
    public String title = "";
    public String description = "";

    public CategoryAddRequestModel(String restaurant_id, String title, String description) {
        this.restaurant_id = restaurant_id;
        this.title = title;
        this.description = description;
    }
}
