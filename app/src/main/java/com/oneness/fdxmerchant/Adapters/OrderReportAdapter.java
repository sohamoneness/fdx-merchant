package com.oneness.fdxmerchant.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.oneness.fdxmerchant.Activity.Order.OrderDetails;
import com.oneness.fdxmerchant.Activity.ReportManagement.DateWiseOrderReport;
import com.oneness.fdxmerchant.Models.ReportManagementModels.DateWiseOrderReportModel;
import com.oneness.fdxmerchant.Models.RestaurantDashboardModels.TodayOrderModel;
import com.oneness.fdxmerchant.Network.Constants;
import com.oneness.fdxmerchant.R;

import java.util.List;

public class OrderReportAdapter extends RecyclerView.Adapter<OrderReportAdapter.Hold> {

    List<DateWiseOrderReportModel> orList;
    Context context;

    public OrderReportAdapter(DateWiseOrderReport dateWiseOrderReport, List<DateWiseOrderReportModel> orderReportList) {
        this.orList = orderReportList;
        this.context = dateWiseOrderReport;
    }

    @NonNull
    @Override
    public OrderReportAdapter.Hold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_report_row, parent, false);
        return new Hold(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderReportAdapter.Hold holder, int position) {

        DateWiseOrderReportModel om = orList.get(position);
        holder.tvId.setText(om.unique_id);
        holder.tvName.setText(om.name);
        //holder.tvStatus.setText(om.ord_status);
        if (om.status.equals("1")){
            holder.tvStatus.setVisibility(View.VISIBLE);
            holder.tvStatus.setBackground(context.getResources().getDrawable(R.drawable.new_status_bg));
            holder.tvStatus.setText("New");
        }else if (om.status.equals("8") || om.status.equals("9")){
            holder.tvStatus.setVisibility(View.VISIBLE);
            holder.tvStatus.setBackground(context.getResources().getDrawable(R.drawable.delivered_bg));
            holder.tvStatus.setText("Delivered");
        }else if (om.status.equals("10")){
            holder.tvStatus.setVisibility(View.VISIBLE);
            holder.tvStatus.setBackground(context.getResources().getDrawable(R.drawable.cancel_order_stat_bg));
            holder.tvStatus.setText("Cancelled");
        }else {
            holder.tvStatus.setVisibility(View.VISIBLE);
            holder.tvStatus.setBackground(context.getResources().getDrawable(R.drawable.status_bg));
            holder.tvStatus.setText("Ongoing");
        }

        holder.tvTrack.setVisibility(View.GONE);


        holder.tvTrack.setText("Details");
        holder.tvTrack.setBackground(context.getResources().getDrawable(R.drawable.track_bg));

        holder.tvTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OrderDetails.ordId = om.id;
                if (om.status.equals("1")) {
                    Constants.isNewOrder = 1;
                }else{
                    Constants.isNewOrder = 0;
                }
                context.startActivity(new Intent(context.getApplicationContext(), OrderDetails.class));
            }
        });

    }

    @Override
    public int getItemCount() {
        return orList.size();
    }

    public class Hold extends RecyclerView.ViewHolder {
        TextView tvId, tvName, tvStatus, tvTrack;

        public Hold(@NonNull View itemView) {
            super(itemView);

            tvId = itemView.findViewById(R.id.tvId);
            tvName = itemView.findViewById(R.id.tvName);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvTrack = itemView.findViewById(R.id.tvTrack);
        }
    }
}
