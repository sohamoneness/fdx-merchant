package com.oneness.fdxmerchant.Activity.Order;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.oneness.fdxmerchant.Activity.EntryPoint.Dashboard;
import com.oneness.fdxmerchant.Activity.ItemManagement.ItemListActivity;
import com.oneness.fdxmerchant.Adapters.CheckoutOrderAdapter;
import com.oneness.fdxmerchant.Fragments.BottomSheets.ItemEditBottomSheet;
import com.oneness.fdxmerchant.Fragments.BottomSheets.UserAddressListBottomSheet;
import com.oneness.fdxmerchant.Models.CartModels.CartDataModel;
import com.oneness.fdxmerchant.Models.CartModels.CartDataResponseModel;
import com.oneness.fdxmerchant.Models.DemoDataModels.CartItemModel;
import com.oneness.fdxmerchant.Models.PlaceOrderModels.PlaceOrderRequest;
import com.oneness.fdxmerchant.Models.PlaceOrderModels.PlaceOrderResponseModel;
import com.oneness.fdxmerchant.Models.PlaceOrderModels.PlacedOrderDetailsModel;
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

public class CheckOutActivity extends AppCompatActivity {

    RecyclerView orderRv;
    public static List<CartDataModel> cartItemList = new ArrayList<>();
    CheckoutOrderAdapter coAdapter;
    CheckBox cbCutlery;
    Button btnProceed;
    TextView tvTip1, tvTip2, tvTip3, tvTip4;
    LinearLayout otherTipLL, adrLL;
    TextView tvTipVal, tvTag, tvAddress, tvGotoSelect;
    RelativeLayout selectAddressRL;
    ImageView ivBack;
    Prefs prefs;
    DialogView dialogView;
    ApiManager manager = new ApiManager();

    public static CartDataResponseModel cartDataResponseModel;

    int totQty = 0;
    int delTip = 0;
    double totalAmount = 0.0;
    double totalItemPrice = 0.0;
    String restId = "";

