package com.oneness.fdxmerchant.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.oneness.fdxmerchant.Activity.ReportManagement.OrderCountReport;
import com.oneness.fdxmerchant.Activity.ReportManagement.SalesReport;
import com.oneness.fdxmerchant.Models.ReportManagementModels.OrderCountDataModel;
import com.oneness.fdxmerchant.R;

import java.text.DecimalFormat;
import java.util.List;

public class OrderCountAdapter extends RecyclerView.Adapter<OrderCountAdapter.Hold> {

    Context context;
    List<OrderCountDataModel> ocdList;
    String Tag = "";

    public OrderCountAdapter(OrderCountReport orderCountReport, List<OrderCountDataModel> orderCountList) {
        this.context = orderCountReport;
        this.ocdList = orderCountList;
    }

    public OrderCountAdapter(SalesReport salesReport, List<OrderCountDataModel> orderCountList, String sale) {
        this.context = salesReport;
        this.ocdList = orderCountList;
        Tag = sale;
    }

    @NonNull
    @Override
    public OrderCountAdapter.Hold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_count_row, parent, false);
        return new Hold(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderCountAdapter.Hold holder, int position) {

        OrderCountDataModel ocdm = ocdList.get(position);

        holder.tvMonth.setText(ocdm.month);
        if (Tag.equals("sale")){
            DecimalFormat formatter = new DecimalFormat("#,##,###.00");
            String formattedDrawing = formatter.format(Double.parseDouble(ocdm.value));
            holder.tvValue.setText("₹" + formattedDrawing);
        }else {
            holder.tvValue.setText(ocdm.value);
        }


    }

    @Override
    public int getItemCount() {
        return ocdList.size();
    }

    public class Hold extends RecyclerView.ViewHolder {
        TextView tvMonth, tvValue;
        public Hold(@NonNull View itemView) {
            super(itemView);
            tvMonth = itemView.findViewById(R.id.tvMonth);
            tvValue = itemView.findViewById(R.id.tvValue);
        }
    }
}
