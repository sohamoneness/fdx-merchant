package com.oneness.fdxmerchant.Activity.EntryPoint;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.oneness.fdxmerchant.Models.RestaurantDataModels.ChangePasswordRequestModel;
import com.oneness.fdxmerchant.Models.RestaurantDataModels.ChangePasswordResponseModel;
import com.oneness.fdxmerchant.Network.ApiManager;
import com.oneness.fdxmerchant.Network.Constants;
import com.oneness.fdxmerchant.R;
import com.oneness.fdxmerchant.Utils.DialogView;
import com.oneness.fdxmerchant.Utils.Prefs;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePassword extends AppCompatActivity {

    ImageView ivBack;
    EditText etOldPass, etNewPass, etConfirmNewPass;
    Button btnUpdate;
    Prefs prefs;
    ApiManager manager = new ApiManager();
    DialogView dialogView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        dialogView = new DialogView();
        prefs = new Prefs(ChangePassword.this);

        ivBack = findViewById(R.id.ivBack);
        etOldPass = findViewById(R.id.etOldPass);
        etNewPass = findViewById(R.id.etNewPass);
        etConfirmNewPass = findViewById(R.id.etConfirmNewPass);
        btnUpdate = findViewById(R.id.btnUpdate);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChangePassword.this, Dashboard.class));
                finish();
                //onBackPressed();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isBlankValidate()){

                    ChangePasswordRequestModel changePasswordRequestModel = new ChangePasswordRequestModel(
                            prefs.getData(Constants.REST_ID),
                            etNewPass.getText().toString()
                    );

                    changePassword(changePasswordRequestModel);

                }
            }
        });

    }

    private void changePassword(ChangePasswordRequestModel changePasswordRequestModel) {
        dialogView.showCustomSpinProgress(ChangePassword.this);
        manager.service.updatePassword(changePasswordRequestModel).enqueue(new Callback<ChangePasswordResponseModel>() {
            @Override
            public void onResponse(Call<ChangePasswordResponseModel> call, Response<ChangePasswordResponseModel> response) {
                if (response.isSuccessful()){
                    dialogView.dismissCustomSpinProgress();
                    ChangePasswordResponseModel cprm = response.body();
                    if (!cprm.error){
                        Toast.makeText(ChangePassword.this, "Password updated successfully!", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                }else {
                    dialogView.dismissCustomSpinProgress();
                }
            }

            @Override
            public void onFailure(Call<ChangePasswordResponseModel> call, Throwable t) {
                dialogView.dismissCustomSpinProgress();
            }
        });
    }

    private boolean isBlankValidate() {
        if (etOldPass.getText().toString().equals("")){
            Toast.makeText(this, "Please enter your current password!", Toast.LENGTH_SHORT).show();
            return false;
        }else if (etNewPass.getText().toString().equals("")){
            Toast.makeText(this, "Please enter your new password!", Toast.LENGTH_SHORT).show();
            return false;
        }else if (etConfirmNewPass.getText().toString().equals("")){
            Toast.makeText(this, "Please re-enter your new password!", Toast.LENGTH_SHORT).show();
            return false;
        }else if (!etNewPass.getText().toString().equals(etConfirmNewPass.getText().toString())){
            Toast.makeText(this, "Please new passwords didn't match!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}