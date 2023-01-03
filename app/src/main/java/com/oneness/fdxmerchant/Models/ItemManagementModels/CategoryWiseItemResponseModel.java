package com.oneness.fdxmerchant.Models.ItemManagementModels;

import java.util.ArrayList;
import java.util.List;

public class CategoryWiseItemResponseModel {
    public boolean error = false;
    public String message = "";
    public List<ItemsModel> items = new ArrayList<>();
}
