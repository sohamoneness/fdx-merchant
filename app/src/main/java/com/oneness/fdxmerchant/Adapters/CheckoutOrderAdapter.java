package com.oneness.fdxmerchant.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.oneness.fdxmerchant.Activity.Order.CheckOutActivity;
import com.oneness.fdxmerchant.Models.CartModels.CartDataModel;
import com.oneness.fdxmerchant.Models.DemoDataModels.CartItemModel;
import com.oneness.fdxmerchant.R;

import java.util.List;

public class CheckoutOrderAdapter extends RecyclerView.Adapter<CheckoutOrderAdapter.Hold> {

    List<CartDataModel> ciList;
    Context context;



    public CheckoutOrderAdapter(CheckOutActivity checkOutActivity, List<CartDataModel> cartItemList) {
        this.context = checkOutActivity;
        this.ciList = cartItemList;
    }

    @NonNull
    @Override
    public Hold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_checkout_row, parent, false);
        return new Hold(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull Hold holder, int position) {

        CartDataModel cim = ciList.get(position);

        holder.tvItemName.setText(cim.product_name);
        //holder.tvExtraTxt.setText(cim.);
       // holder.tvExtraTxt.setVisibility(View.GONE);
        holder.tvPrice.setText("\u20B9 " + cim.price);
        //if (cim.extra_price.equals("0")){
           // holder.tvExtraPrice.setVisibility(View.GONE);
        //}else{
          //  holder.tvExtraPrice.setVisibility(View.VISIBLE);
          //  holder.tvExtraPrice.setText("\u20B9 " + cim.extra_price);
        //}
        double totPrice = Double.parseDouble(cim.price) * Integer.parseInt(cim.quantity);

        holder.tvTotPrice.setText("\u20B9 " + String.valueOf(totPrice));
        holder.tvQty.setText(cim.quantity + " X ");
       // holder.ivMinus.setVisibility(View.GONE);
       // holder.ivPlus.setVisibility(View.GONE);

        /*holder.ivMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int q = Integer.parseInt(holder.tvQty.getText().toString());
                if (q > 1){
                    q = q-1;
                    holder.tvQty.setText(String.valueOf(q));
                }else{
                    Toast.makeText(context, "Can't reduce!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        holder.ivPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int q = Integer.parseInt(holder.tvQty.getText().toString());
                q = q+1;
                holder.tvQty.setText(String.valueOf(q));
            }
        });*/



    }

    @Override
    public int getItemCount() {
        return ciList.size();
    }

    public class Hold extends RecyclerView.ViewHolder {

        TextView tvItemName, tvExtraTxt, tvPrice, tvQty;
        TextView tvTotPrice, tvExtraPrice;
        ImageView ivMinus, ivPlus;

        public Hold(@NonNull View itemView) {
            super(itemView);

            tvItemName = itemView.findViewById(R.id.tvItemName);
            //tvExtraTxt = itemView.findViewById(R.id.tvExtraTxt);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvTotPrice = itemView.findViewById(R.id.tvTotPrice);
            ivMinus = itemView.findViewById(R.id.ivMinus);
            ivPlus = itemView.findViewById(R.id.ivPlus);
            tvQty = itemView.findViewById(R.id.tvQty);
            tvExtraPrice = itemView.findViewById(R.id.tvExtraPrice);

        }
    }
}
