package com.oneness.fdxmerchant.Models.ReportManagementModels;

public class DateWiseOrderReportRequestModel {
    public String start_date = "";
    public String end_date = "";
    public String restaurant_id = "";

    public DateWiseOrderReportRequestModel(String start_date, String end_date, String restaurant_id) {
        this.start_date = start_date;
        this.end_date = end_date;
        this.restaurant_id = restaurant_id;
    }
}
