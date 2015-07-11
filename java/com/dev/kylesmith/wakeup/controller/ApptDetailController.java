package com.dev.kylesmith.wakeup.controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.dev.kylesmith.wakeup.R;
import com.dev.kylesmith.wakeup.model.Constants;
import com.dev.kylesmith.wakeup.model.Converters.StringToDate;
import com.dev.kylesmith.wakeup.model.DB.DBAccessor;
import com.dev.kylesmith.wakeup.model.DB.DBContract;
import com.dev.kylesmith.wakeup.model.Location.CheckIn;
import com.dev.kylesmith.wakeup.model.ServerInterface.AsyncResponse;
import com.dev.kylesmith.wakeup.model.ServerInterface.DeleteApptTask;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by kylesmith on 1/12/15.
 */
public class ApptDetailController extends Activity implements OnMapReadyCallback, AsyncResponse {

    private GoogleMapOptions options = new GoogleMapOptions();
    private GoogleMap map;
    private static long id;
    private static DBAccessor dbAccessor;
    private static Context context;
    private static Intent intent;
    private static TextView titleTextView;
    private static TextView timeTextView;
    private static TextView dateTextView;
    private static float zoomLevel = 0;
    private static LatLng apptLocation;
    private static Date apptTime;
    private static float  apptBoxSize;
    private static StringToDate stringToDate = new StringToDate();
    private static DateFormat dateFormat;
    private static DateFormat timeFormat;
    private static String linekey;
    private static final String TIME_PATTERN = "hh:mm a";
    private DeleteApptTask deleteApptTask = new DeleteApptTask();
    FrameLayout progressBarHolder;
    private Button checkinBtn;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apptdetail);
        context = getApplicationContext();
        intent = getIntent();
        deleteApptTask.delegate = this;

        dateFormat = DateFormat.getDateInstance(DateFormat.LONG, Locale.getDefault());
        timeFormat = new SimpleDateFormat(TIME_PATTERN, Locale.getDefault());

        // Find all view components
        titleTextView = (TextView) findViewById(R.id.titleTextView);
        timeTextView = (TextView) findViewById(R.id.timeTextView);
        dateTextView = (TextView) findViewById(R.id.dateTextView);
        progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);
        checkinBtn = (Button) findViewById(R.id.checkin_btn);
        populateView();


        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mapFragment.newInstance(new GoogleMapOptions().liteMode(true));

        ImageButton backButton = (ImageButton)findViewById(R.id.back_btn);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    @Override
    public void onBackPressed(){
        super.onBackPressed();
    }








    private static void populateView(){
        // Get appointment data from DB
        dbAccessor = new DBAccessor(context);
        id = intent.getLongExtra(Constants.ID, 0);
        Cursor c = dbAccessor.getAppointment(id);
        c.moveToFirst();
        Log.i("CURSOR", "Row Count: " + c.getCount());

        // Set all text views
        titleTextView.setText(c.getString(
                c.getColumnIndexOrThrow(DBContract.Appointments.COLUMN_NAME_TITLE)
        ));

        apptTime = getApptTime(c.getString(
                c.getColumnIndexOrThrow(DBContract.Appointments.COLUMN_NAME_TIME)
        ));

        linekey = c.getString(c.getColumnIndexOrThrow(DBContract.Appointments.COLUMN_NAME_LINEKEY));

        timeTextView.setText(timeFormat.format(apptTime.getTime()));
        dateTextView.setText(dateFormat.format(apptTime.getTime()));


        // Set appointment location information
        apptLocation = new LatLng(
                c.getDouble(
                      c.getColumnIndexOrThrow(DBContract.Appointments.COLUMN_NAME_LATITUDE)
                ),
                c.getDouble(
                        c.getColumnIndexOrThrow(DBContract.Appointments.COLUMN_NAME_LONGITUDE)
                )
        );

        // How zoomed in the map should be
        zoomLevel = c.getFloat(c.getColumnIndexOrThrow(DBContract.Appointments.COLUMN_NAME_ZOOM_LEVEL));
        apptBoxSize = c.getFloat(c.getColumnIndexOrThrow(DBContract.Appointments.COLUMN_NAME_LOC_AREA));
    }







    private static Date getApptTime(String dateString){
        return StringToDate.Convert(dateString);
    }






    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;
        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        centerMapOnApptLocation();
    }






    // Zooms map in and show appt location
    private void centerMapOnApptLocation(){
        map.setMyLocationEnabled(true);


        if (apptLocation != null && zoomLevel != 0)
        {
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(apptLocation.latitude, apptLocation.longitude))      // Sets the center of the map to location user
                    .zoom(zoomLevel)                   // Sets the zoom
                    .build();                   // Creates a CameraPosition from the builder
            map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        }
    }






    // Attempts to check in the user at the appointment
    public void checkin(View view){
        isLoading();
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
        CheckIn checkIn = new CheckIn(new LatLng(location.getLatitude(), location.getLongitude()),
                new LatLng(apptLocation.latitude, apptLocation.longitude),
                apptTime,
                apptBoxSize
                );

        String error = checkIn.isValid();
        // User successfully checked in at the appointment
        if(error.isEmpty()){
            deleteApptTask.execute(linekey);
        }
        // User is either outside the appt location or outside the time window
        else{
            isNotLoading();
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
        }
    }


    private void isLoading(){
        checkinBtn.setEnabled(false);
        progressBarHolder.setAnimation(new Animations().getInAnimation());
        progressBarHolder.setVisibility(View.VISIBLE);
    }

    private void isNotLoading(){
        checkinBtn.setEnabled(true);
        progressBarHolder.setAnimation(new Animations().getOutAnimation());
        progressBarHolder.setVisibility(View.INVISIBLE);
    }

    @Override
    public void processFinish(Object success) {
        isNotLoading();

        if((Boolean)success) {
            dbAccessor.deleteAppointment(id);
            Intent intent = new Intent(this, ScheduleController.class);
            Toast.makeText(this, "Success!", Toast.LENGTH_LONG).show();
            startActivity(intent);
        } else{
            Toast.makeText(this, "Failed to check in!", Toast.LENGTH_LONG).show();
        }
        checkinBtn.setEnabled(true);
    }
}
