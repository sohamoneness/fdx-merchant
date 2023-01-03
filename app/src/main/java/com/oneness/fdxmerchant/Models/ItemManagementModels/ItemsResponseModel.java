package com.oneness.fdxmerchant.Models.ItemManagementModels;

import java.util.ArrayList;
import java.util.List;

public class ItemsResponseModel {

    public boolean error = false;
    public String message = "";
    public List<CategoryWithItemModel> categories = new ArrayList<>();

}
