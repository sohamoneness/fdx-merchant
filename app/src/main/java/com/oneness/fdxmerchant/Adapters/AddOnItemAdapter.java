package com.oneness.fdxmerchant.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.oneness.fdxmerchant.Models.DemoDataModels.AddOnItemModel;
import com.oneness.fdxmerchant.R;

import java.util.List;

public class AddOnItemAdapter extends RecyclerView.Adapter<AddOnItemAdapter.Hold> {

    List<AddOnItemModel> aoList;
    Context context;

    public AddOnItemAdapter(FragmentActivity activity, List<AddOnItemModel> addOnItemList) {
        this.context = activity;
        this.aoList = addOnItemList;
    }

    @NonNull
    @Override
    public Hold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_on_menu_row, parent, false);
        return new Hold(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull Hold holder, int position) {

        AddOnItemModel aoM = aoList.get(position);

        holder.tvAddOnItem.setText(aoM.add_on_item);
        holder.tvAddOnPrice.setText("\u20B9 "+aoM.add_on_price);

        if (aoM.type.equals("non-veg")){
            holder.ivType.setImageDrawable(context.getDrawable(R.drawable.non_veg));
        }else{
            holder.ivType.setImageDrawable(context.getDrawable(R.drawable.veg));
        }


        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean checked = ((CheckBox) view).isChecked();
                if (checked){
                    holder.checkBox.setChecked(true);
                    aoM.add_on_tag = 1;
                }else{
                    holder.checkBox.setChecked(false);
                    aoM.add_on_tag = 0;
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return aoList.size();
    }

    public class Hold extends RecyclerView.ViewHolder {

        CheckBox checkBox;
        TextView tvAddOnItem, tvAddOnPrice;
        ImageView ivType;

        public Hold(@NonNull View itemView) {
            super(itemView);

            checkBox = itemView.findViewById(R.id.checkbox);
            tvAddOnItem = itemView.findViewById(R.id.tvAddOnItem);
            tvAddOnPrice = itemView.findViewById(R.id.tvAddOnPrice);
            ivType = itemView.findViewById(R.id.ivType);

        }
    }
}
