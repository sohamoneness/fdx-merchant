package com.oneness.fdxmerchant.Activity.Order;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.oneness.fdxmerchant.Adapters.MenuItemAdapter;
import com.oneness.fdxmerchant.Models.CartModels.CartDataModel;
import com.oneness.fdxmerchant.Models.CartModels.CartDataResponseModel;
import com.oneness.fdxmerchant.Models.DemoDataModels.MenuItemModel;
import com.oneness.fdxmerchant.Models.OrderModels.CreateOrderModels.CustomCartModel;
import com.oneness.fdxmerchant.Models.OrderModels.OrderItemsModels.OrderCategoryModel;
import com.oneness.fdxmerchant.Models.OrderModels.OrderItemsModels.OrderItemRequestModel;
import com.oneness.fdxmerchant.Models.OrderModels.OrderItemsModels.OrderItemResponseModel;
import com.oneness.fdxmerchant.Models.OrderModels.OrderItemsModels.OrderRestaurantDataModel;
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

public class OrderActivity extends AppCompatActivity {

    private int[] tabIcons = {
            R.drawable.biriyani_img,
            R.drawable.biriyani_img,
            R.drawable.biriyani_img,
            R.drawable.biriyani_img
    };
    TabLayout tabLayout;
    //List<MenuItemModel> menuItemList = new ArrayList<>();
    List<OrderCategoryModel> categoryList = new ArrayList<>();
    List<CartDataModel> cartList = new ArrayList<>();
    List<CustomCartModel> customCartList = new ArrayList<>();

    RecyclerView menuRv;
    MenuItemAdapter miAdapter;
    RelativeLayout viewCartRL;
    TextView tvCartPrice, tvCartQty, tvViewCart, tvNoItem;

    ImageView ivSearch;

    Prefs prefs;
    ApiManager manager = new ApiManager();
    DialogView dialogView;
    ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        dialogView = new DialogView();
        prefs = new Prefs(OrderActivity.this);

        Constants.TAG = 0;
        Constants.cartPrice = 0.0;

        tabLayout = findViewById(R.id.tabLayout);
        menuRv = findViewById(R.id.menuRv);
        viewCartRL = findViewById(R.id.viewCartRL);
        tvCartPrice = findViewById(R.id.tvCartPrice);
        tvCartQty = findViewById(R.id.tvCartQty);
        tvViewCart = findViewById(R.id.tvViewCart);
        ivBack = findViewById(R.id.ivBack);
        tvNoItem = findViewById(R.id.tvNoItem);

        ivSearch = findViewById(R.id.ivSearch);

        OrderItemRequestModel orderItemRequestModel = new OrderItemRequestModel(
                prefs.getData(Constants.REST_ID),
                prefs.getData(Constants.SELECTED_USER_ID)
        );

        getAllItems(orderItemRequestModel);



