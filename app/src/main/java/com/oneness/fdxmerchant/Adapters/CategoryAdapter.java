package com.oneness.fdxmerchant.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.oneness.fdxmerchant.Models.DemoDataModels.CategoryModel;
import com.oneness.fdxmerchant.Models.ItemManagementModels.CategoryListModel;
import com.oneness.fdxmerchant.Models.ItemManagementModels.CategoryWithItemModel;
import com.oneness.fdxmerchant.R;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.Hold> {

    List<CategoryWithItemModel> cList;
    Context context;



    public CategoryAdapter(FragmentActivity activity, List<CategoryWithItemModel> catList) {
        this.cList = catList;
        this.context = activity;
    }


    @NonNull
    @Override
    public CategoryAdapter.Hold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item_row, parent, false);
        return new Hold(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.Hold holder, int position) {

        CategoryWithItemModel cm = cList.get(position);

        holder.tvCatName.setText(cm.title);
        if (cm.image.equals("")){
            Glide.with(context).load(R.drawable.no_image).into(holder.ivCat);
        }else {
            Glide.with(context).load(cm.image).into(holder.ivCat);
        }

    }

    @Override
    public int getItemCount() {
        return cList.size();
    }

    public class Hold extends RecyclerView.ViewHolder {

        TextView tvCatName;
        ImageView ivCat;

        public Hold(@NonNull View itemView) {
            super(itemView);

            tvCatName = itemView.findViewById(R.id.tvCatName);
            ivCat = itemView.findViewById(R.id.ivCat);

        }
    }
}
