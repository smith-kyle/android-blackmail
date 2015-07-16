package com.dev.kylesmith.wakeup.controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;


import com.android.datetimepicker.date.DatePickerDialog;
import com.android.datetimepicker.time.RadialPickerLayout;
import com.android.datetimepicker.time.TimePickerDialog;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import com.dev.kylesmith.wakeup.R;
import com.dev.kylesmith.wakeup.model.Constants;
import com.dev.kylesmith.wakeup.model.DB.DBAccessor;
import com.dev.kylesmith.wakeup.model.Location.DistanceConverter;
import com.dev.kylesmith.wakeup.model.ServerInterface.AddApptTask;
import com.dev.kylesmith.wakeup.model.ServerInterface.AsyncResponse;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import butterknife.Bind;

/**
 * Created by kylesmith on 1/9/15.
 */
public class NewApptController extends Activity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, OnMapReadyCallback, AsyncResponse {
    private static final String TIME_PATTERN = "hh:mm a";

    private AddApptTask addApptTask = new AddApptTask();
    private DBAccessor dbAccessor = new DBAccessor(this);
    private HashMap<String, Object> UserInfo;
    private Calendar calendar;
    private DateFormat dateFormat;
    private SimpleDateFormat timeFormat;
    private GoogleMap map;
    private DistanceConverter distanceConverter = new DistanceConverter();
    private GoogleMapOptions options = new GoogleMapOptions();
    private long NewApptID;
    @Bind(R.id.map) MapFragment mapFragment;
    @Bind(R.id.btnDatePicker) Button datePickerBtn;
    @Bind(R.id.btnTimePicker) Button timePickerBtn;
    @Bind(R.id.title) EditText titleTxt;
    @Bind(R.id.progressBarHolder) FrameLayout progressBarHolder;
    @Bind(R.id.submit_appt_btn)  Button createAppt_btn;
    @Bind(R.id.back_btn) ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newappt);
        setupUI(findViewById(R.id.schedule_parent));
        mapFragment.getMapAsync(this);

        addApptTask.delegate = this;
        UserInfo = dbAccessor.getUserInfo();

        options.mapType(GoogleMap.MAP_TYPE_HYBRID)
                .compassEnabled(false)
                .rotateGesturesEnabled(false)
                .tiltGesturesEnabled(false);

        mapFragment.newInstance(options);

        calendar = Calendar.getInstance();
        dateFormat = DateFormat.getDateInstance(DateFormat.LONG, Locale.getDefault());
        timeFormat = new SimpleDateFormat(TIME_PATTERN, Locale.getDefault());

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        update();
    }


    @Override
    public void onBackPressed(){
        super.onBackPressed();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnDatePicker:
                DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show(getFragmentManager(), "datePicker");
                break;
            case R.id.btnTimePicker:
                TimePickerDialog.newInstance(this, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show(getFragmentManager(), "timePicker");
                break;
            case R.id.submit_appt_btn:
                isLoading();
                if(validate()) newAppointment();
                break;
        }
    }

    private boolean validate(){
        String error = "";
        Date curDate = new Date();
        if(!calendar.getTime().after(curDate)){
            error = "Appointment must be in the future";
        }
        if(titleTxt.getText().toString().isEmpty()){
            error = "Please add a title";
        }
        if(!error.isEmpty()){
            isNotLoading();
            Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void update() {
        datePickerBtn.setText(dateFormat.format(calendar.getTime()));
        timePickerBtn.setText(timeFormat.format(calendar.getTime()));
    }


    @Override
    public void onDateSet(DatePickerDialog dialog, int year, int monthOfYear, int dayOfMonth) {
        calendar.set(year, monthOfYear, dayOfMonth);
        update();
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        update();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;
        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        map.getUiSettings().setMyLocationButtonEnabled(false);
        centerMapOnMyLocation();
    }


    // Zooms map in and shows location
    private void centerMapOnMyLocation(){
        map.setMyLocationEnabled(true);
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
        if (location != null)
        {
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(location.getLatitude(), location.getLongitude()), 13));

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(location.getLatitude(), location.getLongitude()))      // Sets the center of the map to location user
                    .zoom(17)                   // Sets the zoom
                    .build();                   // Creates a CameraPosition from the builder
            map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        }
    }

    private void newAppointment(){
        addApptTask.execute(UserInfo.get(Constants.MAPKEY_USER_ID) , calendar.getTime());
    }


    private float getSelectedAreaWidth(){
        float d = distanceConverter.ZoomLevelToDistance(map.getCameraPosition().zoom);
        DisplayMetrics displayMetrics = getApplicationContext().getResources().getDisplayMetrics();

        float screenWidth = displayMetrics.widthPixels / displayMetrics.density;
        ImageView apptAreaBox = (ImageView) findViewById(R.id.apptAreaBox);
        float boxWidth = (apptAreaBox.getWidth()/2);

        return d * boxWidth/screenWidth;
    }

    private void isLoading(){
        createAppt_btn.setEnabled(false);
        progressBarHolder.setAnimation(new Animations().getInAnimation());
        progressBarHolder.setVisibility(View.VISIBLE);
    }

    private void isNotLoading(){
        createAppt_btn.setEnabled(true);
        progressBarHolder.setAnimation(new Animations().getOutAnimation());
        progressBarHolder.setVisibility(View.INVISIBLE);
    }

    @Override
    public void processFinish(Object linekey) {
        if(((String)linekey).equals("")){
            Toast.makeText(getApplicationContext(), "Failed to create appointment", Toast.LENGTH_SHORT);
        }
        else{
            DBAccessor dbAccessor = new DBAccessor(getApplicationContext());
            LatLng p = map.getCameraPosition().target;
            float distance = getSelectedAreaWidth();

            Log.i("ZOOM LEVEL DATA", "Zoom Level: " + distance);
            NewApptID = dbAccessor.insertAppointment(titleTxt.getText().toString(),
                    Double.toString(p.longitude),
                    Double.toString(p.latitude),
                    distance,
                    map.getCameraPosition().zoom,
                    calendar.getTime(),
                    (String)linekey);
            isNotLoading();
            Intent intent = new Intent(this, ScheduleController.class);
            startActivity(intent);
        }
    }


    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }


    public void setupUI(View view) {

        //Set up touch listener for non-text box views to hide keyboard.
        if(!(view instanceof EditText)) {

            view.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(NewApptController.this);
                    return false;
                }

            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {

            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {

                View innerView = ((ViewGroup) view).getChildAt(i);

                setupUI(innerView);
            }
        }
    }
}
