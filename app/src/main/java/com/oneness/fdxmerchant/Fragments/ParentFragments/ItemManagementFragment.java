package com.oneness.fdxmerchant.Fragments.ParentFragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.oneness.fdxmerchant.Activity.CategoryManagement.AddCategory;
import com.oneness.fdxmerchant.Activity.EntryPoint.Dashboard;
import com.oneness.fdxmerchant.Activity.ItemManagement.ItemListActivity;
import com.oneness.fdxmerchant.Activity.Order.OrderActivity;
import com.oneness.fdxmerchant.Adapters.CategoryAdapter;
import com.oneness.fdxmerchant.Adapters.CategoryManageAdapter;
import com.oneness.fdxmerchant.Models.DemoDataModels.CategoryModel;
import com.oneness.fdxmerchant.Models.ItemManagementModels.CategoryListModel;
import com.oneness.fdxmerchant.Models.ItemManagementModels.CategoryResponseModel;
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

public class ItemManagementFragment extends Fragment {

    RecyclerView categoryRv;
    List<CategoryWithItemModel> catList = new ArrayList<>();
    List<ItemsModel> itemList = new ArrayList<>();

    CategoryManageAdapter catAdapter;
    RelativeLayout createCatRL;

    Prefs prefs;
    ApiManager manager = new ApiManager();
    DialogView dialogView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.item_management_fragment, container, false);

        prefs = new Prefs(getActivity());
        dialogView = new DialogView();

        categoryRv = v.findViewById(R.id.categoryRv);
        createCatRL = v.findViewById(R.id.createCatRL);
        getCatList(prefs.getData(Constants.REST_ID));
        Log.d("REST_ID", prefs.getData(Constants.REST_ID));


        createCatRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddCategory.isFor = "";
                startActivity(new Intent(getActivity(), AddCategory.class));
                //getActivity().finish();
            }
        });



       /* categoryRv.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), categoryRv, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (Constants.isFrom.equals("Order")){
                    startActivity(new Intent(getActivity(), OrderActivity.class));
                }else{
                   // ItemListActivity.itemsList = catList.get(position).items;
                    ItemListActivity.catName = catList.get(position).title;

                   // startActivity(new Intent(getActivity(), ItemListActivity.class).putExtra(Constants.CAT_ID, catList.get(position).id));
                }

            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));*/

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                startActivity(new Intent(getActivity(), Dashboard.class));
                getActivity().finish();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getActivity(), callback);

        return v;
    }

    private void getCatList(String data) {
        dialogView.showCustomSpinProgress(getActivity());

        manager.service.getRestaurantWiseItems(data).enqueue(new Callback<ItemsResponseModel>() {
            @Override
            public void onResponse(Call<ItemsResponseModel> call, Response<ItemsResponseModel> response) {
                if (response.isSuccessful()){
                    dialogView.dismissCustomSpinProgress();
                    ItemsResponseModel crm = response.body();
                    Gson gson = new Gson();
                    Log.d("RESP>>", gson.toJson(crm));
                    if (!crm.error){

                        catList = crm.categories;
                        /*for (int i = 0; i < catList.size(); i++){
                            //itemList = new ArrayList<>();
                           // itemList = crm.categories.get(i).items;
                            itemList.addAll(crm.categories.get(i).items);
                        }*/
                        //Toast.makeText(getActivity(), itemList.size() + "", Toast.LENGTH_SHORT).show();


                        //getAllItems(data);

                        if (catList.size() > 0){
                            catAdapter = new CategoryManageAdapter(getActivity(), catList);
                            categoryRv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                            categoryRv.setAdapter(catAdapter);
                        }

                    }else {

                    }

                }else{

                }
            }

            @Override
            public void onFailure(Call<ItemsResponseModel> call, Throwable t) {

            }
        });

    }


}