        //setupTabIcons();
        //getMenuItems();
        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OrderActivity.this, SearchActivity.class));
            }
        });

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        tvViewCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OrderActivity.this, CartActivity.class));
            }
        });

        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            public void run() {
                //
                // Do the stuff
                //
                Log.d("TAG>>", Constants.TAG+"");
                if (Constants.TAG>0){
                    if (!Constants.isRevTabSelected){
                        viewCartRL.setVisibility(View.VISIBLE);
                        tvCartQty.setText(Constants.TAG + " Items");
                        tvCartPrice.setText("\u20B9 "+Constants.cartPrice);
                    }else{
                        viewCartRL.setVisibility(View.GONE);
                    }
                }else{
                    viewCartRL.setVisibility(View.GONE);
                }


                handler.postDelayed(this, 100);
            }
        };
        runnable.run();



        
    }

    private void getAllItems(OrderItemRequestModel orderItemRequestModel) {
        dialogView.showCustomSpinProgress(OrderActivity.this);
        manager.service.getOrderItems(orderItemRequestModel).enqueue(new Callback<OrderItemResponseModel>() {
            @Override
            public void onResponse(Call<OrderItemResponseModel> call, Response<OrderItemResponseModel> response) {
                if (response.isSuccessful()){
                    dialogView.dismissCustomSpinProgress();
                    OrderItemResponseModel oirm = response.body();
                    if (!oirm.error){
                        OrderRestaurantDataModel ordm = oirm.restaurantData;
                        categoryList = ordm.categories;

                        for (int x = 0 ; x < categoryList.size(); x++){
                            for (int y = 0; y < categoryList.get(x).items.size(); y++){
                                Constants.TAG = Constants.TAG + Integer.parseInt(categoryList.get(x).items.get(y).quantity);
                                Constants.cartPrice = Constants.cartPrice +
                                        (Integer.parseInt(categoryList.get(x).items.get(y).quantity)
                                                * Double.parseDouble(categoryList.get(x).items.get(y).price));
                            }
                        }

                        getCartListData(prefs.getData(Constants.SELECTED_USER_ID));

                        if (categoryList.size() > 0){
                            for (int i = 0; i < categoryList.size(); i++){
                                tabLayout.addTab(tabLayout.newTab().setText(categoryList.get(i).title));
                                //tabLayout.getTabAt(i).setIcon(categoryList.get(i).image);
                            }
                            tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);



                            if (categoryList.get(0).items.size()>0){
                                menuRv.setVisibility(View.VISIBLE);
                                miAdapter = new MenuItemAdapter(categoryList.get(0).items, customCartList, OrderActivity.this);
                                menuRv.setLayoutManager(new LinearLayoutManager(OrderActivity.this, LinearLayoutManager.VERTICAL, false));
                                menuRv.setAdapter(miAdapter);
                            }else {
                                menuRv.setVisibility(View.GONE);
                            }

                            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){

                                @Override
                                public void onTabSelected(TabLayout.Tab tab) {

                                    //viewPager.setCurrentItem(tab.getPosition());
                                    for (int x = 0; x < categoryList.size(); x++){

                                        if(tabLayout.getSelectedTabPosition() == x){
                                            if (categoryList.get(x).items.size()>0){
                                                menuRv.setVisibility(View.VISIBLE);
                                                tvNoItem.setVisibility(View.GONE);
                                                miAdapter = new MenuItemAdapter(categoryList.get(x).items, customCartList , OrderActivity.this);
                                                menuRv.setLayoutManager(new LinearLayoutManager(OrderActivity.this, LinearLayoutManager.VERTICAL, false));
                                                menuRv.setAdapter(miAdapter);
                                                //miAdapter.notifyDataSetChanged();
                                            }else {
                                                menuRv.setVisibility(View.GONE);
                                                tvNoItem.setVisibility(View.VISIBLE);
                                            }
                                           // Toast.makeText(OrderActivity.this, "Tab " + tabLayout.getSelectedTabPosition(), Toast.LENGTH_SHORT).show();

                                        }
                                    }


                                }

                                @Override
                                public void onTabUnselected(TabLayout.Tab tab) {

                                    miAdapter = new MenuItemAdapter(categoryList.get(0).items,customCartList, OrderActivity.this);
                                    menuRv.setLayoutManager(new LinearLayoutManager(OrderActivity.this, LinearLayoutManager.VERTICAL, false));
                                    menuRv.setAdapter(miAdapter);

                                }

                                @Override
                                public void onTabReselected(TabLayout.Tab tab) {

                                }
                            });

                        }

                    }else{

                    }

                }else{
                    dialogView.dismissCustomSpinProgress();
                }
            }

            @Override
            public void onFailure(Call<OrderItemResponseModel> call, Throwable t) {
                dialogView.dismissCustomSpinProgress();
            }
        });
    }

    private void getCartListData(String s) {

        manager.service.getCartList(s).enqueue(new Callback<CartDataResponseModel>() {
            @Override
            public void onResponse(Call<CartDataResponseModel> call, Response<CartDataResponseModel> response) {
                if (response.isSuccessful()){
                    CartDataResponseModel cdrm = response.body();
                    if (!cdrm.error){

                        cartList =cdrm.carts;
                        if (cartList.size()>0){
                            for (int i = 0; i < cartList.size(); i++){
                                CustomCartModel ccm = new CustomCartModel();
                                ccm.cart_id = cartList.get(i).id;
                                ccm.product_id = cartList.get(i).product_id;
                                customCartList.add(ccm);
                            }
                        }

                    }
                }
            }

            @Override
            public void onFailure(Call<CartDataResponseModel> call, Throwable t) {

            }
        });

    }

   /* private void setupTabIcons() {

        //View view1 = getLayoutInflater().inflate(R.layout.custom_tab_label, null);
       // view1.findViewById(R.id.icon).setBackgroundResource(R.drawable.biriyani_chicken);
        //tabLayout.addTab(tabLayout.newTab().setCustomView(view1));
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.getTabAt(3).setIcon(tabIcons[3]);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
    }*/


}