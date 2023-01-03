package com.oneness.fdxmerchant.Models.RestaurantDashboardModels;

import java.util.ArrayList;
import java.util.List;

public class DashboardDataModel {
    public List<TodayOrderModel> todays_orders = new ArrayList<>();
    public String new_order_count = "";
    public String ongoing_order_count = "";
    public String delivered_order_count = "";
    public String cancelled_order_count = "";
    public String today_order_count = "";
    public String todays_order_amount = "";
    public String todays_restaurant_commission = "";
}
