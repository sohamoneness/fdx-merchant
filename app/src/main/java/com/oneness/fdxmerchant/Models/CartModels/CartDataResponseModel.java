package com.oneness.fdxmerchant.Models.CartModels;

import java.util.ArrayList;
import java.util.List;

public class CartDataResponseModel {
    public boolean error = false;
    public String message = "";
    public List<CartDataModel> carts = new ArrayList<>();
    public String discounted_amount = "";
    public String delivery_charge = "";
    public String packing_price = "";
    public String tax_amount = "";
    public String total_amount = "";

}
