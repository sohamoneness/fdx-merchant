package com.oneness.fdxmerchant.Models.RestSearchModels;

import java.util.ArrayList;
import java.util.List;

public class SearchResponseModel {
    public boolean error = false;
    public String message = "";
    public SearchUserDataModel user;
    public List<UserAddressModel> addresses = new ArrayList<>();
}
