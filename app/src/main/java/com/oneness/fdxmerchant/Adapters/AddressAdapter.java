package com.oneness.fdxmerchant.Adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;


import com.oneness.fdxmerchant.Models.RestSearchModels.UserAddressModel;
import com.oneness.fdxmerchant.Network.Constants;
import com.oneness.fdxmerchant.R;
import com.oneness.fdxmerchant.Utils.DialogView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.Hold> {


    Context context;
    DialogView dialogView;
    int selectedPosition= 0;
    List<UserAddressModel> uaList;

    public AddressAdapter(FragmentActivity activity, List<UserAddressModel> userAddressList) {
        this.context = activity;
        this.uaList = userAddressList;
    }


    @NonNull
    @Override
    public Hold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_address_row, parent, false);
        return new Hold(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull Hold holder, @SuppressLint("RecyclerView") int position) {

        dialogView = new DialogView();

        UserAddressModel alm = uaList.get(position);

        holder.tvName.setText(Constants.userName);
        holder.tvAddressType.setText(alm.tag);
        holder.tvPhn.setText("Contact: " + Constants.userMobile);
        holder.tvAddress.setText(alm.location);

        /*holder.chkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.chkBox.isChecked()){
                    alm.flag = 1;
                }else{
                    alm.flag = 0;
                }
            }
        });*/

        if (position == selectedPosition) {
            holder.chkBox.setChecked(true);
            alm.flag = 1;
        } else {
            holder.chkBox.setChecked(false);
            alm.flag = 0;
        }

        // holder.checkBox.setOnClickListener(onStateChangedListener(chk_box, position));
        holder.chkBox.setOnClickListener(onStateChangedListener(holder.chkBox, position));



    }

    private View.OnClickListener onStateChangedListener(final CheckBox chk_box, final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chk_box.isChecked()) {
                    selectedPosition = position;
                } else {
                    selectedPosition = -1;
                }
                notifyDataSetChanged();
            }
        };
    }



    @Override
    public int getItemCount() {
        return uaList.size();
    }

    public class Hold extends RecyclerView.ViewHolder {

        TextView tvName, tvAddress, tvAddressType, tvPhn;
        CheckBox chkBox;

        public Hold(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tv_name);
            tvAddress = itemView.findViewById(R.id.tv_address);
            tvAddressType = itemView.findViewById(R.id.tv_address_type);
            tvPhn = itemView.findViewById(R.id.tv_phone);
            chkBox = itemView.findViewById(R.id.chkBox);

        }
    }
}
