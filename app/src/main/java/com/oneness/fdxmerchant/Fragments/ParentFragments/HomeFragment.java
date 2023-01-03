package com.oneness.fdxmerchant.Fragments.ParentFragments;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentResolver;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.oneness.fdxmerchant.Adapters.TodayOrderAdapter;
import com.oneness.fdxmerchant.Models.DemoDataModels.OrderModel;
import com.oneness.fdxmerchant.Models.RestaurantDashboardModels.DashboardDataModel;
import com.oneness.fdxmerchant.Models.RestaurantDashboardModels.RestaurantDashboardResponseModel;
import com.oneness.fdxmerchant.Models.RestaurantDashboardModels.TodayOrderModel;
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

public class HomeFragment extends Fragment {

    RecyclerView orderRv;
    List<TodayOrderModel> todayOrderList = new ArrayList<>();
    TodayOrderAdapter todayAdapter;

    TextView tvRestNameHeader, tvTodayOrderCount, tvTodayCommission, tvTodayOrderAmount;
    Prefs prefs;
    ApiManager manager = new ApiManager();
    DialogView dialogView;
    TextView tvNewOrder, tvOngoingOrder, tvDeliveredOrder, tvCancelledOrder;
    int orderListSize = 0;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.home_fragment, container, false);

        prefs = new Prefs(getActivity());
        dialogView = new DialogView();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(NotificationManager.class);
            createNotificationChannel(notificationManager);
        }
        // If you are writting code in fragment

        orderRv = v.findViewById(R.id.orderRv);
        tvRestNameHeader = v.findViewById(R.id.tvRestNameHeader);
        tvNewOrder = v.findViewById(R.id.tvNewOrder);
        tvOngoingOrder = v.findViewById(R.id.tvOngoingOrder);
        tvDeliveredOrder = v.findViewById(R.id.tvDeliveredOrder);
        tvCancelledOrder = v.findViewById(R.id.tvCancelledOrder);
        tvTodayOrderCount = v.findViewById(R.id.tvTodayOrderCount);
        tvTodayCommission = v.findViewById(R.id.tvTodayCommission);
        tvTodayOrderAmount = v.findViewById(R.id.tvTodayOrderAmount);

        tvRestNameHeader.setText(prefs.getData(Constants.REST_NAME));

        if (Constants.orderSize == 0){
            if (prefs.getData(Constants.ORDER_SIZE) != null){
                if (!prefs.getData(Constants.ORDER_SIZE).equals("")){
                    String oSize = prefs.getData(Constants.ORDER_SIZE);
                    //int x= Integer.valueOf(oSize);
                    int x = Integer.parseInt(oSize);
                    Constants.orderSize = x;
                }

            }
        }

        //getOrders();

        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            public void run() {
                //
                // Do the stuff
                //
                try {
                    getDashboardData();

                } catch (Exception e) {

                }


                handler.postDelayed(this, 30000);
            }
        };
        runnable.run();



        /*todayAdapter = new TodayOrderAdapter(getActivity(), todayOrderList);
        orderRv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        orderRv.setAdapter(todayAdapter);*/

        return v;
    }

    private void createNotificationChannel(NotificationManager notificationManager) {
        Uri sound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getActivity().getPackageName() + "/" + R.raw.tone_test2); //Here is FILE_NAME is the name of file that you want to play
// Create the NotificationChannel, but only on API 26+ because
// the NotificationChannel class is new and not in the support library if

            CharSequence name = "Echannel";
            String description = "testing";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .build();
        NotificationChannel channel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            channel = new NotificationChannel("CHANNEL_1", name, importance);
            channel.setDescription(description);
            channel.enableLights(true); channel.enableVibration(true);
            channel.setSound(sound, audioAttributes);
            notificationManager.createNotificationChannel(channel);
        }


    }

    private void getDashboardData() {
        dialogView.showCustomSpinProgress(getActivity());
        manager.service.getDashboardData(prefs.getData(Constants.REST_ID)).enqueue(new Callback<RestaurantDashboardResponseModel>() {
            @Override
            public void onResponse(Call<RestaurantDashboardResponseModel> call, Response<RestaurantDashboardResponseModel> response) {
                if (response.isSuccessful()){
                    dialogView.dismissCustomSpinProgress();
                    RestaurantDashboardResponseModel rdrm = response.body();
                    if (!rdrm.error){
                        DashboardDataModel dashboardDataModel = rdrm.data;

                        todayOrderList = dashboardDataModel.todays_orders;
                        tvNewOrder.setText(dashboardDataModel.new_order_count);
                        tvOngoingOrder.setText(dashboardDataModel.ongoing_order_count);
                        tvDeliveredOrder.setText(dashboardDataModel.delivered_order_count);
                        tvCancelledOrder.setText(dashboardDataModel.cancelled_order_count);
                        tvTodayOrderAmount.setText(dashboardDataModel.todays_order_amount);
                        tvTodayCommission.setText(dashboardDataModel.todays_restaurant_commission);
                        tvTodayOrderCount.setText(dashboardDataModel.today_order_count);

                        orderListSize = todayOrderList.size();
                        if (orderListSize > Constants.orderSize){
                            //Constants.orderSize = orderListSize;
                            showNewOrderAlert(orderListSize);
                        }


                        if (todayOrderList.size()>0){
                            todayAdapter = new TodayOrderAdapter(getActivity(), todayOrderList);
                            orderRv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                            orderRv.setAdapter(todayAdapter);

                        }

                    }else{

                    }
                }else{
                    dialogView.dismissCustomSpinProgress();
                }
            }

            @Override
            public void onFailure(Call<RestaurantDashboardResponseModel> call, Throwable t) {
                dialogView.dismissCustomSpinProgress();

            }
        });
    }

    private void showNewOrderAlert(int orderListSize) {
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View promptView = layoutInflater.inflate(R.layout.inflate_custom_alert_dialog, null);
        //Constants.isDialogOn = 1;

        final AlertDialog alertD = new AlertDialog.Builder(getActivity()).create();
        TextView tvHeader=(TextView)promptView.findViewById(R.id.tvHeader);
        tvHeader.setText(getResources().getString(R.string.app_name));

        TextView tvMsg=(TextView)promptView.findViewById(R.id.tvMsg);
        tvMsg.setText(prefs.getData(Constants.REST_NAME) + ", new order received!");
        Button btnCancel = (Button) promptView.findViewById(R.id.btnCancel);
        btnCancel.setVisibility(View.GONE);
        btnCancel.setText("Cancel");
        Button btnOk = (Button) promptView.findViewById(R.id.btnOk);
        btnOk.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        btnOk.setText("Ok");

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                prefs.saveData(Constants.ORDER_SIZE, String.valueOf(orderListSize));

                Constants.orderSize = orderListSize;
                //Constants.isDialogOn = 0;
                alertD.dismiss();

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Constants.isDialogOn = 0;
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

}
