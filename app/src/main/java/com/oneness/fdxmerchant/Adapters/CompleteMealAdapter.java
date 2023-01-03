package com.oneness.fdxmerchant.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.oneness.fdxmerchant.Activity.Order.CartActivity;
import com.oneness.fdxmerchant.Models.DemoDataModels.CompleteMealModel;
import com.oneness.fdxmerchant.R;

import java.util.List;

public class CompleteMealAdapter extends RecyclerView.Adapter<CompleteMealAdapter.Hold> {

    List<CompleteMealModel> cmList;
    Context context;

    public CompleteMealAdapter(CartActivity cartActivity, List<CompleteMealModel> completeMealList) {
        this.cmList = completeMealList;
        this.context = cartActivity;
    }


    @NonNull
    @Override
    public Hold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.complete_meal_row, parent, false);
        return new Hold(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull Hold holder, int position) {

        CompleteMealModel cmm = cmList.get(position);
        holder.tvItem.setText(cmm.item);
        holder.tvPrice.setText("₹ " + cmm.price);

    }

    @Override
    public int getItemCount() {
        return cmList.size();
    }

    public class Hold extends RecyclerView.ViewHolder {

        TextView tvItem,tvPrice;

        public Hold(@NonNull View itemView) {
            super(itemView);

           tvItem = itemView.findViewById(R.id.tvItem);
           tvPrice = itemView.findViewById(R.id.tvPrice);

        }
    }
}
