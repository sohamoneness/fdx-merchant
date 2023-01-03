package com.oneness.fdxmerchant.Fragments.BottomSheets;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.oneness.fdxmerchant.Adapters.AddOnItemAdapter;
import com.oneness.fdxmerchant.Models.DemoDataModels.AddOnItemModel;
import com.oneness.fdxmerchant.Network.Constants;
import com.oneness.fdxmerchant.R;

import java.util.ArrayList;
import java.util.List;

public class AddItemBottomSheet extends BottomSheetDialogFragment {

    RecyclerView addOnRv;
    List<AddOnItemModel> addOnItemList = new ArrayList<>();
    List<AddOnItemModel> tempAddOnItemList = new ArrayList<>();
    AddOnItemAdapter addOnItemAdapter;
    public static String itemPrice;
    int price = 0;

    ImageView ivMinus, ivPlus;
    TextView tvQty;
    TextView tvAddItem;
    int totAddOnPrice = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.additem_bottomsheet, container, false);

        addOnRv = v.findViewById(R.id.addOnRv);
        ivMinus = v.findViewById(R.id.ivMinus);
        ivPlus = v.findViewById(R.id.ivPlus);
        tvQty = v.findViewById(R.id.tvQty);
        tvAddItem = v.findViewById(R.id.tvAddItem);

        getAddOnItems();


        price = Integer.parseInt(itemPrice);

        addOnItemAdapter = new AddOnItemAdapter(getActivity(), addOnItemList);
        addOnRv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        addOnRv.setAdapter(addOnItemAdapter);

        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            public void run() {
                //
                // Do the stuff
                //
                Log.d("TAG>>", Constants.TAG+"");
                totAddOnPrice = 0;
                for (int i = 0; i < addOnItemList.size(); i++){
                    if (addOnItemList.get(i).add_on_tag == 1){
                        int p = Integer.parseInt(addOnItemList.get(i).add_on_price);
                        totAddOnPrice = totAddOnPrice + p;
                        //addOnItemList.get(i).add_on_tag = 0;

                    }
                }
                if (totAddOnPrice != 0){
                    price = price +(Integer.parseInt(tvQty.getText().toString()) * totAddOnPrice);
                }

                tvAddItem.setText("Add item ₹ " + price);

                handler.postDelayed(this, 1000);
            }
        };
        runnable.run();



        ivMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int qnty = Integer.parseInt(tvQty.getText().toString());

                if (qnty>1){
                    qnty = qnty - 1;
                    tvQty.setText(String.valueOf(qnty));
                }else {
                    dismiss();
                }

            }
        });

        ivPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int qnty = Integer.parseInt(tvQty.getText().toString());

                qnty = qnty + 1;

                tvQty.setText(String.valueOf(qnty));

            }
        });

        tvAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Constants.TAG = Integer.parseInt(tvQty.getText().toString());

            }
        });


        return v;
    }

    private void getAddOnItems() {
        AddOnItemModel aom = new AddOnItemModel();
        aom.type = "non-veg";
        aom.add_on_item = "Extra chicken";
        aom.add_on_price = "49";
        aom.id = "ao1";
        addOnItemList.add(aom);

        AddOnItemModel aom1 = new AddOnItemModel();
        aom1.type = "non-veg";
        aom1.add_on_item = "Extra egg";
        aom1.add_on_price = "10";
        aom1.id = "ao2";
        addOnItemList.add(aom1);

        AddOnItemModel aom2 = new AddOnItemModel();
        aom2.type = "veg";
        aom2.add_on_item = "Curd";
        aom2.add_on_price = "49";
        aom2.id = "ao3";
        addOnItemList.add(aom2);

        AddOnItemModel aom3 = new AddOnItemModel();
        aom3.type = "veg";
        aom3.add_on_item = "Green salad";
        aom3.add_on_price = "30";
        aom3.id = "ao4";
        addOnItemList.add(aom3);

        AddOnItemModel aom4 = new AddOnItemModel();
        aom4.type = "non-veg";
        aom4.add_on_item = "Onion salad";
        aom4.add_on_price = "40";
        aom4.id = "ao5";
        addOnItemList.add(aom4);
    }
}
