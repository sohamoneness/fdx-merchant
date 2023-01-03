package com.oneness.fdxmerchant.Activity.ReportManagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
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

public class OrderCountReport extends AppCompatActivity {

    BarChart barChart;
    BarData barData;
    BarDataSet barDataSet;

    ArrayList barEntriesArrayList;
    DialogView dialogView;
    ApiManager manager = new ApiManager();
    Prefs prefs;
    List<OrderCountDataModel> orderCountList = new ArrayList<>();
    List<String> monthList = new ArrayList<>();
    List<BarEntry> valueList = new ArrayList<BarEntry>();
    OrderCountAdapter ocAdapter;
    RecyclerView orderCountRV;
    ImageView ivBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_count_report);

        prefs = new Prefs(OrderCountReport.this);
        dialogView = new DialogView();
        orderCountRV = findViewById(R.id.orderCountRV);
        orderCountRV = findViewById(R.id.orderCountRV);
        ivBack = findViewById(R.id.ivBack);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //barChart = findViewById(R.id.barChart);
        //barDataSet = new BarDataSet()
        getBarEntries(prefs.getData(Constants.REST_ID));

    }




    private void getBarEntries(String data) {
        dialogView.showCustomSpinProgress(OrderCountReport.this);
        manager.service.getOrderCount(data).enqueue(new Callback<OrderCountResponseModel>() {
            @Override
            public void onResponse(Call<OrderCountResponseModel> call, Response<OrderCountResponseModel> response) {
                if (response.isSuccessful()){
                    OrderCountResponseModel ocrm = response.body();
                    if (!ocrm.error){
                        dialogView.dismissCustomSpinProgress();
                        orderCountList = ocrm.data;

                        if (orderCountList.size() > 0){
                            ocAdapter = new OrderCountAdapter(OrderCountReport.this, orderCountList);
                            orderCountRV.setLayoutManager(new LinearLayoutManager(OrderCountReport.this, LinearLayoutManager.VERTICAL, false));
                            orderCountRV.setAdapter(ocAdapter);
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