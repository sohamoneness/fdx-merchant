package com.oneness.fdxmerchant.Activity.Profile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.oneness.fdxmerchant.Activity.ItemManagement.AddNewItem;
import com.oneness.fdxmerchant.Models.ProfileModels.SettingsUpdateRequestModel;
import com.oneness.fdxmerchant.Models.ProfileModels.SettingsUpdateResponseModel;
import com.oneness.fdxmerchant.Models.RestaurantDataModels.RestaurantDataModel;
import com.oneness.fdxmerchant.Models.RestaurantDataModels.RestaurantDataResponseModel;
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

public class Settings extends AppCompatActivity {

    EditText etTaxRate, etMinOrder, etOrderPreparation;
    Spinner spTax, spOutOfStock;
    Button btnSave;
    LinearLayout taxLL;
    ApiManager manager = new ApiManager();
    DialogView dialogView;
    Prefs prefs;
    List<String> spList = new ArrayList<>();
    List<String> spListOut = new ArrayList<>();
    ImageView ivBack;

    String hasTax = "";
    String showOut = "";
    public static RestaurantDataModel restaurantDataModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        dialogView = new DialogView();
        prefs = new Prefs(Settings.this);

        btnSave = findViewById(R.id.btnSave);
        etTaxRate = findViewById(R.id.etTaxRate);
        etMinOrder = findViewById(R.id.etMinOrder);
        etOrderPreparation = findViewById(R.id.etOrderPreparation);
        taxLL = findViewById(R.id.taxLL);
        spTax = findViewById(R.id.spTax);
        spOutOfStock = findViewById(R.id.spOutOfStock);
        ivBack = findViewById(R.id.ivBack);

        spList.add("Select");
        spList.add("Yes");
        spList.add("No");

        spListOut.add("Select");
        spListOut.add("Yes");
        spListOut.add("No");

        getRestData(prefs.getData(Constants.REST_ID));

        ArrayAdapter<String> spTaxAdapter = new ArrayAdapter<String>(
                Settings.this, R.layout.my_spinner_row, spList);
        spTaxAdapter.setDropDownViewResource(R.layout.my_spinner_row);
        spTax.setAdapter(spTaxAdapter);
        spTax.setFocusableInTouchMode(false);

        ArrayAdapter<String> spOutAdapter = new ArrayAdapter<String>(
                Settings.this, R.layout.my_spinner_row, spListOut);
        spOutAdapter.setDropDownViewResource(R.layout.my_spinner_row);
        spOutOfStock.setAdapter(spOutAdapter);
        spOutOfStock.setFocusableInTouchMode(false);

        spTax.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                if (spList.get(position).equals("Select")) {
                    taxLL.setVisibility(View.GONE);
                    //Toast.makeText(getActivity(), "Please select an option", Toast.LENGTH_SHORT).show();
                } else {
                    if (spList.get(position).equals("Yes")){
                        hasTax = "1";
                        taxLL.setVisibility(View.VISIBLE);
                    }else {
                        hasTax = "0";
                        taxLL.setVisibility(View.GONE);
                    }
                    //Toast.makeText(AddNewItem.this, catName + " Selected!", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spOutOfStock.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (spListOut.get(position).equals("Select")) {
                } else {
                    if (spListOut.get(position).equals("Yes")) {
                        showOut = "1";
                    }else {
                        showOut = "0";
                    }
                    //Toast.makeText(AddNewItem.this, catName + " Selected!", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // if (isNotBlank()){}
                String tax = "";
                if (hasTax.equals("1")){
                    tax = etTaxRate.getText().toString();
                }else {
                    tax = "0";
                }
                SettingsUpdateRequestModel settingsUpdateRequestModel = new SettingsUpdateRequestModel(
                        prefs.getData(Constants.REST_ID),
                        hasTax,
                        tax,
                        etMinOrder.getText().toString(),
                        etOrderPreparation.getText().toString(),
                        showOut
                );

                saveSettings(settingsUpdateRequestModel);
            }
        });

    }

    private void getRestData(String data) {
        dialogView.showCustomSpinProgress(Settings.this);
        manager.service.getRestaurantData(data).enqueue(new Callback<RestaurantDataResponseModel>() {
            @Override
            public void onResponse(Call<RestaurantDataResponseModel> call, Response<RestaurantDataResponseModel> response) {
                if (response.isSuccessful()){
                    RestaurantDataResponseModel rdrm = response.body();
                    if (!rdrm.error){
                        dialogView.dismissCustomSpinProgress();
                        restaurantDataModel = rdrm.restaurantData.restaurant;

                        if (restaurantDataModel.including_tax.equals("0")){
                            spTax.setSelection(spList.indexOf("No"));
                            taxLL.setVisibility(View.GONE);
                            hasTax = "0";
                        }else {
                            spTax.setSelection(spList.indexOf("Yes"));
                            taxLL.setVisibility(View.VISIBLE);
                            etTaxRate.setText(restaurantDataModel.tax_rate);
                            hasTax = "1";
                        }

                        etMinOrder.setText(restaurantDataModel.minimum_order_amount);
                        etOrderPreparation.setText(restaurantDataModel.order_preparation_time);
                        if (restaurantDataModel.show_out_of_stock_products_in_app.equals("0")){
                            spOutOfStock.setSelection(spListOut.indexOf("No"));
                            showOut = "0";
                        }else {
                            spOutOfStock.setSelection(spListOut.indexOf("Yes"));
                            showOut = "1";
                        }

                    }else {
                        dialogView.dismissCustomSpinProgress();
                    }
                }else {
                    dialogView.dismissCustomSpinProgress();
                }
            }

            @Override
            public void onFailure(Call<RestaurantDataResponseModel> call, Throwable t) {
                dialogView.dismissCustomSpinProgress();
            }
        });
    }

   /* private boolean isNotBlank() {
        if (etMinOrder.getText().toString().equals("")){
            Toast.makeText(this, "Please enter mini", Toast.LENGTH_SHORT).show();
        }
        return false;
    }*/

    private void saveSettings(SettingsUpdateRequestModel settingsUpdateRequestModel) {
        dialogView.showCustomSpinProgress(Settings.this);
        manager.service.updateSettings(settingsUpdateRequestModel).enqueue(new Callback<SettingsUpdateResponseModel>() {
            @Override
            public void onResponse(Call<SettingsUpdateResponseModel> call, Response<SettingsUpdateResponseModel> response) {
                if (response.isSuccessful()){
                    SettingsUpdateResponseModel surm = response.body();
                    if (!surm.error){
                        dialogView.dismissCustomSpinProgress();
                        Toast.makeText(Settings.this, "Submitted successfully!", Toast.LENGTH_SHORT).show();
                        finish();
                    }else {
                        dialogView.dismissCustomSpinProgress();
                        Toast.makeText(Settings.this, "Error! Not Submitted!", Toast.LENGTH_SHORT).show();
                    }
                }else{

                    dialogView.dismissCustomSpinProgress();

                }
            }

            @Override
            public void onFailure(Call<SettingsUpdateResponseModel> call, Throwable t) {
                dialogView.dismissCustomSpinProgress();
            }
        });
    }
}