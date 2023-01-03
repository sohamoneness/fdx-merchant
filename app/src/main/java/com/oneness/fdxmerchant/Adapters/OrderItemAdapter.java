package com.oneness.fdxmerchant.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.oneness.fdxmerchant.Models.OrderModels.OrderItemsModel;
import com.oneness.fdxmerchant.R;

import java.text.DecimalFormat;
import java.util.List;

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.Hold> {

    Context mContext;
    List<OrderItemsModel> itemList;
    String from = "";

    public OrderItemAdapter(Context context, List<OrderItemsModel> orderItemModelList) {

        this.mContext = context;
        this.itemList = orderItemModelList;

    }




    @NonNull
    @Override
    public Hold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item_row, parent, false);
        return new Hold(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull Hold holder, int position) {

        OrderItemsModel oim = itemList.get(position);

        holder.tvItemName.setText(oim.product_name);
        holder.tvExtra.setText("X " + oim.quantity);
        //holder.tvExtra.setVisibility(View.GONE);
        DecimalFormat formatter1 = new DecimalFormat("#,##,###.00");
        String formatted = formatter1.format(Double.parseDouble(oim.price));
        holder.tvAmount.setText("\u20B9" + " " + formatted);
       /* if (oim.isVeg == true){
            holder.ivVeg.setVisibility(View.VISIBLE);
            holder.ivNonVeg.setVisibility(View.GONE);
        }else{
            holder.ivVeg.setVisibility(View.GONE);
            holder.ivNonVeg.setVisibility(View.VISIBLE);
        }*/

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class Hold extends RecyclerView.ViewHolder {

        ImageView ivVeg, ivNonVeg;
        TextView tvItemName, tvExtra, tvAmount;

        public Hold(@NonNull View itemView) {
            super(itemView);

            ivVeg = itemView.findViewById(R.id.iv_veg);
            ivNonVeg = itemView.findViewById(R.id.iv_non_veg);
            tvItemName = itemView.findViewById(R.id.tv_food_name);
            tvExtra = itemView.findViewById(R.id.tv_extra);
            tvAmount = itemView.findViewById(R.id.tv_amount);

        }
    }
}
