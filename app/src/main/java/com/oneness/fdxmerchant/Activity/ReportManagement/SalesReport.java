package com.oneness.fdxmerchant.Activity.ReportManagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.oneness.fdxmerchant.Adapters.OrderCountAdapter;
import com.oneness.fdxmerchant.Models.ReportManagementModels.OrderCountDataModel;
import com.oneness.fdxmerchant.Models.ReportManagementModels.OrderCountResponseModel;
import com.oneness.fdxmerchant.Network.ApiManager;
import com.oneness.fdxmerchant.Network.Constants;
import com.oneness.fdxmerchant.R;
import com.oneness.fdxmerchant.Utils.DialogView;
import com.oneness.fdxmerchant.Utils.Prefs;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SalesReport extends AppCompatActivity {

    ImageView ivBack;
    RecyclerView salesRv;
    DialogView dialogView;
    ApiManager manager = new ApiManager();
    Prefs prefs;
    List<OrderCountDataModel> orderCountList = new ArrayList<>();
    OrderCountAdapter ocAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_report);
        prefs = new Prefs(SalesReport.this);
        dialogView = new DialogView();

        ivBack = findViewById(R.id.ivBack);
        salesRv = findViewById(R.id.salesRv);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getBarEntries(prefs.getData(Constants.REST_ID));


    }

    private void getBarEntries(String data) {
        dialogView.showCustomSpinProgress(SalesReport.this);
        manager.service.getSaleReport(data).enqueue(new Callback<OrderCountResponseModel>() {
            @Override
            public void onResponse(Call<OrderCountResponseModel> call, Response<OrderCountResponseModel> response) {
                if (response.isSuccessful()){
                    OrderCountResponseModel ocrm = response.body();
                    if (!ocrm.error){
                        dialogView.dismissCustomSpinProgress();
                        orderCountList = ocrm.data;

                        if (orderCountList.size() > 0){
                            ocAdapter = new OrderCountAdapter(SalesReport.this, orderCountList,"sale");
                            salesRv.setLayoutManager(new LinearLayoutManager(SalesReport.this, LinearLayoutManager.VERTICAL, false));
                            salesRv.setAdapter(ocAdapter);
                        }

                    }else {
                        dialogView.dismissCustomSpinProgress();
                    }
                }else{
                    dialogView.dismissCustomSpinProgress();
                }
            }

            @Override
            public void onFailure(Call<OrderCountResponseModel> call, Throwable t) {
                dialogView.dismissCustomSpinProgress();
            }
        });

    }
}