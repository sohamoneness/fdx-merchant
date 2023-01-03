package com.oneness.fdxmerchant.Activity.Profile;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.oneness.fdxmerchant.Models.ProfileModels.AvailabilityRequestModel;
import com.oneness.fdxmerchant.Models.ProfileModels.AvailabilityResponseModel;
import com.oneness.fdxmerchant.Models.ProfileModels.DayModel;
import com.oneness.fdxmerchant.Models.ProfileModels.TimingResponseModel;
import com.oneness.fdxmerchant.Models.ProfileModels.TimingsModel;
import com.oneness.fdxmerchant.Network.ApiManager;
import com.oneness.fdxmerchant.Network.Constants;
import com.oneness.fdxmerchant.R;
import com.oneness.fdxmerchant.Utils.DialogView;
import com.oneness.fdxmerchant.Utils.Prefs;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Formattable;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Availability extends AppCompatActivity {
    ImageView ivBack;
    TextView etAllFrom, etAllTo, etMonFrom, etMonTo, etTueFrom, etTueTo;
    TextView etWedFrom, etWedTo, etThuFrom, etThuTo, etFriFrom, etFriTo;
    TextView etSatFrom, etSatTo, etSunFrom, etSunTo;
    CheckBox chkAll, chkMon, chkTue, chkWed, chkThu, chkFri, chkSat, chkSun;
    LinearLayout allLL;
    Button btnSave;

    Prefs prefs;
    ApiManager manager = new ApiManager();
    DialogView dialogView;

    String day = "", onTime = "", offTime = "";

    List<DayModel> dayList = new ArrayList<>();
    List<TimingsModel> timeList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_availability);

        prefs = new Prefs(Availability.this);
        dialogView = new DialogView();

        ivBack = findViewById(R.id.ivBack);
        allLL = findViewById(R.id.allLL);
        btnSave = findViewById(R.id.btnSave);

        chkAll = findViewById(R.id.chkAll);
        etAllFrom = findViewById(R.id.etAllFrom);
        etAllTo = findViewById(R.id.etAllTo);

        chkMon = findViewById(R.id.chkMon);
        etMonFrom = findViewById(R.id.etMonFrom);
        etMonTo = findViewById(R.id.etMonTo);

        chkTue = findViewById(R.id.chkTue);
        etTueFrom = findViewById(R.id.etTueFrom);
        etTueTo = findViewById(R.id.etTueTo);

        chkWed = findViewById(R.id.chkWed);
        etWedFrom = findViewById(R.id.etWedFrom);
        etWedTo = findViewById(R.id.etWedTo);

        chkThu = findViewById(R.id.chkThu);
        etThuFrom = findViewById(R.id.etThuFrom);
        etThuTo = findViewById(R.id.etThuTo);

        chkFri = findViewById(R.id.chkFri);
        etFriFrom = findViewById(R.id.etFriFrom);
        etFriTo = findViewById(R.id.etFriTo);

        chkSat = findViewById(R.id.chkSat);
        etSatFrom = findViewById(R.id.etSatFrom);
        etSatTo = findViewById(R.id.etSatTo);

        chkSun = findViewById(R.id.chkSun);
        etSunFrom = findViewById(R.id.etSunFrom);
        etSunTo = findViewById(R.id.etSunTo);


        DayModel dm = new DayModel();
        dm.name = "MON";
        dm.startTime = "";
        dm.offTime = "";
        dm.tag = 0;
        dayList.add(dm);

        DayModel dm1 = new DayModel();
        dm1.name = "TUE";
        dm1.startTime = "";
        dm1.offTime = "";
        dm1.tag = 0;
        dayList.add(dm1);

        DayModel dm2 = new DayModel();
        dm2.name = "WED";
        dm2.startTime = "";
        dm2.offTime = "";
        dm2.tag = 0;
        dayList.add(dm2);

        DayModel dm3 = new DayModel();
        dm3.name = "THU";
        dm3.startTime = "";
        dm3.offTime = "";
        dm3.tag = 0;
        dayList.add(dm3);

        DayModel dm4 = new DayModel();
        dm4.name = "FRI";
        dm4.startTime = "";
        dm4.offTime = "";
        dm4.tag = 0;
        dayList.add(dm4);

        DayModel dm5 = new DayModel();
        dm5.name = "SAT";
        dm5.startTime = "";
        dm5.offTime = "";
        dm5.tag = 0;
        dayList.add(dm5);

        DayModel dm6 = new DayModel();
        dm6.name = "SUN";
        dm6.startTime = "";
        dm6.offTime = "";
        dm6.tag = 0;
        dayList.add(dm6);

        getTimings();


        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chkAll.isChecked()) {
                    if (blankValidate(etAllFrom, etAllTo)) {
                        day = "ALL";
                        onTime = etAllFrom.getText().toString();
                        offTime = etAllTo.getText().toString();
                        AvailabilityRequestModel availabilityRequestModel = new AvailabilityRequestModel(
                                prefs.getData(Constants.REST_ID),
                                day,
                                onTime,
                                offTime
                        );
                        saveTimingData(availabilityRequestModel);
                    }
                } else {
                    for (int j = 0; j < dayList.size(); j++){
                        if (dayList.get(j).tag == 1){
                            if (day.equals("")){
                                day = dayList.get(j).name;
                                onTime = dayList.get(j).startTime;
                                offTime = dayList.get(j).offTime;
                            }else {
                                day = day + "*" + dayList.get(j).name;
                                onTime = onTime + "*" + dayList.get(j).startTime;
                                offTime = offTime + "*" + dayList.get(j).offTime;
                            }
                        }
                    }
                    AvailabilityRequestModel availabilityRequestModel = new AvailabilityRequestModel(
                            prefs.getData(Constants.REST_ID),
                            day,
                            onTime,
                            offTime
                    );
                    saveTimingData(availabilityRequestModel);
                }

            }
        });

        etAllFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTimePicker(etAllFrom);
            }
        });

        etAllTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTimePicker(etAllTo);
            }
        });

        etMonFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTimePicker(etMonFrom);
            }
        });

        etMonTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTimePicker(etMonTo);
            }
        });

        etTueFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTimePicker(etTueFrom);
            }
        });

        etTueTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTimePicker(etTueTo);
            }
        });

        etWedFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTimePicker(etWedFrom);
            }
        });

        etWedTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTimePicker(etWedTo);
            }
        });

        etThuFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTimePicker(etThuFrom);
            }
        });

        etThuTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTimePicker(etThuTo);
            }
        });

        etFriFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTimePicker(etFriFrom);
            }
        });

        etFriTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTimePicker(etFriTo);
            }
        });

        etSatFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTimePicker(etSatFrom);
            }
        });

        etSatTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTimePicker(etSatTo);
            }
        });

        etSunFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTimePicker(etSunFrom);
            }
        });

        etSunTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTimePicker(etSunTo);
            }
        });


        chkAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chkAll.isChecked()) {
                    chkMon.setEnabled(false);
                    chkTue.setEnabled(false);
                    chkWed.setEnabled(false);
                    chkThu.setEnabled(false);
                    chkFri.setEnabled(false);
                    chkSat.setEnabled(false);
                    chkSun.setEnabled(false);
                    etMonFrom.setEnabled(false);
                    etMonTo.setEnabled(false);
                    etTueFrom.setEnabled(false);
                    etTueTo.setEnabled(false);
                    etWedFrom.setEnabled(false);
                    etWedTo.setEnabled(false);
                    etThuFrom.setEnabled(false);
                    etThuTo.setEnabled(false);
                    etFriFrom.setEnabled(false);
                    etFriTo.setEnabled(false);
                    etSatFrom.setEnabled(false);
                    etSatTo.setEnabled(false);
                    etSunFrom.setEnabled(false);
                    etSunTo.setEnabled(false);

                } else {

                    chkMon.setEnabled(true);
                    chkTue.setEnabled(true);
                    chkWed.setEnabled(true);
                    chkThu.setEnabled(true);
                    chkFri.setEnabled(true);
                    chkSat.setEnabled(true);
                    chkSun.setEnabled(true);
                    etMonFrom.setEnabled(true);
                    etMonTo.setEnabled(true);
                    etTueFrom.setEnabled(true);
                    etTueTo.setEnabled(true);
                    etWedFrom.setEnabled(true);
                    etWedTo.setEnabled(true);
                    etThuFrom.setEnabled(true);
                    etThuTo.setEnabled(true);
                    etFriFrom.setEnabled(true);
                    etFriTo.setEnabled(true);
                    etSatFrom.setEnabled(true);
                    etSatTo.setEnabled(true);
                    etSunFrom.setEnabled(true);
                    etSunTo.setEnabled(true);

                }
            }
        });

        chkMon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (blankValidate(etMonFrom, etMonTo)){
                    if (chkMon.isChecked()){
                        for (int i = 0; i < dayList.size(); i++){
                            if (dayList.get(i).name.equals("MON")){
                                dayList.get(i).startTime = etMonFrom.getText().toString();
                                dayList.get(i).offTime = etMonTo.getText().toString();
                                dayList.get(i).tag = 1;
                            }
                        }
                    }else {
                        for (int i = 0; i < dayList.size(); i++){
                            if (dayList.get(i).name.equals("MON")){

                                dayList.get(i).tag = 0;
                            }
                        }
                    }
                }

            }
        });

        chkTue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (blankValidate(etTueFrom, etTueTo)){
                    if (chkTue.isChecked()){
                        for (int i = 0; i < dayList.size(); i++){
                            if (dayList.get(i).name.equals("TUE")){
                                dayList.get(i).startTime = etTueFrom.getText().toString();
                                dayList.get(i).offTime = etTueTo.getText().toString();
                                dayList.get(i).tag = 1;
                            }
                        }
                    }else {
                        for (int i = 0; i < dayList.size(); i++){
                            if (dayList.get(i).name.equals("TUE")){
                                dayList.get(i).tag = 0;
                            }
                        }
                    }
                }

            }
        });

        chkWed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (blankValidate(etWedFrom, etWedTo)) {
                    if (chkWed.isChecked()) {
                        for (int i = 0; i < dayList.size(); i++) {
                            if (dayList.get(i).name.equals("WED")) {
                                dayList.get(i).startTime = etWedFrom.getText().toString();
                                dayList.get(i).offTime = etWedTo.getText().toString();
                                dayList.get(i).tag = 1;
                            }
                        }
                    } else {
                        for (int i = 0; i < dayList.size(); i++) {
                            if (dayList.get(i).name.equals("WED")) {
                                dayList.get(i).tag = 0;
                            }
                        }
                    }
                }
            }
        });

        chkThu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (blankValidate(etThuFrom, etThuTo)) {
                    if (chkThu.isChecked()) {
                        for (int i = 0; i < dayList.size(); i++) {
                            if (dayList.get(i).name.equals("THU")) {
                                dayList.get(i).startTime = etThuFrom.getText().toString();
                                dayList.get(i).offTime = etThuTo.getText().toString();
                                dayList.get(i).tag = 1;
                            }
                        }
                    } else {
                        for (int i = 0; i < dayList.size(); i++) {
                            if (dayList.get(i).name.equals("THU")) {
                                dayList.get(i).tag = 0;
                            }
                        }
                    }
                }
            }
        });

        chkFri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (blankValidate(etFriFrom, etFriTo)) {
                    if (chkFri.isChecked()) {
                        for (int i = 0; i < dayList.size(); i++) {
                            if (dayList.get(i).name.equals("FRI")) {
                                dayList.get(i).startTime = etFriFrom.getText().toString();
                                dayList.get(i).offTime = etFriTo.getText().toString();
                                dayList.get(i).tag = 1;
                            }
                        }
                    } else {
                        for (int i = 0; i < dayList.size(); i++) {
                            if (dayList.get(i).name.equals("FRI")) {
                                dayList.get(i).tag = 0;
                            }
                        }
                    }
                }
            }
        });

        chkSat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (blankValidate(etSatFrom, etSatTo)) {
                    if (chkSat.isChecked()) {
                        for (int i = 0; i < dayList.size(); i++) {
                            if (dayList.get(i).name.equals("SAT")) {
                                dayList.get(i).startTime = etSatFrom.getText().toString();
                                dayList.get(i).offTime = etSatTo.getText().toString();
                                dayList.get(i).tag = 1;
                            }
                        }
                    } else {
                        for (int i = 0; i < dayList.size(); i++) {
                            if (dayList.get(i).name.equals("SAT")) {
                                dayList.get(i).tag = 0;
                            }
                        }
                    }
                }
            }
        });

        chkSun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (blankValidate(etSunFrom, etSunTo)) {
                    if (chkSun.isChecked()) {
                        for (int i = 0; i < dayList.size(); i++) {
                            if (dayList.get(i).name.equals("SUN")) {
                                dayList.get(i).startTime = etSunFrom.getText().toString();
                                dayList.get(i).offTime = etSunTo.getText().toString();
                                dayList.get(i).tag = 1;
                            }
                        }
                    } else {
                        for (int i = 0; i < dayList.size(); i++) {
                            if (dayList.get(i).name.equals("SUN")) {
                                dayList.get(i).tag = 0;
                            }
                        }
                    }
                }
            }
        });


    }

    private void getTimings() {
        dialogView.showCustomSpinProgress(Availability.this);
        manager.service.getTimings(prefs.getData(Constants.REST_ID)).enqueue(new Callback<TimingResponseModel>() {
            @Override
            public void onResponse(Call<TimingResponseModel> call, Response<TimingResponseModel> response) {
                if (response.isSuccessful()){
                    TimingResponseModel trm = response.body();
                    if (!trm.error){
                        dialogView.dismissCustomSpinProgress();
                        timeList = trm.data;

                        if (timeList.size() > 1){

                            chkAll.setChecked(false);
                            for (int i = 0; i < timeList.size(); i++){
                                if (timeList.get(i).day.equals("MON")){
                                    chkMon.setChecked(true);
                                    etMonFrom.setText(timeList.get(i).start_time);
                                    etMonTo.setText(timeList.get(i).end_time);
                                }else if(timeList.get(i).day.equals("TUE")){
                                    chkTue.setChecked(true);
                                    etTueFrom.setText(timeList.get(i).start_time);
                                    etTueTo.setText(timeList.get(i).end_time);
                                }else if (timeList.get(i).day.equals("WED")){
                                    chkTue.setChecked(true);
                                    etTueFrom.setText(timeList.get(i).start_time);
                                    etTueTo.setText(timeList.get(i).end_time);
                                }else if (timeList.get(i).day.equals("THU")){
                                    chkThu.setChecked(true);
                                    etThuFrom.setText(timeList.get(i).start_time);
                                    etThuTo.setText(timeList.get(i).end_time);
                                }else if (timeList.get(i).day.equals("FRI")){
                                    chkFri.setChecked(true);
                                    etFriFrom.setText(timeList.get(i).start_time);
                                    etFriTo.setText(timeList.get(i).end_time);
                                }else if (timeList.get(i).day.equals("SAT")){
                                    chkSat.setChecked(true);
                                    etSatFrom.setText(timeList.get(i).start_time);
                                    etSatTo.setText(timeList.get(i).end_time);
                                }else if (timeList.get(i).day.equals("SUN")){
                                    chkSun.setChecked(true);
                                    etSunFrom.setText(timeList.get(i).start_time);
                                    etSunTo.setText(timeList.get(i).end_time);
                                }
                            }


                        }else if (timeList.size() == 1){
                            if (timeList.get(0).day.equals("ALL")){

                                chkAll.setChecked(true);

                                /*chkMon.setChecked(true);
                                chkTue.setChecked(true);
                                chkWed.setChecked(true);
                                chkThu.setChecked(true);
                                chkFri.setChecked(true);
                                chkSat.setChecked(true);
                                chkSun.setChecked(true);*/

                                etAllFrom.setText(timeList.get(0).start_time);

                                etMonFrom.setText(timeList.get(0).start_time);
                                etTueFrom.setText(timeList.get(0).start_time);
                                etWedFrom.setText(timeList.get(0).start_time);
                                etThuFrom.setText(timeList.get(0).start_time);
                                etFriFrom.setText(timeList.get(0).start_time);
                                etSatFrom.setText(timeList.get(0).start_time);
                                etSunFrom.setText(timeList.get(0).start_time);

                                etAllTo.setText(timeList.get(0).end_time);

                                etMonTo.setText(timeList.get(0).end_time);
                                etTueTo.setText(timeList.get(0).end_time);
                                etWedTo.setText(timeList.get(0).end_time);
                                etThuTo.setText(timeList.get(0).end_time);
                                etFriTo.setText(timeList.get(0).end_time);
                                etSatTo.setText(timeList.get(0).end_time);
                                etSunTo.setText(timeList.get(0).end_time);

                                chkMon.setEnabled(false);
                                chkTue.setEnabled(false);
                                chkWed.setEnabled(false);
                                chkThu.setEnabled(false);
                                chkFri.setEnabled(false);
                                chkSat.setEnabled(false);
                                chkSun.setEnabled(false);

                                etMonFrom.setEnabled(false);
                                etMonTo.setEnabled(false);
                                etTueFrom.setEnabled(false);
                                etTueTo.setEnabled(false);
                                etWedFrom.setEnabled(false);
                                etWedTo.setEnabled(false);
                                etThuFrom.setEnabled(false);
                                etThuTo.setEnabled(false);
                                etFriFrom.setEnabled(false);
                                etFriTo.setEnabled(false);
                                etSatFrom.setEnabled(false);
                                etSatTo.setEnabled(false);
                                etSunFrom.setEnabled(false);
                                etSunTo.setEnabled(false);
                            }
                        }

                    }else {
                        dialogView.dismissCustomSpinProgress();
                    }
                }else {
                    dialogView.dismissCustomSpinProgress();
                }
            }

            @Override
            public void onFailure(Call<TimingResponseModel> call, Throwable t) {
                dialogView.dismissCustomSpinProgress();
            }
        });
    }

    private boolean blankValidate(TextView etAllFrom, TextView etAllTo) {
        if (etAllFrom.getText().toString().equals("")) {
            Toast.makeText(this, "Please enter start time!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (etAllTo.getText().toString().equals("")) {
            Toast.makeText(this, "Please enter closing time!", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    private void saveTimingData(AvailabilityRequestModel availabilityRequestModel) {
        dialogView.showCustomSpinProgress(Availability.this);
        manager.service.availabilitySettings(availabilityRequestModel).enqueue(new Callback<AvailabilityResponseModel>() {
            @Override
            public void onResponse(Call<AvailabilityResponseModel> call, Response<AvailabilityResponseModel> response) {
                if (response.isSuccessful()) {
                    AvailabilityResponseModel arm = response.body();
                    if (!arm.error) {
                        dialogView.dismissCustomSpinProgress();
                        Toast.makeText(Availability.this, "Submitted successfully!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        dialogView.dismissCustomSpinProgress();
                        Toast.makeText(Availability.this, "Failed!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    dialogView.dismissCustomSpinProgress();
                }
            }

            @Override
            public void onFailure(Call<AvailabilityResponseModel> call, Throwable t) {
                dialogView.dismissCustomSpinProgress();
            }
        });
    }

    private void setTimePicker(TextView txtView) {
        final Calendar c = Calendar.getInstance();

        // on below line we are getting our hour, minute.
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // on below line we are initializing our Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(Availability.this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        // on below line we are setting selected time
                        // in our text view.
                        setTimeFromPicker(txtView, hourOfDay, minute);

                    }
                }, hour, minute, true);
        // at last we are calling show to
        // display our time picker dialog.
        timePickerDialog.show();

    }

    private void setTimeFromPicker(TextView txtView, int hourOfDay, int minute) {
        String time = hourOfDay + ":" + minute;
       /* Date date=new Date("Your date ");
        SimpleDateFormat HHmmFormat = new SimpleDateFormat("HH:mm", Locale.US);
        String form = HHmmFormat.format(Date.parse(time));*/
        txtView.setText(time);
    }


}