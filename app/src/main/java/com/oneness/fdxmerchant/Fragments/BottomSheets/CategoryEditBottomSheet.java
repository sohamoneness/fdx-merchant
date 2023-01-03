package com.oneness.fdxmerchant.Fragments.BottomSheets;

import static com.oneness.fdxmerchant.R.color.black;
import static com.oneness.fdxmerchant.R.color.white;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.oneness.fdxmerchant.Activity.CategoryManagement.AddCategory;
import com.oneness.fdxmerchant.Activity.EntryPoint.Dashboard;
import com.oneness.fdxmerchant.Activity.ItemManagement.AddNewItem;
import com.oneness.fdxmerchant.Activity.ItemManagement.ItemListActivity;
import com.oneness.fdxmerchant.Models.CategoryManagementModels.CategoryDeleteResponseModel;
import com.oneness.fdxmerchant.Models.ItemManagementModels.CategoryWithItemModel;
import com.oneness.fdxmerchant.Models.ItemManagementModels.DeleteItemResponseModel;
import com.oneness.fdxmerchant.Models.ItemManagementModels.ItemsModel;
import com.oneness.fdxmerchant.Network.ApiManager;
import com.oneness.fdxmerchant.Network.Constants;
import com.oneness.fdxmerchant.R;
import com.oneness.fdxmerchant.Utils.DialogView;
import com.oneness.fdxmerchant.Utils.Prefs;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryEditBottomSheet extends BottomSheetDialogFragment {

    TextView tvEdit, tvBlock, tvUnblock, tvDelete, tvView;

    LinearLayout btnLL;
    Button btnCancel, btnOk;
   // public static ItemsModel itemsModel;
    public static CategoryWithItemModel categoryWithItemModel;
    String isSelected = "";
    DialogView dialogView;
    Prefs prefs;
    ApiManager manager = new ApiManager();

    String catId = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.category_edit_bottom_sheet, container, false);

        dialogView = new DialogView();
        prefs = new Prefs(getActivity());

        tvEdit = v.findViewById(R.id.tvEdit);
        tvView = v.findViewById(R.id.tvView);
        tvBlock = v.findViewById(R.id.tvBlock);
        tvUnblock = v.findViewById(R.id.tvUnblock);
        tvDelete = v.findViewById(R.id.tvDelete);
        btnLL = v.findViewById(R.id.btnLL);
        btnCancel = v.findViewById(R.id.btnCancel);
        btnOk = v.findViewById(R.id.btnOk);

        tvView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //tvEdit.setBackground(getResources().getDrawable(R.drawable.rounded_corner_orange_bg));
                tvView.setBackground(getResources().getDrawable(R.drawable.rounded_corner_orange_bg));
                tvBlock.setBackgroundColor(getResources().getColor(white));
                tvUnblock.setBackgroundColor(getResources().getColor(white));
                tvDelete.setBackgroundColor(getResources().getColor(white));
               // tvView.setBackgroundColor(getResources().getColor(white));
                tvEdit.setBackgroundColor(getResources().getColor(white));

                tvEdit.setTextColor(getResources().getColor(black));
                tvBlock.setTextColor(getResources().getColor(black));
                tvUnblock.setTextColor(getResources().getColor(black));
                tvDelete.setTextColor(getResources().getColor(black));
                tvView.setTextColor(getResources().getColor(white));

                btnLL.setVisibility(View.VISIBLE);
                isSelected = "view";
            }
        });

        tvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvEdit.setBackground(getResources().getDrawable(R.drawable.rounded_corner_orange_bg));
                tvBlock.setBackgroundColor(getResources().getColor(white));
                tvUnblock.setBackgroundColor(getResources().getColor(white));
                tvDelete.setBackgroundColor(getResources().getColor(white));
                tvView.setBackgroundColor(getResources().getColor(white));

                tvEdit.setTextColor(getResources().getColor(white));
                tvBlock.setTextColor(getResources().getColor(black));
                tvUnblock.setTextColor(getResources().getColor(black));
                tvDelete.setTextColor(getResources().getColor(black));
                tvView.setTextColor(getResources().getColor(black));

                btnLL.setVisibility(View.VISIBLE);
                isSelected = "edit";

            }
        });

        tvBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tvEdit.setBackgroundColor(getResources().getColor(white));
                tvBlock.setBackground(getResources().getDrawable(R.drawable.rounded_corner_orange_bg));
                tvUnblock.setBackgroundColor(getResources().getColor(white));
                tvDelete.setBackgroundColor(getResources().getColor(white));

                tvEdit.setTextColor(getResources().getColor(black));
                tvBlock.setTextColor(getResources().getColor(white));
                tvUnblock.setTextColor(getResources().getColor(black));
                tvDelete.setTextColor(getResources().getColor(black));

                btnLL.setVisibility(View.VISIBLE);

            }
        });

        tvUnblock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvBlock.setBackgroundColor(getResources().getColor(white));
                tvUnblock.setBackground(getResources().getDrawable(R.drawable.rounded_corner_orange_bg));
                tvDelete.setBackgroundColor(getResources().getColor(white));
                tvEdit.setBackgroundColor(getResources().getColor(white));

                tvEdit.setTextColor(getResources().getColor(black));
                tvBlock.setTextColor(getResources().getColor(black));
                tvUnblock.setTextColor(getResources().getColor(white));
                tvDelete.setTextColor(getResources().getColor(black));

                btnLL.setVisibility(View.VISIBLE);

            }
        });

        tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvEdit.setBackgroundColor(getResources().getColor(white));
                tvBlock.setBackgroundColor(getResources().getColor(white));
                tvUnblock.setBackgroundColor(getResources().getColor(white));
                tvView.setBackgroundColor(getResources().getColor(white));
                tvDelete.setBackground(getResources().getDrawable(R.drawable.rounded_corner_orange_bg));

                tvEdit.setTextColor(getResources().getColor(black));
                tvBlock.setTextColor(getResources().getColor(black));
                tvUnblock.setTextColor(getResources().getColor(black));
                tvDelete.setTextColor(getResources().getColor(white));
                tvView.setTextColor(getResources().getColor(black));

                btnLL.setVisibility(View.VISIBLE);
                isSelected = "delete";

            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isSelected.equals("edit")){

                   // AddNewItem.itemModel = itemsModel;
                   // AddNewItem.isFrom = 1;
                    AddCategory.cwim = categoryWithItemModel;
                    AddCategory.isFor = "edit";
                    Intent i = new Intent(getActivity(), AddCategory.class);
                    startActivity(i);
                    dismiss();

                }else if (isSelected.equals("view")){
                    ItemListActivity.catName = categoryWithItemModel.title;
                    ItemListActivity.catID = categoryWithItemModel.id;
                    Intent i = new Intent(getActivity(), ItemListActivity.class);
                    //i.putExtra("catID", categoryWithItemModel.id);
                    Log.d("CAT_ID", categoryWithItemModel.id);
                    startActivity(i);
                    dismiss();

                }else {
                    deleteCategory(categoryWithItemModel.id);
                   // deleteItem(itemsModel.id);
                }

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });


        return v;
    }

    private void deleteCategory(String id) {
        manager.service.deleteCategory(id).enqueue(new Callback<CategoryDeleteResponseModel>() {
            @Override
            public void onResponse(Call<CategoryDeleteResponseModel> call, Response<CategoryDeleteResponseModel> response) {
                if (response.isSuccessful()){
                    CategoryDeleteResponseModel cdrm = response.body();
                    if (!cdrm.error){
                        Toast.makeText(getActivity(), "Category deleted successfully!", Toast.LENGTH_SHORT).show();
                        Constants.fromAddItem = true;
                        startActivity(new Intent(getActivity(), Dashboard.class));
                        dismiss();
                    }else{
                        Toast.makeText(getActivity(), "Category not deleted!", Toast.LENGTH_SHORT).show();
                    }
                }else{

                }
            }

            @Override
            public void onFailure(Call<CategoryDeleteResponseModel> call, Throwable t) {

            }
        });
    }

}
