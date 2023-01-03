package com.oneness.fdxmerchant.Activity.Profile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.oneness.fdxmerchant.Activity.ItemManagement.AddNewItem;
import com.oneness.fdxmerchant.Models.ItemManagementModels.ItemImageResponseModel;
import com.oneness.fdxmerchant.Models.RestaurantDataModels.RestaurantDataModel;
import com.oneness.fdxmerchant.Models.RestaurantDataModels.RestaurantDataUpdateResponseModel;
import com.oneness.fdxmerchant.Models.RestaurantDataModels.RestaurantDataUpdateRequest;
import com.oneness.fdxmerchant.Network.ApiManager;
import com.oneness.fdxmerchant.Network.Constants;
import com.oneness.fdxmerchant.Network.ImageApiManager;
import com.oneness.fdxmerchant.R;
import com.oneness.fdxmerchant.Utils.DialogView;
import com.oneness.fdxmerchant.Utils.Prefs;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateProfile extends AppCompatActivity {

    EditText etName, etBusinessName, etGstNum, etContactNum, etEmail, etWhatsappNum;
    EditText etLocation, etAddress;
    TextView tvName, tvId;
    Button btnSave;
    Prefs prefs;
    DialogView dialogView;
    ImageView ivBack, ivBanner;
    List<String> spTakeOrderList = new ArrayList<>();
    ApiManager manager = new ApiManager();
    public static RestaurantDataModel restaurantDataModel = new RestaurantDataModel();
    Spinner spTakeOrder;
    String isTaking = "";
    CircleImageView ivUser;
    ImageApiManager imgManager = new ImageApiManager();
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 101;
    String bannerUrl = "", logoUrl = "", selectedType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        prefs = new Prefs(UpdateProfile.this);
        dialogView = new DialogView();

        etName = findViewById(R.id.etName);
        etBusinessName = findViewById(R.id.etBusinessName);
        etGstNum = findViewById(R.id.etGstNum);
        etContactNum = findViewById(R.id.etContactNum);
        etEmail = findViewById(R.id.etEmail);
        etWhatsappNum = findViewById(R.id.etWhatsappNum);
        etLocation = findViewById(R.id.etLocation);
        etAddress = findViewById(R.id.etAddress);
        tvName = findViewById(R.id.tvName);
        tvId = findViewById(R.id.tvId);
        btnSave = findViewById(R.id.btnSave);
        ivBack = findViewById(R.id.ivBack);
        ivBanner = findViewById(R.id.ivBanner);
        ivUser = findViewById(R.id.ivUser);
        spTakeOrder = findViewById(R.id.spTakeOrder);

        if (restaurantDataModel.id.equals("")){
            restaurantDataModel.id = prefs.getData(Constants.REST_ID);
            restaurantDataModel.name = prefs.getData(Constants.REST_NAME);
            restaurantDataModel.email = prefs.getData(Constants.REST_EMAIL);
            restaurantDataModel.mobile = prefs.getData(Constants.REST_MOBILE);
            restaurantDataModel.address = prefs.getData(Constants.REST_ADR);
            restaurantDataModel.location = prefs.getData(Constants.REST_LOC);
            restaurantDataModel.start_time = prefs.getData(Constants.REST_OPEN);
            restaurantDataModel.close_time = prefs.getData(Constants.REST_CLOSE);
            restaurantDataModel.is_not_taking_orders = prefs.getData(Constants.REST_TAKING);
            restaurantDataModel.image = prefs.getData(Constants.REST_BANNER);
            restaurantDataModel.logo = prefs.getData(Constants.REST_LOGO);
        }

        spTakeOrderList.add("Select");
        spTakeOrderList.add("Yes");
        spTakeOrderList.add("No");
        ArrayAdapter<String> spItemAdapter = new ArrayAdapter<>(
                UpdateProfile.this, R.layout.my_spinner_row, spTakeOrderList);
        spItemAdapter.setDropDownViewResource(R.layout.my_spinner_row);
        spTakeOrder.setAdapter(spItemAdapter);
        spTakeOrder.setFocusableInTouchMode(false);



        Glide.with(UpdateProfile.this).load(prefs.getData(Constants.REST_LOGO)).placeholder(R.drawable.demo_food_item).into(ivUser);
        Glide.with(UpdateProfile.this).load(prefs.getData(Constants.REST_BANNER))
                .skipMemoryCache(true).into(ivBanner);


        tvName.setText(restaurantDataModel.name);
        tvId.setText(restaurantDataModel.email);

        etName.setText(restaurantDataModel.name);
        etBusinessName.setText(restaurantDataModel.name);
        etContactNum.setText(restaurantDataModel.mobile);
        etEmail.setText(restaurantDataModel.email);
        etWhatsappNum.setText(restaurantDataModel.mobile);
        etLocation.setText(restaurantDataModel.location);
        etAddress.setText(restaurantDataModel.address);

        if (restaurantDataModel.is_not_taking_orders.equals("1")){
            spTakeOrder.setSelection(2);
        }else{
            spTakeOrder.setSelection(1);
        }

        spTakeOrder.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                if (spTakeOrderList.get(position).equals("Select")) {

                    //Toast.makeText(getActivity(), "Please select an option", Toast.LENGTH_SHORT).show();
                } else if (spTakeOrderList.get(position).equals("Yes")){
                    //hasAddOn = "1";
                    //itemLL.setVisibility(View.VISIBLE);
                    isTaking = "0";

                }else {
                   // hasAddOn = "0";
                   // itemLL.setVisibility(View.GONE);
                    isTaking = "1";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                /*if (isFrom == 1){
                    catID = itemModel.id;
                }*/

            }
        });

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        etLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        ivUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkAndRequestPermissions(UpdateProfile.this)) {
                    selectedType = "logo";
                    selectPhoto(UpdateProfile.this);
                }
            }
        });

        ivBanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkAndRequestPermissions(UpdateProfile.this)) {
                    selectedType = "banner";
                    selectPhoto(UpdateProfile.this);
                }
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                RestaurantDataUpdateRequest restaurantDataUpdateRequest = new RestaurantDataUpdateRequest(
                        prefs.getData(Constants.REST_ID),
                        etName.getText().toString(),
                        etEmail.getText().toString(),
                        etContactNum.getText().toString(),
                        "",
                        etAddress.getText().toString(),
                        etLocation.getText().toString(),
                        "22.7894",
                        "88.4586",
                        restaurantDataModel.start_time,
                        restaurantDataModel.close_time,
                        "0",
                        "0",
                        "25",
                        isTaking,
                        bannerUrl,
                        logoUrl
                );

                updateRestPrf(restaurantDataUpdateRequest);

            }
        });


    }

    public static boolean checkAndRequestPermissions(final Activity context) {
        int WExtstorePermission = ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int cameraPermission = ContextCompat.checkSelfPermission(context,
                Manifest.permission.CAMERA);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (WExtstorePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded
                    .add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(context, listPermissionsNeeded
                            .toArray(new String[listPermissionsNeeded.size()]),
                    REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }


    private void selectPhoto(Context context) {
        final CharSequence[] optionsMenu = {"Take Photo", "Choose from Gallery", "Exit"}; // create a menuOption Array
        // create a dialog for showing the optionsMenu
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        // set the items in builder
        builder.setItems(optionsMenu, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (optionsMenu[i].equals("Take Photo")) {
                    // Open the camera and get the photo
                    Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);
                } else if (optionsMenu[i].equals("Choose from Gallery")) {
                    // choose from  external storage
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto, 1);
                } else if (optionsMenu[i].equals("Exit")) {
                    dialogInterface.dismiss();
                }
            }
        });
        builder.show();
    }

    private void updateRestPrf(RestaurantDataUpdateRequest restaurantDataUpdateRequest) {
        dialogView.showCustomSpinProgress(UpdateProfile.this);
        manager.service.updateRestaurantData(restaurantDataUpdateRequest).enqueue(new Callback<RestaurantDataUpdateResponseModel>() {
            @Override
            public void onResponse(Call<RestaurantDataUpdateResponseModel> call, Response<RestaurantDataUpdateResponseModel> response) {
                if (response.isSuccessful()){
                    dialogView.dismissCustomSpinProgress();
                    RestaurantDataUpdateResponseModel rdurm = response.body();
                    if (!rdurm.error){
                        restaurantDataModel = rdurm.restaurant;
                        Toast.makeText(UpdateProfile.this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }else {
                    dialogView.dismissCustomSpinProgress();
                }
            }

            @Override
            public void onFailure(Call<RestaurantDataUpdateResponseModel> call, Throwable t) {
                dialogView.dismissCustomSpinProgress();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS:
                if (ContextCompat.checkSelfPermission(UpdateProfile.this,
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(UpdateProfile.this,
                                    "FlagUp Requires Access to Camara.", Toast.LENGTH_SHORT)
                            .show();
                } else if (ContextCompat.checkSelfPermission(UpdateProfile.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(UpdateProfile.this,
                            "FlagUp Requires Access to Your Storage.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    selectPhoto(UpdateProfile.this);
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK && data != null) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                File file = savebitmap(photo);
                //tvPath.setText("image captured");
                uploadImg(file);

            }

        }else if (requestCode == 1){
            if (resultCode == RESULT_OK) {
                try {

                    InputStream is = getContentResolver().openInputStream(data.getData());

                    uploadImage(getBytes(is));

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void uploadImage(byte[] bytes) {
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), bytes);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", "image.jpg", requestFile);
        //Call<Response> call = retrofitInterface.uploadImage(body);
        imgManager.service.uploadRestaurantProfileImage(body).enqueue(new Callback<ItemImageResponseModel>() {
            @Override
            public void onResponse(Call<ItemImageResponseModel> call, Response<ItemImageResponseModel> response) {
                if (response.isSuccessful()){
                    if (response.body().status.equals("1")){
                        if (selectedType.equals("logo")){
                            logoUrl = response.body().file_link;
                            Glide.with(UpdateProfile.this).load(logoUrl).into(ivUser);
                        }else if (selectedType.equals("banner")){
                            bannerUrl = response.body().file_link;
                            Glide.with(UpdateProfile.this).load(bannerUrl).into(ivBanner);
                        }
                        Toast.makeText(UpdateProfile.this, "Success!!!!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ItemImageResponseModel> call, Throwable t) {
                Toast.makeText(UpdateProfile.this, "Failed!!!!", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void uploadImg(File file) {
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));
        imgManager.service.uploadRestaurantProfileImage(filePart).enqueue(new Callback<ItemImageResponseModel>() {
            @Override
            public void onResponse(Call<ItemImageResponseModel> call, Response<ItemImageResponseModel> response) {
                if (response.isSuccessful()){
                    if (response.body().status.equals("1")){

                        if (selectedType.equals("logo")){
                            logoUrl = response.body().file_link;
                            Glide.with(UpdateProfile.this).load(logoUrl).into(ivUser);
                        }else if (selectedType.equals("banner")){
                            bannerUrl = response.body().file_link;
                            Glide.with(UpdateProfile.this).load(bannerUrl).into(ivBanner);
                        }

                        Toast.makeText(UpdateProfile.this, "Success!!!!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ItemImageResponseModel> call, Throwable t) {
                Toast.makeText(UpdateProfile.this, "Failed!!!!", Toast.LENGTH_SHORT).show();


            }
        });
    }

    private File savebitmap(Bitmap bmp) {
        String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
        OutputStream outStream = null;
        // String temp = null;
        File file = new File(extStorageDirectory, "temp.png");
        if (file.exists()) {
            file.delete();
            file = new File(extStorageDirectory, "temp.png");

        }

        try {
            outStream = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
            outStream.flush();
            outStream.close();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return file;
    }

    public byte[] getBytes(InputStream is) throws IOException {
        ByteArrayOutputStream byteBuff = new ByteArrayOutputStream();

        int buffSize = 1024;
        byte[] buff = new byte[buffSize];

        int len = 0;
        while ((len = is.read(buff)) != -1) {
            byteBuff.write(buff, 0, len);
        }

        return byteBuff.toByteArray();
    }




}