package com.oneness.fdxmerchant.Fragments.BottomSheets;

import static com.oneness.fdxmerchant.R.color.black;
import static com.oneness.fdxmerchant.R.color.white;

import android.content.Intent;
import android.os.Bundle;
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
import com.oneness.fdxmerchant.Activity.ItemManagement.AddNewItem;
import com.oneness.fdxmerchant.Models.ItemManagementModels.DeleteItemResponseModel;
import com.oneness.fdxmerchant.Models.ItemManagementModels.ItemsModel;
import com.oneness.fdxmerchant.Network.ApiManager;
import com.oneness.fdxmerchant.R;
import com.oneness.fdxmerchant.Utils.DialogView;
import com.oneness.fdxmerchant.Utils.Prefs;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemEditBottomSheet extends BottomSheetDialogFragment {

    TextView tvEdit, tvBlock, tvUnblock, tvDelete;

    LinearLayout btnLL;
    Button btnCancel, btnOk;
    public static ItemsModel itemsModel;
    String isSelected = "";
    DialogView dialogView;
    Prefs prefs;
    ApiManager manager = new ApiManager();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.item_edit_bottom_sheet, container, false);

        dialogView = new DialogView();
        prefs = new Prefs(getActivity());

        tvEdit = v.findViewById(R.id.tvEdit);
        tvBlock = v.findViewById(R.id.tvBlock);
        tvUnblock = v.findViewById(R.id.tvUnblock);
        tvDelete = v.findViewById(R.id.tvDelete);
        btnLL = v.findViewById(R.id.btnLL);
        btnCancel = v.findViewById(R.id.btnCancel);
        btnOk = v.findViewById(R.id.btnOk);

        tvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvEdit.setBackground(getResources().getDrawable(R.drawable.rounded_corner_orange_bg));
                tvBlock.setBackgroundColor(getResources().getColor(white));
                tvUnblock.setBackgroundColor(getResources().getColor(white));
                tvDelete.setBackgroundColor(getResources().getColor(white));

                tvEdit.setTextColor(getResources().getColor(white));
                tvBlock.setTextColor(getResources().getColor(black));
                tvUnblock.setTextColor(getResources().getColor(black));
                tvDelete.setTextColor(getResources().getColor(black));

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
                tvDelete.setBackground(getResources().getDrawable(R.drawable.rounded_corner_orange_bg));

                tvEdit.setTextColor(getResources().getColor(black));
                tvBlock.setTextColor(getResources().getColor(black));
                tvUnblock.setTextColor(getResources().getColor(black));
                tvDelete.setTextColor(getResources().getColor(white));

                btnLL.setVisibility(View.VISIBLE);
                isSelected = "delete";

            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isSelected.equals("edit")){

                    AddNewItem.itemModel = itemsModel;
                    AddNewItem.isFrom = 1;
                    Intent i = new Intent(getActivity(), AddNewItem.class);
                    startActivity(i);
                    dismiss();
                    //getActivity().finish();

                }else {
                    deleteItem(itemsModel.id);
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

    private void deleteItem(String id) {
        dialogView.showCustomSpinProgress(getActivity());
        manager.service.deleteItem(id).enqueue(new Callback<DeleteItemResponseModel>() {
            @Override
            public void onResponse(Call<DeleteItemResponseModel> call, Response<DeleteItemResponseModel> response) {
                if (response.isSuccessful()){
                    dialogView.dismissCustomSpinProgress();
                    DeleteItemResponseModel dim = response.body();
                    if (!dim.error){
                        Toast.makeText(getActivity(), "Deleted successfully!", Toast.LENGTH_SHORT).show();
                        dismiss();
                    }else {

                    }
                }else {
                    dialogView.dismissCustomSpinProgress();
                }
            }

            @Override
            public void onFailure(Call<DeleteItemResponseModel> call, Throwable t) {
                dialogView.dismissCustomSpinProgress();

            }
        });

    }
}
