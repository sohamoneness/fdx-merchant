package com.oneness.fdxmerchant.Activity.ReportManagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.oneness.fdxmerchant.Adapters.PayoutsAdapter;
import com.oneness.fdxmerchant.Models.ReportManagementModels.PayoutDataModel;
import com.oneness.fdxmerchant.Models.ReportManagementModels.PayoutResponseModel;
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

public class RestaurantPayouts extends AppCompatActivity {

    ImageView ivBack;
    RecyclerView payoutRv;
    Prefs prefs;
    ApiManager manager = new ApiManager();
    DialogView dialogView;
    List<PayoutDataModel> payoutList = new ArrayList<>();
    PayoutsAdapter pAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_payouts);

        prefs = new Prefs(RestaurantPayouts.this);
        dialogView = new DialogView();

        ivBack = findViewById(R.id.ivBack);
        payoutRv = findViewById(R.id.payoutRv);

        getPayoutList(prefs.getData(Constants.REST_ID));

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }

    private void getPayoutList(String data) {
        dialogView.showCustomSpinProgress(RestaurantPayouts.this);
        manager.service.getPayouts(data).enqueue(new Callback<PayoutResponseModel>() {
            @Override
            public void onResponse(Call<PayoutResponseModel> call, Response<PayoutResponseModel> response) {
                if (response.isSuccessful()){
                    PayoutResponseModel prm = response.body();
                    if (!prm.error){
                        dialogView.dismissCustomSpinProgress();

                        payoutList = prm.data;

                        if (payoutList.size()>0){
                            pAdapter = new PayoutsAdapter(RestaurantPayouts.this, payoutList);
                            payoutRv.setLayoutManager(new LinearLayoutManager(RestaurantPayouts.this, LinearLayoutManager.VERTICAL, false));
                            payoutRv.setAdapter(pAdapter);
                        }

                    }else {

                    }
                }else {

                }
            }

            @Override
            public void onFailure(Call<PayoutResponseModel> call, Throwable t) {

            }
        });
    }
}