package com.oneness.fdxmerchant.Models.ReportManagementModels;

import java.util.ArrayList;
import java.util.List;

public class PayoutResponseModel {
    public boolean error = false;
    public String message = "";
    public List<PayoutDataModel> data = new ArrayList<>();
}
