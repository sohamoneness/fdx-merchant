package com.oneness.fdxmerchant.Models.CouponModels;

import java.util.ArrayList;
import java.util.List;

public class CouponResponseModel {
    public boolean error = false;
    public String message = "";
    public List<CouponListModel> offers = new ArrayList<>();

}
