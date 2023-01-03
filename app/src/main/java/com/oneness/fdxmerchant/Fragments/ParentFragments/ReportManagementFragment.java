package com.oneness.fdxmerchant.Fragments.ParentFragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.oneness.fdxmerchant.Activity.EntryPoint.Dashboard;
import com.oneness.fdxmerchant.Activity.ReportManagement.DateWiseOrderReport;
import com.oneness.fdxmerchant.Activity.ReportManagement.DateWiseTransactionReport;
import com.oneness.fdxmerchant.Activity.ReportManagement.ItemWiseReport;
import com.oneness.fdxmerchant.Activity.ReportManagement.OrderCountReport;
import com.oneness.fdxmerchant.Activity.ReportManagement.RestaurantPayouts;
import com.oneness.fdxmerchant.Activity.ReportManagement.SalesReport;
import com.oneness.fdxmerchant.Models.RestaurantDataModels.RestaurantDataModel;
import com.oneness.fdxmerchant.Network.Constants;
import com.oneness.fdxmerchant.R;
import com.oneness.fdxmerchant.Utils.Prefs;

public class ReportManagementFragment extends Fragment {

    TextView tvName, tvEmail, tvContact;
    RelativeLayout dateWiseOrderRL, dateWiseTransactionRL, itemWiseRL;
    RelativeLayout restPayRL, orderCountRL, salesReportRL;
    public static RestaurantDataModel restaurantDataModel;
    Prefs prefs;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.report_management_fragment, container, false);
        prefs = new Prefs(getActivity());

        tvName = v.findViewById(R.id.tvName);
        tvEmail = v.findViewById(R.id.tvEmail);
        tvContact = v.findViewById(R.id.tvContact);
        dateWiseOrderRL = v.findViewById(R.id.dateWiseOrderRL);
        dateWiseTransactionRL = v.findViewById(R.id.dateWiseTransactionRL);
        itemWiseRL = v.findViewById(R.id.itemWiseRL);
        restPayRL = v.findViewById(R.id.restPayRL);
        orderCountRL = v.findViewById(R.id.orderCountRL);
        salesReportRL = v.findViewById(R.id.salesReportRL);

        tvName.setText(prefs.getData(Constants.REST_NAME));
        tvEmail.setText(prefs.getData(Constants.REST_EMAIL));
        tvContact.setVisibility(View.GONE);

        dateWiseOrderRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), DateWiseOrderReport.class));

            }
        });

        dateWiseTransactionRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), DateWiseTransactionReport.class));
            }
        });

        itemWiseRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ItemWiseReport.class));

            }
        });

        restPayRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), RestaurantPayouts.class));
            }
        });

        salesReportRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SalesReport.class));
            }
        });

        orderCountRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), OrderCountReport.class));
            }
        });

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
}
