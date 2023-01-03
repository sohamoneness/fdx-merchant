package com.oneness.fdxmerchant.Models.CategoryManagementModels;

public class CategoryUpdateRequestModel {
    public String id = "";
    public String title = "";
    public String description = "";

    public CategoryUpdateRequestModel(String id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }
}
