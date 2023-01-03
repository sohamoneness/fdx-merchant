package com.oneness.fdxmerchant.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.oneness.fdxmerchant.Activity.ReportManagement.RestaurantPayouts;
import com.oneness.fdxmerchant.Models.ReportManagementModels.PayoutDataModel;
import com.oneness.fdxmerchant.R;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PayoutsAdapter extends RecyclerView.Adapter<PayoutsAdapter.Hold> {
    List<PayoutDataModel> pdmList;
    Context context;

    public PayoutsAdapter(RestaurantPayouts restaurantPayouts, List<PayoutDataModel> payoutList) {
        this.context = restaurantPayouts;
        this.pdmList = payoutList;
    }

    @NonNull
    @Override
    public PayoutsAdapter.Hold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.payout_list_row, parent, false);
        return new Hold(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull PayoutsAdapter.Hold holder, int position) {

        PayoutDataModel pdm = pdmList.get(position);

        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(pdm.created_at);
            String newstring = new SimpleDateFormat("dd MMM, yyyy").format(date);
            holder.tvDate.setText(newstring);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //holder.tvDate.setText(pdm.created_at);
        DecimalFormat formatter = new DecimalFormat("#,##,###.00");
        String formatted = formatter.format(Double.parseDouble(pdm.amount));
        holder.tvAmount.setText("₹"+formatted);
        holder.tvNote.setText(pdm.note);

    }

    @Override
    public int getItemCount() {
        return pdmList.size();
    }

    public class Hold extends RecyclerView.ViewHolder {
        TextView tvDate, tvAmount, tvNote;
        public Hold(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            tvNote = itemView.findViewById(R.id.tvNote);
        }
    }
}
