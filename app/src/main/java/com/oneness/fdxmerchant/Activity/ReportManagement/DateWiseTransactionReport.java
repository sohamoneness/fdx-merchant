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

import com.oneness.fdxmerchant.Adapters.OrderReportAdapter;
import com.oneness.fdxmerchant.Adapters.TransactionReportAdapter;
import com.oneness.fdxmerchant.Models.ReportManagementModels.DateWiseOrderReportModel;
import com.oneness.fdxmerchant.Models.ReportManagementModels.DateWiseOrderReportRequestModel;
import com.oneness.fdxmerchant.Models.ReportManagementModels.DateWiseOrderReportResponseModel;
import com.oneness.fdxmerchant.Models.ReportManagementModels.DateWiseTransactionReportModel;
import com.oneness.fdxmerchant.Models.ReportManagementModels.DateWiseTransactionReportRequestModel;
import com.oneness.fdxmerchant.Models.ReportManagementModels.DateWiseTransactionReportResponseModel;
import com.oneness.fdxmerchant.Network.ApiManager;
import com.oneness.fdxmerchant.Network.Constants;
import com.oneness.fdxmerchant.R;
import com.oneness.fdxmerchant.Utils.DialogView;
import com.oneness.fdxmerchant.Utils.Prefs;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DateWiseTransactionReport extends AppCompatActivity {

    TextView tvFrom, tvTo, tvOrderCount, tvTotalAmount;
    RecyclerView transReportRv;
    Button btnSearch;
    Prefs prefs;
    DialogView dialogView;
    ApiManager manager = new ApiManager();
    List<DateWiseTransactionReportModel> transReportList = new ArrayList<>();
    TransactionReportAdapter transactionReportAdapter;
    Calendar myCalendar = Calendar.getInstance();
    Calendar myCalendar1 = Calendar.getInstance();
    ImageView iv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_wise_transaction_report);

        prefs = new Prefs(DateWiseTransactionReport.this);
        dialogView = new DialogView();

        iv_back = findViewById(R.id.iv_back);
        tvFrom = findViewById(R.id.tvFrom);
        tvTo = findViewById(R.id.tvTo);
        tvOrderCount = findViewById(R.id.tvOrderCount);
        tvTotalAmount = findViewById(R.id.tvOrderAmount);

        transReportRv = findViewById(R.id.transReportRv);

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
                DatePickerDialog datePickerDialog = new DatePickerDialog(DateWiseTransactionReport.this, date1, myCalendar
                        .get(android.icu.util.Calendar.YEAR), myCalendar.get(android.icu.util.Calendar.MONTH),
                        myCalendar.get(android.icu.util.Calendar.DAY_OF_MONTH));
                datePickerDialog.show();


            }
        });

        tvTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(DateWiseTransactionReport.this, date2, myCalendar1
                        .get(android.icu.util.Calendar.YEAR), myCalendar1.get(android.icu.util.Calendar.MONTH),
                        myCalendar1.get(android.icu.util.Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DateWiseTransactionReportRequestModel dateWiseTransactionReportRequestModel = new DateWiseTransactionReportRequestModel(
                        tvFrom.getText().toString(),
                        tvTo.getText().toString(),
                        prefs.getData(Constants.REST_ID)
                );
                getTransactionReport(dateWiseTransactionReportRequestModel);

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

        DateWiseTransactionReportRequestModel dateWiseTransactionReportRequestModel = new DateWiseTransactionReportRequestModel(
                tvFrom.getText().toString(),
                tvTo.getText().toString(),
                prefs.getData(Constants.REST_ID)
        );
        getTransactionReport(dateWiseTransactionReportRequestModel);
    }

    private void getTransactionReport(DateWiseTransactionReportRequestModel dateWiseTransactionReportRequestModel) {
        dialogView.showCustomSpinProgress(DateWiseTransactionReport.this);
        manager.service.getDateWiseTransactionReport(dateWiseTransactionReportRequestModel).enqueue(new Callback<DateWiseTransactionReportResponseModel>() {
            @Override
            public void onResponse(Call<DateWiseTransactionReportResponseModel> call, Response<DateWiseTransactionReportResponseModel> response) {
                if (response.isSuccessful()){
                    dialogView.dismissCustomSpinProgress();
                    DateWiseTransactionReportResponseModel dwtrrm = response.body();
                    if (!dwtrrm.error){
                        transReportList = dwtrrm.orders;
                        tvOrderCount.setText(dwtrrm.total_order_count);
                        if (dwtrrm.total_order_amount != null){
                            DecimalFormat formatter1 = new DecimalFormat("#,##,###.00");
                            String formatted = formatter1.format(Double.parseDouble(dwtrrm.total_order_amount));

                            tvTotalAmount.setText("\u20B9" + formatted);

                        }else {
                            tvTotalAmount.setText("\u20B9" + "0.00");
                        }

                        if (transReportList.size()>0){
                            transactionReportAdapter = new TransactionReportAdapter(DateWiseTransactionReport.this, transReportList);
                            transReportRv.setLayoutManager(new LinearLayoutManager(DateWiseTransactionReport.this, LinearLayoutManager.VERTICAL, false));
                            transReportRv.setAdapter(transactionReportAdapter);

                        }else {

                        }

                    }else {
                        dialogView.dismissCustomSpinProgress();

                    }

                }else{
                    dialogView.dismissCustomSpinProgress();
                }
            }

            @Override
            public void onFailure(Call<DateWiseTransactionReportResponseModel> call, Throwable t) {
                dialogView.dismissCustomSpinProgress();

            }
        });
    }

}