package com.oneness.fdxmerchant.Models.ReportManagementModels;

import java.util.ArrayList;
import java.util.List;

public class DateWiseTransactionReportResponseModel {
    public boolean error = false;
    public String message = "";
    public String total_order_count = "";
    public String total_order_amount = "";
    public List<DateWiseTransactionReportModel> orders = new ArrayList<>();
}
