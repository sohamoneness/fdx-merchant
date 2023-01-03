package com.oneness.fdxmerchant.Activity.ItemManagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.oneness.fdxmerchant.Adapters.ManageItemMenuAdapter;
import com.oneness.fdxmerchant.Fragments.BottomSheets.ItemEditBottomSheet;
import com.oneness.fdxmerchant.Models.DemoDataModels.CompleteMealModel;
import com.oneness.fdxmerchant.Models.ItemManagementModels.CategoryListModel;
import com.oneness.fdxmerchant.Models.ItemManagementModels.CategoryWiseItemResponseModel;
import com.oneness.fdxmerchant.Models.ItemManagementModels.CategoryWithItemModel;
import com.oneness.fdxmerchant.Models.ItemManagementModels.ItemsModel;
import com.oneness.fdxmerchant.Models.ItemManagementModels.ItemsResponseModel;
import com.oneness.fdxmerchant.Network.ApiManager;
import com.oneness.fdxmerchant.Network.Constants;
import com.oneness.fdxmerchant.R;
import com.oneness.fdxmerchant.Utils.DialogView;
import com.oneness.fdxmerchant.Utils.Prefs;
import com.oneness.fdxmerchant.Utils.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemListActivity extends AppCompatActivity {

    RecyclerView itemMenuRv;
    ManageItemMenuAdapter manageItemMenuAdapter;
    ImageView ivBack;

   // List<CompleteMealModel> cmList = new ArrayList<>();
    TextView tvAdd, tvCatName;
    public static String catID = "";

    Prefs prefs;
    ApiManager manager = new ApiManager();
    DialogView dialogView;

   // List<CategoryWithItemModel> catWithItemList = new ArrayList<>();
    List<ItemsModel> itemsList = new ArrayList<>();
   public static String catName = "";

    private int[] tabIcons = {
            R.drawable.biriyani_img,
            R.drawable.biriyani_img,
            R.drawable.biriyani_img,
            R.drawable.biriyani_img
    };
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        prefs = new Prefs(ItemListActivity.this);
        dialogView = new DialogView();

        itemMenuRv = findViewById(R.id.itemMenuRv);
        tabLayout = findViewById(R.id.tabLayout);
        tvAdd = findViewById(R.id.tvAdd);
        ivBack = findViewById(R.id.ivBack);
        tvCatName = findViewById(R.id.tvCatName);

        //catID = getIntent().getStringExtra(Constants.CAT_ID);

        tvCatName.setText(catName);

        //Log.d("AYAN_TEST>>", catID + ", " + catName);

        getItemsList(catID);

       // getItemList(prefs.getData(Constants.REST_ID), catID);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        itemMenuRv.addOnItemTouchListener(new RecyclerItemClickListener(ItemListActivity.this, itemMenuRv, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }

            @Override
            public void onItemLongClick(View view, int position) {
                ItemsModel im = new ItemsModel();
                im = itemsList.get(position);
                ItemEditBottomSheet.itemsModel = im;
                ItemEditBottomSheet itemEditBottomSheet = new ItemEditBottomSheet();
                itemEditBottomSheet.show((ItemListActivity.this).getSupportFragmentManager(), "callAddOn");
            }
        }));

        /*manageItemMenuAdapter = new ManageItemMenuAdapter(ItemListActivity.this, cmList);
        itemMenuRv.setLayoutManager(new GridLayoutManager(ItemListActivity.this, 2));
        itemMenuRv.setAdapter(manageItemMenuAdapter);*/

        tvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddNewItem.isFrom = 0;
                startActivity(new Intent(ItemListActivity.this, AddNewItem.class));
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        /*if (Constants.needToRefresh){
            getItemsList(catID);
        }*/
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        /*catID = getIntent().getStringExtra("CatID");
        getItemsList(catID);*/
    }

    private void getItemsList(String catID) {
        dialogView.showCustomSpinProgress(ItemListActivity.this);
        manager.service.getItemsForCategory(catID).enqueue(new Callback<CategoryWiseItemResponseModel>() {
            @Override
            public void onResponse(Call<CategoryWiseItemResponseModel> call, Response<CategoryWiseItemResponseModel> response) {

                if (response.isSuccessful()){
                    CategoryWiseItemResponseModel cwirm = response.body();
                    if (!cwirm.error){
                        dialogView.dismissCustomSpinProgress();
                        //Constants.needToRefresh = false;
                        itemsList = cwirm.items;
                        if (itemsList.size()>0){
                            manageItemMenuAdapter = new ManageItemMenuAdapter(ItemListActivity.this, itemsList);
                            itemMenuRv.setLayoutManager(new LinearLayoutManager(ItemListActivity.this, LinearLayoutManager.VERTICAL, false));
                            itemMenuRv.setAdapter(manageItemMenuAdapter);
                        }
                    }else{
                        dialogView.dismissCustomSpinProgress();
                    }
                }else{
                    dialogView.dismissCustomSpinProgress();
                }

            }

            @Override
            public void onFailure(Call<CategoryWiseItemResponseModel> call, Throwable t) {
                dialogView.dismissCustomSpinProgress();
            }
        });
    }

    /*private void getItemList(String restId, String catId) {
        dialogView.showCustomSpinProgress(ItemListActivity.this);
        manager.service.getRestaurantWiseItems(restId).enqueue(new Callback<ItemsResponseModel>() {
            @Override
            public void onResponse(Call<ItemsResponseModel> call, Response<ItemsResponseModel> response) {
                if (response.isSuccessful()){
                    dialogView.dismissCustomSpinProgress();
                    ItemsResponseModel irm = response.body();
                    if (!irm.error){
                        catWithItemList = irm.categories;

                        if (catWithItemList.size() > 0) {

                            for (int i = 0; i < catWithItemList.size(); i++) {
                                //itemList.addAll(catWithItemList.get(i).items);
                                if (catId.equals(catWithItemList.get(i).id)) {
                                    //itemList = new ArrayList<>();
                                    itemList = catWithItemList.get(i).items;
                                    if (itemList.size() > 0) {
                                        manageItemMenuAdapter = new ManageItemMenuAdapter(ItemListActivity.this, itemList);
                                        itemMenuRv.setLayoutManager(new GridLayoutManager(ItemListActivity.this, 2));
                                        itemMenuRv.setAdapter(manageItemMenuAdapter);
                                    }
                                }
                               //}
                            }


                            Log.d("ITEMS>>", ""+itemList.size());
                        }



                    }else{

                    }
                }
            }

            @Override
            public void onFailure(Call<ItemsResponseModel> call, Throwable t) {

            }
        });
    }*/


   /* private void setupTabIcons() {

        //View view1 = getLayoutInflater().inflate(R.layout.custom_tab_label, null);
        // view1.findViewById(R.id.icon).setBackgroundResource(R.drawable.biriyani_chicken);
        //tabLayout.addTab(tabLayout.newTab().setCustomView(view1));
        *//*tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.getTabAt(3).setIcon(tabIcons[3]);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);*//*
    }*/




}