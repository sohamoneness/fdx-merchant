package com.oneness.fdxmerchant.Activity.Profile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import com.oneness.fdxmerchant.Models.ProfileModels.DeliveryConfigModel;
import com.oneness.fdxmerchant.Models.ProfileModels.DeliveryConfigResponseModel;
import com.oneness.fdxmerchant.Models.ProfileModels.DeliveryConfigUpdateRequestModel;
import com.oneness.fdxmerchant.Models.ProfileModels.DeliveryConfigUpdateResponseModel;
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

public class DeliveryConfig extends AppCompatActivity {

    CheckBox chkTake, chkHome, chkPick;
    ImageView ivBack;
    Button btnSave;
    Prefs prefs;
    ApiManager manager = new ApiManager();
    DialogView dialogView;
    String config = "", type1 = "", type2 = "", type3 = "";
    List<DeliveryConfigModel> delConfigList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_config);

        prefs = new Prefs(DeliveryConfig.this);
        dialogView = new DialogView();

        chkTake = findViewById(R.id.chkTake);
        chkHome = findViewById(R.id.chkHome);
        chkPick = findViewById(R.id.chkPick);
        ivBack = findViewById(R.id.ivBack);
        btnSave = findViewById(R.id.btnSave);

        getDelConfig(prefs.getData(Constants.REST_ID));


        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (chkTake.isChecked() && chkHome.isChecked() && chkPick.isChecked()){
                    config = "1*2*3";
                }else if (chkTake.isChecked() && chkHome.isChecked()){
                    config = "1*2";
                }else if (chkTake.isChecked() && chkPick.isChecked()){
                    config = "1*3";
                }else if (chkHome.isChecked() && chkPick.isChecked()){
                    config = "2*3";
                }else if (chkTake.isChecked()){
                    config = "1";
                }else if (chkHome.isChecked()){
                    config = "2";
                }else if (chkPick.isChecked()){
                    config = "3";
                }

                DeliveryConfigUpdateRequestModel deliveryConfigUpdateRequestModel = new DeliveryConfigUpdateRequestModel(
                        prefs.getData(Constants.REST_ID),
                        config
                );

                updateConfig(deliveryConfigUpdateRequestModel);

            }
        });



    }

    private void updateConfig(DeliveryConfigUpdateRequestModel deliveryConfigUpdateRequestModel) {
        dialogView.showCustomSpinProgress(DeliveryConfig.this);
        manager.service.updateDeliveryConfig(deliveryConfigUpdateRequestModel).enqueue(new Callback<DeliveryConfigUpdateResponseModel>() {
            @Override
            public void onResponse(Call<DeliveryConfigUpdateResponseModel> call, Response<DeliveryConfigUpdateResponseModel> response) {
                if (response.isSuccessful()){
                    dialogView.dismissCustomSpinProgress();
                    DeliveryConfigUpdateResponseModel dcurm = response.body();
                    if (!dcurm.error){
                        Toast.makeText(DeliveryConfig.this, "Successfully Updated!", Toast.LENGTH_SHORT).show();
                        finish();
                    }else {

                    }
                }else {
                    dialogView.dismissCustomSpinProgress();
                }
            }

            @Override
            public void onFailure(Call<DeliveryConfigUpdateResponseModel> call, Throwable t) {
                dialogView.dismissCustomSpinProgress();
            }
        });
    }

    private void getDelConfig(String id) {
        dialogView.showCustomSpinProgress(DeliveryConfig.this);
        manager.service.getDeliveryConfig(id).enqueue(new Callback<DeliveryConfigResponseModel>() {
            @Override
            public void onResponse(Call<DeliveryConfigResponseModel> call, Response<DeliveryConfigResponseModel> response) {
                if (response.isSuccessful()){
                    dialogView.dismissCustomSpinProgress();
                    DeliveryConfigResponseModel dcrm = response.body();
                    if (!dcrm.error){
                        delConfigList = dcrm.data;
                        if (delConfigList.size()>0){
                            for (int i = 0; i < delConfigList.size(); i++){
                                if (delConfigList.get(i).delivery_type.equals("1")){
                                    chkTake.setChecked(true);
                                }else if (delConfigList.get(i).delivery_type.equals("2")){
                                    chkHome.setChecked(true);
                                }else if (delConfigList.get(i).delivery_type.equals("3")){
                                    chkPick.setChecked(true);
                                }
                            }
                        }

                    }else {

                    }
                }else{
                    dialogView.dismissCustomSpinProgress();
                }
            }

            @Override
            public void onFailure(Call<DeliveryConfigResponseModel> call, Throwable t) {
                dialogView.dismissCustomSpinProgress();
            }
        });
    }
}