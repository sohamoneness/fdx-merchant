package com.oneness.fdxmerchant.Models.OrderModels;

import java.util.ArrayList;
import java.util.List;

public class OngoingOrderResponseModel {
    public boolean error = false;
    public String message = "";
    public List<OngoingOrderModel> orders = new ArrayList<>();
}
