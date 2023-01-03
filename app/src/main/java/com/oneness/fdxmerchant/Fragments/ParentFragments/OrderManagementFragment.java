package com.oneness.fdxmerchant.Fragments.ParentFragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.oneness.fdxmerchant.Activity.EntryPoint.Dashboard;
import com.oneness.fdxmerchant.Activity.Order.OrderActivity;
import com.oneness.fdxmerchant.Activity.Order.SearchActivity;
import com.oneness.fdxmerchant.Adapters.OrdersAdapter;
import com.oneness.fdxmerchant.Models.DemoDataModels.OrderModel;
import com.oneness.fdxmerchant.Network.Constants;
import com.oneness.fdxmerchant.R;
import com.oneness.fdxmerchant.Utils.TabLayoutAdapter;

import java.util.ArrayList;
import java.util.List;

public class OrderManagementFragment extends Fragment {
    List<OrderModel> orderModelList = new ArrayList<>();
    RecyclerView orderRv;
    //OrdersAdapter ordersAdapter;
    RelativeLayout createOrderRL;

    TabLayout tabLayout;
    ViewPager viewPager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.order_management_fragment, container, false);
       // orderRv = v.findViewById(R.id.orderRv);
        createOrderRL = v.findViewById(R.id.createOrderRL);
        tabLayout = v.findViewById(R.id.tab_layout);
        viewPager = v.findViewById(R.id.view_pager);

       // orderModelList = Constants.orderList;

        //ordersAdapter = new OrdersAdapter(getActivity(), orderModelList);
        //orderRv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        //orderRv.setAdapter(ordersAdapter);

        tabLayout.addTab(tabLayout.newTab().setText("New"));
        tabLayout.addTab(tabLayout.newTab().setText("Ongoing"));
        tabLayout.addTab(tabLayout.newTab().setText("Delivered"));
        tabLayout.addTab(tabLayout.newTab().setText("Cancelled"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        TabLayoutAdapter adapter=new TabLayoutAdapter(getFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){

            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                //viewPager.setCurrentItem(tab.getPosition());
                if(tabLayout.getSelectedTabPosition() == 0){
                    //Toast.makeText(getActivity(), "Tab 1" , Toast.LENGTH_SHORT).show();
                    viewPager.setCurrentItem(tab.getPosition());

                }else if(tabLayout.getSelectedTabPosition() == 1){
                    //Toast.makeText(getActivity(), "Tab 2" , Toast.LENGTH_SHORT).show();
                    viewPager.setCurrentItem(tab.getPosition());
                }else if(tabLayout.getSelectedTabPosition() == 2){
                   // Toast.makeText(getActivity(), "Tab 3" , Toast.LENGTH_SHORT).show();
                    viewPager.setCurrentItem(tab.getPosition());
                }else if(tabLayout.getSelectedTabPosition() == 3){
                   // Toast.makeText(getActivity(), "Tab 4" , Toast.LENGTH_SHORT).show();
                    viewPager.setCurrentItem(tab.getPosition());
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

                //viewPager.setCurrentItem(0);

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        createOrderRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), SearchActivity.class));
                /*Constants.isFrom = "Order";
                ItemManagementFragment itemManagementFragment = new ItemManagementFragment();
                Dashboard.pushFragmentsStatic(itemManagementFragment, true, null);*/
                //startActivity(new Intent(getActivity(), OrderActivity.class));
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