    TextView tvTotItem, tvDelFee, tvTax, tvTotItemPrice, tvTotalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);

        prefs = new Prefs(CheckOutActivity.this);
        dialogView = new DialogView();

        orderRv = findViewById(R.id.orderRv);
        btnProceed = findViewById(R.id.btnProceed);
        cbCutlery = findViewById(R.id.cbCutlery);
        tvTip1 = findViewById(R.id.tvTip1);
        tvTip2 = findViewById(R.id.tvTip2);
        tvTip3 = findViewById(R.id.tvTip3);
        tvTip4 = findViewById(R.id.tvTip4);
        otherTipLL = findViewById(R.id.otherTipLL);
        tvTipVal = findViewById(R.id.tvTipVal);
        ivBack = findViewById(R.id.ivBack);

        tvTotItem = findViewById(R.id.tvTotItem);
        tvDelFee = findViewById(R.id.tvDelFee);
        tvTax = findViewById(R.id.tvTax);
        tvTotItemPrice = findViewById(R.id.tvTotItemPrice);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
       // etCustomTip = findViewById(R.id.etCustomTip);
       // tvAddCustomTip = findViewById(R.id.tvAddCustomTip);
       // tvTipRemove = findViewById(R.id.tvTipRemove);
        //tvAddItem = findViewById(R.id.tvAddItem);
        selectAddressRL = findViewById(R.id.selectAddressRL);
        tvTag = findViewById(R.id.tvTag);
        tvAddress = findViewById(R.id.tvAddress);
        adrLL = findViewById(R.id.adrLL);
        tvGotoSelect = findViewById(R.id.tvGotoSelect);
        ivBack = findViewById(R.id.ivBack);



        //getCartItems();
        for (int y = 0; y < cartItemList.size(); y++) {
            int qty = Integer.parseInt(cartItemList.get(y).quantity);

            totQty = totQty + qty;
        }

        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            public void run() {
                //
                // Do the stuff
                //

                if (Constants.addressSelected == true) {
                    adrLL.setVisibility(View.VISIBLE);
                    tvGotoSelect.setVisibility(View.GONE);
                    tvTag.setText("Delivery to " + Constants.userAddressModel.tag);
                    tvAddress.setText(Constants.userAddressModel.location);
                } else {
                    adrLL.setVisibility(View.GONE);
                    tvGotoSelect.setVisibility(View.VISIBLE);
                }


                handler.postDelayed(this, 100);
            }
        };
        runnable.run();


        tvTotItem.setText("Total Items Price (" + totQty + ")");


        totalAmount = Double.parseDouble(cartDataResponseModel.total_amount);
        double deliveryCharge = Double.parseDouble(cartDataResponseModel.delivery_charge);
        double tax = Double.parseDouble(cartDataResponseModel.tax_amount);
        totalItemPrice = totalAmount - (deliveryCharge + tax);

        tvTotItemPrice.setText("\u20B9 " + String.valueOf(totalItemPrice));

        tvDelFee.setText("\u20B9 " + cartDataResponseModel.delivery_charge);
        tvTax.setText("\u20B9 " + cartDataResponseModel.tax_amount);

        tvTotalPrice.setText("\u20B9 " + cartDataResponseModel.total_amount);
        btnProceed.setText("Proceed to Pay \u20B9 " + String.valueOf(totalAmount));

        coAdapter = new CheckoutOrderAdapter(CheckOutActivity.this, cartItemList);
        orderRv.setLayoutManager(new LinearLayoutManager(CheckOutActivity.this, LinearLayoutManager.VERTICAL, false));
        orderRv.setAdapter(coAdapter);

        selectAddressRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserAddressListBottomSheet userAddressListBottomSheet = new UserAddressListBottomSheet();
                userAddressListBottomSheet.show((CheckOutActivity.this).getSupportFragmentManager(), "callAddOn");
            }
        });

        btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                PlaceOrderRequest placeOrderRequest = new PlaceOrderRequest(
                        prefs.getData(Constants.REST_ID),
                        prefs.getData(Constants.SELECTED_USER_ID),
                        Constants.userName,
                        Constants.userMobile,
                        Constants.userEmail,
                        Constants.userAddressModel.address,
                        Constants.userAddressModel.location,
                        Constants.userAddressModel.country,
                        Constants.userAddressModel.city,
                        Constants.userAddressModel.pin,
                        Constants.userAddressModel.lat,
                        Constants.userAddressModel.lng,
                        cartDataResponseModel.total_amount,
                        "",
                        "0",
                        "cod"
                );

               // Gson gson = new Gson();
               // String param = gson.toJson(placeOrderRequest);
               // Log.d("PARAMS_PlaceOrder", param);
                placeOrder(placeOrderRequest);

            }
        });

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        tvTip1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                otherTipLL.setVisibility(View.GONE);
                tvTip1.setBackground(getResources().getDrawable(R.drawable.orange_border_et_bg));
                tvTip2.setBackground(getResources().getDrawable(R.drawable.white_bg_with_border));
                tvTip3.setBackground(getResources().getDrawable(R.drawable.white_bg_with_border));
                tvTip4.setBackground(getResources().getDrawable(R.drawable.white_bg_with_border));
                tvTipVal.setText(tvTip1.getText().toString());
                tvTip1.setTextColor(getResources().getColor(R.color.colorAccent));
                tvTip2.setTextColor(getResources().getColor(R.color.black));
                tvTip3.setTextColor(getResources().getColor(R.color.black));
                tvTip4.setTextColor(getResources().getColor(R.color.black));
                tvTipVal.setTextColor(getResources().getColor(R.color.black));

            }
        });

        tvTip2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                otherTipLL.setVisibility(View.GONE);
                tvTip1.setBackground(getResources().getDrawable(R.drawable.white_bg_with_border));
                tvTip2.setBackground(getResources().getDrawable(R.drawable.orange_border_et_bg));
                tvTip3.setBackground(getResources().getDrawable(R.drawable.white_bg_with_border));
                tvTip4.setBackground(getResources().getDrawable(R.drawable.white_bg_with_border));
                tvTipVal.setText(tvTip2.getText().toString());
                tvTip1.setTextColor(getResources().getColor(R.color.black));
                tvTip2.setTextColor(getResources().getColor(R.color.colorAccent));
                tvTip3.setTextColor(getResources().getColor(R.color.black));
                tvTip4.setTextColor(getResources().getColor(R.color.black));
                tvTipVal.setTextColor(getResources().getColor(R.color.black));
            }
        });

        tvTip3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                otherTipLL.setVisibility(View.GONE);
                tvTip1.setBackground(getResources().getDrawable(R.drawable.white_bg_with_border));
                tvTip2.setBackground(getResources().getDrawable(R.drawable.white_bg_with_border));
                tvTip3.setBackground(getResources().getDrawable(R.drawable.orange_border_et_bg));
                tvTip4.setBackground(getResources().getDrawable(R.drawable.white_bg_with_border));
                tvTipVal.setText(tvTip3.getText().toString());
                tvTip1.setTextColor(getResources().getColor(R.color.black));
                tvTip2.setTextColor(getResources().getColor(R.color.black));
                tvTip3.setTextColor(getResources().getColor(R.color.colorAccent));
                tvTip4.setTextColor(getResources().getColor(R.color.black));
                tvTipVal.setTextColor(getResources().getColor(R.color.black));
            }
        });

        tvTip4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                otherTipLL.setVisibility(View.VISIBLE);
                tvTip1.setBackground(getResources().getDrawable(R.drawable.white_bg_with_border));
                tvTip2.setBackground(getResources().getDrawable(R.drawable.white_bg_with_border));
                tvTip3.setBackground(getResources().getDrawable(R.drawable.white_bg_with_border));
                tvTip4.setBackground(getResources().getDrawable(R.drawable.orange_border_et_bg));
                tvTip1.setTextColor(getResources().getColor(R.color.black));
                tvTip2.setTextColor(getResources().getColor(R.color.black));
                tvTip3.setTextColor(getResources().getColor(R.color.black));
                tvTip4.setTextColor(getResources().getColor(R.color.colorAccent));
            }
        });

    }

    private void placeOrder(PlaceOrderRequest placeOrderRequest) {

        dialogView.showCustomSpinProgress(CheckOutActivity.this);

        manager.service.placeOrder(placeOrderRequest).enqueue(new Callback<PlaceOrderResponseModel>() {
            @Override
            public void onResponse(Call<PlaceOrderResponseModel> call, Response<PlaceOrderResponseModel> response) {
                if (response.isSuccessful()){
                    dialogView.dismissCustomSpinProgress();
                    PlaceOrderResponseModel pom = response.body();
                    if (pom.error != true){
                        PlacedOrderDetailsModel placedOrderDetailsModel = new PlacedOrderDetailsModel();
                        placedOrderDetailsModel = pom.order;

                        showOrderPlacedPopup(placedOrderDetailsModel.id);
                        //dialogView.showSingleButtonDialog(PaymentActivity.this, getResources().getString(R.string.app_name), "Order Placed successfully!");
                        Constants.addressSelected = false;
                        Constants.TAG = 0;
                        Constants.cartPrice = 0;
                        //if (Constants.isDialogOn == 0){
                        // startActivity(new Intent(PaymentActivity.this, Dashboard.class));
                        // finishAffinity();
                        // }

                    }
                }else{
                    dialogView.dismissCustomSpinProgress();
                }
            }

            @Override
            public void onFailure(Call<PlaceOrderResponseModel> call, Throwable t) {

                dialogView.dismissCustomSpinProgress();

            }
        });

    }

    private void showOrderPlacedPopup(String id) {
        LayoutInflater layoutInflater = LayoutInflater.from(CheckOutActivity.this);
        View promptView = layoutInflater.inflate(R.layout.place_order_popup_lay, null);

        final AlertDialog alertD = new AlertDialog.Builder(CheckOutActivity.this).create();
        TextView tvHeader=(TextView)promptView.findViewById(R.id.tvHeader);
        tvHeader.setText(getResources().getString(R.string.app_name));
        //EditText etReasonMsg=(EditText) promptView.findViewById(R.id.etReasonMsg);
        //TextView tvMsg=(TextView) promptView.findViewById(R.id.tvMsg);
        //tvMsg.setText(msg);


        Button btnCancel = (Button) promptView.findViewById(R.id.btnMyOrder);
        //btnCancel.setText("Cancel");
        Button btnOk = (Button) promptView.findViewById(R.id.btnHome);
        //btnOk.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
        //btnOk.setText("Ok");

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CheckOutActivity.this, Dashboard.class));
                finishAffinity();

                //deleteFromCart(id, pos);
                alertD.dismiss();

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //OrderStatusActivity.orderId = id;
                //startActivity(new Intent(PaymentActivity.this, OrderStatusActivity.class));
                //finish();
                alertD.dismiss();

            }
        });

        alertD.setView(promptView);
        alertD.show();
    }

    /*private void getCartItems() {

        CartItemModel cim = new CartItemModel();
        cim.item_name = "Chicken Biriyani";
        cim.extra_item = "Extra chicken(1)";
        cim.price = "250";
        cim.extra_price = "49";
        cim.tot_price = "299";
        cim.qty = "1";
        cim.type = "non-veg";
        cartItemList.add(cim);

        CartItemModel cim1 = new CartItemModel();
        cim1.item_name = "Veg Momo";
        cim1.extra_item = "(6 pieces)";
        cim1.price = "120";
        cim1.extra_price = "0";
        cim1.tot_price = "120";
        cim1.qty = "1";
        cim1.type = "veg";
        cartItemList.add(cim1);

        CartItemModel cim2 = new CartItemModel();
        cim2.item_name = "Chicken Biriyani";
        cim2.extra_item = "Extra chicken(1)";
        cim2.price = "250";
        cim2.extra_price = "49";
        cim2.tot_price = "299";
        cim2.qty = "1";
        cim2.type = "non-veg";
        cartItemList.add(cim2);

    }*/
}