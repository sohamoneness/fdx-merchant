package com.oneness.fdxmerchant.Activity.CouponManagement;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.oneness.fdxmerchant.Activity.EntryPoint.Dashboard;
import com.oneness.fdxmerchant.Activity.ItemManagement.AddNewItem;
import com.oneness.fdxmerchant.Models.CouponModels.CouponListModel;
import com.oneness.fdxmerchant.Models.CouponModels.CreateCouponRequestModel;
import com.oneness.fdxmerchant.Models.CouponModels.CreateCouponResponseModel;
import com.oneness.fdxmerchant.Models.CouponModels.UpdateCouponRequestModel;
import com.oneness.fdxmerchant.Models.CouponModels.UpdateCouponResponseModel;
import com.oneness.fdxmerchant.Network.ApiManager;
import com.oneness.fdxmerchant.Network.Constants;
import com.oneness.fdxmerchant.R;
import com.oneness.fdxmerchant.Utils.DialogView;
import com.oneness.fdxmerchant.Utils.Prefs;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddCouponActivity extends AppCompatActivity {
    EditText etCouponName, etCouponDesc, etCouponCode;
    Spinner spType;
    TextView tvFrom, tvTo, tv_head_text;
    EditText etMaxOffer, etMaxQty, etMaxOneUserUse, etRate;
    Button btnSubmit;
    ImageView ivBack;
    Prefs prefs;
    DialogView dialogView;
    ApiManager manager = new ApiManager();

    List<String> typeList = new ArrayList<>();
    String isActive = "";
    String dateType = "";
    Calendar myCalendar = Calendar.getInstance();
    public static CouponListModel couponListModel = new CouponListModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_coupon);

        prefs = new Prefs(AddCouponActivity.this);
        dialogView = new DialogView();

        typeList.add("Select Type");
        typeList.add("Percentage");
        typeList.add("Flat");
        //typeList.add("Inactive");

        etCouponName = findViewById(R.id.etCouponName);
        etCouponDesc = findViewById(R.id.etCouponDesc);
        etCouponCode = findViewById(R.id.etCouponCode);

        spType = findViewById(R.id.spType);

        etMaxOffer = findViewById(R.id.etMaxOffer);
        etMaxQty = findViewById(R.id.etMaxQty);
        etMaxOneUserUse = findViewById(R.id.etMaxOneUserUse);
        etRate = findViewById(R.id.etRate);
        ivBack = findViewById(R.id.ivBack);

        tvFrom = findViewById(R.id.tvFrom);
        tvTo = findViewById(R.id.tvTo);
        tv_head_text = findViewById(R.id.tv_head_text);

        btnSubmit = findViewById(R.id.btnSubmit);

        ArrayAdapter<String> spTypeAdapter = new ArrayAdapter<String>(
                AddCouponActivity.this, R.layout.my_spinner_row, typeList);
        spTypeAdapter.setDropDownViewResource(R.layout.my_spinner_row);
        spType.setAdapter(spTypeAdapter);
        spType.setFocusableInTouchMode(false);


        if (Constants.isFromCouponEdit){
            etCouponName.setText(couponListModel.title);
            etCouponDesc.setText(couponListModel.description);
            etCouponCode.setText(couponListModel.code);
            etRate.setText(couponListModel.rate);
            etMaxOffer.setText(couponListModel.maximum_offer_rate);
            etMaxQty.setText(couponListModel.maximum_time_of_use);
            etMaxOneUserUse.setText(couponListModel.maximum_time_user_can_use);
            tvFrom.setText(couponListModel.start_date);
            tvTo.setText(couponListModel.end_date);
            tv_head_text.setText("Update Coupon");

            btnSubmit.setText("Update");
        }

        spType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                if (typeList.get(position).equals("Select Type")) {
                    isActive = "0";
                }else if (typeList.get(position).equals("Percentage")){
                    isActive = "1";
                }else if (typeList.get(position).equals("Flat")){
                    isActive = "2";
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        tvFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dateType = "From";
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddCouponActivity.this, date1, myCalendar
                        .get(android.icu.util.Calendar.YEAR), myCalendar.get(android.icu.util.Calendar.MONTH),
                        myCalendar.get(android.icu.util.Calendar.DAY_OF_MONTH));
                datePickerDialog.show();

            }
        });

        tvTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dateType = "TO";
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddCouponActivity.this, date2, myCalendar
                        .get(android.icu.util.Calendar.YEAR), myCalendar.get(android.icu.util.Calendar.MONTH),
                        myCalendar.get(android.icu.util.Calendar.DAY_OF_MONTH));
                datePickerDialog.show();

            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isBlankValid()){
                    if (Constants.isFromCouponEdit){

                        UpdateCouponRequestModel updateCouponRequestModel = new UpdateCouponRequestModel(
                                couponListModel.id,
                                prefs.getData(Constants.REST_ID),
                                etCouponName.getText().toString(),
                                etCouponDesc.getText().toString(),
                                etCouponCode.getText().toString(),
                                isActive,
                                etRate.getText().toString(),
                                etMaxOffer.getText().toString(),
                                tvFrom.getText().toString(),
                                tvTo.getText().toString(),
                                etMaxQty.getText().toString(),
                                etMaxOneUserUse.getText().toString()
                        );

                        submitDataToUpdateCoupon(updateCouponRequestModel);


                    }else{
                        CreateCouponRequestModel createCouponRequestModel = new CreateCouponRequestModel(
                                prefs.getData(Constants.REST_ID),
                                etCouponName.getText().toString(),
                                etCouponDesc.getText().toString(),
                                etCouponCode.getText().toString(),
                                isActive,
                                etRate.getText().toString(),
                                etMaxOffer.getText().toString(),
                                tvFrom.getText().toString(),
                                tvTo.getText().toString(),
                                etMaxQty.getText().toString(),
                                etMaxOneUserUse.getText().toString()
                        );

                        submitDataToAddCoupon(createCouponRequestModel);


                    }


                }
            }
        });



    }

    private void submitDataToUpdateCoupon(UpdateCouponRequestModel updateCouponRequestModel) {
        dialogView.showCustomSpinProgress(AddCouponActivity.this);
        manager.service.updateOffers(updateCouponRequestModel).enqueue(new Callback<UpdateCouponResponseModel>() {
            @Override
            public void onResponse(Call<UpdateCouponResponseModel> call, Response<UpdateCouponResponseModel> response) {
                if (response.isSuccessful()){
                    dialogView.dismissCustomSpinProgress();

                    UpdateCouponResponseModel ucrm = response.body();
                    if (!ucrm.error){
                        Toast.makeText(AddCouponActivity.this, "Coupon updated successfully!", Toast.LENGTH_SHORT).show();
                        //startActivity(new Intent(AddCouponActivity.this, Dashboard.class));
                        //finishAffinity();
                        Constants.isFromCouponEdit = false;
                        Constants.isNewCouponAdded = true;
                        finish();
                    }

                }else{
                    dialogView.dismissCustomSpinProgress();
                }
            }

            @Override
            public void onFailure(Call<UpdateCouponResponseModel> call, Throwable t) {
                dialogView.dismissCustomSpinProgress();
            }
        });
    }

    private void submitDataToAddCoupon(CreateCouponRequestModel createCouponRequestModel) {
        dialogView.showCustomSpinProgress(AddCouponActivity.this);
        manager.service.addNewOffers(createCouponRequestModel).enqueue(new Callback<CreateCouponResponseModel>() {
            @Override
            public void onResponse(Call<CreateCouponResponseModel> call, Response<CreateCouponResponseModel> response) {
                if (response.isSuccessful()){
                    dialogView.dismissCustomSpinProgress();
                    CreateCouponResponseModel ccrm = response.body();
                    if (!ccrm.error){
                        Toast.makeText(AddCouponActivity.this, "New Coupon added!", Toast.LENGTH_SHORT).show();
                        //startActivity(new Intent(AddCouponActivity.this, Dashboard.class));
                        //finishAffinity();
                        Constants.isNewCouponAdded = true;
                        finish();
                    }else{

                    }
                }else{
                    dialogView.dismissCustomSpinProgress();
                }
            }

            @Override
            public void onFailure(Call<CreateCouponResponseModel> call, Throwable t) {
                dialogView.dismissCustomSpinProgress();

            }
        });
    }

    final DatePickerDialog.OnDateSetListener date1 = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(android.icu.util.Calendar.YEAR, year);
            myCalendar.set(android.icu.util.Calendar.MONTH, monthOfYear);
            myCalendar.set(android.icu.util.Calendar.DAY_OF_MONTH, dayOfMonth);
            //if(dateType.equalsIgnoreCase("From"))
            //{
                updateLabel(tvFrom);
           // }
           // else if(dateType.equalsIgnoreCase("To"))
          //  {
          //      updateLabel(tvTo);
          //  }


        }

    };


    final DatePickerDialog.OnDateSetListener date2 = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(android.icu.util.Calendar.YEAR, year);
            myCalendar.set(android.icu.util.Calendar.MONTH, monthOfYear);
            myCalendar.set(android.icu.util.Calendar.DAY_OF_MONTH, dayOfMonth);
           // if(dateType.equalsIgnoreCase("From"))
           // {
           //     updateLabel(tvFrom);
           // }
          //  else if(dateType.equalsIgnoreCase("To"))
          //  {
                updateLabel(tvTo);
          //  }


        }

    };

    private void updateLabel(TextView editText) {
        //   String myFormat = "yyyy-MM-dd"; //In which you need put here
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        editText.setText(sdf.format(myCalendar.getTime()));

    }

    private boolean isBlankValid() {
        if (etCouponName.getText().toString().equals("")){
            dialogView.errorButtonDialog(AddCouponActivity.this, getResources().getString(R.string.app_name), "Coupon Title can not be blank!");
            return false;
        }else if(etCouponDesc.getText().toString().equals("")){
            dialogView.errorButtonDialog(AddCouponActivity.this, getResources().getString(R.string.app_name), "Please enter Coupon Description!");
            return false;
        }else if (etCouponCode.getText().toString().equals("")){
            dialogView.errorButtonDialog(AddCouponActivity.this, getResources().getString(R.string.app_name), "Coupon Code can not be blank!");
            return false;
        }else if (tvFrom.getText().toString().equals("")){
            dialogView.errorButtonDialog(AddCouponActivity.this, getResources().getString(R.string.app_name), "Start Date can not be blank!");
            return false;
        }else if (tvTo.getText().toString().equals("")){
            dialogView.errorButtonDialog(AddCouponActivity.this, getResources().getString(R.string.app_name), "End Date can not be blank!");
            return false;
        }else if (etRate.getText().toString().equals("")){
            dialogView.errorButtonDialog(AddCouponActivity.this, getResources().getString(R.string.app_name), "Discount Rate can not be blank!");
            return false;
        }else {
            return true;
        }

    }
}