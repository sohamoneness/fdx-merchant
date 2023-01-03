package com.oneness.fdxmerchant.Activity.EntryPoint;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.oneness.fdxmerchant.Network.Constants;
import com.oneness.fdxmerchant.R;
import com.oneness.fdxmerchant.Utils.Prefs;

public class SplashActivity extends AppCompatActivity {
    private static int SPLASH_SCREEN_TIME_OUT=2000;
    private static final int MY_PERMISSIONS_REQUEST_CODE = 123;
    Prefs prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        prefs = new Prefs(SplashActivity.this);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            //checkPermission();

            navigationMethod();

        }
        else
        {
            navigationMethod();

        }
    }

    void navigationMethod(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                navigation();
            }
        }, SPLASH_SCREEN_TIME_OUT);
    }
    void navigation(){
        Intent i=null;
        //Log.d("TokenCheck","A "+prefs.getData("Token").equals(""));
        if (prefs.getData(Constants.REST_ID).equals(""))
            i=new Intent(SplashActivity.this, EmailLogin.class);
        else
            i=new Intent(SplashActivity.this, Dashboard.class);
        startActivity(i);
       // startActivity(new Intent(SplashActivity.this, Login.class));
        finish();
    }
    //checkPermission
    protected void checkPermission(){
        if(ContextCompat.checkSelfPermission(SplashActivity.this, Manifest.permission.CAMERA)+
                ContextCompat.checkSelfPermission(SplashActivity.this,Manifest.permission.ACCESS_FINE_LOCATION)
                + ContextCompat.checkSelfPermission(
                SplashActivity.this,Manifest.permission.ACCESS_COARSE_LOCATION)+ ContextCompat.checkSelfPermission(
                SplashActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)+ ContextCompat.checkSelfPermission(
                SplashActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){

            // Do something, when permissions not granted
            if(ActivityCompat.shouldShowRequestPermissionRationale(
                    SplashActivity.this, Manifest.permission.CAMERA) || ActivityCompat.shouldShowRequestPermissionRationale(
                    SplashActivity.this,Manifest.permission.ACCESS_FINE_LOCATION)
                    || ActivityCompat.shouldShowRequestPermissionRationale(
                    SplashActivity.this,Manifest.permission.ACCESS_COARSE_LOCATION)|| ActivityCompat.shouldShowRequestPermissionRationale(
                    SplashActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)|| ActivityCompat.shouldShowRequestPermissionRationale(
                    SplashActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                // If we should give explanation of requested permissions

                // Show an alert dialog here with request explanation
                AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
                builder.setMessage("Camera , Location and Storage" +
                        "  permissions are required to do the task.");
                builder.setTitle("Please grant those permissions");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ActivityCompat.requestPermissions(
                                SplashActivity.this,
                                new String[]
                                        {
                                                Manifest.permission.CAMERA,
                                                Manifest.permission.ACCESS_FINE_LOCATION,
                                                Manifest.permission.ACCESS_COARSE_LOCATION,
                                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                                        },
                                MY_PERMISSIONS_REQUEST_CODE
                        );
                    }
                });
                builder.setNeutralButton("Cancel",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dlg, int sumthin) {
                        startActivity(new Intent(SplashActivity.this,SplashActivity.class));
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }else{
                // Directly request for required permissions, without explanation
                ActivityCompat.requestPermissions(
                        SplashActivity.this,
                        new String[]{
                                Manifest.permission.CAMERA,
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                        },
                        MY_PERMISSIONS_REQUEST_CODE
                );
            }
        }else {

            navigation();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CODE: {
                // When request is cancelled, the results array are empty

                if (
                        (grantResults.length > 0) &&
                                (grantResults[0]
                                        + grantResults[1] + grantResults[2] + grantResults[3] + grantResults[4]
                                        == PackageManager.PERMISSION_GRANTED
                                )
                ) {
                    navigation();
                } else {
                    startActivity(new Intent(SplashActivity.this, SplashActivity.class));
                }

                return;
            }
        }
    }

}