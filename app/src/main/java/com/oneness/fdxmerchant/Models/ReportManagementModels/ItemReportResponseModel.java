package com.oneness.fdxmerchant.Models.ReportManagementModels;

import java.util.ArrayList;
import java.util.List;

public class ItemReportResponseModel {
    public boolean error = false;
    public String message = "";
    public List<ItemReportModel> items = new ArrayList<>();
}
