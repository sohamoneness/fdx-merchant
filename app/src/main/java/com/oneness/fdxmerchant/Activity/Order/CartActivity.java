package com.oneness.fdxmerchant.Activity.Order;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.oneness.fdxmerchant.Adapters.CartItemAdapter;
import com.oneness.fdxmerchant.Adapters.CompleteMealAdapter;
import com.oneness.fdxmerchant.Models.CartModels.CartDataModel;
import com.oneness.fdxmerchant.Models.CartModels.CartDataResponseModel;
import com.oneness.fdxmerchant.Models.DemoDataModels.CartItemModel;
import com.oneness.fdxmerchant.Models.DemoDataModels.CompleteMealModel;
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

public class CartActivity extends AppCompatActivity {

    RecyclerView orderRv, completeMealRv;
    TextView tvItems, tvDelFee, tvTax, tvTotPrice;
    ImageView ivBack;
    List<CartDataModel> cartItemList = new ArrayList<>();
   // List<CompleteMealModel> completeMealList = new ArrayList<>();
    CartItemAdapter ciAdapter;
    //CompleteMealAdapter completeMealAdapter;
    Button btnChkOut;
    CartDataResponseModel cartDataResponseModel;

    String userID = "", restID = "";

    Prefs prefs;
    ApiManager manager = new ApiManager();
    DialogView dialogView;

    TextView tvTotItemPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        
        dialogView = new DialogView();
        prefs = new Prefs(CartActivity.this);
        
        orderRv = findViewById(R.id.orderRv);
        completeMealRv = findViewById(R.id.completeMealRv);
        tvItems = findViewById(R.id.tvTotItem);
       // tvItemPrice = findViewById(R.id.tvTotalPrice);
        tvDelFee = findViewById(R.id.tvDelFee);
        tvTax = findViewById(R.id.tvTax);
        tvTotPrice = findViewById(R.id.tvTotalPrice);
        ivBack = findViewById(R.id.iv_back);
        btnChkOut = findViewById(R.id.btnChkOut);
        tvTotItemPrice = findViewById(R.id.tvTotItemPrice);

        getCartData(prefs.getData(Constants.SELECTED_USER_ID));

        //getCartItems();
        //getCompleteMeal();

        

        /*completeMealAdapter = new CompleteMealAdapter(CartActivity.this, completeMealList);
        completeMealRv.setLayoutManager(new LinearLayoutManager(CartActivity.this, LinearLayoutManager.HORIZONTAL, false));
        completeMealRv.setAdapter(completeMealAdapter);*/



        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btnChkOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckOutActivity.cartItemList = cartItemList;
                CheckOutActivity.cartDataResponseModel = cartDataResponseModel;
                startActivity(new Intent(CartActivity.this, CheckOutActivity.class));
            }
        });

    }

    private void getCartData(String userId) {
        dialogView.showCustomSpinProgress(CartActivity.this);
        manager.service.getCartList(userId).enqueue(new Callback<CartDataResponseModel>() {
            @Override
            public void onResponse(Call<CartDataResponseModel> call, Response<CartDataResponseModel> response) {
                if (response.isSuccessful()){
                    dialogView.dismissCustomSpinProgress();
                    CartDataResponseModel cdrm = response.body();
                    if (!cdrm.error){
                        cartDataResponseModel = cdrm;
                        cartItemList = cdrm.carts;
                        
                        if (cartItemList.size()>0){

                            int totQty = 0;

                            for (int y = 0; y < cartItemList.size(); y++){
                                int qty = Integer.parseInt(cartItemList.get(y).quantity);

                                totQty = totQty + qty;
                            }

                            tvItems.setText("Total Items Price (" + totQty + ")");
                            double totalItemPrice = 0;

                            double totalAmount = Double.parseDouble(cdrm.total_amount);
                            double deliveryCharge = Double.parseDouble(cdrm.delivery_charge);
                            double tax = Double.parseDouble(cdrm.tax_amount);
                            totalItemPrice = totalAmount - (deliveryCharge + tax);

                            tvTotItemPrice.setText("\u20B9 " + String.valueOf(totalItemPrice));

                            tvDelFee.setText("\u20B9 " + cdrm.delivery_charge);
                            tvTax.setText("\u20B9 " + cdrm.tax_amount);

                            tvTotPrice.setText("\u20B9 " + cdrm.total_amount);

                            if (restID != null) {
                                if (restID.equals("")) {
                                    restID = cartItemList.get(0).restaurant_id;
                                    Log.d("RESTID", restID);
                                }
                            }else{
                                restID = cartItemList.get(0).restaurant_id;
                                Log.d("RESTID", restID);
                            }
                            
                            ciAdapter = new CartItemAdapter(CartActivity.this, cartItemList);
                            orderRv.setLayoutManager(new LinearLayoutManager(CartActivity.this, LinearLayoutManager.VERTICAL, false));
                            orderRv.setAdapter(ciAdapter);
                        }
                        
                    }else{
                        
                    }
                    
                }else{
                    dialogView.dismissCustomSpinProgress();
                }
            }

            @Override
            public void onFailure(Call<CartDataResponseModel> call, Throwable t) {
                dialogView.dismissCustomSpinProgress();
            }
        });
    }

}