package com.oneness.fdxmerchant.Activity.ReportManagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.oneness.fdxmerchant.Adapters.ItemReportAdapter;
import com.oneness.fdxmerchant.Adapters.OrderReportAdapter;
import com.oneness.fdxmerchant.Models.ReportManagementModels.DateWiseOrderReportModel;
import com.oneness.fdxmerchant.Models.ReportManagementModels.DateWiseOrderReportRequestModel;
import com.oneness.fdxmerchant.Models.ReportManagementModels.ItemReportModel;
import com.oneness.fdxmerchant.Models.ReportManagementModels.ItemReportRequestModel;
import com.oneness.fdxmerchant.Models.ReportManagementModels.ItemReportResponseModel;
import com.oneness.fdxmerchant.Network.ApiManager;
import com.oneness.fdxmerchant.Network.Constants;
import com.oneness.fdxmerchant.R;
import com.oneness.fdxmerchant.Utils.DialogView;
import com.oneness.fdxmerchant.Utils.Prefs;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemWiseReport extends AppCompatActivity {

    TextView tvFrom, tvTo, tvOrderCount, tvTotalAmount;
    RecyclerView itemReportRv;
    Button btnSearch;
    Prefs prefs;
    DialogView dialogView;
    ApiManager manager = new ApiManager();
    List<ItemReportModel> itemReportList = new ArrayList<>();
    ItemReportAdapter itemReportAdapter;
    Calendar myCalendar = Calendar.getInstance();
    Calendar myCalendar1 = Calendar.getInstance();
    ImageView iv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_wise_report);

        prefs = new Prefs(ItemWiseReport.this);
        dialogView = new DialogView();

        iv_back = findViewById(R.id.iv_back);
        tvFrom = findViewById(R.id.tvFrom);
        tvTo = findViewById(R.id.tvTo);
        tvOrderCount = findViewById(R.id.tvOrderCount);
        tvTotalAmount = findViewById(R.id.tvOrderAmount);

        itemReportRv = findViewById(R.id.itemReportRv);

        btnSearch = findViewById(R.id.btnSearch);

        getCurrentDate();

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        tvFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(ItemWiseReport.this, date1, myCalendar
                        .get(android.icu.util.Calendar.YEAR), myCalendar.get(android.icu.util.Calendar.MONTH),
                        myCalendar.get(android.icu.util.Calendar.DAY_OF_MONTH));
                datePickerDialog.show();


            }
        });

        tvTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(ItemWiseReport.this, date2, myCalendar1
                        .get(android.icu.util.Calendar.YEAR), myCalendar1.get(android.icu.util.Calendar.MONTH),
                        myCalendar1.get(android.icu.util.Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ItemReportRequestModel itemReportRequestModel = new ItemReportRequestModel(
                        tvFrom.getText().toString(),
                        tvTo.getText().toString(),
                        prefs.getData(Constants.REST_ID)
                );
                getItemReport(itemReportRequestModel);

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
            updateLabel(tvFrom);
        }

    };

    final DatePickerDialog.OnDateSetListener date2 = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar1.set(android.icu.util.Calendar.YEAR, year);
            myCalendar1.set(android.icu.util.Calendar.MONTH, monthOfYear);
            myCalendar1.set(android.icu.util.Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel(tvTo);
        }

    };

    private void updateLabel(TextView editText) {
        //   String myFormat = "yyyy-MM-dd"; //In which you need put here
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        editText.setText(sdf.format(myCalendar.getTime()));

    }

    private void getCurrentDate() {
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formattedDate = df.format(c);
        tvFrom.setText(formattedDate);
        tvTo.setText(formattedDate);

        ItemReportRequestModel itemReportRequestModel = new ItemReportRequestModel(
                tvFrom.getText().toString(),
                tvTo.getText().toString(),
                prefs.getData(Constants.REST_ID)
        );
        getItemReport(itemReportRequestModel);
    }

    private void getItemReport(ItemReportRequestModel itemReportRequestModel) {
        dialogView.showCustomSpinProgress(ItemWiseReport.this);
        manager.service.getItemWiseReport(itemReportRequestModel).enqueue(new Callback<ItemReportResponseModel>() {
            @Override
            public void onResponse(Call<ItemReportResponseModel> call, Response<ItemReportResponseModel> response) {
                if (response.isSuccessful()){
                    dialogView.dismissCustomSpinProgress();
                    ItemReportResponseModel itemReportResponseModel = response.body();
                    if (!itemReportResponseModel.error){

                        itemReportList = itemReportResponseModel.items;

                        if (itemReportList.size()>0){
                            itemReportAdapter = new ItemReportAdapter(ItemWiseReport.this, itemReportList);
                            itemReportRv.setLayoutManager(new LinearLayoutManager(ItemWiseReport.this, LinearLayoutManager.VERTICAL, false));
                            itemReportRv.setAdapter(itemReportAdapter);
                        }

                    }else{

                    }

                }else {
                    dialogView.dismissCustomSpinProgress();
                }
            }

            @Override
            public void onFailure(Call<ItemReportResponseModel> call, Throwable t) {
                dialogView.dismissCustomSpinProgress();
            }
        });
    }

}