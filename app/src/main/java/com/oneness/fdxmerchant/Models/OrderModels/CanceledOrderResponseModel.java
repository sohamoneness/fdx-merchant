package com.oneness.fdxmerchant.Models.OrderModels;

import java.util.ArrayList;
import java.util.List;

public class CanceledOrderResponseModel {
    public boolean error = false;
    public String message = "";
    public List<CanceledOrderModel> orders = new ArrayList<>();
}
