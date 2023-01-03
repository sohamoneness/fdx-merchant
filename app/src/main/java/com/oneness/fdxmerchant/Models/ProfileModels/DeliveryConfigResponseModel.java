package com.oneness.fdxmerchant.Models.ProfileModels;

import java.util.ArrayList;
import java.util.List;

public class DeliveryConfigResponseModel {
    public boolean error = false;
    public String message = "";
    public List<DeliveryConfigModel> data = new ArrayList<>();
}
