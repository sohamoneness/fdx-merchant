package com.oneness.fdxmerchant.Activity.CategoryManagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.oneness.fdxmerchant.Activity.EntryPoint.Dashboard;
import com.oneness.fdxmerchant.Activity.ItemManagement.AddNewItem;
import com.oneness.fdxmerchant.Models.CategoryManagementModels.CategoryAddRequestModel;
import com.oneness.fdxmerchant.Models.CategoryManagementModels.CategoryAddResponseModel;
import com.oneness.fdxmerchant.Models.CategoryManagementModels.CategoryUpdateRequestModel;
import com.oneness.fdxmerchant.Models.CategoryManagementModels.CategoryUpdateResponseModel;
import com.oneness.fdxmerchant.Models.ItemManagementModels.CategoryWithItemModel;
import com.oneness.fdxmerchant.Network.ApiManager;
import com.oneness.fdxmerchant.Network.Constants;
import com.oneness.fdxmerchant.R;
import com.oneness.fdxmerchant.Utils.DialogView;
import com.oneness.fdxmerchant.Utils.Prefs;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddCategory extends AppCompatActivity {

    ImageView iv_back;
    EditText etCatName, etCatDes;
    Button btnAdd;

    Prefs prefs;
    ApiManager manager = new ApiManager();
    DialogView dialogView;

    public static CategoryWithItemModel cwim;
    public static String isFor="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);

        prefs = new Prefs(AddCategory.this);
        dialogView = new DialogView();

        iv_back = findViewById(R.id.iv_back);
        etCatName =findViewById(R.id.etCatName);
        etCatDes =findViewById(R.id.etCatDes);
        btnAdd =findViewById(R.id.btnAdd);
        btnAdd.setText("Add");


        if (isFor.equals("edit")){
            etCatName.setText(cwim.title);
            etCatDes.setText(cwim.description);
            btnAdd.setText("Update");
        }else {
            btnAdd.setText("Add");
        }


        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //onBackPressed();
                finish();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isFor.equals("edit")){
                    if (isBlankValidate()){
                        CategoryUpdateRequestModel categoryUpdateRequestModel = new CategoryUpdateRequestModel(
                                cwim.id,
                                etCatName.getText().toString(),
                                etCatDes.getText().toString()
                        );

                        updateCategory(categoryUpdateRequestModel);

                    }

                }else{
                    if (isBlankValidate()){

                        CategoryAddRequestModel addRequestModel = new CategoryAddRequestModel(
                                prefs.getData(Constants.REST_ID),
                                etCatName.getText().toString(),
                                etCatDes.getText().toString()
                        );
                        addNewCategory(addRequestModel);
                    }
                }

            }
        });

    }

    private void updateCategory(CategoryUpdateRequestModel categoryUpdateRequestModel) {
        dialogView.showCustomSpinProgress(AddCategory.this);
        manager.service.updateCategory(categoryUpdateRequestModel).enqueue(new Callback<CategoryUpdateResponseModel>() {
            @Override
            public void onResponse(Call<CategoryUpdateResponseModel> call, Response<CategoryUpdateResponseModel> response) {
                if (response.isSuccessful()){
                    dialogView.dismissCustomSpinProgress();
                    CategoryUpdateResponseModel curm = response.body();
                    if (!curm.error){
                        Toast.makeText(AddCategory.this, "Category updated successfully!", Toast.LENGTH_SHORT).show();
                        Constants.fromAddItem = true;
                        startActivity(new Intent(AddCategory.this, Dashboard.class));
                        finish();
                    }else {
                        Toast.makeText(AddCategory.this, "Category update failed!", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    dialogView.dismissCustomSpinProgress();
                }
            }

            @Override
            public void onFailure(Call<CategoryUpdateResponseModel> call, Throwable t) {
                dialogView.dismissCustomSpinProgress();
                Toast.makeText(AddCategory.this, "Network error! Please check your internet connection and try again!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addNewCategory(CategoryAddRequestModel addRequestModel) {
        dialogView.showCustomSpinProgress(AddCategory.this);

        manager.service.addCategory(addRequestModel).enqueue(new Callback<CategoryAddResponseModel>() {
            @Override
            public void onResponse(Call<CategoryAddResponseModel> call, Response<CategoryAddResponseModel> response) {
                if (response.isSuccessful()){
                    dialogView.dismissCustomSpinProgress();
                    CategoryAddResponseModel carm = response.body();
                    if (!carm.error){
                        Toast.makeText(AddCategory.this, "Category added successfully!", Toast.LENGTH_SHORT).show();
                        Constants.fromAddItem = true;
                        startActivity(new Intent(AddCategory.this, Dashboard.class));
                        finish();
                    }
                }else {
                    dialogView.dismissCustomSpinProgress();
                }
            }

            @Override
            public void onFailure(Call<CategoryAddResponseModel> call, Throwable t) {
                dialogView.dismissCustomSpinProgress();
            }
        });
    }

    private boolean isBlankValidate() {
        if (etCatName.getText().toString().equals("")){
            Toast.makeText(this, "Please enter category name!", Toast.LENGTH_SHORT).show();
            return false;
        }else if (etCatDes.getText().toString().equals("")){
            Toast.makeText(this, "Please add category description!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}