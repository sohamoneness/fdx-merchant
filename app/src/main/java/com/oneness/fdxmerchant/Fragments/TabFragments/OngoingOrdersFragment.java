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

import com.oneness.fdxmerchant.Adapters.NewOrdersAdapter;
import com.oneness.fdxmerchant.Adapters.OngoingOrderAdapter;
import com.oneness.fdxmerchant.Models.OrderModels.NewOrderResponseModel;
import com.oneness.fdxmerchant.Models.OrderModels.OngoingOrderModel;
import com.oneness.fdxmerchant.Models.OrderModels.OngoingOrderResponseModel;
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

public class OngoingOrdersFragment extends Fragment {

    RecyclerView ongoingOrderRv;

    ApiManager manager =new ApiManager();
    DialogView dialogView;
    Prefs prefs;

    OngoingOrderAdapter ordersAdapter;

    List<OngoingOrderModel> ongoingOrderList = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.ongoing_orders_fragment, container, false);

        dialogView = new DialogView();
        prefs = new Prefs(getActivity());

        ongoingOrderRv = v.findViewById(R.id.ongoingOrderRv);

        getOngoingOrders(prefs.getData(Constants.REST_ID));




        return v;
    }

    private void getOngoingOrders(String id) {

        //dialogView.showCustomSpinProgress(getActivity());

        manager.service.getOngoingOrders(id).enqueue(new Callback<OngoingOrderResponseModel>() {
            @Override
            public void onResponse(Call<OngoingOrderResponseModel> call, Response<OngoingOrderResponseModel> response) {

                if (response.isSuccessful()){
                    //dialogView.dismissCustomSpinProgress();

                    OngoingOrderResponseModel norm = response.body();

                    if (!norm.error){
                        ongoingOrderList = norm.orders;

                        if (ongoingOrderList.size() > 0){

                            ordersAdapter = new OngoingOrderAdapter(getActivity(), ongoingOrderList);
                            ongoingOrderRv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                            ongoingOrderRv.setAdapter(ordersAdapter);

                        }else{

                        }

                    }else{

                    }

                }else{
                   // dialogView.dismissCustomSpinProgress();
                }

            }

            @Override
            public void onFailure(Call<OngoingOrderResponseModel> call, Throwable t) {

                //dialogView.dismissCustomSpinProgress();

            }
        });

    }
}
