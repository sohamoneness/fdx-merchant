package com.oneness.fdxmerchant.Activity.Profile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.oneness.fdxmerchant.Activity.EntryPoint.Dashboard;
import com.oneness.fdxmerchant.Models.RestaurantDataModels.RestaurantDataModel;
import com.oneness.fdxmerchant.Network.Constants;
import com.oneness.fdxmerchant.R;
import com.oneness.fdxmerchant.Utils.Prefs;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile extends AppCompatActivity {

    public static RestaurantDataModel restaurantDataModel;
    ImageView ivBack, ivBanner;
    CircleImageView ivUser;
    TextView tvName, tvId;
    RelativeLayout basicRL, settingsRL, availableRL, delConfigRL;
    Prefs prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        prefs = new Prefs(Profile.this);

        ivBack = findViewById(R.id.ivBack);
        ivUser = findViewById(R.id.ivUser);
        ivBanner = findViewById(R.id.ivBanner);
        tvName = findViewById(R.id.tvName);
        tvId = findViewById(R.id.tvId);
        basicRL = findViewById(R.id.basicRL);
        settingsRL = findViewById(R.id.settingsRL);
        availableRL = findViewById(R.id.availableRL);
        delConfigRL = findViewById(R.id.delConfigRL);

        tvName.setText(prefs.getData(Constants.REST_NAME));
        tvId.setText(prefs.getData(Constants.REST_EMAIL));
        if (prefs.getData(Constants.REST_BANNER) != null){
            Glide.with(Profile.this).load(restaurantDataModel.image).placeholder(R.drawable.demo_food_item).into(ivBanner);
        }
        if (prefs.getData(Constants.REST_LOGO) != null){
            Glide.with(Profile.this).load(restaurantDataModel.logo).placeholder(R.drawable.demo_food_item).into(ivUser);
        }


        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        basicRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateProfile.restaurantDataModel = restaurantDataModel;
                startActivity(new Intent(Profile.this, UpdateProfile.class));
            }
        });

        settingsRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Settings.restaurantDataModel = restaurantDataModel;
                startActivity(new Intent(Profile.this, Settings.class));
            }
        });

        availableRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Profile.this, Availability.class));

            }
        });
        delConfigRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Profile.this, DeliveryConfig.class));

            }
        });


    }
}