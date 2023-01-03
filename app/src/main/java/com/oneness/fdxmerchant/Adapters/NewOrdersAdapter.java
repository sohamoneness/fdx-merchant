package com.oneness.fdxmerchant.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.oneness.fdxmerchant.Activity.Order.OrderDetails;
import com.oneness.fdxmerchant.Models.DemoDataModels.OrderModel;
import com.oneness.fdxmerchant.Models.OrderModels.NewOrdersModel;
import com.oneness.fdxmerchant.Network.Constants;
import com.oneness.fdxmerchant.R;

import java.util.List;

public class NewOrdersAdapter extends RecyclerView.Adapter<NewOrdersAdapter.Hold> {

    List<NewOrdersModel> ordList;
    Context context;

    public NewOrdersAdapter(FragmentActivity activity, List<NewOrdersModel> newOrderList) {
        this.context = activity;
        this.ordList = newOrderList;
    }


    @NonNull
    @Override
    public NewOrdersAdapter.Hold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.today_order_row, parent, false);
        return new Hold(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull NewOrdersAdapter.Hold holder, int position) {

        NewOrdersModel om = ordList.get(position);
        holder.tvId.setText(om.unique_id);
        holder.tvName.setText(om.name);

        holder.tvStatus.setVisibility(View.VISIBLE);
        holder.tvStatus.setBackground(context.getResources().getDrawable(R.drawable.new_status_bg));
        holder.tvStatus.setText("New");

        holder.tvTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OrderDetails.ordId = om.id;
                Constants.isNewOrder = 1;
                context.startActivity(new Intent(context.getApplicationContext(), OrderDetails.class));
            }
        });
        //holder.tvTrack.setText("Track");
       // holder.tvTrack.setBackground(context.getResources().getDrawable(R.drawable.track_bg));
        /*holder.tvStatus.setText(om.ord_status);
        if (om.ord_status.equals("New")){
            holder.tvStatus.setVisibility(View.VISIBLE);
            holder.tvStatus.setBackground(context.getResources().getDrawable(R.drawable.new_status_bg));
            holder.tvTrack.setText("Track");
            holder.tvTrack.setBackground(context.getResources().getDrawable(R.drawable.track_bg));
        }else if(om.ord_status.equals("Ongoing")){
            holder.tvStatus.setVisibility(View.VISIBLE);
            holder.tvStatus.setBackground(context.getResources().getDrawable(R.drawable.status_bg));
            holder.tvTrack.setText("Track");
            holder.tvTrack.setBackground(context.getResources().getDrawable(R.drawable.track_bg));
        }else if(om.ord_status.equals("Delivered")){
            holder.tvStatus.setVisibility(View.GONE);
            holder.tvTrack.setText(om.ord_status);
            holder.tvTrack.setBackground(context.getResources().getDrawable(R.drawable.delivered_bg));
        }*/


    }

    @Override
    public int getItemCount() {
        return ordList.size();
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
