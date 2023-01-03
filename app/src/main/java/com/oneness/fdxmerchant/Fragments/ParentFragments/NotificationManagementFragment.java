package com.oneness.fdxmerchant.Fragments.ParentFragments;

import static com.oneness.fdxmerchant.Activity.EntryPoint.Dashboard.pushFragmentsStatic;

import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.oneness.fdxmerchant.Activity.EntryPoint.Dashboard;
import com.oneness.fdxmerchant.Adapters.NotificationAdapter;
import com.oneness.fdxmerchant.Models.NotificationModels.NotificationModel;
import com.oneness.fdxmerchant.Models.NotificationModels.NotificationResponseModel;
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

public class NotificationManagementFragment extends Fragment {

    RecyclerView notificationRv;
    Prefs prefs;
    DialogView dialogView;
    ApiManager manager = new ApiManager();
    NotificationAdapter nAdapter;

    List<NotificationModel> notificationList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.notification_management_fragment, container, false);

        dialogView = new DialogView();
        prefs = new Prefs(getActivity());

        notificationRv = v.findViewById(R.id.notificationRv);

        getNotificationList(prefs.getData(Constants.REST_ID));

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

    private void getNotificationList(String data) {
        dialogView.showCustomSpinProgress(getActivity());
        manager.service.getNotifications(data).enqueue(new Callback<NotificationResponseModel>() {
            @Override
            public void onResponse(Call<NotificationResponseModel> call, Response<NotificationResponseModel> response) {
                if (response.isSuccessful()){
                    dialogView.dismissCustomSpinProgress();
                    NotificationResponseModel nrm = response.body();
                    if (!nrm.error){

                        notificationList = nrm.notifications;

                        if (notificationList.size()>0){

                            nAdapter = new NotificationAdapter(getActivity(), notificationList);
                            notificationRv.setLayoutManager(new LinearLayoutManager(getActivity(),
                                    LinearLayoutManager.VERTICAL, false));
                            notificationRv.setAdapter(nAdapter);

                        }

                    }else{

                    }
                }else {
                    dialogView.dismissCustomSpinProgress();
                }
            }

            @Override
            public void onFailure(Call<NotificationResponseModel> call, Throwable t) {

                dialogView.dismissCustomSpinProgress();

            }
        });

    }


}
