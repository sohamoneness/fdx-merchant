package com.oneness.fdxmerchant.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.oneness.fdxmerchant.Activity.ReportManagement.ItemWiseReport;
import com.oneness.fdxmerchant.Models.ReportManagementModels.ItemReportModel;
import com.oneness.fdxmerchant.R;

import java.text.DecimalFormat;
import java.util.List;

public class ItemReportAdapter extends RecyclerView.Adapter<ItemReportAdapter.Hold> {

    List<ItemReportModel> irList;
    Context context;

    public ItemReportAdapter(ItemWiseReport itemWiseReport, List<ItemReportModel> itemReportList) {
        this.context = itemWiseReport;
        this.irList = itemReportList;
    }

    @NonNull
    @Override
    public ItemReportAdapter.Hold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_report_row, parent, false);
        return new Hold(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemReportAdapter.Hold holder, int position) {

        ItemReportModel irm = irList.get(position);

        holder.tvName.setText(irm.name);
        holder.tvTotOrder.setText(irm.total_order_count);
        DecimalFormat formatter1 = new DecimalFormat("#,##,###.00");
        String formatted = formatter1.format(Double.parseDouble(irm.total_order_amount));
        holder.tvOrderAmount.setText("\u20B9 " + formatted);

    }

    @Override
    public int getItemCount() {
        return irList.size();
    }

    public class Hold extends RecyclerView.ViewHolder {
        TextView tvName, tvTotOrder, tvOrderAmount;
        public Hold(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvName);
            tvTotOrder = itemView.findViewById(R.id.tvTotOrder);
            tvOrderAmount = itemView.findViewById(R.id.tvOrderAmount);

        }
    }
}
