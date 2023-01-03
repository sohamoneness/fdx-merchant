package com.oneness.fdxmerchant.Models.ItemManagementModels;

import com.oneness.fdxmerchant.Models.NotificationModels.NotificationModel;

import java.util.ArrayList;
import java.util.List;

public class CategoryResponseModel {
    public boolean error = false;
    public String message = "";
    public List<CategoryListModel> categories = new ArrayList<>();
}
