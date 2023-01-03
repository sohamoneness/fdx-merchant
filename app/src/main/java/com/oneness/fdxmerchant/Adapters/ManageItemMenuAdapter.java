package com.oneness.fdxmerchant.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.oneness.fdxmerchant.Activity.ItemManagement.ItemListActivity;
import com.oneness.fdxmerchant.Models.DemoDataModels.CompleteMealModel;
import com.oneness.fdxmerchant.Models.ItemManagementModels.ItemsModel;
import com.oneness.fdxmerchant.R;

import java.util.List;

public class ManageItemMenuAdapter extends RecyclerView.Adapter<ManageItemMenuAdapter.Hold> {

    List<ItemsModel> cmList;
    Context context;


    public ManageItemMenuAdapter(ItemListActivity itemListActivity, List<ItemsModel> itemList) {
        this.context = itemListActivity;
        this.cmList = itemList;
    }


    @NonNull
    @Override
    public ManageItemMenuAdapter.Hold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.manage_item_menu_row, parent, false);
        return new Hold(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull ManageItemMenuAdapter.Hold holder, int position) {

        ItemsModel cmm = cmList.get(position);
        holder.tvItemName.setText(cmm.name);
        holder.tvExtraTxt.setText(cmm.description);
        holder.tvPrice.setText("₹ " + cmm.price);
        if (cmm.image != null){
            if (cmm.image.equals("")){
                Glide.with(context).load(R.drawable.no_image).into(holder.ivFood);
            }else {
                Glide.with(context).load(cmm.image).into(holder.ivFood);
            }
        }else {
            Glide.with(context).load(R.drawable.no_image).into(holder.ivFood);
        }



    }

    @Override
    public int getItemCount() {
        return cmList.size();
    }

    public class Hold extends RecyclerView.ViewHolder {

        TextView tvItemName,tvPrice, tvExtraTxt;
        ShapeableImageView ivFood;

        public Hold(@NonNull View itemView) {
            super(itemView);

            tvItemName = itemView.findViewById(R.id.tvItemName);
            tvExtraTxt = itemView.findViewById(R.id.tvExtraTxt);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            ivFood = itemView.findViewById(R.id.ivFood);

        }
    }
}
