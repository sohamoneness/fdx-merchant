package com.oneness.fdxmerchant.Activity.ItemManagement;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
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
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.oneness.fdxmerchant.Activity.EntryPoint.Dashboard;
import com.oneness.fdxmerchant.Models.ItemManagementModels.AddItemRequestModel;
import com.oneness.fdxmerchant.Models.ItemManagementModels.AddItemResponseModel;
import com.oneness.fdxmerchant.Models.ItemManagementModels.CategoryListModel;
import com.oneness.fdxmerchant.Models.ItemManagementModels.CategoryResponseModel;
import com.oneness.fdxmerchant.Models.ItemManagementModels.ItemImageResponseModel;
import com.oneness.fdxmerchant.Models.ItemManagementModels.ItemsModel;
import com.oneness.fdxmerchant.Models.ItemManagementModels.UpdateItemRequestModel;
import com.oneness.fdxmerchant.Models.ItemManagementModels.UpdateResponseModel;
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

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddNewItem extends AppCompatActivity {

    ImageView ivBack;
    EditText etProductName, etQty, etPrice, etProductDes;
    TextView tvbrowse, tvPath, tv_head_text;
    Button btnAdd, btnCancel;
    RadioGroup rgType;
    RadioButton rbVeg, rbNonveg;
    Spinner spCat, spSpecial, spAddOn, spItems;
    List<CategoryListModel> catList = new ArrayList<>();
    List<ItemsModel> itemsList = new ArrayList<>();
    List<String> spCatList = new ArrayList<>();
    List<String> spSpecialList = new ArrayList<>();
    List<String> spAddonList = new ArrayList<>();
    List<String> spItemList = new ArrayList<>();
    public static ItemsModel itemModel;
    RequestQueue mQueue;
    String realPath = "";
    Prefs prefs;
    ApiManager manager = new ApiManager();
    ImageApiManager imgManager = new ImageApiManager();
    DialogView dialogView;
    String catName = "", catID = "";
    RadioButton rb;
    String itemType = "";
    String isVeg = "", isSpecial = "", hasAddOn = "", spItemId = "", spItemName = "";
    public static int isFrom = 0;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 101;

    LinearLayout itemLL;
    public static String IMG_UPLOAD = "http://demo91.co.in/dev/fdx/public/image.php";
   // http://demo91.co.in/dev/fdx/public/restaurant.php
    String imgURL = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_item);

        prefs = new Prefs(AddNewItem.this);
        dialogView = new DialogView();
        mQueue = Volley.newRequestQueue(this);

        ivBack = findViewById(R.id.ivBack);
        tv_head_text = findViewById(R.id.tv_head_text);
        etProductName = findViewById(R.id.etProductName);
        etQty = findViewById(R.id.etQty);
        etPrice = findViewById(R.id.etPrice);
        etProductDes = findViewById(R.id.etProductDes);
        tvbrowse = findViewById(R.id.tvbrowse);
        tvPath = findViewById(R.id.tvPath);
        btnAdd = findViewById(R.id.btnAdd);
        btnCancel = findViewById(R.id.btnCancel);
        rgType = findViewById(R.id.rgType);
        spCat = findViewById(R.id.spCat);
        rbNonveg = findViewById(R.id.rbNonveg);
        rbVeg = findViewById(R.id.rbVeg);
        spSpecial = findViewById(R.id.spSpecial);
        spAddOn = findViewById(R.id.spAddOn);
        itemLL = findViewById(R.id.itemLL);
        spItems = findViewById(R.id.spItems);

        spSpecialList.add("Select");
        spSpecialList.add("Yes");
        spSpecialList.add("No");
        spAddonList.add("Select");
        spAddonList.add("Yes");
        spAddonList.add("No");

        getCategory(prefs.getData(Constants.REST_ID));

        if (isFrom == 1) {
            etProductName.setText(itemModel.name);
            if (itemModel.avalability.equals("1")) {
                if (!itemModel.quantity.equals("0")) {
                    etQty.setText(itemModel.quantity);
                } else {
                    etQty.setText("1");
                }
            } else {
                etQty.setText("0");
            }

            etPrice.setText(itemModel.price);
            etProductDes.setText(itemModel.description);

            btnAdd.setText("Update");
            tv_head_text.setText("Update Item");

            if (itemModel.is_veg.equals("0")) {
                rbNonveg.setChecked(true);
                rbVeg.setChecked(false);
            } else {
                rbVeg.setChecked(true);
                rbNonveg.setChecked(false);
            }
            if (itemModel.avalability.equals("1")){
                etQty.setText("1");
            }else {
                etQty.setText("0");
            }

        }

        rgType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                rb = group.findViewById(checkedId);
            }
        });

        tvbrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkAndRequestPermissions(AddNewItem.this)) {
                    selectPhoto(AddNewItem.this);
                }
            }
        });

        spCat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                if (spCatList.get(position).equals("Select")) {
                    //Toast.makeText(getActivity(), "Please select an option", Toast.LENGTH_SHORT).show();
                } else {
                    catName = spCatList.get(position);
                    for (int i = 0; i < catList.size(); i++) {
                        if (catName.equals(catList.get(i).title)) {
                            catID = catList.get(i).id;
                        }
                    }
                    Toast.makeText(AddNewItem.this, catName + " Selected!", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                if (isFrom == 1) {
                    catID = itemModel.id;
                }

            }
        });


        spItems.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                if (spItemList.get(position).equals("Select")) {
                    //Toast.makeText(getActivity(), "Please select an option", Toast.LENGTH_SHORT).show();
                } else {
                    spItemName = spItemList.get(position);
                    for (int i = 0; i < itemsList.size(); i++) {
                        if (spItemName.equals(itemsList.get(i).name)) {
                            spItemId = itemsList.get(i).id;
                        }
                    }
                    //Toast.makeText(AddNewItem.this, catName + " Selected!", Toast.LENGTH_SHORT).show();

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

        spSpecial.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                if (spSpecialList.get(position).equals("Select")) {
                    //Toast.makeText(getActivity(), "Please select an option", Toast.LENGTH_SHORT).show();
                } else if (spSpecialList.get(position).equals("Yes")) {
                    isSpecial = "1";

                } else {
                    isSpecial = "0";
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

        spAddOn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                if (spAddonList.get(position).equals("Select")) {
                    itemLL.setVisibility(View.GONE);
                    //Toast.makeText(getActivity(), "Please select an option", Toast.LENGTH_SHORT).show();
                } else if (spAddonList.get(position).equals("Yes")) {
                    hasAddOn = "1";
                    itemLL.setVisibility(View.VISIBLE);

                } else {
                    hasAddOn = "0";
                    itemLL.setVisibility(View.GONE);
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
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isFrom == 1) {

                    String isAvailable1 = "";

                    int SelectedID = rgType.getCheckedRadioButtonId();
                    if (SelectedID == -1) {
                        Toast.makeText(AddNewItem.this, "Please select Item type!", Toast.LENGTH_SHORT).show();
                    } else {
                        rb = rgType.findViewById(SelectedID);
                        itemType = rb.getText().toString();
                    }

                    if (isBlankValidate()) {

                        int qty = Integer.parseInt(etQty.getText().toString());
                        if (qty > 0) {
                            isAvailable1 = "1";
                        } else {
                            isAvailable1 = "0";
                        }

                        if (itemType.equals("Veg")) {
                            isVeg = "1";
                        } else {
                            isVeg = "0";
                        }
                        if (hasAddOn.equals("0")) {
                            spItemId = "0";
                        }

                        UpdateItemRequestModel updateItemRequestModel = new UpdateItemRequestModel(
                                itemModel.id,
                                catID,
                                etProductName.getText().toString(),
                                etProductDes.getText().toString(),
                                etPrice.getText().toString(),
                                "0",
                                isVeg,
                                "0",
                                "0",
                                isAvailable1,
                                "",
                                isSpecial,
                                hasAddOn,
                                spItemId
                        );

                        updateItem(updateItemRequestModel, itemModel.id);
                    }

                } else {

                    String isAvailable = "";

                    int SelectedID = rgType.getCheckedRadioButtonId();
                    if (SelectedID == -1) {
                        Toast.makeText(AddNewItem.this, "Please select Item type!", Toast.LENGTH_SHORT).show();
                    } else {
                        rb = rgType.findViewById(SelectedID);
                        itemType = rb.getText().toString();
                    }

                    if (isBlankValidate()) {

                        int qty = Integer.parseInt(etQty.getText().toString());
                        if (qty > 0) {
                            isAvailable = "1";
                        } else {
                            isAvailable = "0";
                        }

                        if (itemType.equals("Veg")) {
                            isVeg = "1";
                        } else {
                            isVeg = "0";
                        }
                        if (imgURL.equals("")) {
                            imgURL = "test";
                        }
                        if (hasAddOn.equals("0")) {
                            spItemId = "0";
                        }


                        AddItemRequestModel addItemRequestModel = new AddItemRequestModel(
                                prefs.getData(Constants.REST_ID),
                                catID,
                                etProductName.getText().toString(),
                                etProductDes.getText().toString(),
                                etPrice.getText().toString(),
                                "0",
                                isVeg,
                                "0",
                                "0",
                                isAvailable,
                                imgURL,
                                isSpecial,
                                hasAddOn,
                                spItemId
                        );

                        addNewItem(addItemRequestModel);

                    }


                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS:
                if (ContextCompat.checkSelfPermission(AddNewItem.this,
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(AddNewItem.this,
                                    "FlagUp Requires Access to Camara.", Toast.LENGTH_SHORT)
                            .show();
                } else if (ContextCompat.checkSelfPermission(AddNewItem.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(AddNewItem.this,
                            "FlagUp Requires Access to Your Storage.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    selectPhoto(AddNewItem.this);
                }
               /* if (requestCode == 1 && grantResults.length > 0
                        && grantResults[0]
                        == PackageManager.PERMISSION_GRANTED) {
                    // When permission is granted
                    // Call method
                    selectPhoto(AddNewItem.this);
                }
                else {
                    // When permission is denied
                    // Display toast
                    Toast.makeText(AddNewItem.this, "Permission Denied",
                            Toast.LENGTH_SHORT).show();
                }*/
                break;
        }


    }

    @SuppressLint("Range")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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

    private void uploadImg(File file) {
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));
        imgManager.service.uploadItemImage(filePart).enqueue(new Callback<ItemImageResponseModel>() {
            @Override
            public void onResponse(Call<ItemImageResponseModel> call, Response<ItemImageResponseModel> response) {
                if (response.isSuccessful()){
                    if (response.body().status.equals("1")){
                        imgURL = response.body().file_link;
                        tvPath.setText(imgURL);
                        Toast.makeText(AddNewItem.this, "Success!!!!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ItemImageResponseModel> call, Throwable t) {
                Toast.makeText(AddNewItem.this, "Failed!!!!", Toast.LENGTH_SHORT).show();


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

    private void uploadImage(byte[] bytes) {
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), bytes);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", "image.jpg", requestFile);
        //Call<Response> call = retrofitInterface.uploadImage(body);
        imgManager.service.uploadItemImage(body).enqueue(new Callback<ItemImageResponseModel>() {
            @Override
            public void onResponse(Call<ItemImageResponseModel> call, Response<ItemImageResponseModel> response) {
                if (response.isSuccessful()){
                    if (response.body().status.equals("1")){
                        imgURL = response.body().file_link;
                        tvPath.setText(imgURL);
                        Toast.makeText(AddNewItem.this, "Success!!!!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ItemImageResponseModel> call, Throwable t) {
                Toast.makeText(AddNewItem.this, "Failed!!!!", Toast.LENGTH_SHORT).show();

            }
        });
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


    private void updateItem(UpdateItemRequestModel updateItemRequestModel, String id) {
        dialogView.showCustomSpinProgress(AddNewItem.this);
        manager.service.updateItems(updateItemRequestModel).enqueue(new Callback<UpdateResponseModel>() {
            @Override
            public void onResponse(Call<UpdateResponseModel> call, Response<UpdateResponseModel> response) {
                if (response.isSuccessful()) {
                    dialogView.showCustomSpinProgress(AddNewItem.this);
                    UpdateResponseModel urm = new UpdateResponseModel();
                    if (!urm.error) {
                        Toast.makeText(AddNewItem.this, "Item updated successfully!", Toast.LENGTH_SHORT).show();
                       // ItemListActivity.catID = id;
                        //Constants.needToRefresh = true;
                        startActivity(new Intent(AddNewItem.this, ItemListActivity.class).putExtra("CatID", catID));
                        finish();
                    } else {
                        Toast.makeText(AddNewItem.this, "Error! While updating item!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    dialogView.dismissCustomSpinProgress();
                }
            }

            @Override
            public void onFailure(Call<UpdateResponseModel> call, Throwable t) {
                dialogView.dismissCustomSpinProgress();

            }
        });
        isFrom = 0;
        finish();
    }

    private void addNewItem(AddItemRequestModel addItemRequestModel) {
        dialogView.showCustomSpinProgress(this);
        manager.service.addItems(addItemRequestModel).enqueue(new Callback<AddItemResponseModel>() {
            @Override
            public void onResponse(Call<AddItemResponseModel> call, Response<AddItemResponseModel> response) {
                if (response.isSuccessful()) {
                    dialogView.dismissCustomSpinProgress();
                    AddItemResponseModel arm = response.body();
                    if (!arm.error) {
                        Toast.makeText(AddNewItem.this, "Item added successfully", Toast.LENGTH_SHORT).show();
                        Constants.fromAddItem = true;
                        startActivity(new Intent(AddNewItem.this, Dashboard.class));
                        finish();
                    } else {
                        Toast.makeText(AddNewItem.this, "Error when adding item! Please try again!", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    dialogView.dismissCustomSpinProgress();
                    Toast.makeText(AddNewItem.this, "Server Error when adding item! Please try again!", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<AddItemResponseModel> call, Throwable t) {
                dialogView.dismissCustomSpinProgress();
                Toast.makeText(AddNewItem.this, "Network Error when adding item! Please try again!", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void getCategory(String data) {
        dialogView.showCustomSpinProgress(AddNewItem.this);
        manager.service.getRestaurantWiseCategories(data).enqueue(new Callback<CategoryResponseModel>() {
            @Override
            public void onResponse(Call<CategoryResponseModel> call, Response<CategoryResponseModel> response) {
                if (response.isSuccessful()) {
                    dialogView.dismissCustomSpinProgress();
                    CategoryResponseModel crm = response.body();
                    if (!crm.error) {
                        catList = crm.categories;
                        spCatList.add("Select");
                        if (catList.size() > 0) {
                            for (int i = 0; i < catList.size(); i++) {
                                spCatList.add(catList.get(i).title);
                            }

                            for (int j = 0; j < catList.size(); j++) {
                                itemsList.addAll(catList.get(j).items);
                            }
                            for (int x = 0; x < itemsList.size(); x++) {
                                spItemList.add(itemsList.get(x).name);
                            }
                        }
                        ArrayAdapter<String> spcatAdapter = new ArrayAdapter<String>(
                                AddNewItem.this, R.layout.my_spinner_row, spCatList);
                        spcatAdapter.setDropDownViewResource(R.layout.my_spinner_row);
                        spCat.setAdapter(spcatAdapter);
                        spCat.setFocusableInTouchMode(false);

                        ArrayAdapter<String> spItemAdapter = new ArrayAdapter<>(
                                AddNewItem.this, R.layout.my_spinner_row, spItemList);
                        spItemAdapter.setDropDownViewResource(R.layout.my_spinner_row);
                        spItems.setAdapter(spItemAdapter);
                        spItems.setFocusableInTouchMode(false);

                        ArrayAdapter<String> spSpecialAdapter = new ArrayAdapter<>(
                                AddNewItem.this, R.layout.my_spinner_row, spSpecialList);
                        spSpecialAdapter.setDropDownViewResource(R.layout.my_spinner_row);
                        spSpecial.setAdapter(spSpecialAdapter);
                        spSpecial.setFocusableInTouchMode(false);

                        ArrayAdapter<String> spAddonAdapter = new ArrayAdapter<>(
                                AddNewItem.this, R.layout.my_spinner_row, spAddonList);
                        spAddonAdapter.setDropDownViewResource(R.layout.my_spinner_row);
                        spAddOn.setAdapter(spAddonAdapter);
                        spAddOn.setFocusableInTouchMode(false);

                        if (isFrom == 1) {
                            for (int x = 0; x < catList.size(); x++) {
                                if (catList.get(x).id.equals(itemModel.category_id)) {
                                    catName = catList.get(x).title;
                                }
                            }
                            spCat.setSelection(spCatList.indexOf(catName));
                            if (itemModel.is_special.equals("1")){
                                spSpecial.setSelection(spSpecialList.indexOf("Yes"));
                            }else {
                                spSpecial.setSelection(spSpecialList.indexOf("No"));
                            }
                            if (itemModel.is_add_on.equals("1")){
                                spAddOn.setSelection(spSpecialList.indexOf("Yes"));
                            }else {
                                spAddOn.setSelection(spSpecialList.indexOf("No"));
                            }

                        }

                    } else {

                    }

                } else {

                }

            }

            @Override
            public void onFailure(Call<CategoryResponseModel> call, Throwable t) {

            }
        });
    }

    private boolean isBlankValidate() {
        if (etProductName.getText().toString().equals("")) {
            dialogView.errorButtonDialog(AddNewItem.this, getResources().getString(R.string.app_name), "Please enter product name!");
            return false;
        } else if (etPrice.getText().toString().equals("")) {
            dialogView.errorButtonDialog(AddNewItem.this, getResources().getString(R.string.app_name), "Please enter price!");
            return false;
        } else if (etQty.getText().toString().equals("")) {
            dialogView.errorButtonDialog(AddNewItem.this, getResources().getString(R.string.app_name), "Please enter quantity!");
            return false;
        } else {
            return true;
        }
    }


    private byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();

    }

}