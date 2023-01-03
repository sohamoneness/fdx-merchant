package com.oneness.fdxmerchant.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.oneness.fdxmerchant.Activity.ReportManagement.DateWiseTransactionReport;
import com.oneness.fdxmerchant.Models.ReportManagementModels.DateWiseOrderReportModel;
import com.oneness.fdxmerchant.Models.ReportManagementModels.DateWiseTransactionReportModel;
import com.oneness.fdxmerchant.R;

import java.text.DecimalFormat;
import java.util.List;

public class TransactionReportAdapter extends RecyclerView.Adapter<TransactionReportAdapter.Hold> {

    List<DateWiseTransactionReportModel> trList;
    Context context;

    public TransactionReportAdapter(DateWiseTransactionReport dateWiseTransactionReport, List<DateWiseTransactionReportModel> transReportList) {
        this.context = dateWiseTransactionReport;
        this.trList = transReportList;
    }

    @NonNull
    @Override
    public TransactionReportAdapter.Hold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_report_row, parent, false);
        return new Hold(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionReportAdapter.Hold holder, int position) {

        DateWiseTransactionReportModel dworm = trList.get(position);

        holder.tvId.setText(dworm.unique_id);
        holder.tvTransId.setText(dworm.transaction_id);
        holder.tvName.setText(dworm.name);

        DecimalFormat formatter1 = new DecimalFormat("#,##,###.00");
        String formatted = formatter1.format(Double.parseDouble(dworm.total_amount));
        holder.tvAmount.setText("\u20B9 " + formatted);

        if (dworm.payment_status.equals("0")){
        holder.tvStatus.setBackground(context.getResources().getDrawable(R.drawable.cancel_order_stat_bg));
        holder.tvStatus.setText("Pending");
        }else {
            holder.tvStatus.setBackground(context.getResources().getDrawable(R.drawable.status_bg));
            holder.tvStatus.setText("Paid");
        }



    }

    @Override
    public int getItemCount() {
        return trList.size();
    }

    public class Hold extends RecyclerView.ViewHolder {

        TextView tvId, tvTransId, tvName, tvAmount, tvStatus;

        public Hold(@NonNull View itemView) {
            super(itemView);

            tvId = itemView.findViewById(R.id.tvId);
            tvTransId = itemView.findViewById(R.id.tvTransId);
            tvName = itemView.findViewById(R.id.tvName);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            tvStatus = itemView.findViewById(R.id.tvStatus);
        }
    }
}
