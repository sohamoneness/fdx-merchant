package com.oneness.fdxmerchant.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.oneness.fdxmerchant.Models.CouponModels.CouponListModel;
import com.oneness.fdxmerchant.R;

import java.util.List;

public class CouponListAdapter extends RecyclerView.Adapter<CouponListAdapter.Hold> {

    Context context;
    List<CouponListModel> cList;

    public CouponListAdapter(FragmentActivity activity, List<CouponListModel> couponList) {
        this.cList = couponList;
        this.context = activity;
    }

    @NonNull
    @Override
    public Hold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.coupon_list_row, parent, false);
        return new Hold(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull Hold holder, int position) {

        CouponListModel clm = cList.get(position);

        holder.tvTitle.setText(clm.title);
        holder.tvDesc.setText(clm.description);
        holder.tvDuration.setText(clm.start_date + " to " + clm.end_date);
        holder.tvCode.setText(clm.code);


    }

    @Override
    public int getItemCount() {
        return cList.size();
    }

    public class Hold extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDesc, tvCode;
        TextView tvDuration;
        public Hold(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDesc = itemView.findViewById(R.id.tvDesc);
            tvCode = itemView.findViewById(R.id.tvCouponCode);
            tvDuration = itemView.findViewById(R.id.tvDuration);
        }
    }
}
