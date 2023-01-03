package com.oneness.fdxmerchant.Models.OrderModels;

import java.util.ArrayList;
import java.util.List;

public class DeliveredOrderResponseModel {
    public boolean error = false;
    public String message = "";
    public List<DeliveredOrderModel> orders = new ArrayList<>();
}
