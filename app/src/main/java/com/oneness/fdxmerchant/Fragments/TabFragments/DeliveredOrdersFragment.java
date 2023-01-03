package com.oneness.fdxmerchant.Fragments.TabFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.oneness.fdxmerchant.Adapters.DeliveredOrderAdapter;
import com.oneness.fdxmerchant.Adapters.NewOrdersAdapter;
import com.oneness.fdxmerchant.Models.OrderModels.DeliveredOrderModel;
import com.oneness.fdxmerchant.Models.OrderModels.DeliveredOrderResponseModel;
import com.oneness.fdxmerchant.Models.OrderModels.NewOrderResponseModel;
import com.oneness.fdxmerchant.Models.OrderModels.NewOrdersModel;
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

public class DeliveredOrdersFragment extends Fragment {
    RecyclerView delOrderRv;

    ApiManager manager =new ApiManager();
    DialogView dialogView;
    Prefs prefs;

    DeliveredOrderAdapter ordersAdapter;

    List<DeliveredOrderModel> delOrderList = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.delivered_orders_fragment, container, false);

        dialogView = new DialogView();
        prefs = new Prefs(getActivity());

        delOrderRv = v.findViewById(R.id.delOrderRv);

        getDeliveredOrders(prefs.getData(Constants.REST_ID));




        return v;
    }

    private void getDeliveredOrders(String id) {

        //dialogView.showCustomSpinProgress(getActivity());

        manager.service.getDeliveredOrders(id).enqueue(new Callback<DeliveredOrderResponseModel>() {
            @Override
            public void onResponse(Call<DeliveredOrderResponseModel> call, Response<DeliveredOrderResponseModel> response) {

                if (response.isSuccessful()){
                   // dialogView.dismissCustomSpinProgress();

                    DeliveredOrderResponseModel norm = response.body();

                    if (!norm.error){
                        delOrderList = norm.orders;

                        if (delOrderList.size() > 0){

                            ordersAdapter = new DeliveredOrderAdapter(getActivity(), delOrderList);
                            delOrderRv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                            delOrderRv.setAdapter(ordersAdapter);

                        }else{

                        }

                    }else{

                    }

                }else{
                    //dialogView.dismissCustomSpinProgress();
                }

            }

            @Override
            public void onFailure(Call<DeliveredOrderResponseModel> call, Throwable t) {

                //dialogView.dismissCustomSpinProgress();

            }
        });

    }
}
