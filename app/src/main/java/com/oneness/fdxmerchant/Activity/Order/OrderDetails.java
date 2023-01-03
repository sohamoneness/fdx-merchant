package com.oneness.fdxmerchant.Activity.Order;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.oneness.fdxmerchant.Activity.EntryPoint.Dashboard;
import com.oneness.fdxmerchant.Adapters.OrderItemAdapter;
import com.oneness.fdxmerchant.Models.OrderModels.AcceptOrderRequestModel;
import com.oneness.fdxmerchant.Models.OrderModels.AcceptOrderResponseModel;
import com.oneness.fdxmerchant.Models.OrderModels.OrderDetailsModel;
import com.oneness.fdxmerchant.Models.OrderModels.OrderDetailsResponseModel;
import com.oneness.fdxmerchant.Models.OrderModels.OrderItemsModel;
import com.oneness.fdxmerchant.Models.OrderModels.RejectOrderRequestModel;
import com.oneness.fdxmerchant.Models.OrderModels.RejectOrderResponseModel;
import com.oneness.fdxmerchant.Network.ApiManager;
import com.oneness.fdxmerchant.Network.Constants;
import com.oneness.fdxmerchant.R;
import com.oneness.fdxmerchant.Utils.DialogView;
import com.oneness.fdxmerchant.Utils.Prefs;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetails extends AppCompatActivity {

    RecyclerView pastOrderItemRv;
    List<OrderItemsModel> orderItemList = new ArrayList<>();
    OrderItemAdapter orderItemAdapter;
    public static String ordId = "";

    TextView tvTotPrice, tvOrderNumber, tvPayment, tvDate, tvPhn, tvDelTo, tvDelBy;
    TextView tvDelFee, tvPackingChrg, tvTax, tvPayType;
    TextView tvRestName, tvRestAdr, tvTransId;

    ImageView iv_back;

    Prefs prefs;
    DialogView dialogView;
    ApiManager manager = new ApiManager();

    LinearLayout acceptanceLL;
    Button btnCancel, btnAccept;
    String msg = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        prefs = new Prefs(OrderDetails.this);
        dialogView = new DialogView();

        pastOrderItemRv = findViewById(R.id.pastOrderItemRv);
        tvTotPrice = findViewById(R.id.tvTotPrice);
        tvOrderNumber = findViewById(R.id.tvOrderNumber);
        tvPayment = findViewById(R.id.tvPayment);
        tvDate = findViewById(R.id.tvDate);
        tvPhn = findViewById(R.id.tvPhn);
        tvDelTo = findViewById(R.id.tvDelTo);
        tvDelBy = findViewById(R.id.tvDelBy);
        tvDelFee = findViewById(R.id.tvDelFee);
        tvPackingChrg = findViewById(R.id.tvPackingChrg);
        tvTax = findViewById(R.id.tvTax);
        tvPayType = findViewById(R.id.tvPayType);
        tvRestName = findViewById(R.id.tvRestName);
        tvRestAdr = findViewById(R.id.tvRestAdr);
        iv_back = findViewById(R.id.iv_back);
        tvTransId = findViewById(R.id.tvTransId);
        acceptanceLL = findViewById(R.id.acceptanceLL);
        btnCancel = findViewById(R.id.btnCancel);
        btnAccept = findViewById(R.id.btnAccept);

        if (Constants.isNewOrder == 1){
            acceptanceLL.setVisibility(View.VISIBLE);
        }else{
            acceptanceLL.setVisibility(View.GONE);
        }

        // makeOrderItemList();
        getOrderDetails(ordId);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                msg = "Please enter approx preparation time (in minutes)!";
                showPopup(msg, "accept");

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                msg = "Please mention cancellation reason below!";
                showPopup(msg, "reject");
            }
        });



    }

    private void acceptOrderRequest(AcceptOrderRequestModel acceptOrderRequestModel) {

        dialogView.showCustomSpinProgress(OrderDetails.this);

        manager.service.acceptNewOrder(acceptOrderRequestModel).enqueue(new Callback<AcceptOrderResponseModel>() {
            @Override
            public void onResponse(Call<AcceptOrderResponseModel> call, Response<AcceptOrderResponseModel> response) {
                if (response.isSuccessful()){
                    dialogView.dismissCustomSpinProgress();
                    AcceptOrderResponseModel acceptOrderResponseModel = response.body();

                    if (!acceptOrderResponseModel.error){
                        Toast.makeText(OrderDetails.this, "Order Accepted!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(OrderDetails.this, Dashboard.class));
                        finish();
                    }else {

                    }

                }else{
                    dialogView.dismissCustomSpinProgress();
                }
            }

            @Override
            public void onFailure(Call<AcceptOrderResponseModel> call, Throwable t) {

                dialogView.dismissCustomSpinProgress();

            }
        });

    }

    private void showPopup(String msg, String from) {
        LayoutInflater layoutInflater = LayoutInflater.from(OrderDetails.this);
        View promptView = layoutInflater.inflate(R.layout.cancel_reason_popup_lay, null);

        final AlertDialog alertD = new AlertDialog.Builder(OrderDetails.this).create();
        TextView tvHeader=(TextView)promptView.findViewById(R.id.tvHeader);
        tvHeader.setText(getResources().getString(R.string.app_name));
        EditText etReasonMsg=(EditText) promptView.findViewById(R.id.etReasonMsg);
        TextView tvMsg=(TextView) promptView.findViewById(R.id.tvMsg);
        tvMsg.setText(msg);
        if (from.equals("accept")){
            etReasonMsg.setHint("Enter preparation time");
        }else{
            etReasonMsg.setHint("Enter reason here");
        }

        Button btnCancel = (Button) promptView.findViewById(R.id.btnCancel);
        btnCancel.setText("Cancel");
        Button btnOk = (Button) promptView.findViewById(R.id.btnOk);
        //btnOk.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
        btnOk.setText("Ok");

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //deleteFromCart(id, pos);

                if (etReasonMsg.getText().toString().equals("")){
                    if (from.equals("accept")) {
                        Toast.makeText(OrderDetails.this, "Please enter approx preparation time!", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(OrderDetails.this, "Please enter your reason for cancellation!", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    if (from.equals("accept")){
                        AcceptOrderRequestModel acceptOrderRequestModel = new AcceptOrderRequestModel(
                                ordId,
                                etReasonMsg.getText().toString()
                        );

                        acceptOrderRequest(acceptOrderRequestModel);


                    }else{
                        RejectOrderRequestModel rejectOrderRequestModel = new RejectOrderRequestModel(
                                ordId,
                                etReasonMsg.getText().toString()
                        );

                        cancelOrderRequest(rejectOrderRequestModel);
                    }



                    alertD.dismiss();

                }



            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertD.dismiss();

            }
        });

        alertD.setView(promptView);
        try {
            alertD.show();
        }
        catch (WindowManager.BadTokenException e) {
            //use a log message
        }
    }

    private void cancelOrderRequest(RejectOrderRequestModel rejectOrderRequestModel) {
        dialogView.showCustomSpinProgress(OrderDetails.this);

        manager.service.rejectNewOrder(rejectOrderRequestModel).enqueue(new Callback<RejectOrderResponseModel>() {
            @Override
            public void onResponse(Call<RejectOrderResponseModel> call, Response<RejectOrderResponseModel> response) {
                if (response.isSuccessful()){
                    dialogView.dismissCustomSpinProgress();
                    RejectOrderResponseModel rejectOrderResponseModel = response.body();
                    if (!rejectOrderResponseModel.error){
                        Toast.makeText(OrderDetails.this, "Order Cancelled!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(OrderDetails.this, Dashboard.class));
                        finish();
                    }
                }else{
                    dialogView.dismissCustomSpinProgress();
                }
            }

            @Override
            public void onFailure(Call<RejectOrderResponseModel> call, Throwable t) {

            }
        });

    }

    private void getOrderDetails(String id) {
        dialogView.showCustomSpinProgress(OrderDetails.this);

        manager.service.getOrderDetails(id).enqueue(new Callback<OrderDetailsResponseModel>() {
            @Override
            public void onResponse(Call<OrderDetailsResponseModel> call, Response<OrderDetailsResponseModel> response) {
                if (response.isSuccessful()){
                    dialogView.dismissCustomSpinProgress();
                    OrderDetailsResponseModel orderDetailsResponseModel = response.body();
                    if(orderDetailsResponseModel.error != true){

                        orderItemList = new ArrayList<>();

                        OrderDetailsModel orderDetailsModel = orderDetailsResponseModel.orderData;

                        orderItemList = orderDetailsModel.items;

                        tvOrderNumber.setText(orderDetailsModel.unique_id);
                        tvDate.setText(orderDetailsModel.created_at);
                        if (orderDetailsModel.payment_status.equals("1")){
                            if (orderDetailsModel.transaction_id.equals("test-cod")){
                                tvPayment.setText("Paid : Using cash");
                            }else {
                                tvPayment.setText("paid : Using razorpay");
                            }
                        }else{
                            tvPayment.setText("Unpaid");
                        }

                        tvDelBy.setText("");
                        if (orderDetailsModel.transaction_id.equals("test-cod")){
                            tvTransId.setText("Cash On Delivery");
                        }else {
                            tvTransId.setText(orderDetailsModel.transaction_id);
                        }

                        tvDelTo.setText(orderDetailsModel.delivery_address);
                        tvPhn.setText(orderDetailsModel.mobile);
                        DecimalFormat formatter1 = new DecimalFormat("#,##,###.00");
                        String formatted = formatter1.format(Double.parseDouble(orderDetailsModel.delivery_charge));
                        tvDelFee.setText("₹ " + formatted);
                        String formatted2 = formatter1.format(Double.parseDouble(orderDetailsModel.packing_price));
                        tvPackingChrg.setText("₹ " + formatted2);
                        String formatted3 = formatter1.format(Double.parseDouble(orderDetailsModel.tax_amount));
                        tvTax.setText("₹ " + formatted3);

                        String formatted1 = formatter1.format(Double.parseDouble(orderDetailsModel.total_amount));
                        tvTotPrice.setText("₹ " + formatted1);

                        if (orderDetailsModel.transaction_id.equals("test-cod")){
                            tvPayType.setText("Cash Payment");
                        }else {
                            tvPayType.setText("Online Payment");
                        }

                        tvRestName.setText(orderDetailsModel.restaurant.name);
                        tvRestAdr.setText(orderDetailsModel.restaurant.address);



                        if (orderItemList.size() > 0){

                            orderItemAdapter = new OrderItemAdapter(OrderDetails.this, orderItemList);
                            pastOrderItemRv.setLayoutManager(new LinearLayoutManager(OrderDetails.this, LinearLayoutManager.VERTICAL, false));
                            pastOrderItemRv.setAdapter(orderItemAdapter);

                        }

                    }else{

                    }


                }else{
                    dialogView.dismissCustomSpinProgress();
                }
            }

            @Override
            public void onFailure(Call<OrderDetailsResponseModel> call, Throwable t) {

                dialogView.dismissCustomSpinProgress();

            }
        });
    }
}