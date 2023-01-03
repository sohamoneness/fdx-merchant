package com.oneness.fdxmerchant.Fragments.ParentFragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.oneness.fdxmerchant.Activity.CouponManagement.AddCouponActivity;
import com.oneness.fdxmerchant.Activity.EntryPoint.Dashboard;
import com.oneness.fdxmerchant.Adapters.CouponListAdapter;
import com.oneness.fdxmerchant.Fragments.BottomSheets.CouponEditBottomSheet;
import com.oneness.fdxmerchant.Models.CouponModels.CouponListModel;
import com.oneness.fdxmerchant.Models.CouponModels.CouponResponseModel;
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

public class CouponManagementFragment extends Fragment {

    RecyclerView couponRv;
    RelativeLayout addCouponRL;
    Prefs prefs;
    DialogView dialogView;
    ApiManager manager = new ApiManager();
    List<CouponListModel> couponList = new ArrayList<>();
    CouponListAdapter clAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.coupon_fragment, container, false);

        dialogView = new DialogView();
        prefs = new Prefs(getActivity());

        addCouponRL = v.findViewById(R.id.addCouponRL);
        couponRv = v.findViewById(R.id.couponRv);

        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            public void run() {
                //
                // Do the stuff
                //
                try {
                    if (Constants.isNewCouponAdded == true || Constants.isCouponDeleted == true){
                        getCouponList(prefs.getData(Constants.REST_ID));
                        Constants.isNewCouponAdded = false;
                        Constants.isCouponDeleted = false;
                    }


                } catch (Exception e) {

                }


                handler.postDelayed(this, 1000);
            }
        };
        runnable.run();

        getCouponList(prefs.getData(Constants.REST_ID));

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                startActivity(new Intent(getActivity(), Dashboard.class));
                getActivity().finish();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getActivity(), callback);


        addCouponRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getActivity(), AddCouponActivity.class));

            }
        });

        couponRv.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), couponRv, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }

            @Override
            public void onItemLongClick(View view, int position) {

                CouponEditBottomSheet addItemBottomSheet = new CouponEditBottomSheet();
                //CouponEditBottomSheet.itemPrice = mim.price;
                CouponEditBottomSheet.couponId = couponList.get(position).id;
                CouponEditBottomSheet.couponListModel = couponList.get(position);
                addItemBottomSheet.show(getActivity().getSupportFragmentManager(), "callAddOn");

            }
        }));

        return v;
    }

    private void getCouponList(String restId) {
        dialogView.showCustomSpinProgress(getActivity());
        manager.service.getCouponsList(restId).enqueue(new Callback<CouponResponseModel>() {
            @Override
            public void onResponse(Call<CouponResponseModel> call, Response<CouponResponseModel> response) {
                if (response.isSuccessful()){
                    dialogView.dismissCustomSpinProgress();
                    CouponResponseModel crm = response.body();
                    if (!crm.error){

                        couponList = crm.offers;

                        if (couponList.size() > 0){
                            clAdapter = new CouponListAdapter(getActivity(), couponList);
                            couponRv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                            couponRv.setAdapter(clAdapter);
                        }

                    }else{
                        Toast.makeText(getActivity(), "Something went wrong please try again!", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    dialogView.dismissCustomSpinProgress();
                }
            }

            @Override
            public void onFailure(Call<CouponResponseModel> call, Throwable t) {
                dialogView.dismissCustomSpinProgress();
            }
        });
    }

}
