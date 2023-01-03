package com.oneness.fdxmerchant.Models.LoginModels;

import com.oneness.fdxmerchant.Models.RestaurantDataModels.RestaurantDataModel;

public class LoginResponseModel {
    public boolean error = false;
    public String message = "";
    public RestaurantDataModel restaurantData;
}
