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
import com.google.android.material.imageview.ShapeableImageView;
import com.oneness.fdxmerchant.Activity.ItemManagement.ItemListActivity;
import com.oneness.fdxmerchant.Fragments.BottomSheets.CategoryEditBottomSheet;
import com.oneness.fdxmerchant.Fragments.BottomSheets.ItemEditBottomSheet;
import com.oneness.fdxmerchant.Models.DemoDataModels.CategoryModel;
import com.oneness.fdxmerchant.Models.ItemManagementModels.CategoryListModel;
import com.oneness.fdxmerchant.Models.ItemManagementModels.CategoryWithItemModel;
import com.oneness.fdxmerchant.R;

import java.util.List;

public class CategoryManageAdapter extends RecyclerView.Adapter<CategoryManageAdapter.Hold> {

    List<CategoryWithItemModel> cList;
    Context context;

    public CategoryManageAdapter(FragmentActivity activity, List<CategoryWithItemModel> catList) {
        this.cList = catList;
        this.context = activity;
    }


    @NonNull
    @Override
    public CategoryManageAdapter.Hold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_management_row, parent, false);
        return new Hold(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryManageAdapter.Hold holder, int position) {

        CategoryWithItemModel clm = cList.get(position);

        if (clm.image != null){
            if (!clm.image.equals("")){
                Glide.with(context).load(clm.image).into(holder.ivCategory);
            }else {
                Glide.with(context).load(R.drawable.no_image).into(holder.ivCategory);
            }
        }else {
            Glide.with(context).load(R.drawable.no_image).into(holder.ivCategory);
        }

        holder.tvCatName.setText(clm.title);
        holder.tvCatDes.setText(clm.description);
        holder.ivMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CategoryEditBottomSheet.categoryWithItemModel = clm;
                CategoryEditBottomSheet categoryEditBottomSheet = new CategoryEditBottomSheet();
                categoryEditBottomSheet.show(((FragmentActivity)context).getSupportFragmentManager(), "callAddOn");
            }
        });


    }

    @Override
    public int getItemCount() {
        return cList.size();
    }

    public class Hold extends RecyclerView.ViewHolder {
        ShapeableImageView ivCategory;
        TextView tvCatName, tvCatDes;
        ImageView ivMore;
        public Hold(@NonNull View itemView) {
            super(itemView);

            ivCategory = itemView.findViewById(R.id.ivCategory);
            tvCatName = itemView.findViewById(R.id.tvCatName);
            tvCatDes = itemView.findViewById(R.id.tvCatDes);
            ivMore = itemView.findViewById(R.id.ivMore);

        }
    }
}
