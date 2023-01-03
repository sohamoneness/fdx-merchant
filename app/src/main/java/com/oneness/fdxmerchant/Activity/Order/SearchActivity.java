package com.oneness.fdxmerchant.Activity.Order;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.oneness.fdxmerchant.Models.CartModels.CartClearResponseModel;
import com.oneness.fdxmerchant.Models.CartModels.CartDataResponseModel;
import com.oneness.fdxmerchant.Models.RestSearchModels.SearchRequestModel;
import com.oneness.fdxmerchant.Models.RestSearchModels.SearchResponseModel;
import com.oneness.fdxmerchant.Models.RestSearchModels.SearchUserDataModel;
import com.oneness.fdxmerchant.Models.RestSearchModels.UserAddressModel;
import com.oneness.fdxmerchant.Network.ApiManager;
import com.oneness.fdxmerchant.Network.Constants;
import com.oneness.fdxmerchant.R;
import com.oneness.fdxmerchant.Utils.DialogView;
import com.oneness.fdxmerchant.Utils.Prefs;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {

    RecyclerView searchRv;
    RelativeLayout btnSearch;
    EditText etSearch;
    ImageView ivBack;
    RelativeLayout userDetailRL;
    TextView tvUserName, tvContact, tvEmail;
    Prefs prefs;
    DialogView dialogView;
    ApiManager manager = new ApiManager();
    SearchUserDataModel searchUserDataModel;
    List<UserAddressModel> userAddressList = new ArrayList<>();
    String userID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        prefs = new Prefs(SearchActivity.this);
        dialogView = new DialogView();

        ivBack = findViewById(R.id.ivBack);
        btnSearch = findViewById(R.id.btnSearch);
        etSearch = findViewById(R.id.etSearch);
        searchRv = findViewById(R.id.searchRv);
        userDetailRL = findViewById(R.id.userDetailRL);
        tvUserName = findViewById(R.id.tvUserName);
        tvContact = findViewById(R.id.tvContact);
        tvEmail = findViewById(R.id.tvEmail);

        userDetailRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SearchActivity.this, OrderActivity.class));
            }
        });

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isBlankValid()){
                    SearchRequestModel searchRequestModel = new SearchRequestModel(
                            etSearch.getText().toString()
                    );
                    startSearchUser(searchRequestModel);
                }
            }
        });

       /* etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });*/

    }

    private boolean isBlankValid() {
        if (etSearch.getText().toString().equals("")){
            dialogView.errorButtonDialog(SearchActivity.this, getResources().getString(R.string.app_name), "Please enter a phone number to search user!");
            return false;
        }else {
            return true;
        }

    }

    private void startSearchUser(SearchRequestModel charSequence) {
        dialogView.showCustomSpinProgress(SearchActivity.this);
        manager.service.findUsers(charSequence).enqueue(new Callback<SearchResponseModel>() {
            @Override
            public void onResponse(Call<SearchResponseModel> call, Response<SearchResponseModel> response) {
                if (response.isSuccessful()){
                    dialogView.dismissCustomSpinProgress();
                    SearchResponseModel searchResponseModel = response.body();
                    if (!searchResponseModel.error){
                        //searchUserDataModel = new SearchUserDataModel();
                        searchUserDataModel = searchResponseModel.user;
                        prefs.saveData(Constants.SELECTED_USER_ID, searchUserDataModel.id);
                        userAddressList = searchResponseModel.addresses;
                        Constants.userAddressList = userAddressList;
                        Constants.userName = searchUserDataModel.name;
                        Constants.userMobile = searchUserDataModel.mobile;
                        Constants.userEmail = searchUserDataModel.email;

                        userDetailRL.setVisibility(View.VISIBLE);
                        tvUserName.setText(searchUserDataModel.name);
                        tvContact.setText("Contact: " + searchUserDataModel.mobile);
                        tvEmail.setText("Email: " + searchUserDataModel.email);
                        clearUserCart();

                    }else{
                        userDetailRL.setVisibility(View.GONE);

                    }
                }else{
                    dialogView.dismissCustomSpinProgress();
                }

            }

            @Override
            public void onFailure(Call<SearchResponseModel> call, Throwable t) {
                dialogView.dismissCustomSpinProgress();
            }
        });
    }

    private void clearUserCart() {
        manager.service.clearCart(prefs.getData(Constants.SELECTED_USER_ID)).enqueue(new Callback<CartClearResponseModel>() {
            @Override
            public void onResponse(Call<CartClearResponseModel> call, Response<CartClearResponseModel> response) {
                if (response.isSuccessful()){
                    CartClearResponseModel ccrm = response.body();
                    if (!ccrm.error){
                        Toast.makeText(SearchActivity.this, "Previous Cart value cleared!", Toast.LENGTH_SHORT).show();
                    }
                }else {

                }
            }

            @Override
            public void onFailure(Call<CartClearResponseModel> call, Throwable t) {

            }
        });
    }
}