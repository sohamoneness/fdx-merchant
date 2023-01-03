package com.oneness.fdxmerchant.Models.OrderModels.OrderItemsModels;

import java.util.ArrayList;
import java.util.List;

public class OrderCategoryModel {
    public String id = "";
    public String restaurant_id = "";
    public String title = "";
    public String description = "";
    public String image = "";
    public String position = "";
    public String status = "";
    public String created_at = "";
    public String updated_at = "";
    public List<OrderItemModel> items = new ArrayList<>();

}
