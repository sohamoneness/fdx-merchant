package com.oneness.fdxmerchant.Fragments.BottomSheets;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.oneness.fdxmerchant.Activity.ItemManagement.ItemListActivity;
import com.oneness.fdxmerchant.Activity.Order.CheckOutActivity;
import com.oneness.fdxmerchant.Adapters.AddressAdapter;
import com.oneness.fdxmerchant.Models.ItemManagementModels.ItemsModel;
import com.oneness.fdxmerchant.Models.RestSearchModels.UserAddressModel;
import com.oneness.fdxmerchant.Network.Constants;
import com.oneness.fdxmerchant.R;
import com.oneness.fdxmerchant.Utils.RecyclerItemClickListener;

public class UserAddressListBottomSheet extends BottomSheetDialogFragment {

    RecyclerView addressRv;
    LinearLayout btnLL;
    Button btnCancel, btnOk;
    AddressAdapter addressAdapter;
    UserAddressModel userAddressModel;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.user_address_bottom_sheet_lay, container, false);

        addressRv = v.findViewById(R.id.addressRv);
        btnLL = v.findViewById(R.id.btnLL);
        btnCancel = v.findViewById(R.id.btnCancel);
        btnOk = v.findViewById(R.id.btnOk);

        addressAdapter = new AddressAdapter(getActivity(), Constants.userAddressList);
        addressRv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        addressRv.setAdapter(addressAdapter);



        addressRv.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), addressRv, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));


        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //CheckOutActivity.cartItemsResponseModel = Constants.cartItemsResponseModel;
                for(int i = 0; i < Constants.userAddressList.size(); i++){
                    if (Constants.userAddressList.get(i).flag == 1){
                        userAddressModel = Constants.userAddressList.get(i);
                    }
                }
                Constants.userAddressModel = userAddressModel;
                Constants.addressSelected = true;
                dismiss();
                //startActivity(new Intent(AddressList.this, CheckOutActivity.class).putExtra(Constants.REST_ID, restId));

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });


        return v;
    }

}
