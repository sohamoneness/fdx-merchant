package com.oneness.fdxmerchant.Models.ReportManagementModels;

import com.oneness.fdxmerchant.Models.RestaurantDataModels.RestaurantDataObjectModel;

import java.util.ArrayList;
import java.util.List;

public class OrderCountResponseModel {
    public boolean error = false;
    public String message = "";
    public List<OrderCountDataModel> data = new ArrayList<>();

}
