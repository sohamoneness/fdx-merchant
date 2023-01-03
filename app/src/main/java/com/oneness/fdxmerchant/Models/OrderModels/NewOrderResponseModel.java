package com.oneness.fdxmerchant.Models.OrderModels;

import java.util.ArrayList;
import java.util.List;

public class NewOrderResponseModel {
    public boolean error = false;
    public String message = "";
    public List<NewOrdersModel> orders = new ArrayList<>();
}

