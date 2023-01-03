package com.oneness.fdxmerchant.Models.ProfileModels;

import java.util.ArrayList;
import java.util.List;

public class TimingResponseModel {
    public boolean error = false;
    public String message = "";
    public List<TimingsModel> data = new ArrayList<>();
}
