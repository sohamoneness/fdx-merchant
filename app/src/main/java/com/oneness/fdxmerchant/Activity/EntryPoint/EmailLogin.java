package com.oneness.fdxmerchant.Activity.EntryPoint;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.oneness.fdxmerchant.Models.LoginModels.LoginRequestModel;
import com.oneness.fdxmerchant.Models.LoginModels.LoginResponseModel;
import com.oneness.fdxmerchant.Models.RestaurantDataModels.RestaurantDataModel;
import com.oneness.fdxmerchant.Network.ApiManager;
import com.oneness.fdxmerchant.Network.Constants;
import com.oneness.fdxmerchant.R;
import com.oneness.fdxmerchant.Utils.DialogView;
import com.oneness.fdxmerchant.Utils.Prefs;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmailLogin extends AppCompatActivity {

    EditText etEmail, etPass;
    Button btnLogin;
    //ImageView ivBack;
    DialogView dialogView;
    ApiManager manager = new ApiManager();
    Prefs prefs;
    String TAG = "EmailLogin";
    TextView tvRegisterNow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_login);

        dialogView = new DialogView();
        prefs = new Prefs(EmailLogin.this);

        etEmail = findViewById(R.id.etEmail);
        etPass = findViewById(R.id.etPass);
        btnLogin = findViewById(R.id.btnLogin);
        //ivBack = findViewById(R.id.ivBack);
        tvRegisterNow = findViewById(R.id.tvRegisterNow);
        getFirebaseToken();


        /*ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });*/

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isBlankValidate()){

                    LoginRequestModel loginRequestModel = new LoginRequestModel(
                            etEmail.getText().toString(),
                            etPass.getText().toString()
                    );

                    callServerToLogin(loginRequestModel);

                }

            }
        });

        tvRegisterNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(EmailLogin.this, "Registration not available right now!", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void getFirebaseToken() {

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();
                        prefs.saveData(Constants.REFRESH_TOKEN, token);

                        // Log and toast
                        //String msg = getString(R.string.msg_token_fmt, token);
                        Log.d(TAG, token);
                        Toast.makeText(EmailLogin.this, "Here it is: "+token+"", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void callServerToLogin(LoginRequestModel loginRequestModel) {

        dialogView.showCustomSpinProgress(EmailLogin.this);

        manager.service.loginWithEmail(loginRequestModel).enqueue(new Callback<LoginResponseModel>() {
            @Override
            public void onResponse(Call<LoginResponseModel> call, Response<LoginResponseModel> response) {
                if (response.isSuccessful()){
                    dialogView.dismissCustomSpinProgress();
                    LoginResponseModel loginResponseModel = response.body();
                    if (!loginResponseModel.error){

                        RestaurantDataModel restaurantDataModel = new RestaurantDataModel();
                                restaurantDataModel = loginResponseModel.restaurantData;
//                        Log.d(Constants.REST_ID, restaurantDataModel.id);

                        prefs.saveData(Constants.REST_ID, restaurantDataModel.id);
                        prefs.saveData(Constants.REST_NAME, restaurantDataModel.name);
                        prefs.saveData(Constants.REST_EMAIL, restaurantDataModel.email);
                        prefs.saveData(Constants.REST_MOBILE, restaurantDataModel.mobile);
                        prefs.saveData(Constants.REST_ADR, restaurantDataModel.address);
                        prefs.saveData(Constants.REST_TAKING, restaurantDataModel.is_not_taking_orders);
                        prefs.saveData(Constants.REST_LOC, restaurantDataModel.location);
                        prefs.saveData(Constants.REST_OPEN, restaurantDataModel.start_time);
                        prefs.saveData(Constants.REST_CLOSE, restaurantDataModel.close_time);
                        if (restaurantDataModel.image != null){
                            prefs.saveData(Constants.REST_BANNER, restaurantDataModel.image);
                        }
                        if (restaurantDataModel.logo != null){
                            prefs.saveData(Constants.REST_LOGO, restaurantDataModel.logo);
                        }

                        Dashboard.restaurantDataModel = restaurantDataModel;
                        startActivity(new Intent(EmailLogin.this, Dashboard.class));
                        Toast.makeText(EmailLogin.this, "Login successful", Toast.LENGTH_SHORT).show();
                        finish();

                    }else{

                        Toast.makeText(EmailLogin.this, "Login failed!", Toast.LENGTH_SHORT).show();

                    }

                }else{
                    dialogView.dismissCustomSpinProgress();
                    Toast.makeText(EmailLogin.this, "error!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponseModel> call, Throwable t) {
                dialogView.dismissCustomSpinProgress();
                Toast.makeText(EmailLogin.this, "Unable to connect to the server!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private boolean isBlankValidate() {
        if (etEmail.getText().toString().equals("")){
            dialogView.errorButtonDialog(EmailLogin.this, getResources().getString(R.string.app_name), "Please enter email id!");
            return false;
        }else if (etPass.getText().toString().equals("")){
            dialogView.errorButtonDialog(EmailLogin.this, getResources().getString(R.string.app_name), "Please enter your password!");
            return false;
        }else {
            return true;
        }
    }
}